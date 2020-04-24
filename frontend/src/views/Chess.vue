<template>
  <!-- Chess Game -->

  <!-- MAINTAIN GAME STATE !!!! -->
  <div>
    <p><b>Game Id:</b> {{ gameId }}</p>
    <p><b>Game Status:</b> {{ getGameStatus }}</p>
    <p><b>Game Active:</b>{{ getGameActive }}</p>
    <p v-if="getOpponent != null">
      <!-- get game opponent -->
      <b>Opponent:</b> {{ getOpponent.username }}
    </p>

    <b-button-group size="lg">
      <!-- game buttons -->
      <b-button variant="success" @click="resumeGame(gameId)">Resume</b-button>
      <b-button variant="info" @click="pauseGame(gameId)">Pause</b-button>
      <b-button variant="danger" @click="resignGame(gameId)">Finish</b-button>

      <b-button
        v-if="getGameActive && !getOpponentActive"
        variant="primary"
        disabled
      >
        <b-spinner small type="grow"></b-spinner>
        Waiting for Opponent...
      </b-button>
      <template v-if="getGameActive && getOpponentActive">
        <button v-if="getMyTurn" disabled class="btn btn-primary">
          My Turn
        </button>
        <button v-else disabled class="btn btn-primary">
          Opponent's Turn
        </button>

        <!-- <b-button variant="primary" @click="addChessMove(gameId)"
          >Make Move</b-button
        > -->
      </template>
    </b-button-group>
    <hr />
    <board
      :class="$style.board"
      :model.sync="game.board"
      :lastMove="lastMove"
      :selectedPiece="selectedPiece"
      @cell-clicked="onCellClicked"
      @piece-clicked="onPieceClicked"
      ref="board"
    />
    <!-- history moves -->
    <activity-log :logs="getGameMoves" />
  </div>
</template>

<script module>
//import wait from "../utils/wait";
import deepEqual from "deep-equal";

import Vue from "vue";

// chess game logic
import Chess from "../model/chess/game";

import UIPlayer from "../model/ui-player";
import api from "../api/game";

import Board from "../game-components/board";
import activityLog from "../components/activity-log";

import ChessMove from "../model/chess/move";

import { mapActions, mapGetters } from "vuex";

// ping backend
const aliveInterval = 7000;

export default {
  components: { Board, activityLog },
  props: {
    gameId: {
      required: true,
    },
  },
  data() {
    return {
      game: new Chess(), // empty instance
      me: null,
      selectedPiece: null, // piece user selects
      gameResult: null,
      pingAliveInterval: null,
    };
  },
  watch: {
    // call again the method if the route changes
    $route: "fetchGame(this.gameId)",
    // watch game statue
    getGameStatus(value) {
      console.log("Game Status changed " + value);

      if (value == "IN_PROGRESS") {
        // ping server
        api.gameAlive(this.gameId);
        this.pingAliveInterval = setInterval(() => {
          api.gameAlive(this.gameId);
        }, aliveInterval);

        // listen for server notifications
        this.listenServer();
        this.isMyTurn(); // get current turn
        //this.startGame(this.game); // start game
      } else {
        // clear interval
        console.log("cleared alive pinging the server");
        clearInterval(this.pingAliveInterval);
        // stop game
        // this.stopGame(this.game);
      }
    },
    getGameMoves(moves) {
      console.log("updating board moves");
      this.loadGameMoves(moves);
    },
  },
  computed: {
    ...mapGetters([
      "getGameId",
      "getGameStatus",
      "getGameMoves",
      "getGameActive",
      "getGameColor",
      "getOpponentActive",
      "getOpponent",
      "getMyTurn",
      "getUsername",
    ]),
  },
  created() {
    // load game state
    this.fetchGame(this.gameId).then(() => {
      // fetch opponent
      this.fetchOpponent(this.gameId).then(() => {
        // fetch my game color
        this.fetchGameColor(this.gameId).then(() => {
          // game fetcher
          this.getChessMoves(this.gameId).then(() => {
            // create game
            this.createGame();
          });
        });
      });
    });
    // listen for game notifications
  },

  beforeDestroy() {
    this.stopListenServer();
    //this.stopGame(this.game);
  },
  methods: {
    ...mapActions([
      "fetchGame",
      "fetchOpponent",
      "fetchGameColor",
      "resumeGame",
      "pauseGame",
      "finishGame",
      "resignGame",
      "notify",
      "listenServer",
      "stopListenServer",
      "isMyTurn",
      "getChessMoves",
    ]),
    createGame() {
      const myColor = this.getGameColor.toString();
      const opponentColor = myColor === "white" ? "black" : "white";

      const me = new UIPlayer(this, 1, myColor, "me");
      this.me = me;
      const opponent = new UIPlayer(this, 2, opponentColor, "opponent");

      this.game = new Chess(me, opponent);

      // load moves after board is rendered
      this.loadGameMoves(this.getGameMoves);
    },
    loadGameMoves(moves) {
      // iterate moves and update board
      // apply latest move to board
      const diff = moves.length - this.game.log.length;
      // {"toBoardRow":7,"game":1,
      // "chessPiece":"KNIGHT",
      // "color":"BLACK","created":"Mon Apr 20 19:31:02 IDT 2020",
      // "fromBoardRow":6,
      // "fromBoardColumn":7,"id":0,
      // "toBoardColumn":5,"moveCount":1,"pieceId":30,"player":"gabriel"}

      // update game board with new moves
      for (var i = diff; i > 0; i--) {
        const move = moves.slice(-i)[0];
        var from = {};
        var to = {};

        // for opponent moves invert board positions
        if (move.color.toLowerCase() != this.getGameColor.toString()) {
          // invert positions
          console.log("inverting position");
          from = {
            x: 7 - move.fromBoardColumn,
            y: 7 - move.fromBoardRow,
          };
          to = { x: 7 - move.toBoardColumn, y: 7 - move.toBoardRow };
        } else {
          from = { x: move.fromBoardColumn, y: move.fromBoardRow };
          to = { x: move.toBoardColumn, y: move.toBoardRow };
        }
        // apply latest move to board
        this.game.commitMove(move.player, {
          piece: {
            id: move.pieceId,
            type: move.piece.toLowerCase(),
            color: move.color.toLowerCase(),
            moveCount: move.moveCount,
          },
          from: from,
          to: to,
        });

        this.notify({ message: "Game moves updated", variant: "info" });
      }
    },
    onPieceClicked(piece, from) {
      if (!this.getMyTurn) {
        this.notify({ message: "It's not your turn!", variant: "danger" });
        return;
      }

      // check if piece move was clicked, if not emit a pieceSelectedEvent
      if (this.selectedPiece === null || piece.color === this.color) {
        this.selectedPiece = { piece, from };
        console.log("Chose selected piece");
      } else {
        // piece is capturing another piece -> finish move
        this.finishNormalMove(from);
      }
    },
    onCellClicked(to) {
      if (!this.getMyTurn) {
        this.notify({ message: "It's not your turn!", variant: "danger" });
        return;
      }
      console.log("cell clicked from board");
      console.log("selected piece");
      console.log(this.selectedPiece);
      // make sure piece was selected before and not moving to the same position
      if (
        this.selectedPiece === null ||
        deepEqual(this.selectedPiece.from, to)
      ) {
        return;
      }
      this.finishNormalMove(to);
    },

    finishNormalMove(to) {
      // without capturing
      this.finishMove({
        piece: this.selectedPiece.piece,
        from: this.selectedPiece.from,
        to,
      });
    },

    finishMove(move) {
      console.log("commiting move to server");
      console.log(move);
      // validate move
      if (this.game.validateMove(this.me, move)) {
        this.notify({
          message: "Move is validated, commiting server",
          variant: "success",
        });
        this.game.commitMove(this.me, move);
        // fromBoardRow, fromBoardColumn, toBoardRow, toBoardColumn,
        //  piece,  color,moveCount, pieceId;
        const apiChessMove = new ChessMove(
          move.from.y,
          move.from.x,
          move.to.y,
          move.to.x,
          move.piece.type,
          move.piece.color,
          move.piece.moveCount,
          move.piece.id
        );
        api
          .commitChessMove(this.gameId, apiChessMove)
          .then(() => {
            this.isMyTurn(); // check if comitted successfully on server
          })
          .catch(() => {
            this.notify({ message: "Failed to add move", variant: "danger" });
          }); // commit api
      } else {
        this.notify({
          message: "Failed to validate move",
          variant: "danger",
        });
      }
      this.selectedPiece = null;
    },

    handleKingInCheck() {
      Vue.nextTick(() => {
        for (const plr of this.game.plrs) {
          const inCheck = this.game.isKingInCheck(plr);
          for (const p of this.game.board.findPiecesOfColor(plr.color)) {
            const pieceVm = this.$refs.board.pieceVm(p.piece, p.x, p.y);
            pieceVm.stickToFearExpresion = inCheck;
          }
        }
      });
    },
  },
};
</script>

<style module>
:root {
  --board-size: 45rem;
  --text-color: rgb(189, 189, 189);
  --board-color: rgb(52, 58, 64);
  --shadow-color: rgb(0, 0, 0, 0.4);
  --background-color: hsl(24, 26%, 32%);
}
.board {
  height: var(--board-size);
  width: var(--board-size);
  margin: auto;
  display: block;
}

.control-button {
  border: none;
  background-color: var(--board-color);
  border-radius: 0.1em;
  padding: 1px 8px 1px 8px;
  outline: none;
  box-shadow: 0 0 0.5em var(--shadow-color);
  z-index: 1;
  color: white;
  margin-top: 0.5em;
  margin-left: 0.2em;
}

@media all and (pointer: coarse) {
  .control-button {
    font-size: 2em;
  }
}

.control-button:active {
  transform: translate(0, 2px);
}
</style>

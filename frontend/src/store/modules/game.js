import api from "../../api/game";
import path from "path";
import sse from "../../eventsource";
//import game from "../../api/game";

/*
{"id":53,"firstPlayer":{"username":"admin",
"password":"$2a$10$V2hg1Y.c5jkqN27q1HZcu.rx6SxrmmbdZtkkNV8LdQQ8kBQpqJ0/u",
"email":"admin@example.com","rating":0},
"secondPlayer":null,
"created":"2020-04-05T22:01:26.006+0000",
"gameType":"CHESS",
"gameStatus":"WAITING_FOR_OPPONENT",
"lastPlayedPlayer":null}
*/

const state = {
  gameActive: false, // true if game status is in progress!
  opponentActive: false, // true if opponent is alive
  opponent: null, // opponent player
  gameId: "",
  gameStatus: "",
  myTurn: false, // whos turn
  gameMoves: [], // game moves
  notification: {
    message: "",
    variant: "primary",
  },
  gameColor: "",
  sse: null, // EventSource()
};

const getters = {
  getGameId: (state) => state.gameId,
  getGameStatus: (state) => state.gameStatus,
  getGameMoves: (state) => state.gameMoves,
  getGameActive: (state) => state.gameActive,
  getGameColor: (state) => state.gameColor.toLowerCase(),
  getLastPlayedPlayer: (state) => state.lastPlayedPlayer,
  getOpponentActive: (state) => state.opponentActive,
  getOpponent: (state) => state.opponent,
  getMyTurn: (state) => state.myTurn,
  getNotification: (state) => state.notification,
  getSse: (state) => state.sse,
};

const actions = {
  /**
   *
   * @param {state} param0
   */
  listenServer({ commit, getters }) {
    console.log("listening to server for game mutations");

    console.log(
      api.getUri() + path.resolve(getters.getGameId.toString(), "notification")
    );

    if (getters.getGameId.toString() === "") {
      // can't listen to non fetched game
      return;
    }

    commit("closeSse"); // close existing sse

    const sourceUrl =
      api.getUri() + path.resolve(getters.getGameId.toString(), "notification");

    // create server event source
    commit("openSse", { url: sourceUrl });
  },
  stopListenServer({ commit }) {
    commit("closeSse");
  },
  addMove({ commit }, move) {
    commit("addMove", move);
  },
  notify({ commit }, payload) {
    commit("updateNotification", payload);
  },
  async fetchOpponent({ commit }, gameId) {
    const respPromise = await api
      .getOpponent(gameId)
      .then((response) => {
        commit("updateOpponent", response);
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async fetchGameColor({ commit }, gameId) {
    const respPromise = await api
      .getColor(gameId)
      .then((response) => {
        commit("updateGameColor", response);
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async fetchGame({ commit }, gameId) {
    const respPromise = await api
      .getGame(gameId)
      .then((response) => {
        commit("updateGame", response);
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getChessMoves({ commit }, gameId) {
    const respPromise = await api.getChessMoves(gameId).then((response) => {
      commit("updateMoves", response);
    });
    return respPromise;
  },
  resumeGame({ commit }, gameId) {
    api
      .resumeGame(gameId)
      .then(() => {
        // wait for game to resume
        api.getGameStatus(gameId).then((status) => {
          if (status == "IN_PROGRESS") {
            commit("gameResumed");
          } else {
            commit("gameFailedToResume");
          }
        });
      })
      .catch();
  },
  pauseGame({ commit }, gameId) {
    api
      .pauseGame(gameId)
      .then(() => {
        // wait for game to pause
        api.getGameStatus(gameId).then((resp) => {
          if (resp == "PAUSED") {
            commit("gamePaused");
            console.debug("Game paused! ");
          } else {
            commit("gameFailedToPause");
          }
        });
      })
      .catch((err) => {
        commit("gameFailedToPause");
        console.error(err);
      });
  },
  resignGame({ commit }, gameId) {
    api
      .resignGame(gameId)
      .then(() => {
        // wait for game to finish
        api.getGameStatus(gameId).then((resp) => {
          if (resp == "FIRST_PLAYER_WON" || resp == "SECOND_PLAYER_WON") {
            commit("gameFinished");
          } else {
            commit("gameFailedToFinish");
          }
        });
      })
      .catch((err) => {
        console.error(err);
        commit("gameFailedToFinish");
      });
  },
  finishGame({ commit }, gameId) {
    api
      .finishGame(gameId)
      .then(() => {
        // wait for game to finish
        api.getGameStatus(gameId).then((resp) => {
          if (resp == "FINSIHED") {
            commit("gameFinished");
          } else {
            commit("gameFailedToFinish");
          }
        });
      })
      .catch((err) => {
        console.error(err);
        commit("gameFailedToFinish");
      });
  },
  isMyTurn({ commit, state }) {
    if (state.gameId === "") {
      return;
    }
    // commit the state?
    api.isTurn(state.gameId).then((myTurn) => {
      if (myTurn) {
        commit("myTurn");
      } else {
        commit("notMyturn");
      }
    });
  },
};

const mutations = {
  closeSse(state) {
    if (state.sse != null) {
      console.log("Closing sse " + state.sse.url);
      state.sse.close();
    }
    state.sse = null;
  },
  openSse(state, payload) {
    if (state.sse != null) {
      console.log("Closing sse " + state.sse.url);
      state.sse.close();
    }
    state.sse = sse(payload.url);
  },
  updateNotification(state, payload) {
    state.notification.message = JSON.stringify(payload.message);
    state.notification.variant = payload.variant;
  },
  addMove(state, move) {
    state.gameMoves.push(JSON.parse(move));
  },
  // {"game":62,"chessPiece":"KING","color":"BLACK","boardRow":3,"created":"Fri Apr 17 23:27:41 IDT 2020","boardColumn":3,"id":0,"player":"example"}
  updateMoves(state, payload) {
    /// TODO: update boardRow/Board Colum + Piece Type
    payload.forEach((move) => {
      state.gameMoves.push({
        player: move.player.username,
        piece: move.chessPiece,
        fromBoardRow: move.fromBoardRow,
        fromBoardColumn: move.fromBoardColumn,
        toBoardRow: move.toBoardRow,
        toBoardColumn: move.toBoardColumn,
        color: move.color,
        moveCount: move.moveCount,
        pieceId: move.pieceId,
      });
    });
  },
  opponentNotActive(state) {
    state.opponentActive = false;
  },
  opponentActive(state) {
    state.opponentActive = true;
  },
  updateGameStatus(state, gameStatus) {
    state.gameStatus = gameStatus;
    if (gameStatus != "IN_PROGRESS") {
      state.gameActive = false;
    } else {
      state.gameActive = true;
    }
    state.notification.message = "Game Status changed to " + gameStatus;
    state.notification.variant = "primary";
  },
  updateOpponent(state, opponent) {
    state.opponent = opponent;
  },
  updateGameColor(state, color) {
    state.gameColor = color;
  },
  updateGame(state, payload) {
    console.log("Game " + payload.id + " fetched and updated in vuex");
    state.gameId = payload.id;
    state.gameType = payload.gameType;
    state.gameStatus = payload.gameStatus;
    state.lastPlayedPlayer = payload.lastPlayedPlayer;
    if (payload.gameStatus == "IN_PROGRESS") {
      state.gameActive = true;
    }
  },
  gameResumed(state) {
    state.gameActive = true;
    state.gameStatus = "IN_PROGRESS";
    state.notification.message = "Game " + state.gameId + " resumed!";
    state.notification.variant = "success";
  },
  gamePaused(state) {
    state.gameStatus = "PAUSED";
    state.gameActive = false;
    state.opponentActive = false;
    state.notification.message = "Game " + state.gameId + " paused!";
    state.notification.variant = "primary";
  },
  gameFinished(state) {
    state.gameStatus = "FINISHED";
    state.gameActive = false;
    state.opponentActive = false;
    state.notification.message = "You resigned from game " + state.gameId + "!";
    state.notification.variant = "primary";
    // udpatae game results
  },
  gameFailedToResume(state) {
    state.gameActive = false;
    state.opponentActive = false;
    state.notification.message = "Game failed to resume! ";
    state.notification.variant = "danger";
  },
  gameFailedToPause(state) {
    state.notification.message = "Game failed to pause! ";
    state.notification.variant = "danger";
  },
  gameFailedToFinish(state) {
    state.notification.message = "Game failed to finish! ";
    state.notification.variant = "danger";
  },
  myTurn(state) {
    state.myTurn = true;
  },
  notMyturn(state) {
    state.myTurn = false;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};

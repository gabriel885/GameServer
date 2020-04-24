import Axios from "axios";

const axios = Axios.create({
  baseURL: "http://10.0.0.3:8881/api/game",
  headers: {
    // allow cross origin
    "Access-Control-Allow-Origin": "http://10.0.0.3:8881",
    Accept: "application/json",
    "Content-Type": "application/json",
  },
  timeout: 1000, // default after 1 second
});

// {gameType} - ["CHESS"]
// {statusType} = ["WAITING_FOR_OPPONENT","IN_PROGRESS","PAUSED", "FIRST_PLAYER_WON", "SECOND_PLAYER_WON"]

/******* /api/game ********/
// /resume - GET - get logged in user's game to resume
// /all - GET - get all games
// /status/{statusType} - GET - get all games with statusType status
// /type/{gameType} - GET - get all games by game type
// /{gameType}/{statusType} - GET - get all games by game type and status
// /player/{playerUsername} - GET - get all games for a specific player
// /players/{playerUsername1}/{playerUsername2} - GET - get all games for player1 and for player2

/******** /api/game/{gameId} *********/
// / - GET - get game by id
// / - DELETE - delete game by id
// /join - POST - join game by id

// /resume - POST - resume playing game
// /pause - POST - pause game
// /finish - POST - finish game

// /turn - GET - return true if it's logged-in player's turn, otherwise return false

/*******  /api/game/{gameType} ********/
// / - GET - get all games by game type
// /create - POST - create new game
// /{gameId}/moves - GET - return all game moves
// /{gameId}/move/add - POST - add move to game

export default {
  async getGameByStatus(gameStatus) {
    const respPromise = await axios
      .get("/status/" + gameStatus)
      .then((resp) => {
        console.log(resp);
        return resp.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getGamesByType(gameType) {
    const respPromise = await axios
      .get("/type/" + gameType)
      .then((resp) => {
        return resp.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  // return all active games for logged in user
  async getGamesToJoin() {
    const respPromise = await axios
      .get("/status/waiting_for_opponent")
      .then((response) => {
        console.log(response);
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  //
  async getGamesToResume() {
    const respPromise = await axios
      .get("/resume")
      .then((response) => {
        console.log(response);
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  // get all games
  async getGames() {
    const respPromise = await axios
      .get("/all")
      .then((response) => {
        console.log(response);
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getChessGamesToJoin() {
    const respPromise = await axios
      .get("/chess/waiting_for_opponent")
      .then((resp) => {
        console.log(resp);
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  // create chess game
  async createChessGame() {
    const respPromise = await axios
      .post("/chess/create")
      .then((response) => {
        console.log(response);
        return response.data; // created GameDTO
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  // join specific game
  async joinGame(gameId) {
    const respPromise = await axios
      .post(gameId + "/join")
      .then((response) => {
        console.log(response.data);
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  // get checkers game
  async getGame(gameId) {
    const respPromise = await axios
      .get(gameId)
      .then((response) => {
        console.log(response);
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  // delete checkers game
  deleteGame(gameId) {
    console.log("deleting checkers game " + gameId);
    axios
      .delete("/" + gameId)
      .then((resp) => {
        console.log(resp);
      })
      .catch((err) => {
        console.log(err);
      });
  },
  gameExists(gameId) {
    return this.getGame(gameId).id == null;
  },
  async getFinishedGames() {
    const respPromise = await axios
      .get("/finished")
      .then((response) => {
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getFinishedChessGames() {
    const respPromise = await axios
      .get("/chess/finished")
      .then((response) => {
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getFinishedCheckersGame() {
    const respPromise = await axios
      .get("/checkers/finished")
      .then((response) => {
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getChessMoves(gameId) {
    const respPromise = await axios
      .get("/chess/" + gameId + "/moves")
      .then((resp) => {
        console.log(resp);
        return resp.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async resumeGame(gameId) {
    const respPromise = await axios
      .post(gameId + "/resume")
      .then((resp) => {
        console.log(resp);
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async pauseGame(gameId) {
    const respPromise = await axios
      .post(gameId + "/pause")
      .then((resp) => {
        console.log(resp);
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async resignGame(gameId) {
    const respPromise = await axios
      .post(gameId + "/resign")
      .then((resp) => {
        console.log(resp); // finished operation
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async finishGame(gameId) {
    // resign from game
    const respPromise = await axios
      .post(gameId + "/finish")
      .then((resp) => {
        console.log(resp); // finished operation
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getColor(gameId) {
    const respPromise = await axios
      .get(gameId + "/color")
      .then((resp) => {
        return resp.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getOpponent(gameId) {
    const respPromise = await axios
      .get(gameId + "/opponent")
      .then((resp) => {
        return resp.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  // return true if it's player's turn, otherwise return false
  async isTurn(gameId) {
    const respPromise = await axios
      .get(gameId + "/turn")
      .then((resp) => {
        if (resp.data == true) {
          return true;
        }
        return false;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getGameStatus(gameId) {
    const respPromise = await axios
      .get(gameId)
      .then((resp) => {
        return resp.data.gameStatus;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  gameAlive(gameId) {
    console.log("Pinged alive signal for game " + gameId);
    axios.get(gameId + "/alive");
  },

  //   private int boardRow;
  //   private int boardColumn;
  //   private ChessPiece chessPiece;

  async commitChessMove(gameId, move) {
    console.log("commiting move " + move);

    var formData = new FormData();

    formData.append("fromBoardRow", move.fromBoardRow);
    formData.append("fromBoardColumn", move.fromBoardColumn);
    formData.append("toBoardRow", move.toBoardRow);
    formData.append("toBoardColumn", move.toBoardColumn);
    formData.append("chessPiece", move.piece.toUpperCase());
    formData.append("color", move.color.toUpperCase());
    formData.append("moveCount", move.moveCount);
    formData.append("pieceId", move.pieceId);

    const respPromise = await axios({
      method: "POST",
      url: "/chess/" + gameId + "/move/add",
      data: formData,
    })
      .then((resp) => {
        return resp;
      })
      .catch((err) => {
        // print error.reponse
        // return loginFailedMessage with error.respose.statusText reason?
        console.error(err);
      });

    // ping /auth/status to check if login succeded and return true/false?
    return respPromise;
  },

  getUri() {
    return axios.defaults.baseURL;
  },
};

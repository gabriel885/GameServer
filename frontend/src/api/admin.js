import Axios from "axios";

const axios = Axios.create({
  baseURL: "http://10.0.0.3:8881/api/admin",
  headers: {
    // allow cross origin
    "Access-Control-Allow-Origin": "http://10.0.0.3:8881",
    Accept: "application/json",
    "Content-Type": "application/json",
  },
  timeout: 2000, // default after 2 second
});

/******* /api/admin *********/
// /api/admin/status - GET - return status code 200 if admin is authenticated, otherwise 401
// /api/admin/players - GET - return all players

/******* /api/admin/player ******/
// /{username} - GET - get player
// /{username} - DELETE - delete player
// /{username}/game - GET - get all games for player
// /{username}/game/{gameStatus} - GET - get all active games for player by status

export default {
  async getAllPlayers() {
    const respPromise = await axios
      .get("/players")
      .then((response) => {
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getPlayer(username) {
    const respPromise = await axios
      .get("/player/" + username)
      .then((response) => {
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async deletePlayer(username) {
    const respPromise = await axios
      .delete("/player/" + username)
      .then((response) => {
        console.log(response);
        return response;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getGamesByPlayer(username) {
    const respPromise = await axios
      .get("/player/" + username + "/game")
      .then((response) => {
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
  async getGamesByPlayerAndStatus(username, gameStatus) {
    const respPromise = await axios
      .get("/player/" + username + "/game/" + gameStatus)
      .then((response) => {
        return response.data;
      })
      .catch((err) => {
        console.log(err);
      });
    return respPromise;
  },
};

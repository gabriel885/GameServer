import store from "../../store";

const gameStatus = {
  eventType: "gameStatus",
  handle: (event) => {
    const parsed = JSON.parse(event.data);

    console.log("Received gameStatus event:  " + parsed);

    const gameStatus = parsed.gameStatus;

    // update game state
    store.commit("updateGameStatus", gameStatus);

    var notification;

    switch (gameStatus) {
      case "IN_PROGRESS":
        notification = { message: "Opponent resumed game", variant: "success" };
        break;
      case "PAUSED":
        notification = { message: "Opponent paused game", variant: "danger" };
        break;
      default:
        notification = {
          message: "Opponent changed game status to " + gameStatus,
          variant: "danger",
        };
    }

    // notify user opponent changed game status!
    store.commit("updateNotification", notification);
  },
};

export default gameStatus;

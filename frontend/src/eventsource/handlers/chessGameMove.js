import store from "../../store";

const chessGameMove = {
  eventType: "chessGameMove",
  handle: (event) => {
    const parsed = JSON.parse(event.data);

    const move = parsed.gameMove;
    // console.log("Receieved move from server");
    // console.log(move);

    store.commit("addMove", move);

    store.dispatch("notify", {
      message: "Player made move",
      variant: "info",
    });

    store.dispatch("isMyTurn"); // check whos turn
  },
};
export default chessGameMove;

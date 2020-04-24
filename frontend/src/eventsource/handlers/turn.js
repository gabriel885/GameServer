import store from "../../store";

const turn = {
  eventType: "turn",
  handle: (event) => {
    const parsed = JSON.parse(event.data);
    if (parsed.turn == store.getters.getUsername.toString()) {
      store.commit("MyTurn");
    }
    if (parsed.turn == store.getters.getOpponent.username.toString()) {
      store.commit("notMyturn");
    }
  },
};
export default turn;

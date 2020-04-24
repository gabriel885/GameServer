import store from "../../store";

const opponentActive = {
  eventType: "alive",
  handle: (event) => {
    const parsed = JSON.parse(event.data);
    // check that opponent name is not my name
    if (store.getters.getUsername.toString().trim() != parsed.username.trim()) {
      console.log("Received alive event from opponent: " + parsed.username);
      store.commit("opponentActive");
    }
  },
};
export default opponentActive;

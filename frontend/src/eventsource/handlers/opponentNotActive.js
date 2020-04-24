import store from "../../store";

const opponentNotActive = {
  eventType: "inactive",
  handle: () => {
    store.commit("updateNotification", {
      message: "Opponent is not active",
      variant: "danger",
    });
    store.commit("opponentNotActive");
  },
};
export default opponentNotActive;

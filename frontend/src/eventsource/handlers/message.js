import store from "../../store";

const message = {
  eventType: "message",
  handle: (event) => {
    // server message
    store.commit("updateNotification", {
      message: event.data,
      variant: "primary",
    });
  },
};
export default message;

<template>
  <td>
    <button type="button" @click="joinGame(gameId)" class="btn btn-primary">
      <i class="fas fa-user-plus"></i>
    </button>
    <button type="button" @click="deleteGame(gameId)" class="btn btn-danger">
      <i class="far fa-trash-alt"></i>
    </button>
  </td>
</template>

<script>
import api from "../api/game";
import { mapActions } from "vuex";

export default {
  props: ["gameType", "gameId"],
  methods: {
    ...mapActions(["notify"]),
    joinGame(gameId) {
      console.log("joining game " + gameId);
      // join game
      api
        .joinGame(gameId)
        .then((resp) => {
          this.notify({ message: resp, variant: "primary" });
          console.log(resp);
        })
        .catch((err) => {
          this.notify({ message: err, variant: "danger" });
          console.log(err);
        });
    },
    deleteGame(gameId) {
      console.log("deleting game " + gameId);
      api.deleteGame(gameId);
      // check that game does not exists
      if (!api.gameExists(gameId)) {
        this.notify({
          message: "Game " + gameId + " deleted",
          variant: "primary",
        });
      }
    },
  },
};
</script>

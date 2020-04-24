<template>
  <td>
    <button
      type="button"
      @click="deletePlayer(username)"
      class="btn btn-danger"
    >
      <i class="far fa-trash-alt"></i>
    </button>
  </td>
</template>

<script>
import api from "../api/admin";
import { mapActions } from "vuex";

export default {
  props: ["username"],
  methods: {
    ...mapActions(["notify"]),
    deletePlayer(username) {
      api
        .deletePlayer(username)
        .then(() => {
          this.notify({
            message: "Player " + username + " deleted ",
            variant: "info",
          });
        })
        .catch((err) => {
          this.notify({ message: err, variant: "danger" });
          console.log(err);
        });
    },
  },
};
</script>

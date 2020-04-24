<template>
  <!-- create games -->
  <div class="card-deck mb-3 text-center">
    <div class="card mb-4 box-shadow">
      <div class="card-header">
        <h4 class="my-0 font-weight-normal">Chess</h4>
      </div>
      <div class="card-body">
        <img src="@/assets/img/chess.png" alt="chess game" />
        <form @submit.prevent="createChess">
          <input type="hidden" id="gameType" name="gameType" />
          <button
            type="submit"
            class="btn btn-lg mt-5 btn-block btn-outline-primary"
          >
            Create Game
          </button>
        </form>
      </div>
    </div>
    <!-- <div class="card mb-4 box-shadow">
      <div class="card-header">
        <h4 class="my-0 font-weight-normal">Checkers</h4>
      </div>
      <div class="card-body">
        <img src="@/assets/img/checkers.png" alt="checkers game" />
        <form @submit.prevent="createCheckers">
          <input type="hidden" />
          <button
            type="submit"
            class="btn btn-lg mt-5 btn-block btn-outline-primary"
          >
            Create Game
          </button>
        </form>
      </div>
    </div> -->
  </div>
</template>

<script>
import api from "../api/game";
import { mapActions } from "vuex";
export default {
  date() {
    return {};
  },
  methods: {
    ...mapActions(["notify"]),
    createChess() {
      // call api POST on /api/game/chess/create
      console.log("creating chess game");
      api
        .createChessGame()
        .then((response) => {
          console.log("received response");
          console.log(response);
          this.notify({ message: response, variant: "primary" });
        })
        .catch((err) => {
          // pop error notification
          this.notify({ message: err, variant: "danger" });
          console.err(err);
        });
    },
    createCheckers() {
      // call api POST on /api/game/checkers/create
      console.log("creating checkers game");
      api
        .createCheckersGame()
        .then((response) => {
          // pop notification with created game -> waiting for opponent
          this.notify({
            message: response,
            variant: "primary",
          });
          console.debug(response);
        })
        .catch((err) => {
          this.notify({ message: err, variant: "danger" });
          console.err(err);
        });
    },
  },
};
</script>

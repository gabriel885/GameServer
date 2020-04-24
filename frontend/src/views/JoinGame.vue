<template>
  <div>
    <!-- if paused a game -> allow to resume -->
    <h3>Game to Join:</h3>
    <game-table :games="gamesToJoin" />
  </div>
</template>

<script>
import gameTable from "../components/game-table";

import api from "../api/game";
export default {
  components: {
    gameTable,
  },
  data() {
    return {
      gamesToJoin: [], // games waiting to opponent
    };
  },
  mounted() {
    this.getGamesToJoin();
  },
  methods: {
    getGamesToJoin() {
      api
        .getGamesToJoin()
        .then((games) => {
          this.gamesToJoin = games;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

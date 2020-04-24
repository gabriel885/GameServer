<template>
  <div>
    <h3>Active games</h3>
    <game-table :games="activeGames" />
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
      activeGames: [], // games waiting to opponent
    };
  },
  mounted() {
    this.getGamesToResume();
  },
  methods: {
    getGamesToResume() {
      api
        .getGameByStatus("IN_PROGRESS")
        .then((games) => {
          this.activeGames = games;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

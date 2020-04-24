<template>
  <div>
    <!-- if paused a game -> allow to resume -->
    <h3>Finished Games</h3>
    <game-table :games="finishedGames" />
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
      finishedGames: [], // games waiting to opponent
    };
  },
  mounted() {
    this.getFinishedGames();
  },
  methods: {
    getFinishedGames() {
      api
        .getFinishedGames()
        .then((games) => {
          this.finishedGames = games;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

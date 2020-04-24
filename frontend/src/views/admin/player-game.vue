<template>
  <div>
    <h1>Games for Player {{ username }}</h1>
    <game-table :games="games" />
  </div>
</template>

<script>
import api from "../../api/admin";
import GameTable from "../../components/game-table";
export default {
  props: {
    username: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      games: [],
    };
  },
  components: {
    GameTable,
  },
  mounted() {
    this.getGamesByPlayer(this.username);
  },
  methods: {
    getGamesByPlayer(username) {
      api
        .getGamesByPlayer(username)
        .then((response) => {
          this.games = response;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

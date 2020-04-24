<template>
  <div>
    <h1>All players</h1>
    <!-- all players in game -->
    <players-table :players="players" />
  </div>
</template>

<script>
import PlayersTable from "../../components/player-table";
import api from "../../api/admin";

export default {
  data() {
    return {
      players: [],
    };
  },
  components: {
    PlayersTable,
  },
  mounted() {
    this.getAllPlayers(); // update game tables
  },
  methods: {
    getAllPlayers() {
      // get all players
      api
        .getAllPlayers()
        .then((players) => {
          console.log(players);
          this.players = players;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

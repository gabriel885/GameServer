<template>
  <div>
    <game-table v-if="resumeGames.length > 0" :games="resumeGames" />
    <create-games />
  </div>
</template>

<script>
// GameDTO
// {"id": 27,
//           "firstPlayer": {
//             "username": "admin",
//             "password": "$2a$10$LW4nOp9aswBS9w7L0kOOYuy7bek6i0St4zWXV78TwebAvp5P7FjJK",
//             "email": "admin@example.com",
//             "rating": 0},
//           "secondPlayer": null,
//           "created": "2020-03-28T16:47:44.541+0000",
//           "gameType": "CHESS",
//           "gameStatus": "WAITING_FOR_OPPONENT",
//           "lastPlayedPlayer": null
//         }
import createGames from "../components/create-games";
import gameTable from "../components/game-table";
import api from "../api/game";
export default {
  components: {
    createGames,
    gameTable,
  },
  watch: {
    paused() {},
  },
  data() {
    return {
      hasGamesToResume: false,
      resumeGames: [], // games to resume
    };
  },
  mounted() {
    this.getGamesToResume();
  },
  methods: {
    getGamesToResume() {
      api
        .getGamesToResume()
        .then((games) => {
          this.resumeGames = games;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

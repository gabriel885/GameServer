<template>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th scope="col">ID</th>
        <th scope="col">Game Type</th>
        <th scope="col">Game Status</th>
        <th scope="col">First Player</th>
        <th scope="col">Second Player</th>
        <th scope="col">Created</th>
        <th scope="col">Actions</th>
      </tr>
    </thead>
    <game-details v-for="game in games" :key="game.id" :game="game" />
  </table>
</template>

<script>
import gameDetails from "./game-details";
export default {
  components: {
    gameDetails,
  },
  props: ["games"],
};
</script>

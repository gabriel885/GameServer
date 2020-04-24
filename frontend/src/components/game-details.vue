<template>
  <!-- game datails -->
  <tbody>
    <tr>
      <td>
        <a :href="'/' + game.gameType + '/' + game.id"> {{ game.id }} </a>
        <!-- <router-link
          class="nav-link"
          :to="{
            name: game.gameType,
            params: { gameId: game.id },
          }"
          >{{ game.id }}</router-link
        > -->
      </td>
      <td>{{ game.gameType }}</td>
      <td>{{ game.gameStatus }}</td>
      <td>{{ game.firstPlayer.username }}</td>
      <td v-if="game.secondPlayer">{{ game.secondPlayer.username }}</td>
      <td v-else>UNDEFINED</td>
      <td>{{ game.created }}</td>
      <game-actions :gameId="game.id" :gameType="game.gameType" />
    </tr>
  </tbody>
</template>

<script>
import gameActions from "./game-actions";

export default {
  props: ["game"],
  components: {
    gameActions,
  },
};
</script>
<style scoped>
tr {
  white-space: nowrap;
}
</style>

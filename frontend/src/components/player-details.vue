<template>
  <!-- game datails -->
  <tbody>
    <tr>
      <td>
        <router-link
          class="nav-link"
          :to="{ name: 'PlayerGames', params: { username: player.username } }"
          >{{ player.username }}</router-link
        >
      </td>
      <td>{{ player.email }}</td>
      <td>{{ player.rating }}</td>
      <player-actions :username="player.username" />
    </tr>
  </tbody>
</template>

<script>
//import api from "../api/game";
import playerActions from "./player-actions";
export default {
  props: ["player"],
  components: {
    playerActions,
  },
};
</script>

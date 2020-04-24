<template>
  <div class="row">
    <div class="col-12">
      <table class="table table-bordered">
        <thead>
          <tr>
            <th scope="col">Username</th>
            <th scope="col">Email</th>
            <th scope="col">Rating</th>
            <th scope="col">Actions</th>
          </tr>
        </thead>
        <player-details
          v-for="player in players"
          :key="player.username"
          :player="player"
        />
      </table>
    </div>
  </div>
</template>

<script>
import playerDetails from "./player-details";
export default {
  props: {
    players: {
      type: Array,
    },
  },
  components: {
    playerDetails,
  },
};
</script>

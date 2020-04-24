<template>
  <div class="user-details container" v-if="getIsAuthenticated">
    <span><b>Username:</b> {{ getUser.username }}</span>
    <span><b>Email:</b> {{ getUser.email }} </span>
    <span><b>Rating</b> {{ getUser.rating }}</span>
  </div>
</template>

<style scoped>
.user-details {
  margin: auto;
  display: inline-block;
}
span {
  padding: 5px;
}
</style>
<script>
import { mapGetters } from "vuex";

export default {
  computed: mapGetters(["getIsAuthenticated", "getUser"]),
};
</script>

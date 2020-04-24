<template>
  <div id="app">
    <!-- if there is an error connecting to the REST API backend -->
    <template v-if="!healthy">
      <div class="container wrapper">
        <p>Failed to connect to REST API backend</p>
      </div>
    </template>
    <!-- api healthcheck passed -->
    <template v-else>
      <navbar />
      <user-details />
      <!-- notification -->
      <notification
        :variant="getNotification.variant"
        :message="getNotification.message"
        :hideOnBlur="true"
      />
      <router-view class="container-fluid wrapper" />
    </template>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import navbar from "./components/Navbar";
import userDetails from "./components/profile/user-details";
import notification from "./components/notification";
import auth from "./api/auth";

export default {
  components: {
    navbar,
    userDetails,
    notification,
  },
  data() {
    return {
      healthy: true, // API backend is healthy
    };
  },
  created() {
    // important! maintain user state on every page inside App
    this.pingBackend();
  },
  updated() {
    this.fetchUser();
  },
  // getters should be computed
  computed: mapGetters(["getIsAuthenticated", "getUser", "getNotification"]),
  methods: {
    // perform authentication checks
    ...mapActions(["isAuthenticated", "fetchUser"]),
    pingBackend() {
      console.log("running health check");
      auth
        .healthCheck()
        .then((flag) => {
          this.healthy = flag;
        })
        .then(() => {
          if (this.healthy) {
            // if healthy authenticate
            // check if user is authenticated
            this.isAuthenticated();
            this.fetchUser(); // fetch user to maintain state
          }
        });
    },
  },
};
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>

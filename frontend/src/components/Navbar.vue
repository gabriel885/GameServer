<template>
  <div>
    <nav
      class="navbar navbar-expand-lg navbar-dark bg-dark"
      v-bind:class="{ navbarOpen: show }"
    >
      <!-- Navbar content -->
      <a class="navbar-brand" href="/">Online Gaming Portal</a>
      <button
        class="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarText"
        aria-controls="navbarText"
        aria-expanded="false"
        aria-label="Toggle navigation"
        @click.stop="toggleNavbar()"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div
        class="collapse navbar-collapse"
        id="navbarText"
        v-bind:class="{ show: show }"
      >
        <ul v-if="getIsAuthenticated" class="navbar-nav">
          <li class="nav-item">
            <router-link class="nav-link" to="/join">Join Games</router-link>
            <!-- <a class="nav-link active" th:href="@{/}">Games</a> -->
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/active"
              >Active Games</router-link
            >
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/history"
              >History Games</router-link
            >
          </li>
          <li v-if="getIsAdmin" class="nav-item">
            <router-link class="nav-link" to="/join">Admin</router-link>
            <!-- <a class="nav-link active" th:href="@{/}">Games</a> -->
          </li>
        </ul>
        <ul class="navbar-nav ml-auto navbar-right">
          <li v-if="getIsAuthenticated" class="nav-item">
            <a class="nav-link btn-outline-success disabled">{{
              getUsername
            }}</a>
          </li>
          <template v-if="getIsAuthenticated">
            <li class="nav-item">
              <form @submit.prevent="logout()">
                <button class="btn btn-md btn-danger btn-block" type="Submit">
                  Logout
                </button>
              </form>
            </li>
          </template>
          <template v-else>
            <li class="nav-item">
              <router-link class="nav-link" to="/login">Login</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/register"
                >Register</router-link
              >
            </li>
          </template>
        </ul>
      </div>
    </nav>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  name: "Navbar",
  data() {
    return {
      show: false,
    };
  },
  computed: mapGetters(["getUsername", "getIsAuthenticated", "getIsAdmin"]),
  methods: {
    ...mapActions(["logout"]),
    toggleNavbar() {
      this.show = !this.show;
    },
  },
};
</script>

<style scoped>
.router-link-active {
  /* active link */
  color: #fff;
}
</style>

<template>
  <div>
    <!-- if authenticated redirect to profile page -->
    <form @submit.prevent="callLogin" class="form-signin">
      <!-- <form method="post" action="/login" class="form-signin"> -->
      <h3 class="form-signin-heading">Welcome</h3>
      <br />
      <input
        type="text"
        v-model="userData.username"
        id="username"
        name="username"
        placeholder="Username"
        class="form-control"
        required
      />
      <br />
      <input
        type="password"
        v-model="userData.password"
        id="password"
        name="password"
        placeholder="Password"
        class="form-control"
        required
      />
      <br />
      <div align="center" v-if="getAuthError">
        <!--  <p style="font-size: 20; color: #FF1C19;">Username or Password invalid, please verify</p> -->
        <p style="font-size: 20; color: #ff1c19;">
          Failed to login. Bad credentials
        </p>
      </div>
      <button
        class="btn btn-lg btn-primary btn-block"
        name="Submit"
        value="Login"
        type="Submit"
      >
        Login
      </button>
    </form>
  </div>
</template>
<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "Login",
  data() {
    return {
      // login form data
      userData: {
        username: "",
        password: "",
      },
    };
  },
  computed: mapGetters(["getAuthError"]),
  methods: {
    ...mapActions(["login"]),
    callLogin() {
      this.login({
        username: this.userData.username,
        password: this.userData.password,
      });
    },
  },
};
</script>

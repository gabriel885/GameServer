<template>
  <!-- TODO: if authenticated -> redirect to root page -->
  <form
    autocomplete="off"
    @submit.prevent="callRegister"
    class="form-signin"
    role="form"
  >
    <!-- try method post directly to /auth/register -->
    <h3 class="form-signin-heading">Registration</h3>

    <input
      type="text"
      id="username"
      name="username"
      v-model="userData.username"
      placeholder="Username"
      class="form-control"
      minlength="5"
      maxlength="25"
      required
    />
    <br />
    <input
      type="email"
      id="email"
      name="email"
      v-model="userData.email"
      placeholder="Email"
      class="form-control"
      required
    />
    <br />
    <input
      type="password"
      id="password"
      name="password"
      v-model="userData.password"
      placeholder="Password"
      class="form-control"
      minlength="5"
      required
    />
    <button type="submit" class="btn btn-primary btn-block">
      Register User
    </button>

    <h2>
      <span class="text-success">{{ successMessage }}</span>
      <span class="text-danger">{{ errorMessage }}</span>
    </h2>
  </form>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      userData: {
        username: "",
        password: "",
        email: "",
      },
      successMessage: "",
      errorMessage: "",
    };
  },
  methods: {
    callRegister() {
      console.log(
        "Registering username: " +
          this.userData.username +
          " with password: " +
          this.userData.password +
          " and email: " +
          this.userData.email
      );
      this.errorMessage = "";
      this.successMessage = "";

      const formData = new FormData();
      formData.append("username", this.userData.username);
      formData.append("password", this.userData.password);
      formData.append("email", this.userData.email);
      console.log("created form " + formData.values());

      axios({
        method: "POST",
        url: "/auth/register",
        headers: {
          "Content-Type": `multipart/form-data; boundary=${formData._boundary}`,
        },
        data: formData,
      })
        .then((response) => {
          console.log(response);
          if (response.status == 200) {
            // user is redirected to profile page
            // authenticate user
            this.successMessage = "User Registered Successfully!";
          } else {
            this.errorMessage = response.data;
          }
        })
        .catch((error) => {
          console.log("An error occured " + error);
          this.errorMessage = error.response.data;
        });
    },
  },
};
</script>

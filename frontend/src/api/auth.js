import Axios from "axios";

const axios = Axios.create({
  baseURL: "http://10.0.0.3:8881",
  headers: {
    "Access-Control-Allow-Origin": "http://10.0.0.3:8881",
    //'Accept': '',
    //'Content-Type': 'application/x-www-form-urlencoded'
    "Content-Type": "application/json; multipart/form-data",
  },
  timeout: 1000, // default after 1 second
});

// TODO: this should be run as Promise!
export default {
  // test REST API connection
  async healthCheck() {
    console.log("Running health check against " + "/api/health");
    /// axios already makes it json
    const respPromise = await axios
      .get("/api/health")
      .then((response) => {
        if (response.status == 200) {
          console.log("REST API backend is healthy!");
          return true;
        }
        return false;
      })
      .catch((err) => {
        console.log(
          "failed to run healcheck against /api/health . Reason " + { err }
        );
        return false;
      });
    return respPromise;
  },
  async fetchUser() {
    const respPromise = await axios({
      method: "GET",
      url: "/auth/user",
    })
      .then((response) => {
        return { status: 200, message: response.data };
      })
      .catch((error) => {
        console.log("fetchUser error: " + error);
        return { status: 400, message: error.response.statusText };
      });
    return respPromise;
  },
  async authenticated() {
    const respPromise = await axios({
      method: "GET",
      url: "/auth/status",
    })
      .then((response) => {
        if (response.status == 200) {
          console.log("user is authenticated");
          return true;
        }
        return false;
      })
      .catch(() => {
        console.log("user is not authenticated");
        return false;
      });
    return respPromise;
  },
  async isAdmin() {
    const respPromise = await axios({
      method: "GET",
      url: "/api/admin/status",
    })
      .then((response) => {
        if (response.status == 200) {
          console.log("user is admin");
          return true;
        }
        return false;
      })
      .catch(() => {
        console.log("user is not admin");
        return false;
      });
    return respPromise;
  },
  async register(username, password, email) {
    // register user
    var formData = new FormData();
    formData.append("username", username);
    formData.append("password", password);
    formData.append("email", email);
    formData.append("rating", 0);
    console.log(
      "created form " +
        formData.get("username") +
        " " +
        formData.get("password") +
        " " +
        formData.get("email")
    );

    const headers = {
      Accept: "",
      "Content-Type": "application/json",
    };

    const respPromise = await Axios.post("/auth/register", formData, headers)
      .then((response) => {
        console.log("Register response: " + response);
        if (response.status == 200) {
          console.log("Registered user " + username + " successfully");
        }
        return { status: response.status, message: response.data };
      })
      .catch((err) => {
        console.log("ERROR - Register response: " + err);
        return { status: 400, message: err.response.statusText };
      });
    return respPromise;
  },
  async login(username, password) {
    var formData = new FormData();
    formData.append("username", username);
    formData.append("password", password);

    const respPromise = await axios({
      method: "POST",
      url: "login",
      data: formData,
    })
      .then((resp) => {
        console.log(resp);
        return { status: resp.status, message: resp.data };
      })
      .catch((err) => {
        // print error.reponse
        // return loginFailedMessage with error.respose.statusText reason?
        console.log(err);
        return {
          status: err.response.status,
          message: err.response.statusText,
        };
      });

    // ping /auth/status to check if login succeded and return true/false?
    return respPromise;
  },
  async logout() {
    // log out user
    const respPromise = await axios
      .get("/logout")
      .then((response) => {
        // if 200 or 302 logout succeeded
        if (response.status == 200 || response.status == 302) {
          return true;
        }
        return false;
      })
      .catch((err) => {
        console.log(err);
        console.log("Failed to logout. Reason: " + err.response);
        return err;
      });
    return respPromise;
  },
};

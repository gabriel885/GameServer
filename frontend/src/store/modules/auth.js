import auth from "../../api/auth";

import router from "../../router";

const loginSuccessRedirect = "/";
const logoutSuccessRedirect = "/logout";

const state = {
  // user state
  userData: {
    username: "",
    email: "",
    rating: 0,
  },
  isAuthenticated: false,
  isAdmin: false,
  authError: false,
};

const getters = {
  getIsAuthenticated: (state) => state.isAuthenticated, // return authenticated state (authorized or not)
  getUser: (state) => state.userData, // returned logged in user data
  getUsername: (state) => state.userData.username,
  getAuthError: (state) => state.authError,
  getIsAdmin: (state) => state.isAdmin,
};

const actions = {
  // can by async
  // login user
  login({ commit }, { username, password }) {
    // max is 2 parameters
    auth
      .login(username, password)
      .then((response) => {
        console.log("Received auth.login() response " + { response });
        // in case the server redirects a successfull login
        if (response.status == 302 || response.status == 200) {
          console.log("Login success");
          commit("login_success", response.username); // mutate authentication state
          console.log("redirecting user to route " + loginSuccessRedirect);
          router.push(loginSuccessRedirect); // redirect to login success
        } else {
          commit("login_failed");
        }
      })
      .catch((error) => {
        console.log("An error occured " + { error });
        commit("login_failed"); // not authenticated
      });
  },
  // logout user
  logout({ commit }) {
    // wait for logging out
    auth
      .logout()
      .then((successFlag) => {
        if (successFlag) {
          console.log("logged out successfully");
          commit("logoutSuccess"); // mutate user state
          router.push(logoutSuccessRedirect); // redirect to login success
        }
      })
      .catch((err) => {
        console.log("failed to logout: " + err);
      });
  },
  //fetch user credentials
  fetchUser({ commit }) {
    auth
      .fetchUser()
      .then((payload) => {
        if (payload.status == 200) {
          commit("updateUser", {
            username: payload.message.username,
            email: payload.message.email,
            rating: payload.message.rating,
          });
        }
      })
      .catch((error) => {
        console.error("Failed to fetchUser. Error " + error);
      });
  },
  // check if user is authorized
  isAuthenticated({ commit }) {
    auth
      .authenticated()
      .then((authFlag) => {
        if (authFlag) {
          console.log("committing authentication");
          commit("authenticated");
        } else {
          commit("nonAuthenticated");
        }
      })
      .catch((errorFlag) => {
        console.log(errorFlag);
        commit("nonAuthenticated");
      });
  },
  isAdmin({ commit }) {
    auth.isAdmin().then((authFlag) => {
      if (authFlag) {
        commit("adminAuthenticated");
      }
    });
  },
};

const mutations = {
  // must be synchronous
  updateUser(state, payload) {
    state.userData.username = payload.username;
    state.userData.email = payload.email;
    state.userData.rating = payload.rating;
  },
  nonAuthenticated(state) {
    state.isAuthenticated = false;
    state.isAdmin = false;
  },
  authenticated(state) {
    state.isAuthenticated = true;
    state.authError = false;
  },
  login_success(state, username) {
    state.userData.username = username;
    state.isAuthenticated = true;
  },
  login_failed(state) {
    // flush all state regarding user
    state.isAuthenticated = false;
    state.isAdmin = false;
    state.userData.username = "";
    state.userData.email = "";
    state.userData.rating = 0;
    state.authError = true;
  },
  logoutSuccess(state) {
    state.isAuthenticated = false;
    state.isAdmin = false;
    state.userData.username = "";
    state.userData.email = "";
    state.userData.rating = 0;
    state.authError = false;
  },
  adminAuthenticated(state) {
    state.isAdmin = true;
  },
};

export default {
  state,
  getters,
  actions,
  mutations,
};

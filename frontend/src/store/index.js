import Vue from "vue";
import Vuex from "vuex";

// manage user state accross application
import auth from "./modules/auth";
import game from "./modules/game";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  getters: {},
  actions: {},
  mutations: {},
  modules: { auth, game },
});

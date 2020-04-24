import "@babel/polyfill";
import "mutationobserver-shim";
import Vue from "vue";
import "./plugins/bootstrap-vue";
import "./assets/css/global.css";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import VueResource from "vue-resource";
import VueLogger from "vuejs-logger";

// bootstrap
import { BootstrapVue } from "bootstrap-vue";
// bootstrap css
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";

// font awesome
import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/js/all.js";

Vue.use(BootstrapVue);
Vue.use(VueResource);

Vue.config.productionTip = false;

const options = {
  isEnabled: true,
  logLevel: "debug",
  stringifyArguments: false,
  showLogLevel: true,
  showMethodName: false,
  separator: "|",
  showConsoleColors: true,
};
Vue.use(VueLogger, options);

new Vue({
  router,
  store, // profile details, game state
  render: (h) => h(App),
}).$mount("#app");

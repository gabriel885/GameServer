import Vue from "vue";
import VueRouter from "vue-router";
import LoginView from "../views/Login.vue";
import RegistrationView from "../views/Registration.vue";
import ErrorView from "../views/Error.vue";
import GameView from "../views/Game.vue";
import GameHistoryView from "../views/GameHistory.vue";
import GamesToJoinView from "../views/JoinGame.vue";
import ActiveGamesToView from "../views/ActiveGames.vue";
import Chess from "../views/Chess";

Vue.use(VueRouter);

// expost REST API requests with axios
const routes = [
  {
    // route reconrd
    path: "/", // game portal home page
    name: "Game",
    component: GameView,
    meta: {
      title: "Portal",
      requiredAuth: true,
    },
  },
  {
    path: "/history",
    name: "GameHistory",
    component: GameHistoryView,
    meta: {
      title: "Finished Games",
    },
  },
  {
    path: "/join",
    name: "JoinGame",
    component: GamesToJoinView,
    meta: {
      title: "Games to join",
    },
  },
  {
    path: "/active",
    name: "ActiveGames",
    component: ActiveGamesToView,
    mets: {
      title: "Games to resume playing",
    },
  },
  {
    path: "/chess/:gameId",
    name: "CHESS",
    component: Chess,
    meta: {
      title: "Chess Game",
    },
    props: true,
  },
  {
    path: "/admin",
    name: "Admin",
    meta: {
      title: "Admin Panel",
    },
    component: () => import("../views/admin/home"),
    children: [
      {
        path: "/players",
        name: "Players",
        component: () => import("../views/admin/all-players"), // main admin view
      },
      {
        path: "/player/:username", // games per player
        name: "PlayerGames",
        component: () => import("../views/admin/player-game"),
        props: true,
      },
    ],
  },
  {
    path: "/login",
    name: "Login",
    component: LoginView,
    meta: {
      title: "Login",
    },
    // TODO: for authorized users redirect to '/' homepage + reload
  },
  {
    path: "/register",
    name: "Register",
    component: RegistrationView,
    meta: {
      title: "Registration",
    },
  },
  {
    path: "/history",
    name: "History",
    component: GameHistoryView,
  },
  {
    path: "/logout", // on logout redirect to main page
    redirect: "/login",
  },
  {
    path: "*",
    name: "Error",
    component: ErrorView,
  },
];

const router = new VueRouter({
  mode: "history",
  base: "/",
  routes,
});

export default router;

// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue"
import App from "./App"
import { default as router, LOGIN_PAGE_PATH } from "./router"
import store from "./store"
import { AUTH_EXTRACT_TOKEN, AUTH_LOGOUT } from "./store/actions/auth"

import VCalendar from 'v-calendar';
import 'v-calendar/lib/v-calendar.min.css';

import axios from "axios";

import moment from 'moment'

window.onerror = function(message, file, line) {
  console.log('An error occured at line ' + line + ' of ' + file + ': ' + message);
};

Vue.filter("formatDate", function(value) {
  if (value) {
    return moment(String(value)).format("MM/DD/YYYY");
  }
});

// @see https://docs.vcalendar.io/#custom-defaults
Vue.filter("formatDateTime", function(value) {
  if (value) {
    return moment(String(value)).format("MM/DD/YYYY - hh:mm")
  }
});

// Use v-calendar, v-date-picker & v-popover components
Vue.use(VCalendar, {
  popoverVisibility: "focus"
});

const theStore = store;
const theRouter = router;

axios.interceptors.response.use(undefined, function(err) {
  return new Promise(function(resolve, reject) {
    if (
      err.response.status === 401 &&
      err.config &&
      !err.config.__isRetryRequest
    ) {
      // if you ever get an unauthorized, logout the user
      theStore.dispatch(AUTH_LOGOUT);
      // you can also redirect to /login if needed !
      theRouter.push(LOGIN_PAGE_PATH);
    }
    throw err;
  });
});

// extract token from Cookie
store.dispatch(AUTH_EXTRACT_TOKEN);

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: "#app",
  router,
  store,
  components: { App },
  template: "<App/>"
});

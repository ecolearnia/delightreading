import axios from "axios"
import * as cookieUtils from "../../utils/cookie-utils"
import { AUTH_EXTRACT_TOKEN, AUTH_SET_TOKEN, AUTH_ERROR, AUTH_SUCCESS, AUTH_LOGOUT } from "../actions/auth"
import { USER_ACCOUNT_REQUEST } from "../actions/user"

const STORAGE_KEY = "dr_token";
const COOKIE_KEY = "dr_token";

const state = { token: localStorage.getItem(STORAGE_KEY) || "", status: "", hasLoadedOnce: false }

const getters = {
  isAuthenticated: state => !!state.token,
  authStatus: state => state.status
}

const actions = {
  [AUTH_EXTRACT_TOKEN]: ({ commit, dispatch }) => {
    let accessToken = cookieUtils.getCookie(COOKIE_KEY);
    if (!accessToken) {
      console.log("Cookie not found.");
      return Promise.reject(new Error("Cookie not found"));
    }
    return dispatch(AUTH_SET_TOKEN, accessToken);
  },
  [AUTH_SET_TOKEN]: ({ commit, dispatch }, token) => {
    return new Promise((resolve, reject) => {
      commit(AUTH_SET_TOKEN);
      localStorage.setItem(STORAGE_KEY, token);
      axios.defaults.headers.common["Authorization"] = "Bearer " + token;
      commit(AUTH_SUCCESS, token);
      dispatch(USER_ACCOUNT_REQUEST)
        .then(account => {
          resolve(account);
        })
        .catch(error => {
          reject(error);
        });
    })
  },
  [AUTH_LOGOUT]: ({ commit, dispatch }) => {
    return new Promise((resolve, reject) => {
      commit(AUTH_LOGOUT);
      localStorage.removeItem(STORAGE_KEY);
      resolve();
    })
  }
}

const mutations = {
  [AUTH_SET_TOKEN]: (state) => {
    state.status = "loading";
  },
  [AUTH_SUCCESS]: (state, token) => {
    state.status = "success";
    state.token = token;
    state.hasLoadedOnce = true;
  },
  [AUTH_ERROR]: (state) => {
    state.status = "error";
    state.hasLoadedOnce = true;
  },
  [AUTH_LOGOUT]: (state) => {
    state.token = undefined;
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}

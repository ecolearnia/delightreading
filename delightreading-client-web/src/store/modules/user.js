import Vue from "vue"
import { AUTH_LOGOUT } from "../actions/auth"
import { USER_ACCOUNT_REQUEST, USER_ACCOUNT_ERROR, USER_ACCOUNT_SUCCESS } from "../actions/user"
import * as userClient from "../../utils/user-client";

const state = { status: "", account: {} }

const getters = {
  myAccount: state => state.account,
  isAccountLoaded: state => !!state.account.name
}

const actions = {
  [USER_ACCOUNT_REQUEST]: ({ commit, dispatch }) => {
    commit(USER_ACCOUNT_REQUEST)
    return userClient.getMyAccount()
      .then(resp => {
        commit(USER_ACCOUNT_SUCCESS, resp.data);
        return resp.data;
      })
      .catch(resp => {
        commit(USER_ACCOUNT_ERROR);
        // if resp is unauthorized, logout, to
        dispatch(AUTH_LOGOUT);
      })
  }
}

const mutations = {
  [USER_ACCOUNT_REQUEST]: (state) => {
    state.status = "loading"
  },
  [USER_ACCOUNT_SUCCESS]: (state, resp) => {
    state.status = "success"
    Vue.set(state, "account", resp)
  },
  [USER_ACCOUNT_ERROR]: (state) => {
    state.status = "error"
  },
  [AUTH_LOGOUT]: (state) => {
    state.account = {}
    state.account = {}
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}

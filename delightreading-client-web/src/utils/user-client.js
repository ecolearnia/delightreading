import * as cookieUtils from './cookie-utils'
import * as requestUtil from "./request-utils"

const STORAGE_KEY = "dr_user";
const COOKIE_KEY = "dr_token";

export function getSessionUser() {
  let user = localStorage.getItem(STORAGE_KEY);
  if (user) {
    user = JSON.parse(user);
    console.log("User in localstorage");
    return Promise.resolve(user);
  } else {
    console.log("LocalStorage is empty, fetching with token.");
    const accessToken = cookieUtils.getCookie(COOKIE_KEY);
    if (!accessToken) {
      console.log("Cookie is empty.");
      return false;
    }
    try {
      return getMyAccount()
        .then((response) => {
          localStorage.setItem(STORAGE_KEY, JSON.stringify(response.data));
          return response.data;
        });
      // Save data to the current local store
    } catch (error) {
      console.warn("Error retrieving user account: " + error);
      return Promise.reject(error);
    }
  }
}

export function getMyAccount() {
  return requestUtil.get("/api/users/me");
}

export function getMyProfile() {
  return requestUtil.get("/api/users/myprofile");
}

export function saveMyProfile(account, profile) {
  return requestUtil.put("/api/users/myprofile", profile);
}

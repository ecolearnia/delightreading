import * as cookieUtils from "./cookie-utils"
import * as requestUtil from "./request-utils"

const STORAGE_KEY = "dr_token";
const COOKIE_KEY = "dr_token";

const baseUrlPath = "/api/users/v1"

export function getSessionUser2() {
  let accessToken = localStorage.getItem(STORAGE_KEY);
  if (accessToken) {
    console.log("Access token in localStorage");
  } else {
    console.log("localStorage is empty, fetching with token.");
    accessToken = cookieUtils.getCookie(COOKIE_KEY);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(accessToken));
  }
  if (!accessToken) {
    console.log("TOken not found.");
    return false;
  }
  try {
    return getMyAccount();
  } catch (error) {
    console.warn("Error retrieving user account: " + error);
    return Promise.reject(error);
  }
}

export function getSessionUser() {
  let accessToken = cookieUtils.getCookie("dr_token");
  if (accessToken) {
    return getMyAccount();
  } else {
    // alert("No Cookie found");
  }
}

export function getMyAccount() {
  return requestUtil.get(baseUrlPath + "/me");
}

export function getMyProfile() {
  return requestUtil.get(baseUrlPath + "/me/profile");
}

export function saveMyProfile(profile) {
  return requestUtil.put(baseUrlPath + "/me/profile", profile);
}

export function signUp(details) {
  return requestUtil.post(baseUrlPath + "/register", details);
}

export function signIn(details) {
  return requestUtil.post(baseUrlPath + "/login", details);
}

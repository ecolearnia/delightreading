import axios from "axios"
import * as cookieUtils from '../utils/cookie-utils'

const serverBaseUrl = process.env.SERVER_BASE_URL;

function createAuthHeaders() {
  const accessToken = cookieUtils.getCookie("dr_token");
  return {
    "Authorization": "Bearer " + accessToken
  }
}

export function get(urlPath) {
  const reqOpts = {
    headers: createAuthHeaders()
  }
  console.log("req opts: " + JSON.stringify(reqOpts, undefined, 2));
  return axios.get(serverBaseUrl + urlPath, reqOpts);
}

export function post(urlPath, data) {
  const reqOpts = {
    headers: createAuthHeaders()
  }
  console.log("req opts: " + JSON.stringify(reqOpts, undefined, 2));
  return axios.post(serverBaseUrl + urlPath, data, reqOpts);
}

export function put(urlPath, data) {
  const reqOpts = {
    headers: createAuthHeaders()
  }
  console.log("req opts: " + JSON.stringify(reqOpts, undefined, 2));
  return axios.post(serverBaseUrl + urlPath, data, reqOpts);
}

export function del(urlPath) {
  const reqOpts = {
    headers: createAuthHeaders()
  }
  console.log("req opts: " + JSON.stringify(reqOpts, undefined, 2));
  return axios.delete(serverBaseUrl + urlPath, reqOpts);
}

export function toQueryString(obj, prependChar = "?") {
  let tuples = [];
  for (var prop in obj) {
    if (obj[prop]) {
      tuples.push(prop + "=" + decodeURIComponent(obj[prop]));
    }
  }

  let qs = tuples.join("&");
  if (prependChar) {
    qs = (qs) ? prependChar + qs : "";
  }

  return qs
}

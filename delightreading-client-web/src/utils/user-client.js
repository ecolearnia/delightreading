import axios from "axios"
import * as cookieUtils from '../utils/cookie-utils'

function createAuthHeaders() {
  const accessToken = cookieUtils.getCookie("dr_token");
  return {
    "Authorization": "Bearer " + accessToken
  }
}

export function getMyAccount() {
  const reqOpts = {
    headers: createAuthHeaders()
  }
  console.log("req opts: " + JSON.stringify(reqOpts, undefined, 2));
  return axios.get(process.env.SERVER_BASE_URL + "/api/users/me", reqOpts);
}

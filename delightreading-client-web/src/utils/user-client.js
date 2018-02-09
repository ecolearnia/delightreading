import * as requestUtil from "./request-utils"

export function getMyAccount() {
  return requestUtil.get("/api/users/me");
}

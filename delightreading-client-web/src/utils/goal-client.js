import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/reading/v1/goals"

export function getActiveGoal() {
  return requestUtil.get(baseUrlPath + "?active=true");
}

export function listGoal(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

export function addGoal(goal) {
  return requestUtil.post(baseUrlPath, goal);
}

export function updateGoal(uid, goal) {
  return requestUtil.put(baseUrlPath + "/" + uid, goal);
}

export function deleteGoal(uid) {
  return requestUtil.del(baseUrlPath + "/" + uid);
}

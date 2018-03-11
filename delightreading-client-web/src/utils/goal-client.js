import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/goals/"

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

export function updateGoal(sid, goal) {
  return requestUtil.put(baseUrlPath + sid, goal);
}

export function deleteGoal(sid) {
  return requestUtil.del(baseUrlPath + sid);
}

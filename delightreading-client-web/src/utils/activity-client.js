import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/activitylogs/"

export function listActivityLog(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

export function addActivityLog(logEntry) {
  return requestUtil.post(baseUrlPath, logEntry);
}

export function deleteActivityLog(sid) {
  return requestUtil.del(baseUrlPath + sid);
}

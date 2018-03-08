import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/referencinglogs/"

export function listReferencingLog(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

export function addReferencingLog(logEntry) {
  return requestUtil.post(baseUrlPath, logEntry);
}

export function updateReferencingLog(sid, logEntry) {
  return requestUtil.put(baseUrlPath + sid, logEntry);
}

export function deleteReferencingLog(sid) {
  return requestUtil.del(baseUrlPath + sid);
}

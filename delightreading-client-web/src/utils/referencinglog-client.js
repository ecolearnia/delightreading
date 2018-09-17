import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/reading/v1/completionlogs"

export function listReferencingLog(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

export function addReferencingLog(logEntry) {
  return requestUtil.post(baseUrlPath, logEntry);
}

// Partial udpate
export function updateReferencingLog(sid, logUpdateFields) {
  return requestUtil.put(baseUrlPath + sid, logUpdateFields);
}

export function deleteReferencingLog(sid) {
  return requestUtil.del(baseUrlPath + sid);
}

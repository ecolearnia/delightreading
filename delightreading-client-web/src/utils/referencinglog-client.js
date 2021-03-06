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
export function updateReferencingLog(uid, logUpdateFields) {
  return requestUtil.put(baseUrlPath + "/" + uid, logUpdateFields);
}

export function deleteReferencingLog(uid) {
  return requestUtil.del(baseUrlPath + "/" + uid);
}

import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/reading/v1/activitylogs"

export function listActivityLog(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

export function addActivityLog(logEntry) {
  return requestUtil.post(baseUrlPath, logEntry);
}

export function updateActivityLog(uid, logEntry) {
  return requestUtil.put(baseUrlPath + "/" + uid, logEntry);
}

export function deleteActivityLog(uid) {
  return requestUtil.del(baseUrlPath + "/" + uid);
}

export function getStats() {
  return requestUtil.get(baseUrlPath + "/stats");
}

export function getTimeSeries(from, to) {
  let qs = requestUtil.toQueryString({fromDate: from, toDate: to});
  return requestUtil.get(baseUrlPath + "/timeseries" + qs);
}

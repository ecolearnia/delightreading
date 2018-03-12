import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/activitylogs/"

export function listActivityLog(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

export function addActivityLog(logEntry) {
  return requestUtil.post(baseUrlPath, logEntry);
}

export function updateActivityLog(sid, logEntry) {
  return requestUtil.put(baseUrlPath + sid, logEntry);
}

export function deleteActivityLog(sid) {
  return requestUtil.del(baseUrlPath + sid);
}

export function getStats() {
  return requestUtil.get(baseUrlPath + "stats");
}

export function getTimeSeries(from, to) {
  let qs = requestUtil.toQueryString({fromDate: from, toDate: to});
  return requestUtil.get(baseUrlPath + "timeseries" + qs);
}

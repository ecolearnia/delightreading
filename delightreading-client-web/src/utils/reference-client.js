import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/reading/v1/literatures"

export function listReferences(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

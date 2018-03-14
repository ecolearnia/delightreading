import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/references/"

export function listReferences(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

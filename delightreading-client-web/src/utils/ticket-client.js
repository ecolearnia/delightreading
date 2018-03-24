import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/tickets/"

export function listTickets(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

export function addTicket(ticket) {
  return requestUtil.post(baseUrlPath, ticket);
}

export function updateTicket(sid, ticket) {
  return requestUtil.put(baseUrlPath + sid, ticket);
}

export function deleteTicket(sid) {
  return requestUtil.del(baseUrlPath + sid);
}

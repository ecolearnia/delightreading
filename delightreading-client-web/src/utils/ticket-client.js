import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/ticket/v1/tickets"

export function listTickets(page, pageSize) {
  let qs = requestUtil.toQueryString({page: page, pageSize: pageSize});
  return requestUtil.get(baseUrlPath + qs);
}

export function addTicket(ticket) {
  return requestUtil.post(baseUrlPath, ticket);
}

export function updateTicket(uid, ticket) {
  return requestUtil.put(baseUrlPath + "/" + uid, ticket);
}

export function deleteTicket(uid) {
  return requestUtil.del(baseUrlPath + "/" + uid);
}

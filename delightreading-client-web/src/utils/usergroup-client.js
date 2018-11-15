import * as requestUtil from "./request-utils"

const baseUrlPath = "/api/users/v1/groups"

export function getMyGroups(groupType) {
  let qs = (groupType) ? requestUtil.toQueryString({type: groupType, fetchMembers: true}) : "";
  return requestUtil.get(baseUrlPath + qs);
}

export function getMyGroup(groupUid) {
  return requestUtil.get(baseUrlPath + "/" + groupUid);
}

export function getMyFamilyGroup() {
  return requestUtil.get(baseUrlPath + "/family");
}

export function getOrCreateMyFamilyGroup(groupDetails) {
  return requestUtil.put(baseUrlPath + "/family", groupDetails);
}

export function createMyGroup(groupDetails) {
  return requestUtil.post(baseUrlPath, groupDetails);
}

export function addMember(groupUid, memberDetails) {
  return requestUtil.post(baseUrlPath + "/" + groupUid + "/members", memberDetails);
}

export function addNewAccountMember(groupUid, memberAccountDetails) {
  return requestUtil.post(baseUrlPath + "/" + groupUid + "/members/account", memberAccountDetails);
}

"use strict";

import * as async from "async";
import { Logger, LoggerUtils } from "../utils/Logger";
import { Response, Request, NextFunction } from "express";

import ObjectUtils from "../utils/ObjectUtils";
import * as controllerHelper from "./controller-helper";

import { UserAccount } from "../entity/UserAccount";
import { UserGroup } from "../entity/UserGroup";
import { UserGroupService } from "../service/UserGroupService";

const logger = LoggerUtils.child({ module: "controller/usergroup" });
const userGroupService = new UserGroupService();

/**
 */
export let addMyUserGroup = async (req: Request, res: Response) => {
  logger.info({ op: "addUserGroup", account: req.user }, "Adding usergroup");

  if (!req.user) {
    logger.warn({ op: "addUserGroup" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  const usergroup = new UserGroup(req.body);
  usergroup.createdBy = req.user.sid;

  const savedUserGroup = await userGroupService.save(usergroup);

  logger.info({ savedUserGroup: savedUserGroup }, "Add usergroup successful");

  res.json(savedUserGroup);
};

// TBD
export let listMyUserGroups = async (req: Request, res: Response) => {
  logger.info({ op: "listMyUserGroup", account: req.user, page: req.query.page, pageSize: req.query.pageSize }, "Listing my usergroup");

  if (!req.user) {
    logger.warn({ op: "listMyUserGroup" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  let usergroups: Array<UserGroup>;
  const pageRequest = controllerHelper.getPageRequest(req);
  const findCriteria = {
    accountSid: req.user.sid
  };

  usergroups = await userGroupService.list(findCriteria, pageRequest.skip(), pageRequest.pageSize);

  logger.info({ op: "listMyUserGroup" }, "Listing my usergroup successful");

  res.json(usergroups);
};

export let updateMyUserGroup = async (req: Request, res: Response) => {

  logger.info({ op: "updateMyUserGroup", account: req.user }, "Updating usergroup");

  if (!req.user) {
    logger.warn({ op: "deleteMyUserGroup" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  const criteria = {
    accountSid: req.user.sid,
    sid: req.params.sid
  };

  const updateFields = ObjectUtils.assignProperties({}, req.body,
    ["name", "description", "description", "pictureUri", "coverImageUri", "rules", "website"]);

  const updatedUserGroup = await userGroupService.update(criteria, updateFields);

  logger.info({ updatedUserGroup: updatedUserGroup }, "Update usergroup successful");

  res.json(updatedUserGroup);
};

/**
 * Create user Account, then add as member to the group
 * uri: [POST]/usergroup/{groupSid}?role=<string>
 * @param req
 *    req.params.groupSid - groupSid
 *    req.query.role - member role {child|guardian}
 *    req.body - member account
 * @param res
 */
export let addUserGroupMember = async (req: Request, res: Response) => {

  logger.info({ op: "addUserGroupMember", account: req.user}, "Adding member to group");

  if (!req.user) {
    logger.warn({ op: "addUserGroupMember" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  // The user is already a guardian
  const group = await userGroupService.findOneBySid(req.params.groupSid);
  // TODO: use const for literals
  const guardian = group.getMembers("guardian", req.user.sid);
  if (guardian.length == 0) {
    return res.status(403).json({ message: "Unauthorized: Not an privileged member" });
  }

  const memberAccount = new UserAccount(req.body);
  const usergroups = await userGroupService.addMember(group, memberAccount, req.query.role);

  logger.info({ op: "addUserGroupMember" }, "Adding member to group successful");

  res.json(usergroups);
};

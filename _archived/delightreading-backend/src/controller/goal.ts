"use strict";

import * as async from "async";
import { Logger, LoggerUtils } from "../utils/Logger";
import { Response, Request, NextFunction } from "express";

import ObjectUtils from "../utils/ObjectUtils";
import * as controllerHelper from "./controller-helper";

import { Goal } from "../entity/Goal";
import { GoalService } from "../service/GoalService";

const logger = LoggerUtils.child({ module: "controller/goal" });
const goalService = new GoalService();

/**
 */
export let addMyGoal = async (req: Request, res: Response) => {
  logger.info({ op: "addGoal", account: req.user }, "Adding goal");

  if (!req.user) {
    logger.warn({ op: "addGoal" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  const goal = new Goal(req.body);
  goal.accountSid = req.user.sid;

  const savedGoal = await goalService.save(goal);

  logger.info({ savedGoal: savedGoal }, "Add goal successful");

  res.json(savedGoal);
};

export let listMyGoals = async (req: Request, res: Response) => {

  logger.info({ op: "listMyGoal", account: req.user, page: req.query.page, pageSize: req.query.pageSize }, "Listing my goal");

  if (!req.user) {
    logger.warn({ op: "listMyGoal" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  let goals: Array<Goal>;
  if (req.query.active) {
    goals = await goalService.findActiveGoals(req.user.sid);
  } else {
    const pageRequest = controllerHelper.getPageRequest(req);
    const findCriteria = {
      accountSid: req.user.sid
    };

    goals = await goalService.list(findCriteria, pageRequest.skip(), pageRequest.pageSize);
  }

  logger.info({ op: "listMyGoal" }, "Listing my goal successful");

  res.json(goals);
};

export let updateMyGoal = async (req: Request, res: Response) => {

  logger.info({ op: "updateMyGoal", account: req.user }, "Updating goal");

  if (!req.user) {
    logger.warn({ op: "deleteMyGoal" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  const criteria = {
    accountSid: req.user.sid,
    sid: req.params.sid
  };

  const updateFields = ObjectUtils.assignProperties({}, req.body,
    ["title", "startDate", "endDate", "activity", "quantity", "quantityUnit"]);

  const updatedGoal = await goalService.update(criteria, updateFields);

  logger.info({ updatedGoal: updatedGoal }, "Update goal successful");

  res.json(updatedGoal);
};

export let getMyActiveGoals = async (req: Request, res: Response) => {

  logger.info({ op: "getMyActiveGoals", account: req.user, page: req.query.page, pageSize: req.query.pageSize }, "Listing my goal");

  if (!req.user) {
    logger.warn({ op: "getMyActiveGoals" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  const goals = await goalService.findActiveGoals(req.user.sid);

  logger.info({ op: "getMyActiveGoals" }, "Listing my goal successful");

  res.json(goals);
};
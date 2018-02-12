"use strict";

import * as async from "async";
import * as rootLogger  from "pino";
import { Response, Request, NextFunction } from "express";
import { ActivityLog } from "../entity/ActivityLog";
import { ActivityLogService } from "../service/ActivityLogService";
import { Reference } from "../entity/Reference";
import { ReferenceService } from "../service/ReferenceService";
import GoogleBooksClient from "../utils/GoogleBooksClient";

const logger = rootLogger().child({ module: "controller/activitylog" });

const referenceService =  new ReferenceService();
const activityLogService =  new ActivityLogService();

/**
 */
export let addMyActivityLog = async (req: Request, res: Response) => {
  logger.info({op: "addActivityLog", account: req.user}, "Adding activityLog");

  if (!req.user) {
    logger.warn({op: "addActivityLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  // TODO: Create reference if does not exists
  let reference = await referenceService.findOne({sourceUri: req.body.referenceSourceUri});
  if (!reference) {
    logger.info({op: "addActivityLog", referenceSourceUri: req.body.referenceSourceUri}, "Adding Reference");
    const gbook = await GoogleBooksClient.getBookByUri(req.body.referenceSourceUri);
    reference = GoogleBooksClient.toReference(gbook);
    reference = await referenceService.save(reference);
  }

  const activityLog = new ActivityLog(req.body);
  activityLog.accountSid = req.user.sid;
  activityLog.logTimestamp = new Date();
  activityLog.referenceSid = reference.sid;

  const savedActivityLog = await activityLogService.save(activityLog);

  logger.info({savedActivityLog: savedActivityLog}, "Add activityLog successful");

  res.json(savedActivityLog);
};

export let listMyActivityLog = async (req: Request, res: Response) => {

  logger.info({op: "listMyActivityLog", account: req.user, page: req.query.page, pageSize: req.query.pageSize}, "Listing my activityLog");

  if (!req.user) {
    logger.warn({op: "listMyActivityLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  const pageSize = req.query.pageSize || 20;
  const skip = (req.query.page || 0) * pageSize;

  const findCriteria = {
    accountSid: req.user.sid
  };

  const activityLogs = await activityLogService.list(findCriteria, skip, pageSize);

  logger.info({op: "listMyActivityLog"}, "Listing my activityLog successful");

  res.json(activityLogs);
};

export let deleteMyActivityLog = async (req: Request, res: Response) => {

  logger.info({op: "deleteMyActivityLog", account: req.user}, "Deleting activityLog");

  if (!req.user) {
    logger.warn({op: "deleteMyActivityLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  const criteria = {
    accountSid: req.user.sid,
    sid: req.params.sid
  };

  const deletedActivityLog = await activityLogService.delete(criteria);

  logger.info({deletedActivityLog: deletedActivityLog}, "Delete activityLog successful");

  res.json(deletedActivityLog);
};

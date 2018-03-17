"use strict";

import * as async from "async";
import * as rootLogger  from "pino";
import { Response, Request, NextFunction } from "express";
import { default as ObjectUtils } from "../utils/ObjectUtils";
import { ActivityLog } from "../entity/ActivityLog";
import { ActivityLogService } from "../service/ActivityLogService";
import { Reference } from "../entity/Reference";
import { ReferenceService } from "../service/ReferenceService";
import { ReferencingLog } from "../entity/ReferencingLog";
import { ReferencingLogService } from "../service/ReferencingLogService";
import GoogleBooksClient from "../utils/GoogleBooksClient";

const logger = rootLogger().child({ module: "controller/activitylog" });

const referenceService =  new ReferenceService();
const referencingLogService =  new ReferencingLogService();
const activityLogService =  new ActivityLogService();

/**
 */
export let addMyActivityLog = async (req: Request, res: Response) => {
  logger.info({op: "addActivityLog", account: req.user}, "Adding activityLog");

  if (!req.user) {
    logger.warn({op: "addActivityLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  // TODO: make it transactional
  let reference = await referenceService.findOne({sourceUri: req.body.referenceSourceUri});
  if (!reference) {
    logger.info({op: "addActivityLog", referenceSourceUri: req.body.referenceSourceUri}, "Adding Reference");
    const gbook = await GoogleBooksClient.getBookByUri(req.body.referenceSourceUri);
    reference = GoogleBooksClient.toReference(gbook);
    reference = await referenceService.save(reference);
  }

  const activityTimestamp = req.body.logTimestamp || new Date();

  // Create ReferencingLog
  let referencingLog = await referencingLogService.findOneRecentByAccountSidAndReferenceSid(req.user.sid, reference.sid);
  if (!referencingLog) {
    logger.info({op: "addActivityLog", referenceSourceUri: req.body.referenceSourceUri}, "Adding Reference");
    referencingLog = new ReferencingLog({
      accountSid: req.user.sid,
      referenceSid: reference.sid,
      startDate: activityTimestamp
    });

    referencingLog = await referencingLogService.save(referencingLog);
  }

  const activityLog = new ActivityLog(req.body);
  activityLog.accountSid = req.user.sid;
  activityLog.logTimestamp = activityTimestamp;
  activityLog.referenceSid = reference.sid;
  activityLog.referencingLogSid = referencingLog.sid;

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


export let updateMyActivityLog = async (req: Request, res: Response) => {
  logger.info({op: "updateMyActivityLog", account: req.user, activityLogSid: req.params.sid}, "Updating my activityLog");

  if (!req.user) {
    logger.warn({op: "updateMyActivityLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  const criteria = {
    accountSid: req.user.sid,
    sid: req.params.sid
  };

  const fields = ObjectUtils.assignProperties({}, req.body,
    ["currentPage", "percentageComplete", "postEmotion", "situation", "feedContext", "feedBody", "retrospective"]);
  const activityLogs = await activityLogService.update(criteria, fields);

  logger.info({op: "updateMyActivityLog"}, "Updating my activityLog successful");

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

///////// Statistics //////////

export let getMyActivityStats = async (req: Request, res: Response) => {
  logger.info({op: "getMyActivityStats", account: req.user}, "Stats activityLog");

  if (!req.user) {
    logger.warn({op: "getMyActivityStats"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  const activityStats = await activityLogService.stats(req.user.sid, new Date());

  logger.info({activityStats: activityStats}, "Stats activityLog successful");

  res.json(activityStats);
};

export let getMyActivityTimeSeries = async (req: Request, res: Response) => {
  logger.info({op: "getMyActivityTimeSeries", account: req.user}, "TimeSeries activityLog");

  if (!req.user) {
    logger.warn({op: "getMyActivityTimeSeries"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  const ofUnit = req.query.of || "week";

  const activityStats = await activityLogService.timeSeriesOf(req.user.sid, "day", ofUnit, "read");

  logger.info({activityStats: activityStats}, "TimeSeries activityLog successful");

  res.json(activityStats);
};
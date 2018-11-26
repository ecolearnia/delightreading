"use strict";

import * as async from "async";
import { Logger, LoggerUtils } from "../utils/Logger";
import { Response, Request, NextFunction } from "express";

import ObjectUtils from "../utils/ObjectUtils";
import * as controllerHelper from "./controller-helper";

import { Reference } from "../entity/Reference";
import { ReferenceService } from "../service/ReferenceService";
import { ReferencingLog } from "../entity/ReferencingLog";
import { ReferencingLogService } from "../service/ReferencingLogService";
import GoogleBooksClient from "../utils/GoogleBooksClient";

const logger = LoggerUtils.child({ module: "controller/referencinglog" });

const referenceService =  new ReferenceService();
const referencingLogService =  new ReferencingLogService();

/**
 */
export let addMyReferencingLog = async (req: Request, res: Response) => {
  logger.info({op: "addReferencingLog", account: req.user}, "Adding referencingLog");

  if (!req.user) {
    logger.warn({op: "addReferencingLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  // TODO: make it transactional
  let reference = await referenceService.findOne({sourceUri: req.body.referenceSourceUri});
  if (!reference) {
    logger.info({op: "addReferencingLog", referenceSourceUri: req.body.referenceSourceUri}, "Adding Reference");
    const gbook = await GoogleBooksClient.getBookByUri(req.body.referenceSourceUri);
    reference = GoogleBooksClient.toReference(gbook);
    reference = await referenceService.save(reference);
  }

  const referencingLog = new ReferencingLog(req.body);
  referencingLog.accountSid = req.user.sid;
  referencingLog.referenceSid = reference.sid;

  const savedReferencingLog = await referencingLogService.save(referencingLog);

  logger.info({savedReferencingLog: savedReferencingLog}, "Add referencingLog successful");

  res.json(savedReferencingLog);
};

export let listMyReferencingLog = async (req: Request, res: Response) => {

  logger.info({op: "listMyReferencingLog", account: req.user, page: req.query.page, pageSize: req.query.pageSize}, "Listing my referencingLog");

  if (!req.user) {
    logger.warn({op: "listMyReferencingLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  const pageRequest = controllerHelper.getPageRequest(req);

  const findCriteria = {
    accountSid: req.user.sid
  };

  const referencingLogs = await referencingLogService.list(findCriteria, pageRequest.skip(), pageRequest.pageSize);

  logger.info({op: "listMyReferencingLog"}, "Listing my referencingLog successful");

  res.json(referencingLogs);
};


export let updateMyReferencingLog = async (req: Request, res: Response) => {

  logger.info({op: "updateMyReferencingLog", account: req.user, referencingLogSid: req.params.sid}, "Updating my referencingLog");

  if (!req.user) {
    logger.warn({op: "updateMyReferencingLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  const criteria = {
    accountSid: req.user.sid,
    sid: req.params.sid
  };

  const fields = ObjectUtils.assignProperties({}, req.body,
    ["startDate", "endDate", "percentageComplete", "postEmotion", "myRating", "review", "likeReason", "synopsys"]);
  const referencingLogs = await referencingLogService.update(criteria, fields);

  logger.info({op: "updateMyReferencingLog"}, "Updating my referencingLog successful");

  res.json(referencingLogs);
};

export let deleteMyReferencingLog = async (req: Request, res: Response) => {

  logger.info({op: "deleteMyReferencingLog", account: req.user}, "Deleting referencingLog");

  if (!req.user) {
    logger.warn({op: "deleteMyReferencingLog"}, "Unauthorized: No user");
    return res.status(401).json({message: "Unauthorized: No user"});
  }

  const criteria = {
    accountSid: req.user.sid,
    sid: req.params.sid
  };

  const deletedReferencingLog = await referencingLogService.delete(criteria);

  logger.info({deletedReferencingLog: deletedReferencingLog}, "Delete referencingLog successful");

  res.json(deletedReferencingLog);
};

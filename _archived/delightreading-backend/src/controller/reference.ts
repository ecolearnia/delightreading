"use strict";

import * as async from "async";
import { Logger, LoggerUtils } from "../utils/Logger";
import { Response, Request, NextFunction } from "express";

import ObjectUtils from "../utils/ObjectUtils";
import * as controllerHelper from "./controller-helper";

import { Reference } from "../entity/Reference";
import { ReferenceService } from "../service/ReferenceService";

const logger = LoggerUtils.child({ module: "controller/reference" });
const referenceService = new ReferenceService();

export let listReferences = async (req: Request, res: Response) => {

  logger.info({ op: "listReferences", account: req.user, page: req.query.page, pageSize: req.query.pageSize }, "Listing reference");

  let references: Array<Reference>;
  const pageRequest = controllerHelper.getPageRequest(req);

  references = await referenceService.list({}, pageRequest.skip(), pageRequest.pageSize);

  logger.info({ op: "listReferences" }, "Listing reference successful");

  res.json(references);
};

// Admin:
export let updateReference = async (req: Request, res: Response) => {

  logger.info({ op: "updateMyreference", account: req.user }, "Updating reference");

  if (!req.user) {
    logger.warn({ op: "deleteMyreference" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  const criteria = {
    accountSid: req.user.sid,
    sid: req.params.sid
  };

  const updateFields = ObjectUtils.assignProperties({}, req.body,
    ["title", "startDate", "endDate", "activity", "quantity", "quantityUnit"]);

  const updatedreference = await referenceService.update(criteria, updateFields);

  logger.info({ updatedreference: updatedreference }, "Update reference successful");

  res.json(updatedreference);
};

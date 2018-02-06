"use strict";

import * as async from "async";
import { Response, Request, NextFunction } from "express";
import { getRepository } from "typeorm";
import { ActivityLog } from "../entity/activitylog";



/**
 * GET /api
 * List of API examples.
 */
export let addActivityLog = async (req: Request, res: Response) => {

  console.log(JSON.stringify(req.body, undefined, 2));

  const activityLogRepo = getRepository(ActivityLog);

  const savedActivityLog = await activityLogRepo.save(req.body);

  console.log(JSON.stringify(savedActivityLog, undefined, 2));

  res.json(savedActivityLog);
};

export let listActivityLog = async (req: Request, res: Response) => {

  console.log(JSON.stringify(req.body, undefined, 2));

  const activityLogRepo = getRepository(ActivityLog);

  const [ActivityLogs, count] = await activityLogRepo.find();

  console.log(JSON.stringify(ActivityLogs, undefined, 2));

  res.json(ActivityLogs);
};
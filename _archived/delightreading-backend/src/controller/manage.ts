"use strict";

import * as async from "async";
import { Response, Request, NextFunction } from "express";


/**
 * GET /api
 * List of API examples.
 */
export let getInfo = (req: Request, res: Response) => {
  res.json({ server: "Server" });
};

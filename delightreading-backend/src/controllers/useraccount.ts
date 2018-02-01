"use strict";

import * as async from "async";
import { Response, Request, NextFunction } from "express";
import { getRepository } from "typeorm";
import { UserAccount } from "../entity/UserAccount";



/**
 * GET /api
 * List of API examples.
 */
export let addUserAccount = async (req: Request, res: Response) => {

  console.log(JSON.stringify(req.body, undefined, 2));

  const userAccountRepo = getRepository(UserAccount);

  const savedUserAccount = await userAccountRepo.save(req.body);

  console.log(JSON.stringify(savedUserAccount, undefined, 2));

  res.json(savedUserAccount);
};

export let listUserAccount = async (req: Request, res: Response) => {

  console.log(JSON.stringify(req.body, undefined, 2));

  const userAccountRepo = getRepository(UserAccount);

  const [userAccounts, count] = await userAccountRepo.find();

  console.log(JSON.stringify(userAccounts, undefined, 2));

  res.json(userAccounts);
};
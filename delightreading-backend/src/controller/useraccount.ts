"use strict";

import * as async from "async";
import { Response, Request, NextFunction } from "express";
import { getRepository } from "typeorm";
import { UserAccount } from "../entity/UserAccount";
import { UserService } from "../service/UserService";

const userService =  new UserService();

/**
 * GET /api
 * List of API examples.
 */
export let addUserAccount = async (req: Request, res: Response) => {

  // console.log(JSON.stringify(req.body, undefined, 2));

  const userAccount = userService.newAccountFromObject(req.body);
  const savedUserAccount = await userService.saveAccount(userAccount);

  // console.log(JSON.stringify(savedUserAccount, undefined, 2));

  res.json(savedUserAccount);
};

export let listUserAccount = async (req: Request, res: Response) => {

  // console.log(JSON.stringify(req.body, undefined, 2));

  const [userAccounts, count] = await userService.listAccounts();

  // console.log(JSON.stringify(userAccounts, undefined, 2));

  res.json(userAccounts);
};


export let registerAccount = async (req: Request, res: Response) => {

  // console.log(JSON.stringify(req.body, undefined, 2));

  const account = await userService.registerAccount(req.body);

  // console.log(JSON.stringify(userAccounts, undefined, 2));

  res.json(account);
};
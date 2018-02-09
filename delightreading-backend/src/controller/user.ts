"use strict";

import * as async from "async";
import * as rootLogger  from "pino";
import { Response, Request, NextFunction } from "express";
import { UserAccount } from "../entity/UserAccount";
import { UserService } from "../service/UserService";

const logger = rootLogger().child({ module: "controller/user" });

const userService =  new UserService();

export let getUserAccount = async (req: Request, res: Response) => {
  const userAccount = userService.findAccountByUid(req.params.uid);
  logger.info({op: "getUserAccount", userAccount: userAccount}, "Retrieve account successful");

  res.json(userAccount);
};

export let getMyAccount = async (req: Request, res: Response) => {

  logger.trace({op: "getMyAccount", user: req.user}, "Retrieving user from context");

  if (req.user) {
    return res.json(req.user);
  }

  return res.status(401).json({"message": "No Authorization header"});
};

/**
 */
export let addUserAccount = async (req: Request, res: Response) => {

  logger.trace({op: "addUserAccount", input: req.body}, "Adding account");

  const userAccount = userService.newAccountFromObject(req.body);
  const savedUserAccount = await userService.saveAccount(userAccount);

  logger.info({op: "addUserAccount", user: savedUserAccount}, "Save account successful");

  res.json(savedUserAccount);
};

export let listUserAccounts = async (req: Request, res: Response) => {

  logger.trace({op: "listUserAccounts"}, "Retrieving accounts");

  const [userAccounts, count] = await userService.listAccounts();

  logger.info({op: "listUserAccounts", count: count}, "List account successful");

  res.json(userAccounts);
};


export let registerAccount = async (req: Request, res: Response) => {

  logger.trace({op: "registerAccount", input: req.body}, "Registering account");

  const account = await userService.registerAccount(req.body);

  logger.info({op: "registerAccount", account: account}, "Register account successful");

  res.json(account);
};
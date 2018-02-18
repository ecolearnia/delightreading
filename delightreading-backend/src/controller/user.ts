"use strict";

import * as async from "async";
import * as rootLogger  from "pino";
import { Response, Request, NextFunction } from "express";
import { UserAccount } from "../entity/UserAccount";
import { UserService } from "../service/UserService";

const logger = rootLogger().child({ module: "controller/user" });

const userService =  new UserService();

const NO_AUTH_MESSAGE = "No Authorization header";

export let getUserAccount = async (req: Request, res: Response) => {
  const userAccount = userService.findAccountByUid(req.params.uid);
  logger.info({op: "getUserAccount", userAccount: userAccount}, "Retrieve account successful");

  res.json(userAccount);
};

export let getMyAccount = async (req: Request, res: Response) => {
  logger.trace({op: "getMyAccount", user: req.user}, "Retrieving user from context");

  if (!req.user) {
    return res.status(401).json({"message": NO_AUTH_MESSAGE});
  }

  return res.json(req.user);
};

export let getMyProfile = async (req: Request, res: Response) => {
  logger.trace({op: "getMyProfile", user: req.user}, "Retrieving my profile");

  if (!req.user) {
    return res.status(401).json({"message": NO_AUTH_MESSAGE});
  }
  const profile = await userService.findProfileByAccountSid(req.user.sid);
  logger.info({op: "getMyProfile", profile: profile}, "Retrieving my profile successful");

  return res.json(profile);
};

export let saveMyProfile = async (req: Request, res: Response) => {
  logger.trace({op: "saveMyProfile", user: req.user}, "Updating user profile");

  if (!req.user) {
    return res.status(401).json({"message": NO_AUTH_MESSAGE});
  }
  const profile = userService.newProfileFromObject(req.body, req.user.sid);
  const savedProfile = await userService.saveProfile(profile);

  return res.json(profile);
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
"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import { Response, Request, NextFunction } from "express";

import ObjectUtils from "../utils/ObjectUtils";
import * as controllerHelper from "./controller-helper";

import { Permission, AccessControlService } from "../service/AccessControlService";
import { Ticket } from "../entity/Ticket";
import { TicketService } from "../service/TicketService";

const logger = rootLogger().child({ module: "controller/ticket" });
const ticketService = new TicketService();
const accessControlService = new AccessControlService();

/**
 */
export let addMyTicket = async (req: Request, res: Response) => {
  logger.info({ op: "addTicket", account: req.user }, "Adding ticket");

  if (!req.user) {
    logger.warn({ op: "addTicket" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  const ticket = new Ticket(req.body);
  ticket.createdBy = req.user.sid;

  const savedTicket = await ticketService.save(ticket);

  logger.info({ savedTicket: savedTicket }, "Add ticket successful");

  res.json(savedTicket);
};

export let listTickets = async (req: Request, res: Response) => {

  logger.info({ op: "listTickets", account: req.user, page: req.query.page, pageSize: req.query.pageSize }, "Listing my ticket");

  let visibility = "public";
  if (!req.user) {
    visibility = "public";
  }

  const pageRequest = controllerHelper.getPageRequest(req);
  const findCriteria = {
    visibility: visibility
  };

  const tickets = await ticketService.list(findCriteria, pageRequest.skip(), pageRequest.pageSize);

  logger.info({ op: "listTickets" }, "Listing my ticket successful");

  res.json(tickets);
};

export let updateMyTicket = async (req: Request, res: Response) => {

  logger.info({ op: "updateMyTicket", account: req.user }, "Updating ticket");

  if (!req.user) {
    logger.warn({ op: "deleteMyTicket" }, "Unauthorized: No user");
    return res.status(401).json({ message: "Unauthorized: No user" });
  }

  const criteria = {
    accountSid: req.user.sid,
    sid: req.params.sid
  };

  const ticket = await ticketService.findOneBySid(req.params.sid);
  const permissions = await accessControlService.getPermissions(req.user, ticket);

  const updateFields = ObjectUtils.assignProperties({}, req.body,
    ["name", "description", "visibility", "tags"]);

  if (permissions.has(Permission.ADMIN)) {
    if (req.body.closed) {
      updateFields.closedBy = req.user.sid;
      updateFields.closeDate = new Date();
    }
  }

  const updatedTicket = await ticketService.update(criteria, updateFields);

  logger.info({ updatedTicket: updatedTicket }, "Update ticket successful");

  res.json(updatedTicket);
};

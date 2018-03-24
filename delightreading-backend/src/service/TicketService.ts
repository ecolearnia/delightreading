"use strict";

import * as async from "async";
import { ServiceBase } from "./ServiceBase";
import { Ticket } from "../entity/Ticket";

import TypeOrmUtils from "../utils/TypeOrmUtils";

export class TicketService extends ServiceBase<Ticket> {

    constructor() {
        super(Ticket);
    }

}

"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import { Repository, getRepository } from "typeorm";
import { ServiceBase } from "./ServiceBase";
import { Reference } from "../entity/Reference";
import { Goal } from "../entity/Goal";

import TypeOrmUtils from "../utils/TypeOrmUtils";

export class GoalService extends ServiceBase<Goal> {

    constructor() {
        super(Goal);
    }
}

"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import { Repository, getRepository } from "typeorm";
import { Goal } from "../entity/Goal";
import { Reference } from "../entity/Reference";
import { ServiceBase } from "./ServiceBase";

import TypeOrmUtils from "../utils/TypeOrmUtils";

const logger = rootLogger().child({ module: "GoalService" });

export class GoalService extends ServiceBase<Goal> {

    constructor() {
        super(Goal);
    }
}

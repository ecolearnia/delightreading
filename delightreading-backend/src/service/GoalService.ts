"use strict";

import * as async from "async";
import { Repository, getRepository } from "typeorm";
import { ServiceBase } from "./ServiceBase";
import { Reference } from "../entity/Reference";
import { Goal } from "../entity/Goal";

import TypeOrmUtils from "../utils/TypeOrmUtils";

export class GoalService extends ServiceBase<Goal> {

    constructor() {
        super(Goal);
    }

    async findActiveGoals(accountSid: number): Promise<Array<Goal>> {
        this.logger.info({ op: "findActiveGoals", accountSid: accountSid }, "Query Active goals");

        const whereParams = {
            accountSid: accountSid,
            activity: "reading",
            today: new Date()
        };

        const goals = await this.repo.createQueryBuilder("goal")
            .where("goal.accountSid = :accountSid AND goal.activity = :activity AND goal.startDate <= :today AND goal.endDate >= :today", whereParams)
            .orderBy({"goal.startDate": "ASC", "goal.endDate": "ASC"})
            .getMany();

        this.logger.info({ op: "findActiveGoals", goals: goals }, "Active goals query successful");

        return goals;
    }
}

"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import * as uuidv4 from "uuid/v4";
import { Repository, getRepository } from "typeorm";
import { ActivityLog } from "../entity/ActivityLog";
import { Reference } from "../entity/Reference";
import { ServiceBase } from "./ServiceBase";

import TypeOrmUtils from "../utils/TypeOrmUtils";

const logger = rootLogger().child({ module: "ActivityLogService" });


export class ActivityLogService extends ServiceBase<ActivityLog>  {

    constructor() {
        super(ActivityLog);
    }

    createEntity(accountSid: number, referenceSid: number, goalSid: number, activity: string, quantity: number): ActivityLog {
        const activityLog = new ActivityLog({
            accountSid: accountSid,
            referenceSid: referenceSid,
            goalSid: goalSid,
            activity: activity,
            logTimestamp: new Date(),
            quantity: quantity,
            createdAt: new Date()
        });
        return activityLog;
    }

    async find(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<ActivityLog>> {

        logger.info({ op: "list", criteria: criteria }, "Listing activityLog");

        // const logs = await this.activityLogRepo.find(criteria);

        const logs = await this.repo.createQueryBuilder("activity_log")
            .leftJoinAndMapOne("activity_log.reference", Reference, "reference", "activity_log.referenceSid=reference.sid")
            .where(TypeOrmUtils.andedWhereClause(criteria, "activity_log"), criteria)
            .skip(skip)
            .take(take)
            .getMany();

        logger.info({ op: "list", logs: logs }, "Listing activityLog successful");

        return logs;
    }

    async list(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<ActivityLog>> {

        logger.info({ op: "list", criteria: criteria }, "Listing activityLog");

        // const logs = await this.activityLogRepo.find(criteria);

        const logs = await this.repo.createQueryBuilder("activity_log")
            .leftJoinAndMapOne("activity_log.reference", Reference, "reference", "activity_log.referenceSid=reference.sid")
            .where(TypeOrmUtils.andedWhereClause(criteria, "activity_log"), criteria)
            .skip(skip)
            .take(take)
            .getMany();

        logger.info({ op: "list", logs: logs }, "Listing activityLog successful");

        return logs;
    }

}
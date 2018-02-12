"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import * as uuidv4 from "uuid/v4";
import { Repository, getRepository } from "typeorm";
import { ActivityLog } from "../entity/ActivityLog";

const logger = rootLogger().child({ module: "ActivityLogService" });


export class ActivityLogService {

    activityLogRepo: Repository<ActivityLog>;

    constructor() {
        this.activityLogRepo = getRepository(ActivityLog);
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

    async save(activityLog: ActivityLog): Promise<ActivityLog> {

        logger.info({ op: "save", activityLog: activityLog }, "Saving activityLog");

        if (!activityLog.uid) {
            activityLog.uid = uuidv4();
            activityLog.createdAt = new Date();
        }
        const savedActivityLog = await this.activityLogRepo.save(activityLog);

        logger.info({ op: "save", activityLog: activityLog }, "Save activityLog successful");

        return savedActivityLog;
    }

    async list(criteria?: any): Promise<Array<ActivityLog>> {

        logger.info({ op: "list", criteria: criteria }, "Listing activityLog");

        const logs = await this.activityLogRepo.find(criteria);

        logger.info({ op: "list", logs: logs }, "Listing activityLog successful");

        return logs;
    }


    async delete(criteria?: any): Promise<Array<ActivityLog>> {

        logger.info({ op: "delete", criteria: criteria }, "Deleting activityLog");

        const toRemove = await this.activityLogRepo.find(criteria);
        const removed = await this.activityLogRepo.remove(toRemove);

        logger.info({ op: "delete", removed: toRemove }, "Delete activityLog successful");

        return removed;
    }
}
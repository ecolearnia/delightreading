"use strict";

import * as async from "async";
import * as uuidv4  from "uuid/v4";
import { getRepository } from "typeorm";
import { ActivityLog } from "../entity/activitylog";

export class ActivityLogService {

    createEntity(goalSid: number, activity: string, quantity: number): ActivityLog {
        const activityLog: ActivityLog = {
            goalSid: goalSid,
            activity: activity,
            logTimestamp: new Date(),
            quantity: quantity,
            createdAt: new Date()
          };
        return activityLog;
    }

    async save(activityLog: ActivityLog): Promise<ActivityLog> {

        // console.log(JSON.stringify(activityLog, undefined, 2));

        const activityLogRepo = getRepository(ActivityLog);
        if (activityLog.uid === undefined) {
            activityLog.uid = uuidv4();
        }
        const savedActivityLog = await activityLogRepo.save(activityLog);

        // console.log(JSON.stringify(savedActivityLog, undefined, 2));

        return savedActivityLog;
    }

    async list(criteria?: any): Promise<Array<ActivityLog>> {

        // console.log(JSON.stringify(criteria, undefined, 2));

        const activityLogRepo = getRepository(ActivityLog);
        const [activityLogs, count] = await activityLogRepo.findAndCount();

        return activityLogs;
    }
}
"use strict";

import * as async from "async";
import { Repository, getRepository } from "typeorm";
import { ServiceBase } from "./ServiceBase";
import { Reference } from "../entity/Reference";
import { ReferencingLog } from "../entity/ReferencingLog";
import { ActivityLog } from "../entity/ActivityLog";

import TypeOrmUtils from "../utils/TypeOrmUtils";

export class ReferencingLogService extends ServiceBase<ReferencingLog> {

    constructor() {
        super(ReferencingLog);
    }

    async findOneRecentByAccountSidAndReferenceSid(accountSid: number, referenceSid: number): Promise<ReferencingLog> {
        const referencingLogs = await this.list({ accountSid: accountSid, referenceSid: referenceSid }, 0, 1);

        if (referencingLogs && referencingLogs.length > 0) {
            return referencingLogs[0];
        }
        return undefined;
    }

    async list(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<ReferencingLog>> {

        this.logger.info({ op: "list", criteria: criteria }, "Listing referencingLog");

        // This was used as subquery...
        const activityStatSelect = getRepository(ActivityLog).createQueryBuilder("activity_log")
            .select("activity_log.\"referencingLogSid\"")
            .addSelect("sum(activity_log.duration)", "totalActivityDuration")
            .addSelect("count(activity_log.sid)", "totalActivityCount")
            .groupBy("activity_log.\"referencingLogSid\"");

        const logs = await this.repo.createQueryBuilder("referencing_log")
            .leftJoinAndMapOne("referencing_log.reference", Reference, "reference", "referencing_log.\"referenceSid\" = reference.sid")
            // .leftJoinAndSelect(`(${activityStatSelect.getSql()})`, "activityStat", "referencing_log.sid = \"activityStat\".\"referencingLogSid\"" )
            // .leftJoinAndMapOne("referencing_log.activityStat", `(${activityStatSelect.getSql()})`, "activitystat", "referencing_log.sid = activitystat.\"referencingLogSid\"" )
            .leftJoinAndMapOne("referencingLog.\"activityStat\"", (qb) => { 
                return qb.subQuery()
                    .select("activity_log.\"referencingLogSid\"")
                    .addSelect("sum(activity_log.duration)", "totalActivityDuration")
                    .addSelect("count(activity_log.sid)", "totalActivityCount")
                    .from(ActivityLog, "activity_log")
                    .groupBy("activity_log.\"referencingLogSid\"");
            } , "activityStat", "referencing_log.sid = \"activityStat\".\"referencingLogSid\"" )
            .where(TypeOrmUtils.andedWhereClause(criteria, "referencing_log"), criteria)
            // TODO: parameterize orderBy
            .orderBy("referencing_log.startDate", "DESC")
            .skip(skip)
            .take(take)
            .getMany();
        
        // const activityStat = await activityStatSelect.getOne();
        // console.log("activityStat: " + JSON.stringify(activityStat, undefined, 3));
        
        console.log(JSON.stringify(logs, undefined, 3));

        this.logger.info({ op: "list", logs: logs }, "Listing referencingLog successful");

        return logs;
    }

}
"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import { Repository, getRepository } from "typeorm";
import { ServiceBase } from "./ServiceBase";
import { Reference } from "../entity/Reference";
import { ReferencingLog } from "../entity/ReferencingLog";

import TypeOrmUtils from "../utils/TypeOrmUtils";

const logger = rootLogger().child({ module: "ReferencingLogService" });

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

        logger.info({ op: "list", criteria: criteria }, "Listing referencingLog");

        // const logs = await this.referencingLogRepo.find(criteria);

        const logs = await this.repo.createQueryBuilder("referencing_log")
            .leftJoinAndMapOne("referencing_log.reference", Reference, "reference", "referencing_log.referenceSid=reference.sid")
            .where(TypeOrmUtils.andedWhereClause(criteria, "referencing_log"), criteria)
            // TODO: parameterize orderBy
            .orderBy("referencing_log.startDate", "DESC")
            .skip(skip)
            .take(take)
            .getMany();

        logger.info({ op: "list", logs: logs }, "Listing referencingLog successful");

        return logs;
    }

}
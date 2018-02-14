"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import * as uuidv4 from "uuid/v4";
import { Repository, getRepository } from "typeorm";
import { ReferencingLog } from "../entity/ReferencingLog";
import { Reference } from "../entity/Reference";

import TypeOrmUtils from "../utils/TypeOrmUtils";

const logger = rootLogger().child({ module: "ReferencingLogService" });


export class ReferencingLogService {

    referencingLogRepo: Repository<ReferencingLog>;

    constructor() {
        this.referencingLogRepo = getRepository(ReferencingLog);
    }

    async save(referencingLog: ReferencingLog): Promise<ReferencingLog> {

        logger.info({ op: "save", referencingLog: referencingLog }, "Saving referencingLog");

        if (!referencingLog.uid) {
            referencingLog.uid = uuidv4();
            referencingLog.createdAt = new Date();
        }
        const savedReferencingLog = await this.referencingLogRepo.save(referencingLog);

        logger.info({ op: "save", referencingLog: referencingLog }, "Save referencingLog successful");

        return savedReferencingLog;
    }

    async findOne(criteria?: any): Promise<ReferencingLog> {

        logger.info({ op: "findOne", criteria: criteria }, "Retrieving single referencingLog");

        const reference = await this.referencingLogRepo.findOne(criteria);

        logger.info({ op: "findOne", reference: reference }, "Retrieving single referencingLog successful");

        return reference;
    }

    async list(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<ReferencingLog>> {

        logger.info({ op: "list", criteria: criteria }, "Listing referencingLog");

        // const logs = await this.referencingLogRepo.find(criteria);

        const logs = await this.referencingLogRepo.createQueryBuilder("activity_log")
            .leftJoinAndMapOne("activity_log.reference", Reference, "reference", "activity_log.referenceSid=reference.sid")
            .where(TypeOrmUtils.andedWhereClause(criteria, "activity_log"), criteria)
            .skip(skip)
            .take(take)
            .getMany();

        logger.info({ op: "list", logs: logs }, "Listing referencingLog successful");

        return logs;
    }


    async delete(criteria?: any): Promise<Array<ReferencingLog>> {

        logger.info({ op: "delete", criteria: criteria }, "Deleting referencingLog");

        const toRemove = await this.referencingLogRepo.find(criteria);
        const removed = await this.referencingLogRepo.remove(toRemove);

        logger.info({ op: "delete", removed: toRemove }, "Delete referencingLog successful");

        return removed;
    }
}
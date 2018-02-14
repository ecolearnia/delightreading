"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import * as uuidv4 from "uuid/v4";
import { Repository, getRepository } from "typeorm";
import { Reference } from "../entity/Reference";

const logger = rootLogger().child({ module: "ReferenceService" });

export class ReferenceService {

    referenceRepo: Repository<Reference>;

    constructor() {
        this.referenceRepo = getRepository(Reference);
    }

    async save(reference: Reference): Promise<Reference> {

        logger.info({ op: "save", reference: reference }, "Saving reference");

        if (!reference.uid) {
            reference.uid = uuidv4();
            reference.createdAt = new Date();
        }
        const savedReference = await this.referenceRepo.save(reference);

        logger.info({ op: "save", reference: reference }, "Save reference successful");

        return savedReference;
    }

    async findOne(criteria?: any): Promise<Reference> {

        logger.info({ op: "findOne", criteria: criteria }, "Retrieving single reference");

        const reference = await this.referenceRepo.findOne(criteria);

        logger.info({ op: "findOne", reference: reference }, "Retrieving single reference successful");

        return reference;
    }

    async list(criteria?: any): Promise<Array<Reference>> {

        logger.info({ op: "list", criteria: criteria }, "Listing reference");

        const logs = await this.referenceRepo.find(criteria);

        logger.info({ op: "list", logs: logs }, "Listing reference successful");

        return logs;
    }

    async delete(criteria?: any): Promise<Array<Reference>> {

        logger.info({ op: "delete", criteria: criteria }, "Deleting reference");

        const toRemove = await this.referenceRepo.find(criteria);
        const removed = await this.referenceRepo.remove(toRemove);

        logger.info({ op: "delete", removed: toRemove }, "Delete reference successful");

        return removed;
    }
}
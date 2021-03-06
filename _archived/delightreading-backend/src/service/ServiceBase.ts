import * as uuidv4 from "uuid/v4";
import { Logger, LoggerUtils } from "../utils/Logger";
import { ObjectType } from "typeorm/common/ObjectType";
import { Repository, getRepository, Entity } from "typeorm";
import { EntityBase } from "../entity/EntityBase";
import { UserAccount } from "../entity/UserAccount";

export class ServiceBase<T extends EntityBase> {

    logger: Logger = undefined;

    entityType: string;
    repo: Repository<T>;

    constructor(entityClass: ObjectType<T>) {
        this.entityType = entityClass.name;
        this.logger = LoggerUtils.child({ module: "ServiceBase/" + this.entityType });
        this.repo = getRepository(entityClass);
    }

    prepareForSaving(entity: EntityBase, account?: UserAccount) {
        if (!entity.uid) {
            entity.uid = uuidv4();
            entity.createdAt = new Date();
            if (account) {
                entity.createdBy = account.sid;
            }
        } else {
            entity.updatedAt = new Date();
            if (account) {
                entity.updatedBy = account.sid;
            }
        }
        return entity;
    }

    async save(entity: T): Promise<T> {
        this.logger.info({ op: "save", entity: entity }, "Saving");

        this.prepareForSaving(entity);
        const savedEntity = await this.repo.save(entity);

        this.logger.info({ op: "save", entity: entity }, "Save successful");

        return savedEntity;
    }

    async saveMany(entities: T[]): Promise<T[]> {
        this.logger.info({ op: "saveMany", entities: entities }, "Saving");

        for (const entity of entities) {
            this.prepareForSaving(entity);
        }

        const savedEntities = await this.repo.save(entities); // Ignore the red IDE underline

        this.logger.info({ op: "saveMany", entities: savedEntities }, "Save successful");

        return savedEntities;
    }

    async update(criteria: any, fields: any): Promise<void> {
        this.logger.trace({ op: "update", criteria: criteria, fields: fields }, "Updating");

        await this.repo.update(criteria, fields);

        this.logger.info({ op: "update", criteria: criteria, fields: fields }, "Update successful");
    }

    async findOne(criteria?: any): Promise<T> {
        this.logger.info({ op: "findOne", criteria: criteria }, "Retrieving single record");

        const resource = await this.repo.findOne(criteria);

        this.logger.info({ op: "findOne", resource: resource }, "Retrieving single record successful");

        return resource;
    }

    async findOneBySid(sid: number): Promise<T> {
        const criteria = {
            sid: sid
        };
        const resource = await this.findOne(sid);

        return resource;
    }

    async count(criteria: any): Promise<number> {
        this.logger.info({ op: "count", criteria: criteria }, "Counting");
        const count = await this.repo.count(criteria);
        this.logger.info({ op: "count", count: count }, "Counting successful");

        return count;
    }

    async list(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<T>> {
        this.logger.info({ op: "list", criteria: criteria }, "Listing");

        const entities = await this.repo.find(criteria);

        this.logger.info({ op: "list", entities: entities }, "Listing successful");

        return entities;
    }

    async delete(criteria?: any): Promise<Array<T>> {
        this.logger.info({ op: "delete", criteria: criteria }, "Deleting");

        const toRemove = await this.repo.find(criteria);
        const removed = await this.repo.remove(toRemove);

        this.logger.info({ op: "delete", removed: removed }, "Delete successful");

        return removed;
    }
}
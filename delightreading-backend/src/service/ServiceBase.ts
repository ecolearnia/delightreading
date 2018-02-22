import * as uuidv4 from "uuid/v4";
import * as rootLogger from "pino";
import { ObjectType } from "typeorm/common/ObjectType";
import { Repository, getRepository, Entity } from "typeorm";
import { EntityBase } from "../entity/EntityBase";
import { UserAccount } from "../entity/UserAccount";

export class ServiceBase<T extends EntityBase> {

    logger: any = undefined;

    entityType: string;
    repo: Repository<T>;

    constructor(entityClass: ObjectType<T>) {
        this.entityType = entityClass.name;
        this.logger = rootLogger().child({ module: "ServiceBase/" + this.entityType });
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
        const savedEntities = await this.repo.save(entities);

        this.logger.info({ op: "saveMany", entities: savedEntities }, "Save successful");

        return savedEntities;
    }

    async update(criteria: any, fields: any): Promise<void> {

        this.logger.trace({ op: "update", criteria: criteria, fields: fields }, "Updating");

        await this.repo.update(criteria, fields);

        this.logger.info({ op: "update", criteria: criteria, fields: fields }, "Update successful");
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
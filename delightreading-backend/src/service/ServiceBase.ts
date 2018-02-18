import * as uuidv4 from "uuid/v4";
import { EntityBase } from "../entity/EntityBase";
import { UserAccount } from "../entity/UserAccount";

export class ServiceBase {

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
}
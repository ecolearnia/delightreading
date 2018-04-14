"use strict";

import * as async from "async";
import { EntityBase } from "../entity/EntityBase";
import { UserAccount } from "../entity/UserAccount";


const ROLE_HOST = "host";

export enum Permission {
    READ = "READ",
    CREATE = "CREATE",
    UPDATE = "UPDATE",
    DELETE = "DELETE",
    ADMIN = "ADMIN"
}

export class AccessControlService {

    async hasPermission(account: UserAccount, resource: EntityBase, permission: string): Promise<boolean> {
        const permissions = await this.getPermissions(account, resource);
        const hasPermission = permissions && permissions.has(permission);
        return Promise.resolve(hasPermission);
    }

    async getPermissions(account: UserAccount, resource: EntityBase): Promise<Set<string>> {

        const permissions: Set<string> = new Set<string>();
        if (account.role && account.role.includes(ROLE_HOST)) {
            permissions.add(Permission.READ).add(Permission.CREATE).add(Permission.DELETE).add(Permission.ADMIN);
        }
        if (account.sid == resource.createdBy) {
            permissions.add(Permission.READ).add(Permission.CREATE).add(Permission.DELETE).add(Permission.UPDATE);
        }
        return Promise.resolve(permissions);
    }
}
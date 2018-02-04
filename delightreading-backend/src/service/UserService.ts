"use strict";

import * as async from "async";
import * as uuidv4 from "uuid/v4";
import { getRepository } from "typeorm";
import { UserAccount } from "../entity/UserAccount";

export class UserService {

    createEntity(username: string, password: string, givenName: string, familyName: string): UserAccount {
        const userAccount: UserAccount = {
            username: username,
            password: password,
            givenName: givenName,
            familyName: familyName,
            createdAt: new Date()
        };
        return userAccount;
    }

    createEntityFromObject(obj: any): UserAccount {
        return this.createEntity(obj.username, obj.password, obj.givenName, obj.familyName);
    }

    async save(userAccount: UserAccount): Promise<UserAccount> {

        // console.log(JSON.stringify(UserAccount, undefined, 2));

        const UserAccountRepo = getRepository(UserAccount);
        if (userAccount.uid === undefined) {
            userAccount.uid = uuidv4();
        }
        const savedUserAccount = await UserAccountRepo.save(userAccount);

        // console.log(JSON.stringify(savedUserAccount, undefined, 2));

        return savedUserAccount;
    }

    async list(criteria?: any): Promise<[Array<UserAccount>, number]> {

        // console.log(JSON.stringify(criteria, undefined, 2));

        const UserAccountRepo = getRepository(UserAccount);
        // result [UserAccounts, count]
        const response = await UserAccountRepo.findAndCount();

        return response;
    }
}
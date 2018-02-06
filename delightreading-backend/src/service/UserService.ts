"use strict";

import * as async from "async";
import * as uuidv4 from "uuid/v4";
import { getRepository } from "typeorm";
import { UserAccount } from "../entity/UserAccount";
import { UserAuth } from "../entity/UserAuth";

export class UserService {

    newAccount(username: string, email: string, password: string, givenName: string, familyName: string, auth?: UserAuth): UserAccount {
        const account = this.newAccountFromObject({
            username: username,
            email: email,
            password: password,
            givenName: givenName,
            familyName: familyName,
            createdAt: new Date()
        });
        if (auth) {
            account.auths = new Array();
            account.auths.push(auth);
        }
        return account;
    }

    newAccountFromObject(obj: any): UserAccount {
        const userAccount: UserAccount = {
            username: obj.username,
            email: obj.email,
            password: obj.password,
            givenName: obj.givenName,
            familyName: obj.familyName,
            middleName: obj.middleName,
            dateOfBirth: obj.dateOfBirth,
            pictureUri: obj.pictureUri,
            locale: obj.locale,
            timezone: obj.timezone,
            timeoffset: obj.timeoffset,
            createdAt: new Date()
        };
        return userAccount;
    }

    newAuth(obj: any): UserAuth {
        const auth: UserAuth = {
            accountSid: obj.accountSid,
            provider: obj.provider,
            providerAccountId: obj.providerAccountId,
            token: obj.token,
            createdAt: new Date()
        };
        return auth;
    }

    async saveAccount(userAccount: UserAccount): Promise<UserAccount> {

        // console.log(JSON.stringify(UserAccount, undefined, 2));

        const userAccountRepo = getRepository(UserAccount);
        if (userAccount.uid === undefined) {
            userAccount.uid = uuidv4();
        }
        const savedUserAccount = await userAccountRepo.save(userAccount);

        // console.log(JSON.stringify(savedUserAccount, undefined, 2));

        return savedUserAccount;
    }

    async listAccounts(criteria?: any): Promise<[Array<UserAccount>, number]> {

        // console.log(JSON.stringify(criteria, undefined, 2));

        const userAccountRepo = getRepository(UserAccount);
        // result [UserAccounts, count]
        const response = await userAccountRepo.findAndCount();

        return response;
    }

    async registerAccount(account: UserAccount): Promise<UserAccount> {
        const savedAccount = await this.saveAccount(account);
        if (account.auths) {
            const self = this;
            account.auths.forEach(async function (auth: UserAuth, idx: number) {
                const savedAuth = await self.linkAuth(savedAccount, auth);
                savedAccount.auths.push(savedAuth);
            });
        }
        return savedAccount;
    }

    async linkAuth(account: UserAccount, auth: UserAuth): Promise<UserAuth> {
        if (!account.sid) {
            throw new Error("User account does not have sid");
        }
        const authRepo = getRepository(UserAuth);
        auth.uid = uuidv4();
        auth.accountSid = account.sid;
        const savedAuth = await authRepo.save(auth);

        return savedAuth;
    }

    async signIn(auth: UserAuth): Promise<UserAccount> {

        const authRepo = getRepository(UserAuth);

        let account: UserAccount;

        if (auth.provider && auth.providerAccountId) {
            // const foundAuth = await authRepo.findOne({provider: auth.provider, providerAccountId: auth.providerAccountId});
            const foundAuth = await authRepo.createQueryBuilder("user_auth")
                .leftJoinAndMapOne("user_auth.account", UserAccount, "user_account", "user_account.sid=user_auth.accountSid")
                .where("user_auth.provider = :provider AND user_auth.providerAccountId = :providerAccountId", 
                    { provider: auth.provider, providerAccountId: auth.providerAccountId })
                .getOne();
            account = foundAuth.account;
            account.auths.push(foundAuth);
            foundAuth.account = undefined;
            return account;
        } else {
            throw new Error("Auth.provider/accountId not provided");
        }

    }
}
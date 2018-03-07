"use strict";

import * as async from "async";
import * as uuidv4 from "uuid/v4";
import * as rootLogger from "pino";

import { getRepository } from "typeorm";
import { UserAccount } from "../entity/UserAccount";
import { UserAuth } from "../entity/UserAuth";
import { UserProfile } from "../entity/UserProfile";
import { ServiceBase } from "./ServiceBase";

import TypeOrmUtils from "../utils/TypeOrmUtils";

const logger = rootLogger().child({ module: "UserService" });

export class UserService extends ServiceBase<UserAccount> {

    constructor() {
        super(UserAccount);
    }

    newAccount(username: string, email: string, password: string, givenName: string, familyName: string, auth?: UserAuth): UserAccount {
        const account = new UserAccount({
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
        const userAccount = new UserAccount(obj);
        userAccount.createdAt = new Date();
        return userAccount;
    }

    newAuth(obj: any): UserAuth {
        const auth = new UserAuth({
            accountSid: obj.accountSid,
            provider: obj.provider,
            providerAccountId: obj.providerAccountId,
            token: obj.token,
            createdAt: new Date()
        });
        return auth;
    }

    async saveAccount(userAccount: UserAccount): Promise<UserAccount> {

        logger.trace({ op: "saveAccount", userAccount: userAccount }, "Saving account");

        const userAccountRepo = getRepository(UserAccount);
        if (userAccount.uid === undefined) {
            userAccount.uid = uuidv4();
        }
        const savedUserAccount = await userAccountRepo.save(userAccount);
        logger.info({ op: "saveAccount", userAccount: savedUserAccount }, "Save account successfufl");

        return savedUserAccount;
    }

    async updateAccount(criteria: any, fields: any): Promise<void> {

        logger.trace({ op: "updateAccount", criteria: criteria, fields: fields }, "Updating account");

        const userAccountRepo = getRepository(UserAccount);
        await userAccountRepo.update(criteria, fields);
        logger.info({ op: "updateAccount", criteria: criteria, fields: fields }, "Update account successful");
    }

    async findAccountBySid(sid: number): Promise<UserAccount> {
        return this.findAccountBy({ sid: sid });
    }

    async findAccountByUid(uid: string): Promise<UserAccount> {
        return this.findAccountBy({ uid: uid });
    }

    async findAccountByUsername(username: string): Promise<UserAccount> {
        return this.findAccountBy({ username: username });
    }

    async findAccountBy(criteria: any): Promise<UserAccount> {

        logger.trace({ op: "findAccountBy", criteria: criteria }, "Retrieving account");

        const userAccountRepo = getRepository(UserAccount);
        const foundAccount = await userAccountRepo.findOne(criteria);

        logger.info({ op: "findAccountBy", foundAccount: foundAccount }, "Retrieve account successful");

        return foundAccount;
    }

    async listAccounts(criteria?: any): Promise<[Array<UserAccount>, number]> {

        // console.log(JSON.stringify(criteria, undefined, 2));

        const userAccountRepo = getRepository(UserAccount);
        // result [UserAccounts, count]
        const response = await userAccountRepo.findAndCount();

        return response;
    }

    /**
     * TODO: Change the return objec to be {UserAcccount, boolean}, where second element is true for existing, false for new
     * @param account
     */
    async registerAccount(account: UserAccount): Promise<UserAccount> {
        logger.trace({ op: "registerAccount", account: account }, "Registering account");

        const existingAccount = await this.findAccountByUsername(account.username);

        if (existingAccount) {
            return existingAccount;
        }

        const savedAccount = await this.saveAccount(account);
        const savedAuths: UserAuth[] = Array();
        if (account.auths) {
            // console.log("-- ENTERING AUTH LOOP --: " + account.auths.length);
            for (const auth of account.auths) {

                const savedAuth = await this.linkAuth(savedAccount, auth);
                savedAuths.push(savedAuth);
            }
            savedAccount.auths = savedAuths;
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
        let account: UserAccount;

        if (auth.provider && auth.providerAccountId) {
            // const foundAuth = await authRepo.findOne({provider: auth.provider, providerAccountId: auth.providerAccountId});
            const foundAuth = await this.findAuth(auth);
            // console.log("-- FOUND AUTH: " + JSON.stringify(foundAuth, undefined, 2));
            account = foundAuth.account;
            account.addAuth(foundAuth);
            foundAuth.account = undefined;
            return account;
        } else {
            throw new Error("Auth.provider/accountId not provided");
        }

    }

    async findAuthByProviderId(provider: string, providerAccountId: string): Promise<UserAuth> {
        const auth = new UserAuth({ provider: provider, providerAccountId: providerAccountId });
        return await this.findAuth(auth);
    }

    async findAuth(auth: UserAuth): Promise<UserAuth> {
        const authRepo = getRepository(UserAuth);

        // const foundAuth = await authRepo.findOne({provider: auth.provider, providerAccountId: auth.providerAccountId});

        const foundAuth = await authRepo.createQueryBuilder("user_auth")
            .leftJoinAndMapOne("user_auth.account", UserAccount, "user_account", "user_account.sid=user_auth.accountSid")
            .where("user_auth.provider = :provider AND user_auth.providerAccountId = :providerAccountId",
                { provider: auth.provider, providerAccountId: auth.providerAccountId })
            .getOne();
        return foundAuth;
    }

    /**********
     * Profile
     */

    newProfileFromObject(obj: any, accountSid: number): UserProfile {
        const profile = new UserProfile(obj);
        profile.accountSid = accountSid;
        profile.createdAt = new Date();
        return profile;
    }

    async saveProfile(profile: UserProfile): Promise<UserProfile> {

        logger.info({ op: "saveProfile", profile: profile }, "Saving profile");

        super.prepareForSaving(profile);
        if (profile.account) {
            const fields: any = {};
            if (profile.account.nickname) {
                fields["nickname"] = profile.account.nickname.trim();
            }
            if (profile.account.givenName) {
                fields["givenName"] = profile.account.givenName.trim();
            }
            if (profile.account.familyName) {
                fields["familyName"] = profile.account.familyName.trim();
            }
            await this.updateAccount({ sid: profile.accountSid }, fields);
            profile.account = await this.findAccountBySid(profile.accountSid);
        }

        const profileRepo = getRepository(UserProfile);
        const foundProfile = await this.findProfileByAccountSid(profile.accountSid);
        if (foundProfile) {
            profile.sid = foundProfile.sid;
        }
        const savedProfile = await profileRepo.save(profile);

        logger.info({ op: "saveProfile", profile: profile }, "Save profile successful");

        return savedProfile;
    }

    async findProfileByAccountSid(accountSid: number): Promise<UserProfile> {
        return this.findProfile({ accountSid: accountSid });
    }

    async findProfile(criteria?: any): Promise<UserProfile> {

        logger.info({ op: "findProfile", criteria: criteria }, "Retrieving single userProfile");

        const profileRepo = getRepository(UserProfile);

        // const profile = await profileRepo.findOne(criteria);
        const profile = await profileRepo.createQueryBuilder("user_profile")
            .leftJoinAndMapOne("user_profile.account", UserAccount, "account", "user_profile.accountSid=account.sid")
            .where(TypeOrmUtils.andedWhereClause(criteria, "user_profile"), criteria)
            .getOne();


        logger.info({ op: "findProfile", profile: profile }, "Retrieving single userProfile successful");

        return profile;
    }
}
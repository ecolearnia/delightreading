"use strict";

import * as async from "async";
import { ServiceBase } from "./ServiceBase";
import { UserGroupMember } from "../entity/UserGroupMember";
import { UserAccount } from "../entity/UserAccount";

import TypeOrmUtils from "../utils/TypeOrmUtils";

export class UserGroupMemberService extends ServiceBase<UserGroupMember> {

    constructor() {
        super(UserGroupMember);
    }

    async list(criteria?: any, skip: number = 0, take: number = 10): Promise<Array<UserGroupMember>> {
        this.logger.info({ op: "listMembers", criteria: criteria, skip: skip, take: take }, "Listing groupMembers");

        const members = await this.repo.createQueryBuilder("user_group_member")
            .leftJoinAndMapOne("user_group_member.account", UserAccount, "account", "user_group_member.\"accountSid\"=account.sid")
            .where(TypeOrmUtils.andedWhereClause(criteria, "user_group_member"), criteria)
            .orderBy("user_group_member.role", "DESC")
            .skip(skip)
            .take(take)
            .getMany();

            this.logger.info({ op: "list", resultCount: members && members.length }, "Listing groupMembers successful");

        return members;
    }
}

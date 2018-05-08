"use strict";

import * as async from "async";
import { Repository, getRepository } from "typeorm";
import { ServiceBase } from "./ServiceBase";
import { UserAccount } from "../entity/UserAccount";
import { UserGroup } from "../entity/UserGroup";
import { UserGroupMember } from "../entity/UserGroupMember";
import { UserGroupMemberService } from "./UserGroupMemberService";
import { UserService } from "./UserService";

import TypeOrmUtils from "../utils/TypeOrmUtils";

export class UserGroupService extends ServiceBase<UserGroup> {

    userService: UserService;
    userGroupMemberService: UserGroupMemberService;

    constructor() {
        super(UserGroup);
        this.userService = new UserService();
        this.userGroupMemberService = new UserGroupMemberService();
    }

    async findOne(criteria?: any, withMembers: boolean = true): Promise<UserGroup> {
        const group = await super.findOne(criteria);
        // TODO: fetch members
        if (withMembers) {
            group.members = await this.listMembers(group.sid);
        }
        return group;
    }

    async list(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<UserGroup>> {
        this.logger.info({ op: "list", criteria: criteria }, "Listing userGroups");

        let sql = "SELECT user_group.*, count(user_group_member.sid) as \"memberCount\" FROM user_group "
            + " LEFT JOIN user_group_member ON user_group.sid = user_group_member.\"groupSid\" ";

        let paramVals;
        if (criteria) {
            sql += " WHERE " + TypeOrmUtils.andedWhereClause(criteria, "user_group", TypeOrmUtils.SqlParamType.DOLLAR);
            paramVals = Object.values(criteria);
        }
        sql += " GROUP BY user_group.sid";

        this.logger.debug({ op: "list", sql: sql }, "Executing SQL");
        const groups = await this.repo.query(sql, paramVals);

        this.logger.info({ op: "list", groups: groups }, "Listing userGroups successful");

        return groups;
    }

    async addMember(group: UserGroup, account: UserAccount, role: string): Promise<UserGroupMember> {
        if (!group.sid) {
            throw new Error("Group sid is null");
        }
        if (!account.sid) {
            throw new Error("Account sid is null");
        }
        const userGroupMember = new UserGroupMember({
            accountSid: account.sid,
            groupSid: group.sid,
            role: role,
            status: "active",
            sinceDate: new Date()
          });
        return this.userGroupMemberService.save(userGroupMember);
    }

    async listMembers(groupSid: number, skip: number = 0, take: number = 10): Promise<Array<UserGroupMember>> {
        const criteria = {groupSid: groupSid};
        return this.userGroupMemberService.list(criteria);
    }

    async deleteMember(criteria?: any): Promise<Array<UserGroupMember>> {
        return this.userGroupMemberService.delete(criteria);
    }

}

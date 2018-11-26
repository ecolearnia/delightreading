"use strict";

import * as async from "async";

import ErrorUtils from "../utils/ErrorUtils";

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

    /**
     * FInd groups which includes member
     * @param accountSid criteria for member
     * @param memberRole criteria for member role
     * @param groupType criteria for groupType, e.g. family
     */
    async findByMember(accountSid: number, memberRole: string, groupType?: string): Promise<Array<UserGroup>> {
        this.logger.info({ op: "findByMember", accountSid: accountSid, memberRole: memberRole }, "Find userGroups");

        let sql = "SELECT user_group.*, (SELECT count(sid) FROM user_group_member WHERE user_group_member.\"groupSid\" = user_group.sid) AS \"memberCount\" "
            + " FROM user_group "
            + " LEFT JOIN user_group_member ON user_group.sid = user_group_member.\"groupSid\" ";

        const memberCriteria = {
            accountSid: accountSid,
            role: memberRole
        };
        sql += " WHERE " + TypeOrmUtils.andedWhereClause(memberCriteria, "user_group_member", TypeOrmUtils.SqlParamType.DOLLAR);
        let paramVals  = Object.values(memberCriteria);

        if (groupType) {
            const groupCriteria = {
                type: groupType
            };
            sql += " AND " + TypeOrmUtils.andedWhereClause(groupCriteria, "user_group", TypeOrmUtils.SqlParamType.DOLLAR, 3);
            paramVals = paramVals.concat(Object.values(groupCriteria));
        }

        sql += " GROUP BY user_group.sid";

        this.logger.warn({ op: "list", sql: sql, paramVals: paramVals }, "Executing SQL");
        const groups = await this.repo.query(sql, paramVals);

        // TODO: convert memberCount from string to number
        for (const group of groups) {
            group.memberCount = Number(group.memberCount);
        }

        this.logger.info({ op: "findByMember", groups: groups }, "Find userGroups successful");

        return groups;
    }

    /**
     * Find member and populate the members belonging to the group
     * @param criteria
     * @param withMembers
     */
    async findOne(criteria?: any, withMembers: boolean = true): Promise<UserGroup> {
        this.logger.info({ op: "findOne", criteria: criteria }, "Retrieving single userGroup");

        const group = await super.findOne(criteria);

        if (withMembers) {
            this.logger.info({ op: "findOne", criteria: criteria }, "Retrieving members of the userGroup");
            group.memberCount = await this.countMembers(group.sid);
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

        for (const group of groups) {
            group.memberCount = Number(group.memberCount);
        }

        this.logger.info({ op: "list", groups: groups }, "Listing userGroups successful");

        return groups;
    }

    /**
     * Add account to the group
     * @param group - The group where the account should be added to
     * @param account - Account, eiter existing or new. If it is new, it will be added
     * @param role - {child|guardian}
     */
    async addMember(group: UserGroup, account: UserAccount, role: string): Promise<UserGroupMember> {
        if (!group.sid) {
            throw ErrorUtils.createError("Group sid is null", "InvalidArgument");
        }
        // TODO: validate role value
        let memberAccount = account;
        if (!account.sid) {
            memberAccount = await this.userService.registerAccount(account);
        }
        const userGroupMember = new UserGroupMember({
            accountSid: memberAccount.sid,
            groupSid: group.sid,
            role: role,
            status: "active",
            sinceDate: new Date()
          });
        return this.userGroupMemberService.save(userGroupMember);
    }

    // TODO: criteria for role, status
    async countMembers(groupSid: number): Promise<number> {
        const criteria = {groupSid: groupSid};
        return this.userGroupMemberService.count(criteria);
    }

    // TODO: criteria for role, status
    async listMembers(groupSid: number, skip: number = 0, take: number = 10): Promise<Array<UserGroupMember>> {
        const criteria = {groupSid: groupSid};
        return this.userGroupMemberService.list(criteria);
    }

    async deleteMember(criteria?: any): Promise<Array<UserGroupMember>> {
        return this.userGroupMemberService.delete(criteria);
    }

}

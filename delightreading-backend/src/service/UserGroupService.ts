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
        return group;
    }

    async list(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<UserGroup>> {
        this.logger.info({ op: "list", criteria: criteria }, "Listing userGroups");

        // const groups = await this.repo.createQueryBuilder("user_group")
        //     .select("count(user_group_member.sid)", "memberCount")
        //     .leftJoinAndSelect("user_group_member", "user_group_member", "user_group.sid = \"user_group_member\".\"groupSid\" ")
        //     .groupBy("user_group.sid")
        //     .addGroupBy("user_group_member.sid")
        //     .addGroupBy("\"user_group_member\".\"groupSid\"")
        //     .skip(skip).take(take).getMany();

        let sql = "SELECT user_group.*, count(user_group_member.sid) as \"memberCount\" FROM user_group " 
            + " LEFT JOIN user_group_member ON user_group.sid = user_group_member.\"groupSid\" "
        
        let paramVals;
        if (criteria) {
            sql += " WHERE " + TypeOrmUtils.andedWhereClause(criteria, "user_group", TypeOrmUtils.SqlParamType.DOLLAR);
            paramVals = Object.values(criteria);
        }
        sql += " GROUP BY user_group.sid";

        
        console.log("SQL:" + sql);
        const groups = await this.repo.query(sql, paramVals)
        
        this.logger.info({ op: "list", groups: groups }, "Listing userGroups successful");

        return groups;

    }

    async list2(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<UserGroup>> {

        this.logger.info({ op: "list", criteria: criteria }, "Listing userGroups");

        const groups = await this.repo.createQueryBuilder("user_group")
            // Join member count
            .leftJoinAndMapOne("user_group.\"stat\"", (qb) => {
                return qb.subQuery()
                    .select("user_group_member.\"groupSid\"")
                    .addSelect("count(user_group_member.sid)", "memberCount")
                    .from(UserGroupMember, "user_group_member")
                    .groupBy("user_group_member.\"groupSid\"");
                    // .where("user_group_member.status = :status", { status: "active"});
            // "stat" is the subquery alias
            } , "stat", "user_group.sid = \"stat\".\"groupSid\" " )
            .where(TypeOrmUtils.andedWhereClause(criteria, "user_group"), criteria)
            // TODO: parameterize orderBy
            .orderBy("user_group.startDate", "DESC")
            .skip(skip)
            .take(take)
            .getMany();

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

        this.logger.info({ op: "listMembers", groupSid: groupSid, skip: skip, take: take }, "Listing groupMembers");

        const memberRepo = getRepository(UserGroupMember);
        const members = await memberRepo.createQueryBuilder("user_group_member")
            .leftJoinAndMapOne("user_group_member.reference", UserGroupMember, "reference", "activity_log.referenceSid=reference.sid")
            .where("user_group_member.\"groupSid\" = :groupSid", {groupSid: groupSid})
            .orderBy("activity_log.logTimestamp", "DESC")
            .skip(skip)
            .take(take)
            .getMany();

            this.logger.info({ op: "list", membersCount: members && members.length }, "Listing groupMembers successful");

        return members;
    }

}

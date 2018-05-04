"use strict";

import * as async from "async";
import { ServiceBase } from "./ServiceBase";
import { UserGroupMember } from "../entity/UserGroupMember";

import TypeOrmUtils from "../utils/TypeOrmUtils";

export class UserGroupMemberService extends ServiceBase<UserGroupMember> {

    constructor() {
        super(UserGroupMember);
    }

}

import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";
import { UserAccount } from "./UserAccount";

@Entity("user_group_member")
export class UserGroupMember extends EntityBase {

    @Column({
        type: "bigint"
    })
    groupSid?: number;

    @Column({
        type: "bigint"
    })
    accountSid?: number;

    // TODO: use enum
    @Column({
        type: "varchar",
        length: 64
    })
    role: string; // student, guardian, teacher, club-admin, club-member

    @Column({
        type: "date",
        nullable: true
    })
    sinceDate?: Date;

    @Column({
        type: "date",
        nullable: true
    })
    untilDate?: Date;

    @Column({
        type: "varchar",
        length: 16,
        nullable: true
    })
    status?: string; // active, inactive

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.groupSid = obj.groupSid;
            this.accountSid = obj.accountSid;
            this.role = obj.role;
            this.sinceDate = obj.sinceDate;
            this.untilDate = obj.untilDate;
            this.status = obj.status;
        }
    }
}

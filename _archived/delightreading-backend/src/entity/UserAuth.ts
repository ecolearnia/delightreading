import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";
import { UserAccount } from "./UserAccount";

@Entity("user_auth")
export class UserAuth extends EntityBase {

    @Column({
        type: "bigint"
    })
    accountSid?: number;

    @Column({
        type: "varchar",
        length: 64
    })
    provider: string;

    @Column({
        type: "varchar",
        length: 64,
        nullable: true
    })
    providerAccountId?: string;

    @Column({
        type: "varchar",
        length: 64,
        nullable: true
    })
    password?: string;

    @Column({
        type: "varchar",
        length: 64,
        nullable: true
    })
    token?: string;

    @Column({
        type: "date",
        nullable: true
    })
    expires?: Date;

    @Column({
        type: "text",
        nullable: true
    })
    rawProfile?: string;

    // To be fetched through leftJoin
    account?: UserAccount;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.accountSid = obj.accountSid;
            this.provider = obj.provider;
            this.providerAccountId = obj.providerAccountId;
            this.password = obj.password;
            this.token = obj.token;
            this.expires = obj.expires;
            this.rawProfile = obj.rawProfile;
        }
    }
}

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
    token?: string;

    @Column({
        type: "date",
        nullable: true
    })
    expires?: Date;

    // To be fetched through leftJoin
    account?: UserAccount;
}

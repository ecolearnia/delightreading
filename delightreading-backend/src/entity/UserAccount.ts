import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

@Entity("user_account")
export class UserAccount extends EntityBase {

    @Column({
        type: "varchar",
        length: 64
    })
    username: string;

    @Column({
        type: "varchar",
        length: 64
    })
    password: string;

    @Column({
        type: "varchar",
        length: 64
    })
    givenName: string;

    @Column({
        type: "varchar",
        length: 64,
        nullable: true
    })
    familyName?: string;

    @Column({
        type: "varchar",
        length: 64,
        nullable: true
    })
    middleName?: string;

    @Column({
        type: "date",
        nullable: true
    })
    dateOfBirth?: Date;

    @Column({
        type: "varchar",
        length: 255,
        nullable: true
    })
    pictureUri?: string;

    @Column({
        type: "varchar",
        length: 12,
        nullable: true
    })
    locale?: string;

    @Column({
        type: "varchar",
        length: 20,
        nullable: true
    })
    timezone?: string;

    @Column({
        type: "int",
        nullable: true
    })
    timeoffset?: number;

    @Column({
        type: "bool",
        nullable: true
    })
    verifiedInd?: boolean;

    @Column({
        type: "varchar",
        length: 32,
        nullable: true
    })
    passwordResetToken?: string;

    @Column({
        type: "timestamp",
        nullable: true
    })
    passwordResetExpires?: Date;

}

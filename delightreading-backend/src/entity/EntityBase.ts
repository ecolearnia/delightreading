import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";

// @see https://github.com/typeorm/typeorm/issues/1151
export class EntityBase {

    @PrimaryGeneratedColumn()
    sid?: number;

    @Column({
        type: "varchar",
        name: "uid",
        length: 80
    })
    uid?: string;

    @Column({
        type: "varchar",
        length: 12,
        nullable: true
    })
    status?: string;

    @Column({
        type: "timestamp",
        nullable: true
    })
    createdAt: Date;

    @Column({
        type: "bigint",
        nullable: true
    })
    createdBy?: number;

    @Column({
        type: "timestamp",
        nullable: true
    })
    updatedAt?: Date;

    @Column({
        type: "bigint",
        nullable: true
    })
    updatedBy?: number;

    constructor(obj: any) {
        if (obj) {
            this.status = obj.status;
            this.createdBy = obj.createdBy;
            this.updatedAt = obj.updatedAt;
            this.updatedBy = obj.updatedBy;
        }
    }

}

import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

@Entity("activity_log")
export class ActivityLog extends EntityBase {

    @Column({
        type: "bigint"
    })
    goalSid: number;

    @Column({
        type: "varchar",
        length: 64
    })
    activity: string;

    @Column({
        type: "timestamp"
    })
    logTimestamp: Date;

    @Column({
        type: "decimal",
        precision: 6,
        scale: 3
    })
    quantity: number;

    @Column({
        type: "text",
        nullable: true
    })
    situation?: string;

    @Column({
        type: "varchar",
        length: 256,
        nullable: true
    })
    feedContext?: string;

    @Column({
        type: "text",
        nullable: true
    })
    feedBody?: string;

    @Column({
        type: "bigint",
        nullable: true
    })
    referencingLogSid?: number;
}

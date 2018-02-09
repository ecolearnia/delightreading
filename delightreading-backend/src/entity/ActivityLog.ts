import * as _  from "lodash";
import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

@Entity("activity_log")
export class ActivityLog extends EntityBase {

    @Column({
        type: "bigint"
    })
    accountSid: number;

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
        type: "varchar",
        length: 256,
        nullable: true
    })
    referenceTitle?: string;

    @Column({
        type: "bigint",
        nullable: true
    })
    referencingLogSid?: number;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.accountSid = obj.accountSid;
            this.goalSid = obj.goalSid;
            this.activity = obj.activity;
            if (_.isString(obj.logTimestamp)) {
                this.logTimestamp = new Date();
                this.logTimestamp.setTime(Date.parse(obj.logTimestamp));
            } else {
                this.logTimestamp = obj.logTimestamp;
            }
            this.quantity = obj.quantity;
            this.situation = obj.situation;
            this.feedContext = obj.feedContext;
            this.feedBody = obj.feedBody;
            this.referenceTitle = obj.referenceTitle;
            this.referencingLogSid = obj.referencingLogSid;
        }
    }
}

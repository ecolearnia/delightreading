import * as _  from "lodash";
import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";
import { Reference } from "./Reference";

@Entity("activity_log")
export class ActivityLog extends EntityBase {

    @Column({
        type: "bigint"
    })
    accountSid: number;

    @Column({
        type: "bigint"
    })
    referenceSid?: number;

    @Column({
        type: "bigint",
        nullable: true
    })
    referencingLogSid?: number;

    @Column({
        type: "bigint"
    })
    goalSid: number;

    @Column({
        type: "varchar",
        length: 64
    })
    activity: string; // "read", "write", etc.

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
    postEmotion: string;

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
        type: "text",
        nullable: true
    })
    retrospective?: string;

    reference?: Reference;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.accountSid = obj.accountSid;
            this.referenceSid = obj.referenceSid;
            this.referencingLogSid = obj.referencingLogSid;
            this.goalSid = obj.goalSid;
            this.activity = obj.activity;
            if (_.isString(obj.logTimestamp)) {
                this.logTimestamp = new Date();
                this.logTimestamp.setTime(Date.parse(obj.logTimestamp));
            } else {
                this.logTimestamp = obj.logTimestamp;
            }
            this.quantity = obj.quantity;
            this.postEmotion = obj.postEmotion;
            this.situation = obj.situation;
            this.feedContext = obj.feedContext;
            this.retrospective = obj.retrospective;
            this.feedBody = obj.feedBody;
        }
    }
}

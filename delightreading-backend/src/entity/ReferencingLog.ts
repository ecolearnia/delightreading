import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";
import { Reference } from "./Reference";

/**
 * This entity represents an instance of the user reading a reference(literature)
 * A user may read a book more than once.
 */
@Entity("referencing_log")
export class ReferencingLog extends EntityBase {

    @Column({
        type: "bigint"
    })
    accountSid: number;

    @Column({
        type: "bigint"
    })
    referenceSid: number;

    @Column({
        type: "date",
        nullable: true
    })
    startDate: Date;

    @Column({
        type: "date",
        nullable: true
    })
    endDate: Date;

    @Column({
        type: "int",
        nullable: true
    })
    percentageComplete?: number;

    @Column({
        type: "text",
        nullable: true
    })
    postEmotion: string;

    @Column({
        type: "int",
        nullable: true
    })
    myRating: number; // my rate out of 10

    @Column({
        type: "text",
        nullable: true
    })
    review: string;

    @Column({
        type: "text",
        nullable: true
    })
    likeReason: string;

    @Column({
        type: "text",
        nullable: true
    })
    synopsys: string;

    reference?: Reference;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.accountSid = obj.accountSid;
            this.referenceSid = obj.referenceSid;
            this.startDate = obj.startDate;
            this.endDate = obj.endDate;
            this.percentageComplete = obj.percentageComplete;
            this.postEmotion = obj.postEmotion;
            this.myRating = obj.myRating;
            this.review = obj.review;
            this.likeReason = obj.likeReason;
            this.synopsys = obj.synopsys;
        }
        // this.createdAt = new Date();
    }

}

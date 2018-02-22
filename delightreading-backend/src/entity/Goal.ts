import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

@Entity("goal")
export class Goal extends EntityBase {

    @Column({
        type: "bigint"
    })
    accountSid: number;

    @Column({
        type: "varchar",
        length: 256
    })
    title: string;

    @Column({
        type: "varchar",
        length: 64
    })
    activity: string;

    @Column({
        type: "date"
    })
    startDate: Date;

    @Column({
        type: "date"
    })
    endDate: Date;

    @Column({
        type: "date",
        nullable: true
    })
    actualCompletionDate?: Date;

    @Column({
        type: "decimal",
        precision: 6,
        scale: 3,
    })
    quantity: number;

    @Column({
        type: "varchar",
        length: 64
    })
    quantityUnit: string; // book, minutes

    @Column({
        type: "varchar",
        length: 127,
        nullable: true
    })
    timePeriod?: string; // every day, week, month, year

    @Column({
        type: "text",
        nullable: true
    })
    retrospective?: string;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.accountSid = obj.accountSid;
            this.title = obj.title;
            this.activity = obj.activity;
            // TODO: parse date. Note: it is possible that the date is partial, e.g. only year.
            this.startDate = obj.startDate;
            this.endDate = obj.endDate;
            this.actualCompletionDate = obj.actualCompletionDate;
            this.quantity = obj.quantity;
            this.quantityUnit = obj.quantityUnit;
            this.timePeriod = obj.timePeriod;
            this.retrospective = obj.retrospective;
        }
    }
}

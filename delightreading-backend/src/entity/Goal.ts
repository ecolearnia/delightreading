import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

@Entity("goal")
export class Goal extends EntityBase {

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
    actualCompletionDate: Date;

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
        type: "text",
        nullable: true
    })
    retrospective: string;

}

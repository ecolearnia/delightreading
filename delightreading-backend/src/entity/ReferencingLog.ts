import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

@Entity("referencing_log")
export class ReferencingLog extends EntityBase {

    @Column({
        type: "varchar",
        length: 256
    })
    title: string;

    @Column({
        type: "varchar",
        length: 128,
        nullable: true
    })
    author: string;

    @Column({
        type: "varchar",
        length: 64,
        nullable: true
    })
    isbn: string;

    @Column({
        type: "varchar",
        length: 256,
        nullable: true
    })
    bookUrl: string;

    @Column({
        type: "varchar",
        length: 256,
        nullable: true
    })
    imageUrl: string;

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
    myRate: number; // my rate out of 10

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
}

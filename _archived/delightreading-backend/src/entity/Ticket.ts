import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

@Entity("ticket")
export class Ticket extends EntityBase {

    @Column({
        type: "bigint",
        nullable: true
    })
    threadOfSid?: number;

    @Column({
        type: "varchar",
        length: 120
    })
    category?: string;

    @Column({
        type: "varchar",
        length: 256,
        nullable: true
    })
    name?: string;

    @Column({
        type: "text"
    })
    description?: string;

    @Column({
        type: "int",
        nullable: true
    })
    priority?: number;

    @Column({
        type: "json",
        nullable: true
    })
    tags: Array<string>;

    @Column({
        type: "varchar",
        length: 12
    })
    visibility: string;

    @Column({
        type: "varchar",
        length: 12
    })
    status: string;

    @Column({
        type: "date",
        nullable: true
    })
    closeDate?: Date;

    @Column({
        type: "bigint",
        nullable: true
    })
    closedBy?: number;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.threadOfSid = obj.threadOfSid;
            this.category = obj.category;
            this.name = obj.name;
            this.description = obj.description;
            this.priority = obj.priority;
            this.tags = obj.tags;
            this.visibility = obj.visibility;
            this.closeDate = obj.closeDate;
        }
    }
}

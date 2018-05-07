import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

@Entity("user_group")
export class UserGroup extends EntityBase {

    // TODO: use enum
    @Column({
        type: "varchar",
        length: 64
    })
    type: string; // family, academic, club, etc.

    @Column({
        type: "varchar",
        length: 255,
        nullable: true
    })
    name?: string;

    @Column({
        type: "text",
        nullable: true
    })
    description?: string;

    @Column({
        type: "varchar",
        length: 255,
        nullable: true
    })
    pictureUri?: string;

    @Column({
        type: "varchar",
        length: 255,
        nullable: true
    })
    coverImageUri?: string;

    @Column({
        type: "json",
        nullable: true
    })
    category?: string;

    @Column({
        type: "text",
        nullable: true
    })
    rules?: string;

    @Column({
        type: "varchar",
        length: 255,
        nullable: true
    })
    website?: string;

    @Column({
        type: "date",
        nullable: true
    })
    startDate?: Date;

    @Column({
        type: "date",
        nullable: true
    })
    closureDate?: Date;

    @Column({
        type: "varchar",
        length: 16,
        nullable: true
    })
    status?: string; // active, closed

    memberCount?: number;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.type = obj.type;
            this.name = obj.name;
            this.description = obj.description;
            this.pictureUri = obj.pictureUri;
            this.coverImageUri = obj.coverImageUri;
            this.category = obj.category;
            this.rules = obj.rules;
            this.website = obj.website;
            this.startDate = obj.startDate;
            this.closureDate = obj.closureDate;
            this.status = obj.status;
        }
    }
}

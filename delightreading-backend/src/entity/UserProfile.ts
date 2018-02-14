import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";
import { UserAccount } from "./UserAccount";

import { Address } from "./valueobject/Address";
import { Experience } from "./valueobject/Experience";

@Entity("user_profile")
export class UserProfile extends EntityBase {

    @Column({
        type: "bigint"
    })
    accountSid?: number;

    @Column({
        type: "json",
        nullable: true
    })
    emails: Array<string>;

    @Column({
        type: "text",
        nullable: true
    })
    synopsys?: string;

    @Column({
        type: "text",
        nullable: true
    })
    hometown?: string;

    @Column({
        type: "json",
        nullable: true
    })
    education?: Array<Experience>;

    @Column({
        type: "json",
        nullable: true
    })
    expertise?: object; // {subject, level}

    @Column({
        type: "json",
        nullable: true
    })
    experiences?: Array<Experience>;

    @Column({
        type: "json",
        nullable: true
    })
    accomplishments?: Array<Experience>;

    @Column({
        type: "text",
        nullable: true
    })
    style?: string; // Working/teaching/reading style: hands-on, theory-first...

    @Column({
        type: "json",
        nullable: true
    })
    interests?: Array<string>;

    @Column({
        type: "json",
        nullable: true
    })
    languages?: Array<string>;

    @Column({
        type: "varchar",
        length: 4,
        nullable: true
    })
    gender?: string;

    @Column({
        type: "json",
        nullable: true
    })
    websites?: Array<string>;

    // To be fetched through leftJoin
    account?: UserAccount;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.accountSid = obj.accountSid;
            this.emails = obj.emails;
            this.synopsys = obj.synopsys;
            this.hometown = obj.hometown;
            this.education = obj.education;
            this.expertise = obj.expertise;
            this.experiences = obj.experiences;
            this.accomplishments = obj.accomplishments;
            this.style = obj.style;
            this.interests = obj.interests;
            this.languages = obj.languages;
            this.gender = obj.gender;
            this.websites = obj.websites;
        }
    }
}

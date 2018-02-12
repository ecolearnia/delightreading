import { Entity, PrimaryGeneratedColumn, Column, PrimaryColumn, Generated } from "typeorm";
import { EntityBase } from "./EntityBase";

// Aka: Book, literature, resource
@Entity("reference")
export class Reference extends EntityBase {

    // Source where the data was obtained from, eg. Google Book's selfLink
    @Column({
        type: "varchar",
        length: 256,
        nullable: true
    })
    sourceUri: string;

    @Column({
        type: "varchar",
        length: 128
    })
    title: string;

    @Column({
        type: "varchar",
        length: 128,
        nullable: true
    })
    authors: string;

    @Column({
        type: "varchar",
        length: 128,
        nullable: true
    })
    publisher: string;

    @Column({
        type: "date",
        nullable: true
    })
    publishedDate: Date;

    @Column({
        type: "text",
        nullable: true
    })
    description: string;

    @Column({
        type: "text",
        nullable: true
    })
    synopsys: string;

    // ISBN's
    @Column({
        type: "json",
        nullable: true
    })
    identifiers: object;

    @Column({
        type: "int",
        nullable: true
    })
    pageCount: number;

    @Column({
        type: "json",
        nullable: true
    })
    categories: object;

    @Column({
        type: "varchar",
        length: 64,
        nullable: true
    })
    maturityRating: string;

    @Column({
        type: "varchar",
        length: 6,
        nullable: true
    })
    language: string;

    @Column({
        type: "varchar",
        length: 350,
        nullable: true
    })
    imageUrl: string;

    @Column({
        type: "varchar",
        length: 350,
        nullable: true
    })
    thumbnailImageUrl: string;

    @Column({
        type: "json",
        nullable: true
    })
    awards: object;

    constructor(obj: any = undefined) {
        super(obj);
        if (obj) {
            this.sourceUri = obj.sourceUri;
            this.title = obj.title;
            this.authors = obj.authors;
            this.publisher = obj.publisher;
            this.publishedDate = obj.publishedDate;
            this.description = obj.description;
            this.synopsys = obj.synopsys;
            this.identifiers = obj.identifiers;
            this.pageCount = obj.pageCount;
            this.categories = obj.categories;
            this.maturityRating = obj.maturityRating;
            this.language = obj.language;
            this.imageUrl = obj.imageUrl;
            this.thumbnailImageUrl = obj.thumbnailImageUrl;
            this.awards = obj.awards;
        }
        // this.createdAt = new Date();
    }
}

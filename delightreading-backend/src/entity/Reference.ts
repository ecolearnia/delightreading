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
        type: "json",
        nullable: true
    })
    authors: object;

    @Column({
        type: "varchar",
        length: 128,
        nullable: true
    })
    publisher: string;

    @Column({
        type: "varchar",
        length: 12,
        nullable: true
    })
    publishedDate: string;

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
        type: "decimal",
        precision: 3,
        scale: 1,
        nullable: true
    })
    averageRating: number;

    @Column({
        type: "int",
        nullable: true
    })
    ratingsCount: number;

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
            // TODO: parse date. Note: it is possible that the date is partial, e.g. only year.
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

import { MigrationInterface, QueryRunner } from "typeorm";

export class RefactorEntities1520917666731 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`CREATE TABLE "activity_log" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "accountSid" bigint NOT NULL, "referenceSid" bigint NOT NULL, "referencingLogSid" bigint, "goalSid" bigint, "activity" character varying(64) NOT NULL, "logTimestamp" TIMESTAMP NOT NULL, "quantity" numeric(6,2), "duration" integer, "currentPage" integer, "percentageComplete" integer, "postEmotion" text, "situation" text, "feedContext" character varying(256), "feedBody" text, "retrospective" text, "approvedByUid" character varying(80), "approvedAt" TIMESTAMP, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "goal" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "accountSid" bigint NOT NULL, "title" character varying(256) NOT NULL, "activity" character varying(64) NOT NULL, "startDate" date NOT NULL, "endDate" date NOT NULL, "actualCompletionDate" date, "quantity" numeric(6,2) NOT NULL, "quantityUnit" character varying(64) NOT NULL, "timePeriod" character varying(127), "retrospective" text, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "reference" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "sourceUri" character varying(256), "title" character varying(128) NOT NULL, "authors" json, "publisher" character varying(128), "publishedDate" character varying(12), "description" text, "synopsys" text, "identifiers" json, "pageCount" integer, "categories" json, "averageRating" numeric(3,1), "ratingsCount" integer, "maturityRating" character varying(64), "language" character varying(6), "imageUrl" character varying(350), "thumbnailImageUrl" character varying(350), "awards" json, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "referencing_log" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "accountSid" bigint NOT NULL, "referenceSid" bigint NOT NULL, "startDate" date, "endDate" date, "percentageComplete" integer, "postEmotion" text, "myRating" integer, "review" text, "likeReason" text, "synopsys" text, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "user_account" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "username" character varying(64) NOT NULL, "email" character varying(127) NOT NULL, "nickname" character varying(64), "givenName" character varying(64) NOT NULL, "familyName" character varying(64), "middleName" character varying(64), "dateOfBirth" date, "pictureUri" character varying(255), "locale" character varying(12), "timezone" character varying(20), "timeoffset" integer, "verifiedInd" boolean, "passwordResetToken" character varying(32), "passwordResetExpires" TIMESTAMP, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "user_auth" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "accountSid" bigint NOT NULL, "provider" character varying(64) NOT NULL, "providerAccountId" character varying(64), "password" character varying(64), "token" character varying(64), "expires" date, "rawProfile" text, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "user_profile" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "accountSid" bigint NOT NULL, "emails" json, "synopsys" text, "hometown" text, "education" json, "expertise" json, "experiences" json, "accomplishments" json, "style" text, "interests" json, "languages" json, "gender" character varying(4), "websites" json, PRIMARY KEY("sid"))`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`DROP TABLE "user_profile"`);
        await queryRunner.query(`DROP TABLE "user_auth"`);
        await queryRunner.query(`DROP TABLE "user_account"`);
        await queryRunner.query(`DROP TABLE "referencing_log"`);
        await queryRunner.query(`DROP TABLE "reference"`);
        await queryRunner.query(`DROP TABLE "goal"`);
        await queryRunner.query(`DROP TABLE "activity_log"`);
    }

}

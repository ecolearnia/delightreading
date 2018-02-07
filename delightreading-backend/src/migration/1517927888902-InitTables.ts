import { MigrationInterface, QueryRunner } from "typeorm";

export class InitTables1517927888902 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`CREATE TABLE "activity_log" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "goalSid" bigint NOT NULL, "activity" character varying(64) NOT NULL, "logTimestamp" TIMESTAMP NOT NULL, "quantity" numeric(6,3) NOT NULL, "situation" text, "feedContext" character varying(256), "feedBody" text, "referencingLogSid" bigint, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "goal" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "title" character varying(256) NOT NULL, "activity" character varying(64) NOT NULL, "startDate" date NOT NULL, "endDate" date NOT NULL, "actualCompletionDate" date, "quantity" numeric(6,3) NOT NULL, "quantityUnit" character varying(64) NOT NULL, "retrospective" text, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "referencing_log" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "title" character varying(256) NOT NULL, "author" character varying(128), "isbn" character varying(64), "bookUrl" character varying(256), "imageUrl" character varying(256), "startDate" date, "endDate" date, "myRate" integer, "review" text, "likeReason" text, "synopsys" text, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "user_account" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "username" character varying(64) NOT NULL, "email" character varying(127) NOT NULL, "password" character varying(64) NOT NULL, "givenName" character varying(64) NOT NULL, "familyName" character varying(64), "middleName" character varying(64), "dateOfBirth" date, "pictureUri" character varying(255), "locale" character varying(12), "timezone" character varying(20), "timeoffset" integer, "verifiedInd" boolean, "passwordResetToken" character varying(32), "passwordResetExpires" TIMESTAMP, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "user_auth" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "accountSid" bigint NOT NULL, "provider" character varying(64) NOT NULL, "providerAccountId" character varying(64), "token" character varying(64), "expires" date, PRIMARY KEY("sid"))`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`DROP TABLE "user_auth"`);
        await queryRunner.query(`DROP TABLE "user_account"`);
        await queryRunner.query(`DROP TABLE "referencing_log"`);
        await queryRunner.query(`DROP TABLE "goal"`);
        await queryRunner.query(`DROP TABLE "activity_log"`);
    }

}

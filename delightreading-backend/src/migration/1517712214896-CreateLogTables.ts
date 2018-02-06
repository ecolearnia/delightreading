import { MigrationInterface, QueryRunner } from "typeorm";

export class CreateLogTables1517712214896 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`CREATE TABLE "activity_log" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "goalSid" bigint NOT NULL, "activity" character varying(64) NOT NULL, "logTimestamp" TIMESTAMP NOT NULL, "quantity" numeric(6,3) NOT NULL, "situation" text, "feedContext" character varying(256), "feedBody" text, "referencingLogSid" bigint NOT NULL, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "goal" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "title" character varying(256) NOT NULL, "activity" character varying(64) NOT NULL, "startDate" date NOT NULL, "endDate" date NOT NULL, "actualCompletionDate" date, "quantity" numeric(6,3) NOT NULL, "quantityUnit" character varying(64) NOT NULL, "retrospective" text, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "referencing_log" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "title" character varying(256) NOT NULL, "author" character varying(128), "isbn" character varying(64), "bookUrl" character varying(256), "imageUrl" character varying(256), "startDate" date, "endDate" date, "myRate" integer, "review" text, "likeReason" text, "synopsys" text, PRIMARY KEY("sid"))`);
        await queryRunner.query(`ALTER TABLE "public"."user_account" ALTER COLUMN "sid" TYPE integer`);
        await queryRunner.query(`CREATE SEQUENCE "public"."user_account_sid_seq" OWNED BY "public"."user_account"."sid"`);
        await queryRunner.query(`ALTER TABLE "public"."user_account" ALTER COLUMN "sid" SET DEFAULT nextval('"public.user_account_sid_seq"')`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`-- TODO: revert ALTER TABLE "public"."user_account" ALTER COLUMN "sid" SET DEFAULT nextval('"public.user_account_sid_seq"')`);
        await queryRunner.query(`-- TODO: revert CREATE SEQUENCE "public"."user_account_sid_seq" OWNED BY "public"."user_account"."sid"`);
        await queryRunner.query(`-- TODO: revert ALTER TABLE "public"."user_account" ALTER COLUMN "sid" TYPE integer`);
        await queryRunner.query(`DROP TABLE "referencing_log"`);
        await queryRunner.query(`DROP TABLE "goal"`);
        await queryRunner.query(`DROP TABLE "activity_log"`);
    }

}

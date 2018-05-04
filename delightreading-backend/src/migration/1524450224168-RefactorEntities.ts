import { MigrationInterface, QueryRunner } from "typeorm";

export class RefactorEntities1524450224168 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`CREATE TABLE "user_group" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "type" character varying(64) NOT NULL, "description" text, "pictureUri" character varying(255), "coverImageUri" character varying(255), "category" json, "rules" text, "website" character varying(255), "startDate" date, "closureDate" date, PRIMARY KEY("sid"))`);
        await queryRunner.query(`CREATE TABLE "user_group_member" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "groupSid" bigint NOT NULL, "accountSid" bigint NOT NULL, "role" character varying(64) NOT NULL, "sinceDate" date, "untilDate" date, PRIMARY KEY("sid"))`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`DROP TABLE "user_group_member"`);
        await queryRunner.query(`DROP TABLE "user_group"`);
    }

}

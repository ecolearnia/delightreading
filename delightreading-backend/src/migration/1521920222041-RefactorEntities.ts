import { MigrationInterface, QueryRunner } from "typeorm";

export class RefactorEntities1521920222041 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`CREATE TABLE "ticket" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdAt" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "threadOfSid" bigint, "category" character varying(120) NOT NULL, "name" character varying(256), "description" text NOT NULL, "priority" integer, "tags" json, "visibility" character varying(12) NOT NULL, "closeDate" date, "closedBy" bigint, PRIMARY KEY("sid"))`);
        await queryRunner.query(`ALTER TABLE "public"."user_account" ADD "role" json`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."user_account" DROP "role"`);
        await queryRunner.query(`DROP TABLE "ticket"`);
    }

}

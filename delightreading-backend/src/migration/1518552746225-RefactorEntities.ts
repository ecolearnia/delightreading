import { MigrationInterface, QueryRunner } from "typeorm";

export class RefactorEntities1518552746225 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`CREATE TABLE "user_profile" ("sid" SERIAL NOT NULL, "uid" character varying(80) NOT NULL, "status" character varying(12), "createdat" TIMESTAMP, "createdBy" bigint, "updatedAt" TIMESTAMP, "updatedBy" bigint, "accountSid" bigint NOT NULL, "emails" json, "synopsys" text, "hometown" text, "education" json, "expertise" json, "experiences" json, "accomplishments" json, "style" text, "interests" json, "languages" json, "gender" character varying(4), "websites" json, PRIMARY KEY("sid"))`);
        await queryRunner.query(`ALTER TABLE "public"."activity_log" ADD "postEmotion" text`);
        await queryRunner.query(`ALTER TABLE "public"."activity_log" ADD "retrospective" text`);
        await queryRunner.query(`ALTER TABLE "public"."referencing_log" ADD "postEmotion" text`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."referencing_log" DROP "postEmotion"`);
        await queryRunner.query(`ALTER TABLE "public"."activity_log" DROP "retrospective"`);
        await queryRunner.query(`ALTER TABLE "public"."activity_log" DROP "postEmotion"`);
        await queryRunner.query(`DROP TABLE "user_profile"`);
    }

}

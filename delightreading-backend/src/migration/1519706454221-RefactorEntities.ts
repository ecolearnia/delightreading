import {MigrationInterface, QueryRunner} from "typeorm";

export class RefactorEntities1519706454221 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."referencing_log" ADD "percentageComplete" integer`);
        await queryRunner.query(`ALTER TABLE "public"."activity_log" ALTER COLUMN "goalSid" TYPE bigint`);
        await queryRunner.query(`ALTER TABLE "public"."activity_log" ALTER COLUMN "goalSid" DROP NOT NULL`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`-- TODO: revert ALTER TABLE "public"."activity_log" ALTER COLUMN "goalSid" DROP NOT NULL`);
        await queryRunner.query(`-- TODO: revert ALTER TABLE "public"."activity_log" ALTER COLUMN "goalSid" TYPE bigint`);
        await queryRunner.query(`ALTER TABLE "public"."referencing_log" DROP "percentageComplete"`);
    }

}

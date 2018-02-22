import {MigrationInterface, QueryRunner} from "typeorm";

export class RefactorEntities1519280123004 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."goal" ADD "accountSid" bigint NOT NULL`);
        await queryRunner.query(`ALTER TABLE "public"."goal" ADD "timePeriod" character varying(127)`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."goal" DROP "timePeriod"`);
        await queryRunner.query(`ALTER TABLE "public"."goal" DROP "accountSid"`);
    }

}

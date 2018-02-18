import {MigrationInterface, QueryRunner} from "typeorm";

export class RefactorEntities1518907695427 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."user_account" DROP "password"`);
        await queryRunner.query(`ALTER TABLE "public"."user_account" ADD "nickname" character varying(64)`);
        await queryRunner.query(`ALTER TABLE "public"."user_auth" ADD "password" character varying(64)`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."user_auth" DROP "password"`);
        await queryRunner.query(`ALTER TABLE "public"."user_account" DROP "nickname"`);
        await queryRunner.query(`ALTER TABLE "public"."user_account" ADD "password" character varying(64) NOT NULL`);
    }

}

import { MigrationInterface, QueryRunner } from "typeorm";

export class RefactorEntities1518845313726 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."reference" ADD "averageRating" numeric(3,1)`);
        await queryRunner.query(`ALTER TABLE "public"."reference" ADD "ratingsCount" integer`);
        await queryRunner.query(`ALTER TABLE "public"."reference" ALTER COLUMN "publishedDate" TYPE character varying(12)`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`-- TODO: revert ALTER TABLE "public"."reference" ALTER COLUMN "publishedDate" TYPE character varying(12)`);
        await queryRunner.query(`ALTER TABLE "public"."reference" DROP "ratingsCount"`);
        await queryRunner.query(`ALTER TABLE "public"."reference" DROP "averageRating"`);
    }

}

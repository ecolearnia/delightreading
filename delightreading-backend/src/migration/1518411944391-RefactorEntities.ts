import {MigrationInterface, QueryRunner} from "typeorm";

export class RefactorEntities1518411944391 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."reference" ADD "imageUrl" character varying(350)`);
        await queryRunner.query(`ALTER TABLE "public"."reference" ADD "thumbnailImageUrl" character varying(350)`);
    }

    public async down(queryRunner: QueryRunner): Promise<any> {
        await queryRunner.query(`ALTER TABLE "public"."reference" DROP "thumbnailImageUrl"`);
        await queryRunner.query(`ALTER TABLE "public"."reference" DROP "imageUrl"`);
    }

}

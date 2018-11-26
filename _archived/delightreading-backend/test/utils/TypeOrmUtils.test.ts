import TypeOrmUtils from "../../src/utils/TypeOrmUtils";

describe("TypeOrmUtils", () => {

    describe("andedWhereClause",  async () => {
        it("should return anded clause", () => {

            const criteria = {
                provider: "TEST",
                providerAccountId: "TEST"
            };
            const clause = TypeOrmUtils.andedWhereClause(criteria, "user_auth");

            expect (clause).toEqual('user_auth."provider" = :provider AND user_auth."providerAccountId" = :providerAccountId');
        });
    });
});

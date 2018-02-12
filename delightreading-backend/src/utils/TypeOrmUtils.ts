
export default class TypeOrmUtils {

    /**
     * given alias = "user_auth"
     * Sample outcome: "user_auth.provider = :provider AND user_auth.providerAccountId = :providerAccountId"
     * @param criteria
     * @param alias
     */
    static andedWhereClause(criteria: any, alias: string = "") {

        if (alias) {
            alias += ".";
        }
        const conditions: string[] = [];
        for (const field in criteria) {
            conditions.push(alias + field + " = :" + field);
        }

        return conditions.join(" AND ");
    }
}
enum SqlParamType {
    NAMED, QMARK, DOLLAR
}

export default class TypeOrmUtils {

    static SqlParamType = SqlParamType;

    /**
     * given alias = "user_auth"
     * Sample outcome: "user_auth.provider = :provider AND user_auth.providerAccountId = :providerAccountId"
     * @param criteria - Object of name value pairs
     * @param alias - string
     */
    static andedWhereClause(criteria: any, alias: string = "", paramType: SqlParamType = SqlParamType.NAMED, paramIndexStart = 1) {

        if (alias) {
            alias += ".";
        }
        const conditions: string[] = [];
        let pIndex = paramIndexStart;
        for (const prop in criteria) {
            let paramLiteral;
            switch (paramType) {
                case SqlParamType.NAMED: paramLiteral = ":" + prop; break;
                case SqlParamType.QMARK: paramLiteral = "?"; break;
                default: paramLiteral = "$" + (pIndex++);
            }
            conditions.push(alias + "\"" + prop + "\" = " + paramLiteral);
        }

        return conditions.join(" AND ");
    }
}

export const DEFAULT_COLUMNS = "sid BIGSERIAL PRIMARY KEY, " +
    "uid varchar(80) UNIQUE , " +
    "status varchar(12) UNIQUE, " +
    "createdAt date NULL, " +
    "createdBy bigint NULL, " +
    "updatedAt date NULL, " +
    "updatedBy bigint NULL, ";
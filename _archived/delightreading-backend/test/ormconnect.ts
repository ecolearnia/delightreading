import { Connection, createConnection } from "typeorm";

import { ActivityLog } from "../src/entity/ActivityLog";
import { Goal } from "../src/entity/Goal";
import { Reference } from "../src/entity/Reference";
import { ReferencingLog } from "../src/entity/ReferencingLog";
import { UserAccount } from "../src/entity/UserAccount";
import { UserAuth } from "../src/entity/UserAuth";
import { UserProfile } from "../src/entity/UserProfile";
import { UserGroup } from "../src/entity/UserGroup";
import { UserGroupMember } from "../src/entity/UserGroupMember";
import { Ticket } from "../src/entity/Ticket";

export async function createTestConnection( entities: any) {
    return await createConnection({
        type: "postgres",
        host: "localhost",
        port: 5432,
        username: "delightreading",
        password: "delightreading",
        database: "delightreading_test",
        entities: [ActivityLog, Goal, Reference, ReferencingLog, 
            UserAccount, UserAuth, UserProfile, UserGroup, UserGroupMember,
            Ticket
        ],
        logging: false, // true,
        dropSchema: true, // Isolate each test case
        synchronize: true // regenerate tables
      });
}

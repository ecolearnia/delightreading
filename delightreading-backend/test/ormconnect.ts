import { Connection, createConnection } from "typeorm";

import { ActivityLog } from "../src/entity/ActivityLog";
import { Goal } from "../src/entity/Goal";
import { Reference } from "../src/entity/Reference";
import { ReferencingLog } from "../src/entity/ReferencingLog";
import { UserAccount } from "../src/entity/UserAccount";
import { UserAuth } from "../src/entity/UserAuth";
import { UserProfile } from "../src/entity/UserProfile";
import { Ticket } from "../src/entity/Ticket";

export async function createTestConnection( entities: any) {
    return await createConnection({
        type: "postgres",
        host: "localhost",
        port: 5432,
        username: "test",
        password: "test",
        database: "test",
        entities: [ActivityLog, Goal, Reference, ReferencingLog, UserAccount, UserAuth, UserProfile,
            Ticket
        ],
        logging: false, // true,
        dropSchema: true, // Isolate each test case
        synchronize: true // regenerate tables
      });
}

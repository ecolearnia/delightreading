import { Connection, createConnection } from "typeorm";

export async function createTestConnection( entities: any) {
    return await createConnection({
        type: "postgres",
        host: "localhost",
        port: 5432,
        username: "test",
        password: "test",
        database: "test",
        entities: entities,
        logging: false,
        dropSchema: true, // Isolate each test case
        synchronize: true
      });
}

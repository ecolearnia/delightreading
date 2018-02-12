import * as request from "supertest";
import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";

describe("GET /random-url", () => {

  let connection: Connection;
  let app;
  beforeEach(async () => {
    connection = await createTestConnection(undefined);

    app = require("../../src/app");
  });

  afterEach(async () => {
    // await this.connection.dropDatabase();
    await connection.close();
  });

  it("should return 404", (done) => {
    request(app).get("/reset")
      .expect(404, done);
  });
});

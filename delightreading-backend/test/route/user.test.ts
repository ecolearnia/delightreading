import * as request from "supertest";
import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";

var chai = require("chai");
var expect = chai.expect;

describe("POST /user/", () => {

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

  describe("POST /user/", () => {
    it("should create user and return 200", (done) => {
      request(app).post("/api/users/")
        .send({ username: "itest-user", email: "j@testland.com", givenName: "John", familyName: "Doe", dateOfBirth: "1975-05-25", password: "abc" })
        .expect(200)
        .end(function (err, res) {
          if (err) throw err;
          // console.log(res);
          done();
        });
    });
  });

  describe("GET /user/", () => {
    it("should return list of users", (done) => {
      request(app).get("/api/users/")
        .expect(200)
        .end(function (err, res) {
          if (err) throw err;
          // console.log(res);
          done();
        });
    });
  });
});

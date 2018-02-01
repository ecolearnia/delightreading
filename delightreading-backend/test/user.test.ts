import * as request from "supertest";
import { createConnection } from "typeorm";
import * as app from "../src/app";

var chai = require('chai');
var expect = chai.expect;

beforeEach(() => {
  // this.connection = createConnection({ separate configuration for testing });
});

afterEach(() => {
  // await this.connection.dropDatabase();
});

describe("POST /user/", () => {
  it("should create user and return 200", (done) => {
    request(app).post("/api/user/")
      .send({ username: "", givenName: "John", familyName: "Doe", dateOfBirth: "1975-05-25" })
      .expect(200)
      .end(function(err, res) {
        if (err) throw err;
        console.log(res);
        done();
      });
  });
});

describe("GET /user/", () => {
  it("should return list of users", (done) => {
    request(app).get("/api/user/")
      .expect(200)
      .end(function(err, res) {
        if (err) throw err;
        console.log(res);
        done();
      });
  });
});


import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";
import { Goal } from "../../src/entity/Goal";
import { GoalService } from "../../src/service/GoalService";

const sampleReference = require("../sample-data/reference.hole.sample.json");

const expect = require("chai").expect;

describe("GoalService", () => {

  let connection: Connection;
  let sut: GoalService;

  function newTestGoal(accountSid: number, title: string): Goal {
    return new Goal({
      accountSid: accountSid,
      title: title,
      activity: "Testing",
      startDate: new Date(),
      endDate: new Date(),
      quantity: 10,
      quantityUnit: "books"
    });
  }

  beforeEach(async () => {
    connection = await createTestConnection(undefined);
  });

  afterEach(async () => {
    await connection.close();
  });

  describe("Save", () => {
    it("should save Goal", async () => {
      const service = new GoalService();
      const aGoal = newTestGoal(1, "Test  Goal");
      const saved = await service.save(aGoal);
      expect(saved.title).equal("Test  Goal");
    });
  });

  describe("List", () => {
    it("should list Goals", async () => {
      const service = new GoalService();

      const goals = Array<Goal>();
      goals.push(newTestGoal(1, "Test  Goal A"));
      goals.push(newTestGoal(1, "Test  Goal B"));
      await service.saveMany(goals);

      const result = await service.list({accountSid: 1});
      // console.log("Goals: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(2);
      expect(result[0].title).to.equal("Test  Goal A");
      expect(result[1].title).to.equal("Test  Goal B");
    });
  });
});

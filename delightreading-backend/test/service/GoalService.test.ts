import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";
import { Goal } from "../../src/entity/Goal";
import { GoalService } from "../../src/service/GoalService";

const sampleReference = require("../sample-data/reference.hole.sample.json");

const expect = require("chai").expect;

describe("GoalService", () => {

  let connection: Connection;
  let sut: GoalService;

  function newTestGoal(accountSid: number, title: string, startDate: Date = new Date, endDate: Date = new Date, activity: string = "Testing"): Goal {
    return new Goal({
      accountSid: accountSid,
      title: title,
      activity: activity,
      startDate: startDate,
      endDate: endDate,
      quantity: 10,
      quantityUnit: "books"
    });
  }

  function dayFromDate(date:Date, days: number) {
    const DAY_IN_MS = 86400000;

    if (!date) {
      date = new Date();
    }
    return new Date(date.getTime() + (DAY_IN_MS * days) );
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

  describe("findActiveGoals", () => {
    it("should find active Goals", async () => {
      const service = new GoalService();

      const goals = Array<Goal>();
      const today: Date = new Date();

      goals.push(newTestGoal(1, "Test Goal A s:before, e:before", dayFromDate(today, -4), dayFromDate(today, -2), "reading" ));
      goals.push(newTestGoal(1, "Test Goal B s:before, e:after", dayFromDate(today, -2), dayFromDate(today, 1), "reading" ));
      goals.push(newTestGoal(1, "Test Goal C s:before, e:after", dayFromDate(today, -1), dayFromDate(today, 2), "reading" ));
      goals.push(newTestGoal(1, "Test Goal D s:after, e:after", dayFromDate(today, 3), dayFromDate(today, 4), "reading" ));
      goals.push(newTestGoal(1, "Test Goal D-testing s:after, e:after", dayFromDate(today, 3), dayFromDate(today, 4), "testing" ));
      goals.push(newTestGoal(2, "Test Goal 2X s:before, e:after", dayFromDate(today, -2), dayFromDate(today, 1), "reading" ));
      await service.saveMany(goals);

      const result = await service.findActiveGoals(1);
      // console.log("Goals: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(2);
      expect(result[0].title).to.equal("Test Goal B s:before, e:after");
      expect(result[1].title).to.equal("Test Goal C s:before, e:after");
    });
  });
});

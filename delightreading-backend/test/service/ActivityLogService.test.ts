import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";
import { ActivityLog } from "../../src/entity/ActivityLog";
import { ActivityLogService } from "../../src/service/ActivityLogService";

import { Reference } from "../../src/entity/Reference";
import { ReferenceService } from "../../src/service/ReferenceService";

const sampleReference = require("../sample-data/reference.hole.sample.json");

const expect = require("chai").expect;

describe("ActivityLogService", () => {

  let connection: Connection;
  let sut: ActivityLogService;

  function newActivityLog(accountSid: number, referenceSid: number, goalSid: number, 
    activity: string, duration: number, logTimestamp: Date = new Date(), referencingLogSid = 1): ActivityLog 
  {
    const activityLog = new ActivityLog({
        accountSid: accountSid,
        referenceSid: referenceSid,
        referencingLogSid: referencingLogSid,
        goalSid: goalSid,
        activity: activity,
        logTimestamp: logTimestamp,
        duration: duration,
        createdAt: new Date()
    });
    return activityLog;
}

  async function createReference(title: string): Promise<Reference> {
    const referenceService = new ReferenceService();
    const reference = new Reference(sampleReference);
    reference.title = title;
    return referenceService.save(reference);
  }

  beforeEach(async () => {
    connection = await createTestConnection(undefined);

    sut = new ActivityLogService();
    const activityLogs = Array<ActivityLog>();

    const reference = await createReference("TEST-TITLE");
    // 2018-02-21 is the Monday 
    activityLogs.push(newActivityLog(1, reference.sid, 0, "read", 11, new Date(2018,1,21), 1));
    activityLogs.push(newActivityLog(1, 0, 0, "read", 12, new Date(2018,1,22), 1));
    activityLogs.push(newActivityLog(1, reference.sid, 0, "read", 13, new Date(2018,1,26), 2));
    activityLogs.push(newActivityLog(1, reference.sid, 0, "read", 14, new Date(2018,1,26), 2));
    activityLogs.push(newActivityLog(1, 0, 0, "read", 15, new Date(2018,2,2), 2));
    activityLogs.push(newActivityLog(2, 0, 0, "read", 13, new Date(2018,1,27), 3));

    const saved = await sut.saveMany(activityLogs);
  });

  afterEach(async () => {
    await connection.close();
  });

  describe("Save", () => {
    it("should save ActivityLog", async () => {
      const service = new ActivityLogService();

      const activityLog = newActivityLog(1, 0, 0, "read", 11);

      const saved = await service.save(activityLog);
      expect(saved.activity).equal("read");
    });
  });

  describe("List", () => {
    it("should list ActivityLog", async () => {
      const service = new ActivityLogService();

      const result = await service.list({accountSid: 1});
      console.log("ActivityLogs: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(5);
      expect(result[0].reference).to.equal(undefined);
      expect(result[1].reference).to.be.not.null;
    });
  });

  describe("stats", () => {
    it("should return statistics summary", async () => {
      const service = new ActivityLogService();

      const result = await service.stats(1, new Date(2018, 1, 26), "read");
      // console.log("stats: " + JSON.stringify(result, undefined, 2));

      expect(result.month.activityDuration).to.equal(50);
      expect(result.month.activityCount).to.equal(4);
      expect(result.week.activityDuration).to.equal(42);
      expect(result.week.activityCount).to.equal(3);
      expect(result.day.activityDuration).to.equal(27);
      expect(result.day.activityCount).to.equal(2);
    });
  });

  describe("statsByReferencingLog", () => {
    it("should return statistics for specific referencingLog", async () => {
      const service = new ActivityLogService();

      const result = await service.statsByReferencingLog(1);
      console.log("statsByReferencingLog: " + JSON.stringify(result, undefined, 2));

      expect(result.totalDuration).to.equal(23);
      expect(result.totalCount).to.equal(2);
    });
  });


  describe("timeSeries", () => {
    it("should return time series for day", async () => {
      const service = new ActivityLogService();

      const result = await service.timeSeries(1, "day", new Date(2018, 1, 25), new Date(2018, 2, 1), "read");
      // console.log("day timeSeries: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(5);
      expect(result[1].activityDuration).to.equal(27);
      expect(result[1].activityCount).to.equal(2);
      // expect(result[1].activityDuration).to.equal(15);
      // expect(result[1].activityCount).to.equal(1);
    });

    it("should return time series for week", async () => {
      const service = new ActivityLogService();

      const result = await service.timeSeries(1, "week", new Date(2018, 1, 4), new Date(2018, 1, 25), "read");
      // console.log("week timeSeries: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(4);
      expect(result[2].activityDuration).to.equal(23);
      expect(result[2].activityCount).to.equal(2);
      expect(result[3].activityDuration).to.equal(42);
      expect(result[3].activityCount).to.equal(3);
    });

    it("should return time series for month", async () => {
      const service = new ActivityLogService();

      const result = await service.timeSeries(1, "month", new Date(2018, 1, 1), new Date(2018, 2, 31), "read");
      // console.log("month timeSeries: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(2);
      expect(result[0].activityDuration).to.equal(50);
      expect(result[0].activityCount).to.equal(4);
      expect(result[1].activityDuration).to.equal(15);
      expect(result[1].activityCount).to.equal(1);
    });
  });
});

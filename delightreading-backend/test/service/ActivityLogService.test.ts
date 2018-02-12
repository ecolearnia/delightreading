import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";
import { ActivityLogService } from "../../src/service/ActivityLogService";
import { ActivityLog } from "../../src/entity/activitylog";

import { Reference } from "../../src/entity/Reference";
import { ReferenceService } from "../../src/service/ReferenceService";

const sampleReference = require("../sample-data/reference.hole.sample.json");

const expect = require("chai").expect;

describe("ActivityLogService", () => {

  let connection: Connection;
  let sut: ActivityLogService;

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
    activityLogs.push(sut.createEntity(1, reference.sid, 0, "read", 11));
    activityLogs.push(sut.createEntity(1, 0, 0, "read", 12));
    activityLogs.push(sut.createEntity(2, 0, 0, "read", 12));

    const saved1 = await sut.save(activityLogs[0]);
    const saved2 = await sut.save(activityLogs[1]);
  });

  afterEach(async () => {
    await connection.close();
  });

  describe("Save", () => {
    it("should save ActivityLog", async () => {
      const service = new ActivityLogService();

      const activityLog = service.createEntity(1, 0, 0, "read", 11);

      const saved = await service.save(activityLog);
      expect(saved.activity).equal("read");
    });
  });

  describe("List", () => {
    it("should list ActivityLog", async () => {
      const service = new ActivityLogService();

      const result = await service.list({accountSid: 1});
      console.log("ActivityLogs: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(2);
      expect(result[0].reference).to.be.not.null;
      expect(result[1].reference).to.equal(undefined);
    });
  });
});

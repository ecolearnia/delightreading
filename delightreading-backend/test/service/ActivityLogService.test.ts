import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";
import { ActivityLogService } from "../../src/service/ActivityLogService";
import { ActivityLog } from "../../src/entity/activitylog";

var chai = require("chai");
var expect = chai.expect;

describe("ActivityLogService", () => {

  let connection: Connection;
  let sut: ActivityLogService;

  beforeEach(async () => {
    connection = await createTestConnection(undefined);

    sut = new ActivityLogService();
    const activityLogs = Array<ActivityLog>();
    activityLogs.push(sut.createEntity(1, 0, 0, "read", 11));
    activityLogs.push(sut.createEntity(1, 0, 0, "read", 12));

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

    it("should list ActivityLog", async () => {
      const service = new ActivityLogService();

      const result = await service.list();
      expect(result).to.have.lengthOf(2);
    });
  });
});

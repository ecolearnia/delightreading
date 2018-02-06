import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect"
import { ActivityLogService } from "../../src/service/ActivityLogService";
import { ActivityLog } from "../../src/entity/activitylog";

var chai = require('chai');
var expect = chai.expect;

describe('ActivityLogService', () => {

  let connection: Connection;
  let sut: ActivityLogService;

  beforeEach(async () => {
    connection = await createTestConnection([ActivityLog]);

    sut = new ActivityLogService()
    let activityLogs = Array<ActivityLog>();
    activityLogs.push(sut.createEntity(0, "read", 11));
    activityLogs.push(sut.createEntity(0, "read", 12));

    let saved1 = await sut.save(activityLogs[0]);
    let saved2 = await sut.save(activityLogs[1]);
  });

  afterEach(async () => {
    await connection.close();
  });

  describe("Save", () => {
    it("should save ActivityLog", async () => {
      let service = new ActivityLogService()

      let activityLog = service.createEntity(0, "read", 11);

      let saved = await service.save(activityLog);
      expect(saved.activity).equal("read");
    });

    it("should list ActivityLog", async () => {
      let service = new ActivityLogService()

      let result = await service.list();
      expect(result).to.have.lengthOf(2);
    });
  });
});

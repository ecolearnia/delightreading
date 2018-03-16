import { Connection, createConnection, getRepository } from "typeorm";
import { createTestConnection } from "../ormconnect";
import { ReferencingLog } from "../../src/entity/ReferencingLog";
import { ReferencingLogService } from "../../src/service/ReferencingLogService";

import { Reference } from "../../src/entity/Reference";
import { ReferenceService } from "../../src/service/ReferenceService";

import { ActivityLog } from "../../src/entity/ActivityLog";
import * as entityUtils from "../utils/EntityUtils";

const sampleReference = require("../sample-data/reference.hole.sample.json");

const expect = require("chai").expect;

describe("ReferencingLogService", () => {

  let connection: Connection;
  let sut: ReferencingLogService;
  let reference: Reference;

  async function createReference(title: string): Promise<Reference> {
    const referenceService = new ReferenceService();
    const reference = new Reference(sampleReference);
    reference.title = title;
    return referenceService.save(reference);
  }

  function newReferencingLog(accountSid: number, referenceSid: number, startDate: Date, endDate: Date, review: string = "test-review"): ReferencingLog {
    return new ReferencingLog({
      accountSid: accountSid,
      referenceSid: referenceSid,
      startDate: startDate,
      endDate: endDate,
      percentageComplete: 100,
      postEmotion: null,
      myRating: 5,
      review: review,
      likeReason: "likeReason",
      synopsys: "synopsys"
    });
  }

  beforeEach(async () => {
    connection = await createTestConnection(undefined);

    sut = new ReferencingLogService();
    const referencingLogs = Array<ReferencingLog>();

    reference = await createReference("TEST-TITLE");
    referencingLogs.push(newReferencingLog(1, reference.sid, new Date(2017, 2, 3),  new Date(2017, 3, 3), "r1"));
    referencingLogs.push(newReferencingLog(1, reference.sid, new Date(2017, 3, 3),  new Date(2017, 4, 5), "r2"));
    referencingLogs.push(newReferencingLog(2, reference.sid, new Date(2017, 6, 2),  new Date(2017, 7, 19), "r3"));

    const saved = await sut.saveMany(referencingLogs);

    const activityLogRepo = getRepository(ActivityLog);
    activityLogRepo.save(entityUtils.newActivityLog(1, reference.sid, 0, "read", 11, new Date(2018,1,21), saved[0].sid));
  });

  afterEach(async () => {
    await connection.close();
  });

  describe("Save", () => {
    it("should save ReferencingLog", async () => {
      const service = new ReferencingLogService();

      const reference = await createReference("TEST-TITLE2");
      const referencingLog = newReferencingLog(1, reference.sid, new Date(2017, 3, 3),  new Date(2017, 4, 5), "test-save");

      const saved = await service.save(referencingLog);
      expect(saved.review).equal("test-save");
    });
  });

  describe("List", () => {
    it("should list ReferencingLog", async () => {
      const service = new ReferencingLogService();

      const result = await service.list({accountSid: 1});
      // console.log("ReferencingLogs: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(2);
      expect(result[0].review).to.equal("r2");
      expect(result[1].review).to.equal("r1");
    });
  });

  describe("findOneRecentByAccountSidAndReferenceSid", () => {
    it("should find the most recent one", async () => {
      const service = new ReferencingLogService();

      const result = await service.findOneRecentByAccountSidAndReferenceSid(1, reference.sid);

      expect(result.review).to.equal("r2");
    });
  });
});

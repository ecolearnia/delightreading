import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";

import { Reference } from "../../src/entity/Reference";
import { ReferenceService } from "../../src/service/ReferenceService";

const sampleReference = require("../sample-data/reference.hole.sample.json");

const expect = require("chai").expect;

describe("ReferenceService", () => {

  let connection: Connection;
  let sut: ReferenceService;

  function newReference(title: string, authors: Array<string> = ["test"]): Reference {
    const reference = new Reference(sampleReference);
    reference.title = title;
    reference.authors = authors;
    return reference;
  }

  beforeEach(async () => {
    connection = await createTestConnection(undefined);

    sut = new ReferenceService();
    const references = Array<Reference>();

    references.push(newReference("test-title1", ["a2"]));
    references.push(newReference("test-title2", ["a1"]));
    references.push(newReference("test-title2", ["a2", "a22"]));
    references.push(newReference("test-title2", ["a3"]));

    const saved = await sut.saveMany(references);
  });

  afterEach(async () => {
    await connection.close();
  });

  describe("Save", () => {
    it("should save ReferencingLog", async () => {
      const service = new ReferenceService();

      const referencingLog = newReference("test-save", ["a1"]);

      const saved = await service.save(referencingLog);
      expect(saved.title).equal("test-save");
    });
  });

  describe("findOneByTitleAndAuthor", () => {
    it("given matching exists, should return one", async () => {
      const service = new ReferenceService();

      const result = await service.findOneByTitleAndAuthor("test-title2", "a22");

      expect(result.authors).deep.equal(["a2", "a22"]);
    });
    
    it("given title exists but not author, should return undefined", async () => {
      const service = new ReferenceService();

      const result = await service.findOneByTitleAndAuthor("test-title2", "a4");

      expect(result).to.be.undefined;
    });
  });
});

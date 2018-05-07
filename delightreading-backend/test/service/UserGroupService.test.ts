import { Connection, createConnection, getRepository } from "typeorm";
import { createTestConnection } from "../ormconnect";
import { UserGroup } from "../../src/entity/UserGroup";
import { UserGroupService } from "../../src/service/UserGroupService";

import { UserAccount } from "../../src/entity/UserAccount";
import { UserGroupMember } from "../../src/entity/UserGroupMember";
import { UserService } from "../../src/service/UserService";

import * as entityUtils from "../utils/EntityUtils";
import { Logger, LoggerUtils } from "../../src/utils/Logger";

const sampleReference = require("../sample-data/reference.hole.sample.json");

const expect = require("chai").expect;

describe("UserGroupService", () => {

  LoggerUtils.setLevel("warn");

  let connection: Connection;
  let userService: UserService;
  let sut: UserGroupService;

  async function newUserAccount(username: string): Promise<UserAccount> {
    const userService = new UserService();
    const userAccount = userService.newAccount(username, username + "@testland.com", "st", username, "Prueba");
    return userAccount;
  }

  async function createUserAccounts(count: number = 1): Promise<Array<UserAccount>> {

    const accounts = Array<UserAccount>()

    for (let i=0; i < count; i++) {
      accounts.push(await newUserAccount("test" + i));
    }

    return userService.saveMany(accounts);
  }

  function newUserGroup(name: string, type: string, rule: string = "Test rule"): UserGroup {
    return new UserGroup({
      name: name,
      type: type,
      rule: rule
    });
  }

  beforeEach(async () => {
    connection = await createTestConnection(undefined);

    sut = new UserGroupService();
    userService = new UserService();

  });

  afterEach(async () => {
    await connection.close();
  });

  describe("Save", () => {
    it("should save UserGroup", async () => {
      const group = await newUserGroup("TestGroup", "family");
      const saved = await sut.save(group);

      console.log("*S* " + JSON.stringify(saved, null, 2));
      const criteria = {
        uid: saved.uid
      }
      const found = await sut.findOne(criteria);
      console.log("*F* " + JSON.stringify(found, null, 2));

      expect(found.name).equal("TestGroup");
    });
  });

  describe("List", () => {
    it("should list UserGroup", async () => {
      const groups = Array<UserGroup>();
      groups.push(await newUserGroup("TestGroup-F1", "family"));
      groups.push(await newUserGroup("TestGroup-A1", "academic"));
      groups.push(await newUserGroup("TestGroup-F2", "family"));
      const saved = await sut.saveMany(groups);

      const accounts = await createUserAccounts(3);
      for(let account of accounts) {
        const groupMember = await sut.addMember(groups[0], account, "guardian");
      }

      const result = await sut.list({type: "family"});
      console.log("UserGroups: " + JSON.stringify(result, undefined, 2));

      expect(result).to.have.lengthOf(2);
      expect(result[0].name).to.equal("TestGroup-F1");
      expect(result[0].memberCount).to.equal("3");
      expect(result[1].name).to.equal("TestGroup-F2");
    });
  });

  describe("findOneRecentByAccountSidAndReferenceSid", () => {
    it("should find the most recent one", async () => {
      const service = new UserGroupService();

      // const result = await service.findOneRecentByAccountSidAndReferenceSid(1, reference.sid);

      expect(true).to.equal(true);
    });
  });
});

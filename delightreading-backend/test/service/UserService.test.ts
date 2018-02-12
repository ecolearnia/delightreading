import { Connection, createConnection } from "typeorm";
import { createTestConnection } from "../ormconnect";
import { UserService } from "../../src/service/UserService";
import { UserAccount } from "../../src/entity/UserAccount";
import { UserAuth } from "../../src/entity/UserAuth";

var chai = require("chai");
var expect = chai.expect;

describe("UserService", () => {

  let connection: Connection;
  let sut: UserService;

  beforeEach(async () => {
    connection = await createTestConnection([UserAccount, UserAuth]);

    sut = new UserService();
    const activityLogs = Array<UserAccount>();
    activityLogs.push(sut.newAccount("john", "john@testland.com", "jo", "John", "Doe"));
    activityLogs.push(sut.newAccount("jane", "jane@testland.com", "ja", "Jane", "Deer"));

    const saved1 = await sut.saveAccount(activityLogs[0]);
    const saved2 = await sut.saveAccount(activityLogs[1]);
  });

  afterEach(async () => {
    await connection.close();
  });

  describe("Save", () => {
    it("should save User", async () => {
      const service = new UserService();

      const account = service.newAccount("savetest", "savetest@testland.com", "st", "Tester", "Testez");

      const saved = await service.saveAccount(account);
      expect(saved.givenName).equal("Tester");
    });

    it("should list User", async () => {
      const service = new UserService();

      const result = await service.listAccounts();
      expect(result).to.have.lengthOf(2);
    });
  });

  describe("Register and SignIn", () => {
    it("should save Account and Auth, signIn", async () => {
      const service = new UserService();

      const auth  = service.newAuth({
        accountSid: undefined,
        provider: "testsite",
        providerAccountId: "testsidteid"
      });
      const account = service.newAccount("registtest", "registtest@testland.com", "st", "Tester", "Testez", auth);

      const registered = await service.registerAccount(account);

      expect(account.givenName).equal("Tester");
      expect(account.auths).to.have.lengthOf(1);

      try {
        const loggedAccount = await service.signIn(auth);
        expect(loggedAccount.givenName).equal("Tester");
        expect(loggedAccount.auths).to.have.lengthOf(1);
      } catch (e) {
        console.log(e);
      }
    });
  });

  // describe("Register", () => {
  //   it("should save Account and Auth", async () => {
  //     let service = new UserService()

  //     let auth  = service.newAuth({
  //       accountSid: null,
  //       provider: "testsite",
  //       providerAccountId: "testsidteid"
  //     });
  //     let account = service.newAccount("savetest", "savetest@testland.com", "st", "Tester", "Testez", auth);

  //     let registered = await service.registerAccount(account);
  //     expect(account.givenName).equal("Tester");
  //     expect(account.auths).to.have.lengthOf(1);
  //   });
  // });
});

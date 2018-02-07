import "reflect-metadata";
import { createConnection } from "typeorm";

import { UserAccount } from "./entity/UserAccount";
import { UserAuth } from "./entity/UserAuth";

import * as errorHandler from "errorhandler";

createConnection({
  type: "postgres",
  host: "localhost",
  port: 5432,
  username: "test",
  password: "test",
  database: "test",
  "entities": [
    UserAccount, UserAuth
  ],
  logging: true
}).then(async connection => {
  const app = require("./app");

  /**
   * Error Handler. Provides full stack - remove for production
   */
  app.use(errorHandler());

  /**
   * Start Express server.
   */
  const server = app.listen(app.get("port"), () => {
    console.log(("  App is running at http://localhost:%d in %s mode"), app.get("port"), app.get("env"));
    console.log("  Press CTRL-C to stop\n");
  });

}).catch(error => console.log("TypeORM connection error: ", error));

// export = server

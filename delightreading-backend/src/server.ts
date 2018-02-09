import "reflect-metadata";
import { createConnection, ConnectionOptions } from "typeorm";

import { UserAccount } from "./entity/UserAccount";
import { UserAuth } from "./entity/UserAuth";
import { ActivityLog } from "./entity/ActivityLog";

import * as errorHandler from "errorhandler";

const connConfig: ConnectionOptions = {
  type: "postgres",
  host: "localhost",
  port: 5432,
  username: "postgres",
  password: "",
  database: "delightreading_test",
  "entities": [
    UserAccount, UserAuth, ActivityLog
  ],
  synchronize: false, // --> Re-creates database at every start
  logging: true // true
};


createConnection(connConfig).then(async connection => {
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

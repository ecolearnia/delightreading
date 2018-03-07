import "reflect-metadata";

import * as express from "express";
import { createConnection, ConnectionOptions } from "typeorm";

import { ActivityLog } from "./entity/ActivityLog";
import { Goal } from "./entity/Goal";
import { Reference } from "./entity/Reference";
import { ReferencingLog } from "./entity/ReferencingLog";
import { UserAccount } from "./entity/UserAccount";
import { UserAuth } from "./entity/UserAuth";
import { UserProfile } from "./entity/UserProfile";

// Using Nuxt - Servier Side Rendering - instead of templates
const { Nuxt, Builder } = require("nuxt");

import * as errorHandler from "errorhandler";

const connConfig: ConnectionOptions = {
  type: "postgres",
  host: "localhost",
  port: 5432,
  username: "postgres",
  password: "",
  database: "delightreading_test",
  "entities": [
    ActivityLog, Goal, Reference, ReferencingLog, UserAccount, UserAuth, UserProfile
  ],
  synchronize: false, // --> Re-creates database at every start
  logging: true // true
};

createConnection(connConfig).then(async connection => {
  const app = require("./app");

  // @see: https://github.com/nuxt-community/express-template/blob/master/template/server/index.js
  process.env.DEBUG = "nuxt:*";
  const nuxtConfig = require("../nuxt.config.js");
  nuxtConfig.dev = !(process.env.NODE_ENV === "production");
  const nuxt = new Nuxt(nuxtConfig);

  // Enable live build & reloading on dev
  if (nuxt.options.dev) {
    new Builder(nuxt).build();
  }
  // Give nuxt middleware to express
  app.use(nuxt.render);

  // Following not needed as Nuxt already exposes the folder defined in  nuxt.config.json/srcDir
  // app.use(express.static("web-ui/static"));

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

import "reflect-metadata";

import * as express from "express";
import { Logger, LoggerUtils } from "./utils/Logger";

// Using Nuxt - Servier Side Rendering - instead of templates
const { Nuxt, Builder } = require("nuxt");

import * as errorHandler from "errorhandler";

LoggerUtils.setLevel("info");

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

// export = server

import * as express from "express";
import * as compression from "compression";  // compresses requests
import * as session from "express-session";
import * as bodyParser from "body-parser";
import * as logger from "morgan";
import * as dotenv from "dotenv";
import * as path from "path";
import * as passport from "passport";
import * as expressValidator from "express-validator";
import * as bluebird from "bluebird";

// Load environment variables from .env file, where API keys and passwords are configured
dotenv.config({ path: ".env.example" });

import * as passportConfig from "./config/passport";
console.log("{\"passportConfig.strategies\"=" + JSON.stringify(passportConfig.strategies) + "}");

// API keys and Passport configuration
// import * as passportConfig from "./config/passport";

// Create Express server
const app = express();

// Express configuration
app.set("port", process.env.PORT || 8080);
app.use(compression());
app.use(logger("dev"));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(expressValidator());
app.use(passport.initialize());
app.use(passport.session());

/*
 * OAuth authentication routes. (Sign in)
app.get("/auth/facebook", passport.authenticate("facebook", { scope: ["email", "public_profile"] }));
app.get("/auth/facebook/callback", passport.authenticate("facebook", { failureRedirect: "/login" }), (req, res) => {
  res.redirect(req.session.returnTo || "/");
});
 */


// API keys and Passport configuration
import initRoutes from "./routes";

initRoutes(app);

module.exports = app;
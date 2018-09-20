import * as express from "express";
import * as cors from "cors";
import * as compression from "compression";  // compresses requests
import * as bodyParser from "body-parser";
import * as dotenv from "dotenv";
import * as path from "path";
import * as expressValidator from "express-validator";

/**
 * Load environment variables from .env file, where API keys and passwords are configured.
 */
const APP_ENV = process.env.APP_ENV || ".env.localdev";
console.log("{\"envpath\"=\"" + APP_ENV + "\"}");
dotenv.config({ path: APP_ENV });


// Create Express server
const app = express();

// Express configuration
app.set("port", process.env.PORT || 8080);

if (process.env.TRUST_PROXY) {
  // Becuase Heroku SSL is handled by nginx, Express' req.connection.encrypted is undefined
  // @see - https://stackoverflow.com/questions/20739744/passportjs-callback-switch-between-http-and-https
  console.log("  trust proxy enabled.");
  app.enable("trust proxy");
}

//app.use(cors());
app.use(compression());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(expressValidator());

// Initialize Routers (endpoints)
import initRoutes from "./routes";

initRoutes(app);

module.exports = app;
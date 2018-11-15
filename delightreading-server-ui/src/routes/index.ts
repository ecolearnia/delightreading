import * as express from "express";
import { Logger, LoggerUtils } from "../utils/Logger";
import * as manageRoute from "./manage";

const apiRouter = express.Router();

apiRouter.use("/_manage", manageRoute);

export default function initRoutes(app: express.Express) {
    app.use("/api", apiRouter);
}
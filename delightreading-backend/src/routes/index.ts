import * as express from "express";
import * as manageRoute from "./manage";

const router = express.Router();

router.use("/_manage", manageRoute);

export default function initRoutes(app: express.Express) {
    app.use("/api", router);
}
import * as express from "express";
import * as manageRoute from "./manage";
import * as usersRoute from "./users";

const router = express.Router();

router.use("/_manage", manageRoute);
router.use("/users", usersRoute);

export default function initRoutes(app: express.Express) {
    app.use("/api", router);
}
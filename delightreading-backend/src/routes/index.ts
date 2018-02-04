import * as express from "express";
import * as manageRoute from "./manage";
import * as usersRoute from "./users";
import * as activityLogRoute from "./activitylog";

const router = express.Router();

router.use("/_manage", manageRoute);
router.use("/users", usersRoute);
router.use("/activitylogs", activityLogRoute);

export default function initRoutes(app: express.Express) {
    app.use("/api", router);
}
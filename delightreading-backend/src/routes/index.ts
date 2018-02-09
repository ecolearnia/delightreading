import * as express from "express";
import * as rootLogger  from "pino";
import * as passport from "passport";
import * as manageRoute from "./manage";
import * as authRoute from "./auth";
import * as usersRoute from "./users";
import * as activityLogRoute from "./activitylog";

const apiRouter = express.Router();

apiRouter.use("/_manage", manageRoute);
apiRouter.use("/users", usersRoute);
apiRouter.use("/activitylogs", passport.authenticate("jwt", {session: false}), activityLogRoute);

export default function initRoutes(app: express.Express) {
    app.use("/api", apiRouter);
    app.use("/auth", authRoute);
}
import * as express from "express";
import * as rootLogger  from "pino";
import * as passport from "passport";
import * as manageRoute from "./manage";
import * as authRoute from "./auth";
import * as usersRoute from "./users";
import * as activityLogRoute from "./activitylog";
import * as referencingLogRoute from "./referencinglog";
import * as goalRoute from "./goal";
import * as referenceRoute from "./reference";

const apiRouter = express.Router();

apiRouter.use("/_manage", manageRoute);
apiRouter.use("/users", usersRoute);
apiRouter.use("/references", referenceRoute);
apiRouter.use("/activitylogs", passport.authenticate("jwt", {session: false}), activityLogRoute);
apiRouter.use("/referencinglogs", passport.authenticate("jwt", {session: false}), referencingLogRoute);
apiRouter.use("/goals", passport.authenticate("jwt", {session: false}), goalRoute);

export default function initRoutes(app: express.Express) {
    app.use("/api", apiRouter);
    app.use("/auth", authRoute);
}
import * as express from "express";
import { Logger, LoggerUtils } from "../utils/Logger";
import * as passport from "passport";
import * as manageRoute from "./manage";
import * as authRoute from "./auth";
import * as usersRoute from "./users";
import * as activityLogRoute from "./activitylog";
import * as referencingLogRoute from "./referencinglog";
import * as goalRoute from "./goal";
import * as referenceRoute from "./reference";
import * as ticketRoute from "./ticket";

const apiRouter = express.Router();

apiRouter.use("/_manage", manageRoute);
apiRouter.use("/users", usersRoute);
apiRouter.use("/references", referenceRoute);
apiRouter.use("/activitylogs", passport.authenticate("jwt", {session: false}), activityLogRoute);
apiRouter.use("/referencinglogs", passport.authenticate("jwt", {session: false}), referencingLogRoute);
apiRouter.use("/goals", passport.authenticate("jwt", {session: false}), goalRoute);

apiRouter.use("/tickets", ticketRoute);

export default function initRoutes(app: express.Express) {
    app.use("/api", apiRouter);
    app.use("/auth", authRoute);
}
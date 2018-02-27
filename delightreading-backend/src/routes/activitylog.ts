import * as express from "express";
import * as passport from "passport";
import * as activityLogController from "../controller/activitylog";

const router = express.Router();

// No need to guard with passport.authenticate("jwt"), it is done at index.js
router.post("/", activityLogController.addMyActivityLog);
router.put("/:sid", activityLogController.updateMyActivityLog);
router.get("/", activityLogController.listMyActivityLog);
router.delete("/:sid", activityLogController.deleteMyActivityLog);

export = router;

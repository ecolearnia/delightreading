import * as express from "express";
import * as passport from "passport";
import * as activityLogController from "../controller/activitylog";

const router = express.Router();

router.post("/", activityLogController.addMyActivityLog);
router.get("/", activityLogController.listMyActivityLog);
router.delete("/:sid", activityLogController.deleteMyActivityLog);

export = router;

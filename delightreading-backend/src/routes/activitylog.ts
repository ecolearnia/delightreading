import * as express from "express";
import * as activityLogController from "../controller/activityLog";

const router = express.Router();

router.post("/", activityLogController.addActivityLog);
router.get("/", activityLogController.listActivityLog);

export = router;

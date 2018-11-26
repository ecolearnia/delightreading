import * as express from "express";
import * as passport from "passport";
import * as referencingLogController from "../controller/referencinglog";

const router = express.Router();

// No need to guard with passport.authenticate("jwt"), it is done at index.js
router.post("/", referencingLogController.addMyReferencingLog);
router.put("/:sid", referencingLogController.updateMyReferencingLog);
router.get("/", referencingLogController.listMyReferencingLog);
router.delete("/:sid", referencingLogController.deleteMyReferencingLog);

export = router;

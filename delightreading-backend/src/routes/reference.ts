import * as express from "express";
import * as passport from "passport";
import * as referenceController from "../controller/reference";

const router = express.Router();

// No need to guard with passport.authenticate("jwt"), it is done at index.js
// router.post("/", referenceController.addMyReferencingLog);
// router.put("/:sid", referenceController.updateMyReferencingLog);
router.get("/", referenceController.listReferences);

export = router;

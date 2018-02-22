import * as express from "express";
import * as passport from "passport";
import * as goalController from "../controller/goal";

const router = express.Router();

// No need to guard with passport.authenticate("jwt"), it is done at index.js
router.post("/", goalController.addMyGoal);
router.get("/", goalController.listMyGoals);
router.put("/:sid", goalController.updateMyGoal);

export = router;

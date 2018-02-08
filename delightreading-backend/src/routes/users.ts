import * as express from "express";
import * as passport from "passport";
import * as userController from "../controller/user";

const router = express.Router();

router.get("/me", passport.authenticate("jwt", {session: false}), userController.getMyAccount);
router.get("/:uid", userController.getUserAccount);
router.post("/", userController.addUserAccount);
router.get("/", userController.listUserAccount);

export = router;

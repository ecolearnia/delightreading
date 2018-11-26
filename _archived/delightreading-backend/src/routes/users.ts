import * as express from "express";
import * as passport from "passport";
import * as userController from "../controller/user";

const router = express.Router();

router.get("/me", passport.authenticate("jwt", {session: false}), userController.getMyAccount);
router.get("/myprofile", passport.authenticate("jwt", {session: false}), userController.getMyProfile);
router.put("/myprofile", passport.authenticate("jwt", {session: false}), userController.saveMyProfile);
router.get("/:uid", userController.getUserAccount);
router.post("/", userController.addUserAccount);
router.get("/", userController.listUserAccounts);

export = router;

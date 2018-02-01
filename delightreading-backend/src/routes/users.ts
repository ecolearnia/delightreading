import * as express from "express";
import * as userAccountController from "../controllers/useraccount";

const router = express.Router();

router.post("/", userAccountController.addUserAccount);
router.get("/", userAccountController.listUserAccount);

export = router;

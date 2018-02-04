import * as express from "express";
import * as manageController from "../controller/manage";

const router = express.Router();

router.use("/info", manageController.getInfo);

export = router;

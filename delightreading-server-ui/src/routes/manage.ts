import * as express from "express";
import * as manageController from "../controllers/manage";

const router = express.Router();

router.use("/info", manageController.getInfo);

export = router;

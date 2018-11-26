import * as express from "express";
import * as passport from "passport";
import * as ticketController from "../controller/ticket";

const router = express.Router();

// No need to guard with passport.authenticate("jwt"), it is done at index.js
router.post("/", passport.authenticate("jwt", {session: false}), ticketController.addMyTicket);
router.put("/:sid", passport.authenticate("jwt", {session: false}), ticketController.updateMyTicket);
router.get("/", passport.authenticate("jwt", {session: false}), ticketController.listTickets);

export = router;

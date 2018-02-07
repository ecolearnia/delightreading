import * as jwt from "jsonwebtoken";
import * as passport from "passport";
import * as express from "express";

const router = express.Router();

router.get("/google", passport.authenticate("google", { scope: "profile email" }));
router.get("/google/callback", passport.authenticate("google", { failureRedirect: "/login" }), (req, res) => {
    console.log("-- REDIRECTING");
    res.redirect(req.session.returnTo || "/");
});

export = router;

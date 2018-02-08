import * as jwtUtils from "../config/jwt-utils";
import * as passport from "passport";
import * as express from "express";

const router = express.Router();

router.get("/google", passport.authenticate("google", { scope: "profile email" }));
router.get("/google/callback", passport.authenticate("google", { failureRedirect: "/login" }), (req, res) => {
    console.log("-- in google/callback req.user=" + JSON.stringify(req.user, undefined, 2));

    const drToken = jwtUtils.generateAccessToken(req.user);
    res.cookie("dr_token", drToken, { maxAge: 60 * 60 * 1000 });

    const returnUrl = "http://localhost:8080/#/home";
    res.redirect(req.session.returnTo || returnUrl);
});

export = router;

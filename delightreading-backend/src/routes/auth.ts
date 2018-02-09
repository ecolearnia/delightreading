import * as jwtUtils from "../config/jwt-utils";
import * as rootLogger  from "pino";
import * as passport from "passport";
import * as express from "express";

const logger = rootLogger().child({ module: "rountes/auth" });

const router = express.Router();

router.get("/google", passport.authenticate("google", { scope: "profile email" }));
router.get("/google/callback", passport.authenticate("google", { failureRedirect: "/login" }), (req, res) => {
    logger.info({op: "GET:google/callback", account: req.user}, "Handling Google auth callback");

    const drToken = jwtUtils.generateAccessToken(req.user);
    res.cookie("dr_token", drToken, { maxAge: 60 * 60 * 1000 });

    const returnUrl = "http://localhost:8080/#/home";

    logger.info({op: "GET:google/callback", accessToken: drToken}, "Redirecting from Google auth callback");
    res.redirect(req.session.returnTo || returnUrl);
});

export = router;

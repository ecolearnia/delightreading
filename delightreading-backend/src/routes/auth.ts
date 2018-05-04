import * as jwtUtils from "../config/jwt-utils";
import { Logger, LoggerUtils } from "../utils/Logger";
import * as passport from "passport";
import * as express from "express";

const COOKIE_NAME = "dr_token";

const logger = LoggerUtils.child({ module: "routes/auth" });

const router = express.Router();

const POST_LOGIN_SUCCESS_PATH = "/app/#/home";

router.get("/google", passport.authenticate("google", { scope: "profile email" }));
router.get("/google/callback", passport.authenticate("google", { failureRedirect: "/login" }), (req, res) => {
    logger.info({op: "GET:google/callback", account: req.user}, "Handling Google auth callback");

    const drToken = jwtUtils.generateAccessToken(req.user);
    res.cookie(COOKIE_NAME, drToken, { maxAge: 3 * 60 * 60 * 1000 });

    const returnUrl = process.env.UI_BASE_URL + POST_LOGIN_SUCCESS_PATH;

    logger.info({op: "GET:google/callback", accessToken: drToken, returnUrl: returnUrl}, "Redirecting from Google auth callback");
    res.redirect(req.session.returnTo || returnUrl);
});


router.get("/facebook", passport.authenticate("facebook", { scope: ["email", "public_profile"] }));
router.get("/facebook/callback", passport.authenticate("facebook", { failureRedirect: "/login" }), (req, res) => {
    logger.info({op: "GET:facebook/callback", account: req.user}, "Handling Facebook auth callback");

    const drToken = jwtUtils.generateAccessToken(req.user);
    res.cookie(COOKIE_NAME, drToken, { maxAge: 3 * 60 * 60 * 1000 });

    const returnUrl = process.env.UI_BASE_URL + POST_LOGIN_SUCCESS_PATH;

    logger.info({op: "GET:facebook/callback", accessToken: drToken, returnUrl: returnUrl}, "Redirecting from Facebook auth callback");
    res.redirect(req.session.returnTo || returnUrl);
});

export = router;

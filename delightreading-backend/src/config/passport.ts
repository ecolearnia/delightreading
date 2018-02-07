
import * as passport from "passport";
import * as request from "request";
import * as passportLocal from "passport-local";
import * as passportGoogle from "passport-google-oauth";
import * as _ from "lodash";

import { Request, Response, NextFunction } from "express";
import { UserAccount } from "../entity/UserAccount";
import { UserAuth } from "../entity/UserAuth";
import { UserService } from "../service/UserService";
import * as googleUtils from "./google-utils";

const LocalStrategy = passportLocal.Strategy;
const GoogleStrategy = passportGoogle.OAuth2Strategy;

const userService = new UserService();

passport.serializeUser<any, any>((user, done) => {
  console.log("* serializing: " + JSON.stringify(user));
  done(undefined, user.sid);
});

passport.deserializeUser((id, done) => {
  console.log("* DEserializing: " + JSON.stringify(id));
  userService.findAccountBySid(id as number)
    .then((account) => {
      done(undefined, account);
    })
    .catch((err) => {
      done(err, undefined);
    });
});


/**
 * Sign in using Email and Password.
 */
passport.use(new LocalStrategy({ usernameField: "email" }, (email, password, done) => {
  /*
  authRepo.f({ email: email.toLowerCase() }, (err, user: any) => {
    if (err) { return done(err); }
    if (!user) {
      return done(undefined, false, { message: `Email ${email} not found.` });
    }
    user.comparePassword(password, (err: Error, isMatch: boolean) => {
      if (err) { return done(err); }
      if (isMatch) {
        return done(undefined, user);
      }
      return done(undefined, false, { message: "Invalid email or password." });
    });
  });
  */
}));

console.log("GOOGLE_CLIENT_ID: " + process.env.GOOGLE_CLIENT_ID);
console.log("GOOGLE_CLIENT_SECRET: " + process.env.GOOGLE_CLIENT_SECRET);
/**
 * Sign in with Facebook.
 */
passport.use(new GoogleStrategy({
  clientID: process.env.GOOGLE_CLIENT_ID,
  clientSecret: process.env.GOOGLE_CLIENT_SECRET,
  callbackURL: "/auth/google/callback",
  passReqToCallback: true
}, (req, token, tokenSecret, profile, done) => {
  console.log(JSON.stringify(profile, undefined, 2));
  if (req.user) {
    // User already authenticated, link new SNS account
    const account: UserAccount = req.user;
    const auth = new UserAuth();
    userService.linkAuth(account, auth)
      .then((auth) => {
        account.addAuth(auth);
        done(account);
      })
      .catch((err: any) => {
        done(err);
      });
  } else {
    // User unauthenticated, create new user from SNS profile
    // Verify email first

    const account = googleUtils.translateProfileToUser(profile);
    console.log("account: " + JSON.stringify(account, undefined, 2));

    userService.registerAccount(account)
      .then((registeredAccount) => {
        console.log("REGISTER SUCCESS");
        done(undefined, registeredAccount);
      })
      .catch((err: any) => {
        console.log("REGISTER ERROR");
        done(err);
      });
  }
}));

/**
 * Login Required middleware.
 */
export let isAuthenticated = (req: Request, res: Response, next: NextFunction) => {
  if (req.isAuthenticated()) {
    return next();
  }
  res.redirect("/login");
};

/**
 * Authorization Required middleware.
 */
export let isAuthorized = (req: Request, res: Response, next: NextFunction) => {
  const provider = req.path.split("/").slice(-1)[0];

  if (_.find(req.user.tokens, { kind: provider })) {
    next();
  } else {
    res.redirect(`/auth/${provider}`);
  }
};

export let strategies = ["google"];
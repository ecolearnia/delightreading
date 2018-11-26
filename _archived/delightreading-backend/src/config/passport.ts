
import * as passport from "passport";
import * as request from "request";
import * as passportLocal from "passport-local";
import * as passportJwt from "passport-jwt";
import * as passportGoogle from "passport-google-oauth";
import * as passportFacebook from "passport-facebook";
import * as _ from "lodash";

import { Request, Response, NextFunction } from "express";
import { UserAccount } from "../entity/UserAccount";
import { UserAuth } from "../entity/UserAuth";
import { UserService } from "../service/UserService";
import * as googleUtils from "./google-utils";
import * as facebookUtils from "./facebook-utils";

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
passport.use(new passportLocal.Strategy({ usernameField: "email" }, (email, password, done) => {
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

const jwtOptions = {
  // Get the JWT from the "Authorization" header.
  // By default this looks for a "JWT " prefix
  jwtFromRequest: passportJwt.ExtractJwt.fromAuthHeaderAsBearerToken(),
  // The secret that was used to sign the JWT
  secretOrKey: process.env.AUTH_TOKEN_SECRET,
  // The issuer stored in the JWT
  issuer: process.env.AUTH_TOKEN_ISSUER,
  // The audience stored in the JWT
  audience: process.env.AUTH_TOKEN_AUDIENCE
};

passport.use(new passportJwt.Strategy(jwtOptions, async (payload, done) => {
  console.log("--- JWT Strategy: " + JSON.stringify(payload, undefined, 2));
  const account = await userService.findAccountByUid(payload.sub);
  if (account) {
    return done(undefined, account, payload);
  }
  return done(undefined);
}));

// console.log("GOOGLE_CLIENT_ID: " + process.env.GOOGLE_CLIENT_ID);
// console.log("GOOGLE_CLIENT_SECRET: " + process.env.GOOGLE_CLIENT_SECRET);

/**
 * Sign in with Google.
 */
passport.use(new passportGoogle.OAuth2Strategy({
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
 * Sign in with Facebook.
 */
passport.use(new passportFacebook.Strategy({
  clientID: process.env.FACEBOOK_ID,
  clientSecret: process.env.FACEBOOK_SECRET,
  callbackURL: "/auth/facebook/callback", // endpoint defined in routes/auth.ts
  profileFields: [
    "name", "about", "email", "age_range", "birthday", "gender", "languages", "hometown"
    , "education", "interested_in", "work", "quotes", "website"
    , "link", "locale", "timezone", "updated_time"
    ],
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

    const account = facebookUtils.translateProfileToUser(profile);
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
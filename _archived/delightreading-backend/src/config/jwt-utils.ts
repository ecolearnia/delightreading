import * as jwt from "jsonwebtoken";

import { UserAccount } from "../entity/UserAccount";

export function generateAccessToken(account: UserAccount): string {
    const expiresIn = "2 hours";
    const audience = process.env.AUTH_TOKEN_AUDIENCE;
    const issuer = process.env.AUTH_TOKEN_ISSUER;
    const secret = process.env.AUTH_TOKEN_SECRET;

    const token = jwt.sign({}, secret, {
        expiresIn: expiresIn,
        audience: audience,
        issuer: issuer,
        subject: account.uid
    });

    return token;
}
import { UserAccount } from "../entity/UserAccount";
import { UserAuth } from "../entity/UserAuth";


export function translateProfileToUser(profile: any): UserAccount {
    const token = Math.random().toString(36).substr(2, 12);
    const exp = new Date();
    exp.setHours(exp.getHours() + 24);

    const account = new  UserAccount({
        username: profile._json.email,
        password: "changeme",
        email: profile.emails[0].value,
        givenName: profile.name && profile.name.givenName || undefined,
        familyName: profile.name && profile.name.familyName || undefined,
        middleName: profile.name && profile.name.middleName || undefined,
        dateOfBirth: profile._json.birthday,
        pictureUri: `https://graph.facebook.com/${profile.id}/picture?type=large`,

        locale: profile._json.locale,
        timezone: profile._json.timezone
    });
    account.addAuth(new UserAuth({
        provider: "facebook",
        providerAccountId: profile.id,
        rawProfile: profile._raw
    }));

    return account;
}
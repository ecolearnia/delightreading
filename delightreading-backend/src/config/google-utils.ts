import { UserAccount } from "../entity/UserAccount";
import { UserAuth } from "../entity/UserAuth";

export const mappings = {
};

export function translateProfileToUser(profile: any): UserAccount {
    const token = Math.random().toString(36).substr(2, 12);
    const exp = new Date();
    exp.setHours(exp.getHours() + 24);

    const account = new  UserAccount({
        username: profile.emails[0].value,
        password: "changeme",
        email: profile.emails[0].value,
        givenName: profile.name && profile.name.givenName || undefined,
        familyName: profile.name && profile.name.familyName || undefined,
        middleName: profile.name && profile.name.middleName || undefined,
        // dateOfBirth: obj.dateOfBirth,
        pictureUri: profile.photos && profile.photos.value  || undefined
    });
    account.addAuth(new UserAuth({
        provider: "google",
        providerAccountId: profile.id,
        rawProfile: profile._raw
    }));

    return account;
}
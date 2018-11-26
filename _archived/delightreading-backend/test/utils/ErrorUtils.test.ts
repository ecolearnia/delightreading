import ErrorUtils from "../../src/utils/ErrorUtils";


const expect = require("chai").expect;

describe("ErrorUtils", () => {
    it("throw error", async () => {
        try {
             throw ErrorUtils.createError("FakeError", "FAKE", {prop1: "My Prop"});
            //throw new Error ("Native");
        } catch (err) {
            //console.log(err.stack);
            //console.log(JSON.stringify(err, undefined, 2));
        }
    });
});
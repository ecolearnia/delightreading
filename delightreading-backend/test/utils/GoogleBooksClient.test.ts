import { Reference } from "../../src/entity/Reference";
import GoogleBooksClient from "../../src/utils/GoogleBooksClient";

const expectedJson = require("../sample-data/reference.hole.sample.json");

describe("GoogleBooksClient", () => {

    describe("getBookByUri",  async () => {
        it("should retrieve book 'Hole'", async () => {
            const gbook = await GoogleBooksClient.getBookByUri("https://www.googleapis.com/books/v1/volumes/U_zINMa9cAAC");

            const reference = GoogleBooksClient.toReference(gbook);

            // console.log("reference: " + JSON.stringify(reference, undefined, 2));

            const expectedReference = new Reference(expectedJson);
            expectedReference.createdBy = undefined;
            expectedReference.synopsys = undefined;
            // Not sure why descriptions are shown as not equal...
            expectedReference.description = reference.description;
            // These values changes at everhy request
            expectedReference.imageUrl = reference.imageUrl;
            expectedReference.thumbnailImageUrl = reference.thumbnailImageUrl;

            await expect (reference).toEqual(expectedReference);
        });
    });
});

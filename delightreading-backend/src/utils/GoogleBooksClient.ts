import * as async from "async";
import * as request from "request-promise";

import { Reference } from "../entity/Reference";

const GBOOKS_API_BASE_URL = "https://www.googleapis.com/books/v1/volumes";

export default class GoogleBooksClient {

    static AWARDS = ["Newbery", "National Book Award", "Caldecott"];
    static RATING_NOT_MATURE = "NOT_MATURE";
    static RATING_MATURE = "MATURE";
    static NWORDS = [
        "violence", "sex", "erotic", "sensual", "fetish", "f**k", "fuck", "s**t", "shit"
    ];

    static async queryBooks(q: string, title: string = undefined,
        author: string = undefined, subject: string = undefined): Promise<Array<any>> {
        const params = new Array();
        if (title) {
            params.push("+intitle:" + title);
        }
        if (author) {
            params.push("+inauthor:" + author);
        }
        if (subject) {
            params.push("+subject:" + subject);
        }
        return request.get(GBOOKS_API_BASE_URL + "?q=" + q + params.join(""));
    }

    static async getBookByUri(uri: string): Promise<any> {
        return request.get({ uri: uri, json: true} );
    }

    static toReference(gbookVol: any): Reference {

        const awards: string[] = [];
        if (gbookVol.volumeInfo.description) {
            for (let i = 0; i < GoogleBooksClient.AWARDS.length; i++) {
                if (gbookVol.volumeInfo.description.indexOf(GoogleBooksClient.AWARDS[i]) !== -1) {
                    awards.push(GoogleBooksClient.AWARDS[i]);
                }
            }
        }

        // Double check for NOT_MATURE labeled content for words
        if (gbookVol.volumeInfo.maturityRating === GoogleBooksClient.RATING_NOT_MATURE) {}
        for (let i = 0; i < GoogleBooksClient.NWORDS.length; i++) {
            if (gbookVol.volumeInfo.description.indexOf(GoogleBooksClient.NWORDS[i]) !== -1) {
                gbookVol.volumeInfo.maturityRating = GoogleBooksClient.RATING_MATURE;
            }
        }

        return new Reference({
            sourceUri: gbookVol.selfLink,
            title: gbookVol.volumeInfo.title,
            authors: gbookVol.volumeInfo.authors,
            publisher: gbookVol.volumeInfo.publisher,
            publishedDate: gbookVol.volumeInfo.publishedDate,
            description: gbookVol.volumeInfo.description,
            synopsys: gbookVol.searchInfo && gbookVol.searchInfo.textSnippet,
            identifiers: gbookVol.volumeInfo.industryIdentifiers,
            pageCount: gbookVol.volumeInfo.pageCount,
            categories: gbookVol.volumeInfo.categories,
            maturityRating: gbookVol.volumeInfo.maturityRating,
            language: gbookVol.volumeInfo.language,
            imageUrl: gbookVol.volumeInfo.imageLinks.small,
            thumbnailImageUrl: gbookVol.volumeInfo.imageLinks.thumbnail,
            awards: awards
        });

    }
}
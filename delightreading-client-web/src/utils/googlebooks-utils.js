/**
 * Examines the Google book's API response and determines whether or not it is fit for kids
 *
 * TODO: Consier using categories: ["Self-Help", "Juvenile Fiction", "Biography & Autobiography", "Performing Arts", "Travel"]
 * @param {*} volumeInfo
 */
export function sensorFilter(volumeInfo) {
  if (volumeInfo.maturityRating === "MATURE") {
    return false;
  }
  if (volumeInfo.description) {
    const nwords = [
      "violence", "sex", "erotic", "sensual", "fetish", "f**k", "fuck", "s**t", "shit"
    ];
    for (let i = 0; i < nwords.length; i++) {
      if (volumeInfo.description.indexOf(nwords[i]) !== -1) {
        return false;
      }
    }
  }
  return true;
}

export function transform(response) {
  return response.items
    .filter(row => sensorFilter(row.volumeInfo))
    .map(row => {
      return {
        title: row.volumeInfo.title,
        author: row.volumeInfo.authors && row.volumeInfo.authors[0],
        isbn: row.volumeInfo.industryIdentifiers,
        link: row.selfLink,
        imageLink:
          row.volumeInfo.imageLinks &&
          row.volumeInfo.imageLinks.smallThumbnail,
        serchInfo: row.searchInfo
      };
    });
}

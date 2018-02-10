/**
 * Examines the Google book's API response and determines whether or not it is fit for kids
 * 
 * TODO: Consier using categories: ["Self-Help", "Juvenile Fiction"]
 * @param {*} volumeInfo 
 */
export function sensorGoogleBook(volumeInfo) {
  if (volumeInfo.maturityRating === "MATURE") {
    return false;
  }
  if (volumeInfo.description) {
    const nwords = [
      "violence", "sex", "erotic", "sensual", "fetish", "f**k", "fuck"
    ];
    for (let i = 0; i < nwords.length; i++) {
      if (volumeInfo.description.indexOf(nwords[i]) !== -1) {
        return false;
      }
    }
  }
  return true;
}

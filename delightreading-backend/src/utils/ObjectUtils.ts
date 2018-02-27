
export default class ObjectUtils {

  /**
   * Assigns properties from source to target only those with the name provided in the fields.
   * @param target target object to copy to
   * @param source source objecct fo copy from
   * @param fields selection of fields to copy from the source
   */
  static assignProperties(target: any, source: any, fields: string[]) {
    for (const field of fields) {
      target[field] = source[field];
    }
    return target;
  }
}
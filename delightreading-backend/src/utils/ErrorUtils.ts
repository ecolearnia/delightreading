
export default class ErrorUtils {

  static createError(message: string, code?: string, props?: any): Error {
    const error = new Error(message);
    if (props) {
      for (const [key, value] of Object.entries(props)) {
        (error as any)[key] = value;
      }
    }
    if (code) {
      (error as any).code = code;
    }
    return error;
  }
}
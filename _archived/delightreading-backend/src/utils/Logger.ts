import * as pino from "pino";

const rootLogger = pino();

export type Logger = pino.Logger;

export class LoggerUtils {
    static getRoot() {
        return rootLogger;
    }

    static child(options: any) {
        return rootLogger.child(options);
    }

    static setLevel(level: string) {
        rootLogger.level  = level;
    }
}
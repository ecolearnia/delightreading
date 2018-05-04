import { ActivityLog } from "../../src/entity/ActivityLog";


export function newActivityLog(accountSid: number, referenceSid: number, goalSid: number, 
    activity: string, duration: number, logTimestamp: Date = new Date(), referencingLogSid = undefined): ActivityLog 
{
    const activityLog = new ActivityLog({
        accountSid: accountSid,
        referenceSid: referenceSid,
        goalSid: goalSid,
        activity: activity,
        logTimestamp: logTimestamp,
        duration: duration,
        createdAt: new Date(),
        referencingLogSid: referencingLogSid
    });
    activityLog.uid = (Math.floor(Math.random() * 1000) + 1).toString(); 
    return activityLog;
}

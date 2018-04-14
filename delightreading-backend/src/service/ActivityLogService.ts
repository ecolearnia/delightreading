"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import * as moment from "moment";
import { ServiceBase } from "./ServiceBase";
import { Reference } from "../entity/Reference";
import { ActivityLog } from "../entity/ActivityLog";
import { ActivityStat } from "../entity/valueobject/ActivityStat";


import TypeOrmUtils from "../utils/TypeOrmUtils";

const logger = rootLogger().child({ module: "ActivityLogService" });


export class ActivityLogService extends ServiceBase<ActivityLog> {

    constructor() {
        super(ActivityLog);
    }


    async find(criteria?: any, skip: number = 0, take: number = 20): Promise<Array<ActivityLog>> {

        logger.info({ op: "list", criteria: criteria }, "Listing activityLog");

        // const logs = await this.activityLogRepo.find(criteria);

        const logs = await this.repo.createQueryBuilder("activity_log")
            .leftJoinAndMapOne("activity_log.reference", Reference, "reference", "activity_log.referenceSid=reference.sid")
            .where(TypeOrmUtils.andedWhereClause(criteria, "activity_log"), criteria)
            .skip(skip)
            .take(take)
            .getMany();

        logger.info({ op: "list", logs: logs }, "Listing activityLog successful");

        return logs;
    }

    async list(criteria?: any, skip: number = 0, take: number = 10): Promise<Array<ActivityLog>> {

        logger.info({ op: "list", criteria: criteria, skip: skip, take: take }, "Listing activityLog");

        const logs = await this.repo.createQueryBuilder("activity_log")
            .leftJoinAndMapOne("activity_log.reference", Reference, "reference", "activity_log.referenceSid=reference.sid")
            .where(TypeOrmUtils.andedWhereClause(criteria, "activity_log"), criteria)
            .orderBy("activity_log.logTimestamp", "DESC")
            .skip(skip)
            .take(take)
            .getMany();

        logger.info({ op: "list", resultCount: logs && logs.length }, "Listing activityLog successful");

        return logs;
    }

    async statsByReferencingLog(referencingLogSid: number): Promise<any> {
        const criteria = [referencingLogSid];
        const stats = await this.repo.query("select \"referencingLogSid\", sum(duration) as \"totalDuration\", count(sid) as \"totalCount\" \
            from activity_log where \"referencingLogSid\" = $1 group by \"referencingLogSid\"", criteria);

        // console.log("ActivityStat: " + JSON.stringify(stats, undefined, 2));
        return stats && stats.length > 0 && new ActivityStat(stats[0].totalDuration, stats[0].totalCount) || new ActivityStat(0, 0);
    }

    /**
     * Returns statistics
     * {
     *  date: {date},
     *  month: {
     *      name: "May",
     *      activityDuration: 120,
     *      booksCount: 2,
     *      books [ {title: "Holes"}, {title: "My Book"} ]
     *  },
     *  week: {
     *      name: "3"
     *      activityDuration: 60,
     *      booksCount: 1,
     *      books [ {title: "Holes"}]
     *  },
     *  day: {
     *      name: "23"
     *      activityDuration: 10,
     *      booksCount: 0,
     *      books []
     *  },
     * }
     * @param accountSid - The account number
     */
    async stats(accountSid: number, date: Date = new Date(), activity: string = "read"): Promise<any> {
        logger.info({ op: "currentStats", accountSid: accountSid }, "Stats activityLog");

        const startOfMonth = moment(date).startOf("month");
        const endOfMonth = moment(date).endOf("month");

        const startOfWeek = moment(date).startOf("week");
        const endOfWeek = moment(date).endOf("week");

        const startOfDay = moment(date).startOf("day");
        const endOfDay = moment(date).endOf("day");

        const stats = {
            date: date,
            month: (await this.timeSeries(accountSid, "month", startOfMonth.toDate(), endOfMonth.toDate(), activity))[0],
            week: (await this.timeSeries(accountSid, "week", startOfWeek.toDate(), endOfWeek.toDate(), activity))[0],
            day: (await this.timeSeries(accountSid, "day", startOfDay.toDate(), endOfDay.toDate(), activity))[0]
        };

        // logger.info({ op: "currentStats", stats: stats }, "Stats activityLog successful");

        return stats;
    }

    async timeSeriesOf(accountSid: number, period: string, ofUnit: string, activity: string): Promise<any> {
        const date = new Date();
        let startOf;
        let endOf;
        if (ofUnit == "year") {
            startOf = moment(date).startOf("year");
            endOf = moment(date).endOf("year");
        } else if (ofUnit == "month") {
            startOf = moment(date).startOf("month");
            endOf = moment(date).endOf("month");
        } else /* if (ofUnit == "week") */ {
            startOf = moment(date).startOf("week");
            endOf = moment(date).endOf("week");
        }

        return this.timeSeries(accountSid, period, startOf.toDate(), endOf.toDate(), activity);
    }

    /**
     * Returns the time series statistics grouped by the period
     *
     * {
     *  period: "week"
     *  from: "2017-01-01"
     *  to: "2018-03-04"
     *  series: [{
     *      periodName: 34 (e.g. week number)
     *      periodStart: "2017-01-01"
     *      periodEnd: "2017-01-07"
     *      activityDuration: 120,
     *      activityCount: 10,
     *      booksCount: 2,
     *      books [ {title: "Holes"}, {title: "My Book"} ]
     *  }, ...
     * }
     *
     * @param period - day, week, month, year
     * @param fromDate - Range start for the time serie (innclusive)
     * @param toDate - Range end for the time serie
     */
    async timeSeries(accountSid: number, period: string, fromDate: Date, toDate: Date, activity: string): Promise<any> {
        // TODO: validate period

        logger.info({ op: "timeSeries", accountSid: accountSid, period: period, fromDate: fromDate, toDate: toDate }, "TimeSeries activityLog");

        // This does not include time periods where no there is no matching rows
        // const queryParams = { accountSid: accountSid, fromDate: fromDate, toDate: toDate }
        // const timeSeries = await this.repo.createQueryBuilder("activity_log")
        //     .select("date_trunc('" + period + "', \"activity_log\".\"logTimestamp\"::timestamp) AS period")
        //     .addSelect("max(date_trunc('" + period + "', \"activity_log\".\"logTimestamp\"::timestamp)) AS periodEnd")
        //     .addSelect("SUM(duration)", "activityDuration")
        //     .addSelect("COUNT(sid)", "activityCount")
        //     .where("activity_log.accountSid = :accountSid AND activity_log.logTimestamp >= :fromDate AND activity_log.logTimestamp <= :toDate ",
        //         queryParams)
        //     .groupBy("period")
        //     .getRawMany();

        // @see: http://www.craigkerstiens.com/2017/06/08/working-with-time-in-postgres/
        // @see: https://stackoverflow.com/questions/22114824/postgres-entries-per-week-for-every-week
        const sql =
        "WITH periods as ( \
        SELECT period \
            FROM generate_series($1::date, $2::date, $3::interval) period \
        ) \
        SELECT periods.period as \"periodStart\", (periods.period + $3::interval) as \"periodEnd\", \
            SUM(duration) AS \"activityDuration\",\
            COUNT(activity_log.sid) AS \"activityCount\"\
        FROM periods\
        LEFT JOIN activity_log \
            ON \"activity_log\".\"accountSid\" = $4 AND \"activity_log\".\"logTimestamp\" > periods.period AND \"activity_log\".\"logTimestamp\"  <= (periods.period + $3::interval) \
        GROUP BY periods.period\
        ORDER BY periods.period";

        const queryParams = [ fromDate, toDate, "1 " + period, accountSid];
        const timeSeries = await this.repo.query(sql, queryParams);

        timeSeries.forEach(function(element: any, index: number) {
            element.activityCount = Number(element.activityCount);
            element.activityDuration = Number(element.activityDuration);
        });

        logger.info({ op: "timeSeries", timeSeries: timeSeries }, "TimeSeries activityLog successful");

        return timeSeries;
    }

}
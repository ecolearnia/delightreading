Hello, I am using subquery to retrieve referencing_log along with aggregation of some fields in joined table.

To implement above, my code uses query builder with subquery, as shown below:
```
const logs = await this.repo.createQueryBuilder("referencing_log")
	.leftJoinAndMapOne("referencingLog.\"activityStat\"", (qb) => { 
		return qb.subQuery()
			.select("activity_log.\"referencingLogSid\"")
			.addSelect("sum(activity_log.duration)", "totalActivityDuration")
			.addSelect("count(activity_log.sid)", "totalActivityCount")
			.from(ActivityLog, "activity_log")
			.groupBy("activity_log.\"referencingLogSid\"");
	} , "activityStat", "referencing_log.sid = \"activityStat\".\"referencingLogSid\"" )
	.getMany();
```

This does generate correctly sql that is equivalent to this:
```
select sid, totaldDuration, totalCount
from referencing_log rlog
left join (
	select "referencingLogSid", sum(duration) as totaldDuration, count(sid) as totalCount 
	from activity_log  
	group by activity_log."referencingLogSid") as alog
on rlog.sid = alog."referencingLogSid"
where rlog."accountSid" = 1
```

The issue is that it is not populating the the attribute `activityStat` in the object of type ReferencingLog 
See table schema and entity code below.

Can anyone explain me how to populate a property in the entity with values obtained from subquery?

Thanks in advance


TABLES:
```
table activity_log:
	sid int,
	duration int,
	referencingLogSid int

table referencing_log:
	sid int
```
	
ENTITY CLASSES:
```
@Entity("referencing_log")
export class ReferencingLog  {

    @PrimaryGeneratedColumn()
    sid?: number;

	...

    activityStat: {
		totaldDuration: number,
		totalCount: number
	};
}
```





 "activityStat".* FROM "referencing_log" "referencing_log" 
 LEFT JOIN "reference" "reference" ON referencing_log."referenceSid" = "reference"."sid"  
 LEFT JOIN (
	 SELECT activity_log."referencingLogSid", sum("activity_log"."duration") AS "totalDuration", 
	 count("activity_log"."sid") AS "totalCount" 
	 FROM "activity_log" "activity_log" GROUP BY activity_log."referencingLogSid"
	 ) "activityStat" 
ON "referencing_log"."sid" = "activityStat"."referencingLogSid" 
WHERE ("referencing_log"."accountSid" = $1) AND "referencing_log"."sid" IN (2, 1) 
ORDER BY "referencing_log"."startDate" DESC -- PARAMETERS: [1]

 "stat".* FROM "user_group" "user_group" 
LEFT JOIN (
	SELECT user_group_member."groupSid", count("user_group_member"."sid") AS "memberCount" 
	FROM "user_group_member" "user_group_member" GROUP BY user_group_member."groupSid"
	) "stat" 
ON "user_group"."sid" = "stat"."groupSid"  
WHERE ("user_group"."type" = $1) AND "user_group"."sid" IN (1, 3) 
ORDER BY "user_group"."startDate" DESC -- PARAMETERS: ["family"]
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


export class ActivityStat {
  totalDuration?: number;
  totalCount?: number;

  constructor(totalDuration?: string | number, totalCount?: string | number) {
    this.totalDuration = Number(totalDuration);
    this.totalCount = Number(totalCount);
  }
}

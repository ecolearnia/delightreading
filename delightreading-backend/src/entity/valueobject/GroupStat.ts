
export class GroupStat {
  memberCount?: number;

  constructor(memberCount?: string | number) {
    this.memberCount = Number(memberCount);
  }
}

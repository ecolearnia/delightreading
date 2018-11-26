import { Response, Request } from "express";

export class PageRequest {
    pageSize: number;
    page: number;

    constructor(page: number = 0, pageSize: number = 20) {
        this.page = page;
        this.pageSize = pageSize;
    }

    skip(): number {
        return this.page * this.pageSize;
    }
}

export function getPageRequest(req: Request): PageRequest {
    return new PageRequest(req.query.page || 0, req.query.pageSize || 20);
}
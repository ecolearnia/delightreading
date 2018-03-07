"use strict";

import * as async from "async";
import * as rootLogger from "pino";
import { Repository, getRepository } from "typeorm";
import { ServiceBase } from "./ServiceBase";
import { Reference } from "../entity/Reference";

const logger = rootLogger().child({ module: "ReferenceService" });

export class ReferenceService extends ServiceBase<Reference>  {

    constructor() {
        super(Reference);
    }

    async findOneByTitleAndAuthor(title: string, author: string): Promise<Reference> {
        const references = await this.list({ title: title });

        const reference = references.find((element) => {
            if (element.authors && (element.authors as string[]).includes(author)) {
                return true;
            }
            return false;
        });

        return reference;
    }
}
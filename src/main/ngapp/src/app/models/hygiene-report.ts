import {User} from "./User";
import {School} from "./school";
import {District} from "./district";

export class HygieneReport {
    id: number;
    reporter: User;
    district: District;
    school: School;
    creationTime: number;
    role: string;
    compliant: boolean;
    type: string;

    constructor(type = 'HAND') {
        this.type = type;
    }
}
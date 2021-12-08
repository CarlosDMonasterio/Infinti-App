import {User} from "./User";
import {School} from "./school";
import {District} from "./district";
import {NgbDate, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";

export class IncidentReport {
    id: number;
    user: User;
    district: District;
    school: School;
    details: string;
    dateTime: number;
    department: string;
    location: string;
    date: NgbDate;
    time: NgbTimeStruct;
    supervisorNotified: boolean;
    supervisor: string;
    witnesses: string;
    additionalDetails: string;
}
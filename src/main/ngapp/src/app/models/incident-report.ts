import {User} from "./User";
import {School} from "./school";
import {District} from "./district";

export class IncidentReport {
    id: number;
    user: User;
    district: District;
    school: School;
    details: string;
    dateTime: number;
    department: string;
    location: string;
    date: string;
    time: string;
    supervisorNotified: boolean;
    supervisor: string;
    witnesses: string;
    additionalDetails: string;
}
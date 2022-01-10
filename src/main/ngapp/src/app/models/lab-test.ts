import {User} from './User';
import {District} from './district';
import {School} from './school';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';

export class LabTest {
    id: number;
    created: number;
    account: User;
    district: District;
    school: School;
    date: NgbDate;
    location: string;
    department: string;
    dateTime: number;
    result: string;
    fileId: string;
}

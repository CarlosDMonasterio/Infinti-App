import {User} from "./User";
import {School} from "./school";
import {SurveyQuestion} from "./survey-question";
import {District} from "./district";

export class Survey {
    id: number;
    type: string;
    account: User;
    district: District;
    school: School;
    questions: SurveyQuestion[];
    creationTime: number;

    constructor(type = 'AUDIT') {
        this.questions = [];
        this.type = type;
    }
}
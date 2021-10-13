import {User} from "./User";
import {School} from "./school";
import {SurveyQuestion} from "./survey-question";
import {District} from "./district";

export class Survey {
    id: number;
    account: User;
    district: District;
    school: School;
    questions: SurveyQuestion[];
    creationTime: number;

    constructor() {
        this.questions = [];
    }
}
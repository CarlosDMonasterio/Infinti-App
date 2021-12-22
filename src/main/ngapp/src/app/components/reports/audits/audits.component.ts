import {Component, OnInit} from '@angular/core';
import {Result} from "../../../models/Result";
import {Survey} from "../../../models/survey";
import {Paging} from "../../../models/Paging";
import {HttpService} from "../../../http.service";
import {SurveyQuestion} from "../../../models/survey-question";
import {SurveyService} from "../../../services/survey.service";
import {District} from "../../../models/district";

const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
    selector: 'app-audits',
    templateUrl: './audits.component.html',
    styleUrls: ['./audits.component.css']
})
export class AuditsComponent implements OnInit {

    paging: Paging;
    reports: Survey[];
    availableQuestions: SurveyQuestion[];
    districts: District[];
    selectedDistrict: string;

    constructor(private http: HttpService, private survey: SurveyService) {
        this.paging = new Paging();
        this.availableQuestions = this.survey.getQuestions();
        this.selectedDistrict = 'All';
    }

    ngOnInit(): void {
        this.paging.type = 'AUDIT';
        this.getAudits();

        // get districts
        this.http.get('districts').subscribe((result: Result<District>) => {
            this.districts = result.requested;
        });

        // get graph data
        // this.http.get('surveys/graph?type=AUDIT')
    }

    getAudits(): void {
        this.paging.processing = true;
        this.http.get('surveys', this.paging).subscribe((result: Result<Survey>) => {
            this.paging.available = result.available;
            this.reports = result.requested;

            for (let survey of result.requested) {
                for (let question of survey.questions) {
                    question = this.getQuestion(question);
                }
            }

            this.paging.processing = false;
        });
    }

    pageQualityAudits(page): void {
        this.paging.currentPage = parseInt(page, 10) || 1;
        this.paging.start = (this.paging.currentPage - 1) * this.paging.limit;
        this.getAudits();
    }

    filterDistricts(newD): void {

    }

    getQuestion(question: SurveyQuestion): SurveyQuestion {
        for (let availableQuestion of this.availableQuestions) {
            if (availableQuestion.label === question.label) {
                question.expected = availableQuestion.expected;
                question.details = availableQuestion.details;
                return question;
            }
        }
        return question;
    }

    sortQuestions(questions: SurveyQuestion[]): SurveyQuestion[] {
        return questions.sort((a: SurveyQuestion, b: SurveyQuestion) => a.label > b.label ? 1 : a.label === b.label ? 0 : -1);
    }

    formatInput(input: HTMLInputElement) {
        input.value = input.value.replace(FILTER_PAG_REGEX, '');
    }
}

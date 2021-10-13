import {Component, OnInit} from '@angular/core';
import {Result} from "../../../models/Result";
import {HttpService} from "../../../http.service";
import {School} from "../../../models/school";
import {Survey} from "../../../models/survey";
import {Observable} from "rxjs";
import {debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";
import {SchoolService} from "../../../services/school.service";
import {Router} from "@angular/router";
import {SurveyService} from "../../../services/survey.service";
import {SurveyQuestion} from "../../../models/survey-question";
import {District} from "../../../models/district";

@Component({
    selector: 'app-survey',
    templateUrl: './survey.component.html',
    styleUrls: ['./survey.component.css']
})
export class SurveyComponent implements OnInit {

    survey: Survey;
    districts: District[];
    availableSchools: School[];     // available schools by district
    retrievingSchools: boolean;
    searching: boolean;
    roles: string[];
    compliance: string[];
    progress: number;
    submittingReport: boolean;
    errorSubmitting: boolean;
    questions: SurveyQuestion[];
    answeredCount: number;

    page: number;

    constructor(private http: HttpService, private search: SchoolService,
                private router: Router, private surveyService: SurveyService) {
        this.survey = new Survey();
        this.progress = 0;
        this.answeredCount = 0;
        this.questions = this.surveyService.getQuestions();
        this.page = -1;
    }

    ngOnInit(): void {
        this.http.get('districts').subscribe((result: Result<District>) => {
            this.districts = result.requested;
        });
    }

    nextQuestion(): void {
        if (this.page === this.questions.length)
            return;

        this.page += 1;
    }

    previousQuestion(): void {
        if (this.page === -1)
            return;

        this.page -= 1;
    }

    // change detection when a district is selected
    districtSelected(): void {
        this.retrievingSchools = true;
        this.progress = 1;
        this.survey.school = undefined;
        this.errorSubmitting = false;
    }

    schoolSelected($event): void {
        this.survey.school = $event.item;
        this.progress = 3;
        this.errorSubmitting = false;
    }

    setAnswer(answer: boolean): void {
        if (this.questions[this.page].answer != false && this.questions[this.page].answer != true) {
            // then answering a new question
            this.answeredCount += 1;
            this.progress = (this.answeredCount / this.questions.length) * 100;
        }

        this.questions[this.page].answer = answer;
    }

    roleSelected(): void {
        this.progress = 60;
        this.errorSubmitting = false;
    }

    nextDisabled(): boolean {
        if (this.page === -1 && (!this.survey.school || !this.survey.district))
            return true;

        return this.page === this.questions.length - 1;
    }

    isLastQuestion(): boolean {
        return (this.page === this.questions.length - 1);
    }

    submitReport(): void {
        this.errorSubmitting = false;
        this.progress = 100;
        this.submittingReport = true;
        Object.assign(this.survey.questions, this.questions);
        this.http.post('surveys', this.survey).subscribe((result: Survey) => {
            this.submittingReport = false;

            if (result) {
                this.router.navigate(['/']);
            } else {
                this.errorSubmitting = true;
            }
        }, error => {
            this.submittingReport = false;
            this.errorSubmitting = true;
            this.progress = 90;
        });
    }

    formatter = (school: School) => school.name;
    searchDistrictSchools = (text$: Observable<string>) => {
        return text$.pipe(
            debounceTime(200),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term => this.search.searchDistrictSchools(term, this.survey.district.id)),
            // catchError(),
            tap(() => this.searching = false));
    }
}

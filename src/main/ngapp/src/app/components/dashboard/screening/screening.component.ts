import {Component, OnInit} from '@angular/core';
import {Survey} from "../../../models/survey";
import {SurveyQuestion} from "../../../models/survey-question";
import {ScreeningService} from "../../../services/screening.service";
import {HttpService} from "../../../http.service";
import {Router} from "@angular/router";
import {Result} from "../../../models/Result";
import {District} from "../../../models/district";
import {Pass} from "../../../models/pass";

@Component({
    selector: 'app-screening',
    templateUrl: './screening.component.html',
    styleUrls: ['./screening.component.css']
})
export class ScreeningComponent implements OnInit {

    survey: Survey;
    progress: number;
    submittingReport: boolean;
    errorSubmitting: boolean;
    questions: SurveyQuestion[];
    answeredCount: number;
    attested: boolean;
    districts: District[];
    page: number;
    selectedDistrict = false;
    pass: Pass;
    reportSubmitted: boolean;

    constructor(private http: HttpService, private screenService: ScreeningService, private router: Router) {
        this.survey = new Survey('DAILY_HEALTH');
        this.progress = 0;
        this.answeredCount = 0;
        this.page = 0;
        this.attested = false;
        this.questions = this.screenService.getQuestions();
    }

    ngOnInit(): void {
        this.http.get('districts').subscribe((result: Result<District>) => {
            this.districts = result.requested;
        });
    }

    setAnswer(answer: boolean): void {
        if (this.questions[this.page].answer != false && this.questions[this.page].answer != true) {
            // then answering a new question
            this.answeredCount += 1;
            this.progress = (this.answeredCount / (this.questions.length + 2)) * 100;
        }

        this.questions[this.page].answer = answer;
    }

    nextDisabled(): boolean {
        return this.page === this.questions.length;
    }

    nextQuestion(): void {
        if (this.page === this.questions.length)
            return;

        this.page += 1;
    }

    previousQuestion(): void {
        if (this.page === 0)
            return;

        this.page -= 1;
    }

    isLastQuestion(): boolean {
        return (this.page === this.questions.length);
    }

    getSubmissionClass(): string {
        if (this.answeredCount < (this.questions.length + 1))
            return 'text-secondary';

        if (this.answeredCount === (this.questions.length + 1))
            return 'text-success';

        return 'text-secondary';
    }

    districtSelected(): void {
        if (!this.selectedDistrict) {
            this.answeredCount += 1;
            this.progress = (this.answeredCount / (this.questions.length + 2)) * 100;
            this.selectedDistrict = true;
        }
        this.errorSubmitting = false;
    }

    submitReport(): void {
        this.errorSubmitting = false;
        this.progress = 100;
        this.submittingReport = true;
        Object.assign(this.survey.questions, this.questions);

        // set the details to the label since screening questions don't have labels
        for (let i = 0; i < this.survey.questions.length; i += 1) {
            this.survey.questions[i].label = this.survey.questions[i].details;
        }

        // submit to the backend
        this.http.post('surveys', this.survey).subscribe((result: Survey) => {
            this.survey = result;

            // get pass
            this.http.get('passes?userId=' + this.survey.account.email).subscribe((pass: Pass) => {
                this.pass = pass;
                this.reportSubmitted = true;
                this.submittingReport = false;
            }, error => {
                this.reportSubmitted = true;
                this.submittingReport = false;
            });

        }, error => {
            this.submittingReport = false;
            this.errorSubmitting = true;
            this.progress = 90;
        });
    }

    submitDisabled(): boolean {
        if (this.submittingReport || !this.attested)
            return true;

        // number of answers should equal number of questions
        // + 1 accounts for attestation
        return this.answeredCount < this.questions.length + 1;
    }

    setAttestation(): void {
        this.attested = !this.attested;
        if (this.attested) {
            this.answeredCount += 1;
        } else {
            this.answeredCount -= 1;
        }
        this.progress = (this.answeredCount / (this.questions.length + 1)) * 100;
    }

    goToHome(): void {
        this.router.navigate(['/']);
    }
}

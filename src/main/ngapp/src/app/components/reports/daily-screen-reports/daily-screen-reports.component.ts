import {Component, OnInit} from '@angular/core';
import {Result} from "../../../models/Result";
import {Survey} from "../../../models/survey";
import {HttpService} from "../../../http.service";
import {Paging} from "../../../models/Paging";
import {SurveyQuestion} from "../../../models/survey-question";
import {ScreeningService} from "../../../services/screening.service";

@Component({
    selector: 'app-daily-screen-reports',
    templateUrl: './daily-screen-reports.component.html',
    styleUrls: ['./daily-screen-reports.component.css']
})
export class DailyScreenReportsComponent implements OnInit {

    paging: Paging;
    reports: Survey[];
    questions: SurveyQuestion[];

    constructor(private http: HttpService, private screenService: ScreeningService) {
        this.paging = new Paging();
        this.paging.type = 'DAILY_HEALTH';
        this.questions = this.screenService.getQuestions();
    }

    ngOnInit(): void {
        this.getReports();
    }

    pageDailyHealthReports(object?): void {
        if (object) {
            this.paging.start = (object.params.currentPage - 1) * this.paging.limit;
            this.paging.currentPage = object.params.currentPage;
        }

        this.getReports();
    }

    getReports(): void {
        this.http.get("surveys", this.paging).subscribe((result: Result<Survey>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
        });
    }
}

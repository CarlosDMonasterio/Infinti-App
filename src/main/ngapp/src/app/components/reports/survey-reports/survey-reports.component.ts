import {Component, OnInit} from '@angular/core';
import {Paging} from "../../../models/Paging";
import {Survey} from "../../../models/survey";
import {HttpService} from "../../../http.service";
import {Result} from "../../../models/Result";

@Component({
    selector: 'app-survey-reports',
    templateUrl: './survey-reports.component.html',
    styleUrls: ['./survey-reports.component.css']
})
export class SurveyReportsComponent implements OnInit {

    paging: Paging;
    reports: Survey[];

    constructor(private http: HttpService) {
        this.paging = new Paging();
    }

    ngOnInit(): void {
        this.http.get('surveys', this.paging).subscribe((result: Result<Survey>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
        });
    }
}

import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-survey-reports',
    templateUrl: './survey-reports.component.html',
    styleUrls: ['./survey-reports.component.css']
})
export class SurveyReportsComponent implements OnInit {

    active: string;

    constructor() {
        this.active = 'tests';
    }

    ngOnInit(): void {
    }
}

import {Component, OnInit} from '@angular/core';
import {HygieneReport} from "../../../models/hygiene-report";

@Component({
    selector: 'app-tracing',
    templateUrl: './tracing.component.html',
    styleUrls: ['./tracing.component.css']
})
export class TracingComponent implements OnInit {

    report: HygieneReport;
    roles: string[];
    compliance: string[];
    caseTypes: string[];
    progress: number;
    submittingReport: boolean;
    errorSubmitting: boolean;

    caseType: string;
    caseNumber: string;

    constructor() {
        this.caseTypes = ['Positive Case', 'Exposure', 'Unconfirmed Symptomatic'];
    }

    ngOnInit(): void {
    }

    submitReport(): void {
    }

    caseTypeSelected(): void {

    }
}

import {Component, OnInit} from '@angular/core';
import {District} from "../../../models/district";
import {School} from "../../../models/school";
import {HygieneReport} from "../../../models/hygiene-report";

@Component({
    selector: 'app-tests',
    templateUrl: './tests.component.html',
    styleUrls: ['./tests.component.css']
})
export class TestsComponent implements OnInit {

    districts: District[];
    availableSchools: School[];     // available schools by district
    retrievingSchools: boolean;
    report: HygieneReport;
    searching: boolean;
    roles: string[];
    compliance: { display: string, value: boolean }[];
    progress: number;
    submittingReport: boolean;
    errorSubmitting: boolean;

    constructor() {
    }

    ngOnInit(): void {
    }

    submitTestResult(): void {

    }

}

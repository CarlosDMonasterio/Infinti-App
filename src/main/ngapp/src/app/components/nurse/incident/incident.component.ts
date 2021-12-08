import {Component, Injectable, OnInit} from '@angular/core';
import {HttpService} from "../../../http.service";
import {Result} from "../../../models/Result";
import {IncidentReport} from "../../../models/incident-report";
import {School} from "../../../models/school";
import {Observable} from "rxjs";
import {debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";
import {Router} from "@angular/router";
import {SchoolService} from "../../../services/school.service";
import {District} from "../../../models/district";
import {NgbDateParserFormatter, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";

@Injectable()
export class CustomDateParserFormatter extends NgbDateParserFormatter {

    readonly DELIMITER = '/';

    parse(value: string): NgbDateStruct | null {
        if (value) {
            let date = value.split(this.DELIMITER);
            return {
                day: parseInt(date[0], 10),
                month: parseInt(date[1], 10),
                year: parseInt(date[2], 10)
            };
        }
        return null;
    }

    format(date: NgbDateStruct | null): string {
        return date ? date.month + this.DELIMITER + date.day + this.DELIMITER + date.year : '';
    }
}

@Component({
    selector: 'app-incident',
    templateUrl: './incident.component.html',
    styleUrls: ['./incident.component.css'],
    providers: [
        {provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter}
    ]
})
export class IncidentComponent implements OnInit {
    districts: District[];
    availableSchools: School[];     // available schools by district
    retrievingSchools: boolean;

    question: number;
    totalQuestions: number;
    report: IncidentReport;

    searching: boolean;
    progress: number;
    submittingReport: boolean;
    errorSubmitting: boolean;
    page: number;
    time: { hour: number, minute: number };
    minuteStep = 15;

    constructor(private http: HttpService, private search: SchoolService, private router: Router) {
        this.question = 1;
        this.totalQuestions = 2;
        this.time = {hour: 12, minute: 0};

        this.report = new IncidentReport();
        this.progress = 0;
        this.page = 0;
    }

    ngOnInit(): void {
        this.http.get('districts').subscribe((result: Result<District>) => {
            this.districts = result.requested;
        });
    }

    schoolSelected($event): void {
        this.report.school = $event.item;
        this.progress = 50;
    }

    previousQuestion(): void {
        if (this.page === 0)
            return;

        this.page -= 1;
    }

    nextQuestion(): void {
        this.progress = 0;

        // calculate progress
        // dept/location or district/school
        if ((this.report.district && this.report.school) || (this.report.department && this.report.location)) {
            this.progress += 20;
        }

        // date
        if (this.report.date) {
            this.progress += 20;
        }

        // time
        if (this.report.time) {
            this.progress += 20;
        }

        // details
        if (this.report.details) {
            this.progress += 20;
        }

        if ((this.report.supervisorNotified === true && this.report.supervisor) || this.report.supervisorNotified === false) {
            this.progress += 20;
        }

        if (this.isLastQuestion())
            return;

        this.page += 1;
    }

    submitDisabled(): boolean {
        return this.progress < 100;
    }

    isLastQuestion(): boolean {
        return this.page === 3;
    }

    nextDisabled(): boolean {
        return this.isLastQuestion();
    }

    goToHome(): void {
        this.router.navigate(['/']);
    }

    submitReport(): void {


        this.submittingReport = true;
        let dataString = this.report.date.year + '-' + ("0" + this.report.date.month).slice(-2) + '-' +
            ("0" + this.report.date.day).slice(-2)
        dataString += ('T' + ("0" + this.report.time.hour).slice(-2) + ':' + ("0" + this.report.time.minute).slice(-2));
        this.report.dateTime = Date.parse(dataString);

        this.http.post('incidents', this.report).subscribe((result: IncidentReport) => {
            this.submittingReport = false;
            this.page = 4;
        }, error => {
            this.submittingReport = false;
        });
    }

    // change detection when a district is selected
    districtSelected(): void {
        this.retrievingSchools = true;
        this.progress = 25;
        this.errorSubmitting = false;
    }

    formatter = (school: School) => school.name;
    searchDistrictSchools = (text$: Observable<string>) => {
        return text$.pipe(
            debounceTime(200),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term => this.search.searchDistrictSchools(term, this.report.district.id)),
            // catchError(),
            tap(() => this.searching = false));
    }
}

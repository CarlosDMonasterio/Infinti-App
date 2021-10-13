import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../http.service";
import {Result} from "../../../models/Result";
import {IncidentReport} from "../../../models/incident-report";
import {School} from "../../../models/school";
import {Observable} from "rxjs";
import {debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";
import {Router} from "@angular/router";
import {SchoolService} from "../../../services/school.service";
import {District} from "../../../models/district";

@Component({
    selector: 'app-incident',
    templateUrl: './incident.component.html',
    styleUrls: ['./incident.component.css']
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

    constructor(private http: HttpService, private search: SchoolService, private router: Router) {
        this.question = 1;
        this.totalQuestions = 2;
        this.report = new IncidentReport();
        this.progress = 0;
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

    submitReport(): void {
        this.progress = 100;
        this.submittingReport = true;
        this.http.post('incidents', this.report).subscribe((result: IncidentReport) => {
            this.submittingReport = false;
            this.router.navigate(['/']);
        }, error => {
            this.submittingReport = false;
            this.progress = 90;
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

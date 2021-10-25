import {Component, OnInit} from '@angular/core';
import {School} from "../../../models/school";
import {HygieneReport} from "../../../models/hygiene-report";
import {Observable} from "rxjs";
import {debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";
import {HttpService} from "../../../http.service";
import {Router} from "@angular/router";
import {SchoolService} from "../../../services/school.service";
import {Result} from "../../../models/Result";
import {District} from "../../../models/district";

@Component({
    selector: 'app-hygiene',
    templateUrl: './hygiene.component.html',
    styleUrls: ['./hygiene.component.css']
})
export class HygieneComponent implements OnInit {

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

    constructor(private http: HttpService, private search: SchoolService, private router: Router) {
        this.report = new HygieneReport();
        this.roles = ['HCP - Health Care Professional', 'Data Entry', 'Nurse', 'Grounds Coordinator'];
        this.compliance = [{display: 'No hand hygiene performed', value: false}, {
            display: 'Wash/Rub performed',
            value: true
        }];
        this.progress = 0;
    }

    ngOnInit(): void {
        this.http.get('districts').subscribe((result: Result<District>) => {
            this.districts = result.requested;
        });
    }

    // change detection when a district is selected
    districtSelected(): void {
        this.retrievingSchools = true;
        this.progress = 20;
        this.report.school = undefined;
        this.errorSubmitting = false;
    }

    schoolSelected($event): void {
        this.report.school = $event.item;
        this.progress = 40;
        this.errorSubmitting = false;
    }

    roleSelected(): void {
        this.progress = 60;
        this.errorSubmitting = false;
    }

    submitReport(): void {
        this.errorSubmitting = false;
        this.progress = 100;
        this.submittingReport = true;
        this.http.post('reports/hygiene', this.report).subscribe((result: HygieneReport) => {
            this.router.navigate(['/']);
            this.submittingReport = false;
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
            switchMap(term => this.search.searchDistrictSchools(term, this.report.district.id)),
            // catchError(),
            tap(() => this.searching = false));
    }
}

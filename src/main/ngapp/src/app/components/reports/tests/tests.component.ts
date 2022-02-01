import {Component, OnInit} from '@angular/core';
import {District} from '../../../models/district';
import {School} from '../../../models/school';
import {Observable} from 'rxjs';
import {debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {SchoolService} from '../../../services/school.service';
import {Router} from '@angular/router';
import {HttpService} from '../../../http.service';
import {Result} from '../../../models/Result';
import {LabTest} from '../../../models/lab-test';
import {HttpClient, HttpEventType, HttpHeaders, HttpParams, HttpRequest, HttpResponse} from '@angular/common/http';
import {UserService} from '../../../user.service';
import {environment} from '../../../../environments/environment';
import {NgbDate, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-tests',
    templateUrl: './tests.component.html',
    styleUrls: ['./tests.component.css']
})
export class TestsComponent implements OnInit {

    districts: District[];
    availableSchools: School[];     // available schools by district
    retrievingSchools: boolean;
    test: LabTest;
    searching: boolean;
    departments: string[];
    resultOptions: string[];
    compliance: { display: string, value: boolean }[];
    progress: number;
    submittingReport: boolean;
    savingReport: boolean;
    errorSubmitting: boolean;
    errorSaving: boolean;
    successSaving: boolean;
    successSubmitting: boolean;
    fileName: string;
    loaded: number;
    maxDate: NgbDateStruct;

    constructor(private http: HttpService, private search: SchoolService, private user: UserService,
                private router: Router, private httpClient: HttpClient) {
        this.test = new LabTest();
        this.departments = ['Operations', 'Nursing'];
        this.resultOptions = ['Positive', 'Negative', 'Inconclusive'];
        const today = new Date();
        this.maxDate = {year: today.getFullYear(), month: today.getMonth() + 1, day: today.getDate()};
        this.progress = 0;
    }

    ngOnInit(): void {
        // retrieve available
        this.http.get('districts').subscribe((result: Result<District>) => {
            this.districts = result.requested;
        });
    }

    // change detection when a district is selected
    districtSelected(): void {
        this.retrievingSchools = true;
        this.test.school = undefined;
        this.errorSubmitting = false;
    }

    schoolSelected($event): void {
        this.test.school = $event.item;
        this.errorSubmitting = false;
    }

    // set the progress value
    modelChange(): void {
        this.errorSubmitting = false;
        this.progress = 0;

        // check department
        if (this.test.department) {
            this.progress += 20;
        }

        // check test date
        if (this.test.date) {
            this.progress += 20;
        }

        // check test location
        if (this.test.location) {
            this.progress += 20;
        }

        // check test result
        if (this.test.result) {
            this.progress += 20;
        }

        // check file id
        if (this.test.fileId) {
            this.progress += 20;
        }
    }

    dateSelected(value: NgbDate): void {
        this.modelChange();
    }

    testResultSelected(): void {
        this.modelChange();
    }

    /**
     * Check if user can submit the form (i.e. the submit button can be enabled)
     * District and school are both optional
     */
    canSubmit(): boolean {
        if (!this.test.department) {
            return false;
        }
        if (!this.test.date) {
            return false;
        }
        return !(!this.test.location || !this.test.result || !this.test.fileId);
    }

    canSave(): boolean {
        return true; //User can always save except if the case number is empty
    }

    submitTestResult(): void {
        this.errorSubmitting = false;
        this.submittingReport = true;
        const dataString = this.test.date.year + '-' + ('0' + this.test.date.month).slice(-2) + '-' +
            ('0' + this.test.date.day).slice(-2);
        this.test.dateTime = Date.parse(dataString);
        this.test.result = this.test.result.toUpperCase();

        this.http.post('tests', this.test).subscribe((result: LabTest) => {
            this.submittingReport = false;
            this.progress = 100;
            this.router.navigate(['/']);
        }, error => {
            this.submittingReport = false;
        });
    }

    saveResults(): void{
        this.errorSaving = false;
        this.savingReport = true;

        //TODO - Save to database

        //TODO - IF successful display success div
        this.successSaving = true;
        //TODO - Else display error saving div
    }

    formatter = (school: School) => school.name;
    searchDistrictSchools = (text$: Observable<string>) => {
        return text$.pipe(
            debounceTime(200),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term => this.search.searchDistrictSchools(term, this.test.district.id)),
            // catchError(),
            tap(() => this.searching = false));
    }

    onFileSelected(event): void {
        const file: File = event.target.files[0];

        if (file) {
            this.fileName = file.name;
            const formData = new FormData();
            formData.append('file', file);

            const params = new HttpParams();
            const headers = new HttpHeaders({'X-IH-Authentication-SessionId': this.user.getUser().sessionId});
            const options = {
                headers,
                params,
                reportProgress: true
            };

            const request = new HttpRequest('POST', environment.apiUrl + '/files', formData, options);
            this.httpClient.request(request).subscribe(event => {
                console.log(event);
                if (event.type === HttpEventType.UploadProgress) {
                    this.loaded = Math.round(100 * event.loaded / event.total);
                } else if (event instanceof HttpResponse) {
                    const data: any = event.body; // {id: number, identifier: string}
                    this.test.fileId = data.identifier;
                    this.modelChange();
                    console.log('server response', data);
                }
            });
        }
    }
}

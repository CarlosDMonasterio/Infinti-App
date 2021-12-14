import {Component, OnInit} from '@angular/core';
import {Paging} from "../../../models/Paging";
import {LabTest} from "../../../models/lab-test";
import {HttpService} from "../../../http.service";
import {Result} from "../../../models/Result";

@Component({
    selector: 'app-test-results',
    templateUrl: './test-results.component.html',
    styleUrls: ['./test-results.component.css']
})
export class TestResultsComponent implements OnInit {

    paging: Paging;
    tests: LabTest[];

    constructor(private http: HttpService) {
        this.paging = new Paging();
    }

    ngOnInit(): void {
        this.getDataFromServer();
    }

    pageTests(page): void {
        this.paging.start = ((page - 1) * this.paging.limit);
        this.getDataFromServer();
    }

    private getDataFromServer(): void {
        this.paging.processing = true;
        this.http.get('tests', this.paging).subscribe((result: Result<LabTest>) => {
            this.paging.available = result.available;
            this.tests = result.requested;
            this.paging.processing = false;
        });
    }

}

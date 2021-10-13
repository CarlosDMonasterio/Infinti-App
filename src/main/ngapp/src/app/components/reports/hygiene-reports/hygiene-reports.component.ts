import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../http.service";
import {Paging} from "../../../models/Paging";
import {HygieneReport} from "../../../models/hygiene-report";
import {Result} from "../../../models/Result";

@Component({
    selector: 'app-hygiene-reports',
    templateUrl: './hygiene-reports.component.html',
    styleUrls: ['./hygiene-reports.component.css']
})
export class HygieneReportsComponent implements OnInit {

    paging: Paging;
    reports: HygieneReport[];

    constructor(private http: HttpService) {
        this.paging = new Paging();
    }

    ngOnInit(): void {
        this.http.get('reports/hygiene/HAND', this.paging).subscribe((result: Result<HygieneReport>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
            console.log(result);
        })
    }
}

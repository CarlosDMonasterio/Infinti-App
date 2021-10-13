import {Component, OnInit} from '@angular/core';
import {Paging} from "../../../models/Paging";
import {HygieneReport} from "../../../models/hygiene-report";
import {Result} from "../../../models/Result";
import {HttpService} from "../../../http.service";

@Component({
    selector: 'app-glove-reports',
    templateUrl: './glove-reports.component.html',
    styleUrls: ['./glove-reports.component.css']
})
export class GloveReportsComponent implements OnInit {

    paging: Paging;
    reports: HygieneReport[];

    constructor(private http: HttpService) {
        this.paging = new Paging();
    }

    ngOnInit(): void {
        this.http.get('reports/hygiene/GLOVE', this.paging).subscribe((result: Result<HygieneReport>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
            console.log(result);
        })
    }
}

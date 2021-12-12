import {Component, OnInit} from '@angular/core';
import {Paging} from "../../../models/Paging";
import {IncidentReport} from "../../../models/incident-report";
import {Result} from "../../../models/Result";
import {HttpService} from "../../../http.service";

@Component({
    selector: 'app-incident-reports',
    templateUrl: './incident-reports.component.html',
    styleUrls: ['./incident-reports.component.css']
})
export class IncidentReportsComponent implements OnInit {

    paging: Paging;
    reports: IncidentReport[];

    constructor(private http: HttpService) {
        this.paging = new Paging();
    }

    ngOnInit(): void {
        this.getDataFromServer();
    }

    pageIncidents(page): void {
        this.paging.start = ((page - 1) * this.paging.limit);
        this.getDataFromServer();
    }

    private getDataFromServer(): void {
        this.paging.processing = true;
        this.http.get('incidents', this.paging).subscribe((result: Result<IncidentReport>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
            this.paging.processing = false;
        });
    }
}

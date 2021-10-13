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
        this.http.get('incidents', this.paging).subscribe((result: Result<IncidentReport>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
            console.log(result);
        })
    }

}

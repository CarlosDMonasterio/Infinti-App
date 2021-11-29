import {Component, OnInit} from '@angular/core';
import {Result} from "../../../models/Result";
import {Survey} from "../../../models/survey";
import {Paging} from "../../../models/Paging";
import {HttpService} from "../../../http.service";

@Component({
    selector: 'app-audits',
    templateUrl: './audits.component.html',
    styleUrls: ['./audits.component.css']
})
export class AuditsComponent implements OnInit {

    paging: Paging;
    reports: Survey[];

    constructor(private http: HttpService) {
        this.paging = new Paging();
    }

    ngOnInit(): void {
        this.paging.type = 'AUDIT';
        this.http.get('surveys', this.paging).subscribe((result: Result<Survey>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
        });

        // get graph data
        // this.http.get('surveys/graph?type=AUDIT')
    }
}

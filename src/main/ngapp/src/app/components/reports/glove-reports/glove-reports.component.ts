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

    multi: any[];

    // options
    showXAxis: boolean = true;
    showYAxis: boolean = true;
    gradient: boolean = false;
    showLegend: boolean = false;
    showXAxisLabel: boolean = false;
    showYAxisLabel: boolean = false;

    animations: boolean = true;

    colorScheme = {
        domain: ['#b10c0c', '#047c26', '#AAAAAA']
    };

    constructor(private http: HttpService) {
        this.paging = new Paging();
    }

    ngOnInit(): void {
        this.http.get('reports/hygiene/GLOVE', this.paging).subscribe((result: Result<HygieneReport>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
        })

        // get hygiene graph data
        this.http.get('reports/hygiene/GLOVE/graph').subscribe((result: any[]) => {
            this.multi = result;
        });
    }

    onSelect(event): void {
        console.log(event);
    }
}

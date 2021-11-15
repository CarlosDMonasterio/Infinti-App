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
        // this.multi = [
        //     {
        //         "name": "13 Nov",
        //         "series": [
        //             {
        //                 "name": "Non-Compliance",
        //                 "value": 6
        //             },
        //             {
        //                 "name": "Compliance",
        //                 "value": 13
        //             }
        //         ]
        //     },
        //     {
        //         "name": "12 Nov",
        //         "series": [
        //             {
        //                 "name": "Non-Compliance",
        //                 "value": 0
        //             },
        //             {
        //                 "name": "Compliance",
        //                 "value": 14
        //             }
        //         ]
        //     },
        //     {
        //         "name": "11 Nov",
        //         "series": [
        //             {
        //                 "name": "Non-Compliance",
        //                 "value": 19
        //             },
        //             {
        //                 "name": "Compliance",
        //                 "value": 6
        //             }
        //         ]
        //     },
        //     {
        //         "name": "10 Nov",
        //         "series": [
        //             {
        //                 "name": "Non-Compliance",
        //                 "value": 78
        //             },
        //             {
        //                 "name": "Compliance",
        //                 "value": 5
        //             }
        //         ]
        //     },
        //     {
        //         "name": "9 Nov",
        //         "series": [
        //             {
        //                 "name": "Non-Compliance",
        //                 "value": 98
        //             },
        //             {
        //                 "name": "Compliance",
        //                 "value": 100
        //             }
        //         ]
        //     },
        //     {
        //         "name": "8 Nov",
        //         "series": [
        //             {
        //                 "name": "Non-Compliance",
        //                 "value": 0
        //             },
        //             {
        //                 "name": "Compliance",
        //                 "value": 0
        //             }
        //         ]
        //     },
        //     {
        //         "name": "7 Nov",
        //         "series": [
        //             {
        //                 "name": "Non-Compliance",
        //                 "value": 3
        //             },
        //             {
        //                 "name": "Compliance",
        //                 "value": 74
        //             }
        //         ]
        //     },
        // ];
    }

    ngOnInit(): void {
        this.http.get('reports/hygiene/HAND', this.paging).subscribe((result: Result<HygieneReport>) => {
            this.paging.available = result.available;
            this.reports = result.requested;
        })

        // get hygiene graph data
        this.http.get('reports/hygiene/HAND/graph').subscribe((result: any[]) => {
            this.multi = result;
        });
    }

    onSelect(event): void {
        console.log(event);
    }
}

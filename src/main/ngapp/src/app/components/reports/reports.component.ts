import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
    selector: 'app-reports',
    templateUrl: './reports.component.html',
    styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

    active: string;

    constructor(private router: Router) {
    }

    ngOnInit(): void {
        const urlSplit: string[] = this.router.url.split('/');
        if (urlSplit.length === 3)
            this.active = urlSplit[2];
        else
            this.active = 'settings';
    }

    showReport(page: string): void {
        this.active = page;
        this.router.navigate(['/reports/' + page]);
    }
}

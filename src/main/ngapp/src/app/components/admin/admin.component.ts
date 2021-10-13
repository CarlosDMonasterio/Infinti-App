import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

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

    showPage(page: string): void {
        this.active = page;
        this.router.navigate(['/admin/' + page]);
    }
}

import {Component, OnInit} from '@angular/core';
import {HttpService} from '../../http.service';

@Component({
    selector: 'app-config',
    templateUrl: './config.component.html',
    styleUrls: ['./config.component.css']
})
export class ConfigComponent implements OnInit {

    config: any;

    constructor(private http: HttpService) {
    }

    ngOnInit(): void {
        this.config = {};
        this.http.get('config/init').subscribe(result => {
            this.config = result;
        });
    }
}

import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {UserService} from "../../user.service";
import {HttpService} from "../../http.service";
import {Pass} from "../../models/pass";

@Component({
    selector: 'app-passes',
    templateUrl: './passes.component.html',
    styleUrls: ['./passes.component.css']
})
export class PassesComponent implements OnInit {

    user: User;
    date: any;
    pass: Pass;

    constructor(private users: UserService, private http: HttpService) {
    }

    ngOnInit(): void {
        this.user = this.users.getUser();
        this.date = new Date();

        this.http.get('passes?userId=' + this.user.email).subscribe((pass: Pass) => {
            if (this.user.email !== pass.account.email)
                return;

            this.pass = pass;
        }, error => {
        });
    }
}

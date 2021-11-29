import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {Router} from "@angular/router";
import {HttpService} from "../../http.service";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

    newUser: User;
    validationError: any;
    loginCompleted: boolean;

    constructor(private router: Router, private http: HttpService) {
        this.newUser = new User();
        this.validationError = {};
    }

    ngOnInit(): void {
    }

    registerUser(): void {
        // validate
        this.validationError = {};

        if (!this.newUser.firstName) {
            this.validationError.firstName = true;
        }

        if (!this.newUser.lastName) {
            this.validationError.lastName = true;
        }

        if (!this.newUser.email) {
            this.validationError.email = true;
        }

        if (Object.keys(this.validationError).length > 0) {
            return;
        }

        // create user then show popup with password
        this.http.post('users', this.newUser, undefined, false).subscribe((result: User) => {
            if (!result) {
                return;
            }

            this.loginCompleted = true;
        }, error => {
            if (error.status === 409) {
                this.validationError.userIdNotAvailable = true;
                this.loginCompleted = false;
            }
        });
    }

    cancelRegister(): void {
        this.router.navigate(['/login']);
    }
}

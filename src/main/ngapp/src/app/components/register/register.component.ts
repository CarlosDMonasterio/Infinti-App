import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {Router} from "@angular/router";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

    newUser: User;
    validationError: any;

    constructor(private router: Router) {
        this.newUser = new User();
        this.validationError = {};
    }

    ngOnInit(): void {
    }

    registerUser(): void {
    }

    cancelRegister(): void {
        this.router.navigate(['/login']);
    }
}

import {Component, OnInit} from '@angular/core';
import {HttpService} from '../../http.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-forgot-password',
    templateUrl: './forgot-password.component.html',
    styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

    userId = undefined;
    processing = false;
    passwordWasReset = false;
    errorResetting = false;

    constructor(private http: HttpService, private router: Router) {
    }

    ngOnInit(): void {
    }

    resetPassword(): void {
        this.processing = false;
        this.passwordWasReset = false;
        this.errorResetting = false;

        if (!this.userId) {
            // Util.setError('Please enter your userId and try again');
            return;
        }

        this.processing = true;
        this.http.post('users/' + this.userId + '/password', null).subscribe(response => {
            this.processing = false;
            this.passwordWasReset = true;
        }, error => {
            console.log(error);
            this.processing = false;
            this.errorResetting = true;
        });
    }

    goToLogin(): void {
        this.router.navigate((['/login']));
    }
}

import {Component, OnInit} from '@angular/core';
import {HttpService} from '../../http.service';
import {User} from '../../models/User';
import {Router} from '@angular/router';
import {UserService} from '../../user.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    loggedInUser: User;
    processing = false;
    remember: boolean;
    validation: { validId: boolean, validPassword: boolean, invalidPassword: boolean };

    constructor(private http: HttpService, private router: Router, private userService: UserService) {
        this.validation = {validId: true, validPassword: true, invalidPassword: false};
    }

    ngOnInit(): void {
        // check remember me setting
        this.remember = (localStorage.getItem('rememberUser') !== null);

        // verify if sessionId is valid when visiting the login page and
        // redirect user to main page if so
        this.loggedInUser = this.userService.getUser(false);
        if (this.loggedInUser && this.loggedInUser.sessionId) {
            this.http.get('accesstoken').subscribe(() => {
                this.router.navigate(['/']);
            }, error => {
                this.userService.clearUser();
            });
            return;
        }

        if (!this.loggedInUser) {
            this.loggedInUser = new User();
        }
    }

    setRemember(): void {
        this.remember = !this.remember;
        if (this.remember) {
            localStorage.setItem('rememberUser', 'yes');
            console.log('set local storage');
            console.log(localStorage.getItem('rememberUser'));
        } else {
            localStorage.setItem('rememberUser', null);
        }
    }

    loginUser(): void {
        this.validation.validId = (this.loggedInUser.email !== undefined && this.loggedInUser.email !== '');
        this.validation.validPassword = (this.loggedInUser.password !== undefined && this.loggedInUser.password !== '');

        if (!this.validation.validId || !this.validation.validPassword) {
            return;
        }
        this.validation.invalidPassword = false;

        this.processing = true;
        this.http.post('accesstoken', this.loggedInUser).subscribe((result: User) => {
            console.log(result); //**ASK IF NECESSARY**
            this.processing = false;

            // this relies on the user knowing their password before being notified which is highly unlikely
            if (result.disabled) {
                console.log('Account is still being vetted');
                return;
            }

            // check if password needs to be created and re-direct if so
            if (result.usingTemporaryPassword) {
                this.userService.setUser(result);
                this.router.navigate(['/resetPassword']);
                return;
            }

            // save to session
            this.loggedInUser = result;
            if (result && result.sessionId) {
                this.userService.setUser(result);

                // redirect
                let redirectUrl = this.userService.getLoginRedirect();
                if (redirectUrl === '/forgotPassword' || redirectUrl === '/login' || !redirectUrl) {
                    redirectUrl = '/';
                }
                this.router.navigate([redirectUrl]);
            }
        }, error => {
            this.processing = false;
            console.error(error);
            if (error.status === 401) {
                this.validation.invalidPassword = true;
            }
        });
    }
}

import {Component, OnInit} from '@angular/core';
import {User} from '../../models/User';
import {UserService} from '../../user.service';
import {HttpService} from '../../http.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-reset-password',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

    user: User;
    passwords: any;

    constructor(private userService: UserService, private http: HttpService, private router: Router) {
    }

    ngOnInit(): void {
        this.user = this.userService.getUser();
        if (!this.user.usingTemporaryPassword) {
            this.router.navigate(['/']);
        }
        this.passwords = {validation: {}};
    }

    resetPassword(): void {
        this.user.newPassword = this.passwords.newPassword;
        this.http.post('users/' + this.user.email + '/password', this.user).subscribe((result) => {
            this.userService.setUser(result);
            this.user = result;
            this.router.navigate(['/login']);
        });
    }

    checkPassword(): void {
        this.passwords.validation.validLength = this.passwords.newPassword.length >= 8;
        this.passwords.validation.containsNumber = /\d/.test(this.passwords.newPassword);
        this.passwords.validation.containsLetter = /[a-zA-Z]/.test(this.passwords.newPassword);
        this.passwords.validation.containsSpace = /\s+/.test(this.passwords.newPassword);

        this.passwords.validation.match = (this.passwords.newPassword === this.passwords.newPasswordRepeat
            && this.passwords.newPassword !== undefined);

        this.passwords.validation.valid = (this.passwords.validation.validLength && this.passwords.validation.containsNumber &&
            this.passwords.validation.containsLetter && !this.passwords.validation.containsSpace);
    }

    validateCurrentPassword(): void {
        this.passwords.validation.invalidExistingPassword = false;
        this.passwords.validation.validExistingPassword = false;

        if (!this.user.password) {
            return;
        }

        this.http.post('accesstoken', this.user).subscribe((result) => {
            console.log(result);
            if (result && result.sessionId && result.userId) {
                this.user.sessionId = result.sessionId;
                this.passwords.validation.invalidExistingPassword = false;

                // indicates that the existing password was validated. nothing else should be inferred
                this.passwords.validation.validExistingPassword = true;
            } else {
                this.passwords.validation.invalidExistingPassword = true;
            }
        }, (err) => {
            if (err.status === 401) {
                this.passwords.validation.invalidExistingPassword = true;
            }
            //   else
            //     $rootScope.error("Server error validating current password");
            // });
        });
    }
}

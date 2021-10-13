import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpService} from '../../../http.service';
import {User} from '../../../models/User';
import {UserService} from '../../../user.service';

@Component({
    selector: 'app-user-info',
    templateUrl: './user-info.component.html',
    styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {

    user: User;
    mode: string;
    updatingUser: boolean;
    passwords: {
        validLength?: boolean, containsNumber?: boolean, match?: boolean, valid?: boolean
        containsString?: boolean, containsLetter?: boolean, containsSpace?: boolean
    };
    passwordCheck: {
        newPassword?: string, newPasswordRepeat?: string, invalidExistingPassword?: boolean,
        validExistingPassword?: boolean
    };

    constructor(private route: ActivatedRoute, private http: HttpService, private userService: UserService) {
        this.passwordCheck = {};
        this.passwords = {};
    }

    ngOnInit(): void {
        this.route.parent.data
            .subscribe((data) => {
                this.user = data.user;
            });
    }

    resetPassword(): void {
        this.http.post('users/' + this.user.email + '/password', this.user).subscribe((result) => {
            // todo
            // $cookies.remove("user");
            // $cookies.putObject("user", result);
            // this.user = result;
            // $rootScope.user = result;
            // $location.path("/");
        });
    }

    checkPassword(): void {
        this.passwords = {};

        this.passwords.validLength = this.passwordCheck.newPassword.length >= 8;
        this.passwords.containsNumber = /\d/.test(this.passwordCheck.newPassword);
        this.passwords.containsLetter = /[a-zA-Z]/.test(this.passwordCheck.newPassword);
        this.passwords.containsSpace = /\s+/.test(this.passwordCheck.newPassword);

        this.passwords.match = (this.passwordCheck.newPassword === this.passwordCheck.newPasswordRepeat
            && this.passwordCheck.newPassword !== undefined);

        this.passwords.valid = (this.passwords.validLength && this.passwords.containsNumber &&
            this.passwords.containsLetter && !this.passwords.containsSpace);
    }

    validateCurrentPassword(): void {
        this.passwordCheck.invalidExistingPassword = false;
        this.passwordCheck.validExistingPassword = false;

        if (!this.user.password) {
            return;
        }

        this.http.post('accesstoken', this.user).subscribe((result) => {

            if (result && result.sessionId && result.email) {
                this.user.sessionId = result.sessionId;
                this.passwordCheck.invalidExistingPassword = false;

                // indicates that the existing password was validated. nothing else should be inferred
                this.passwordCheck.validExistingPassword = true;
            } else {
                this.passwordCheck.invalidExistingPassword = true;
            }
        }, (err) => {
            if (err.status === 401) {
                this.passwordCheck.invalidExistingPassword = true;
            }
            // else
            //     $rootScope.error("Server error validating current password");
        });
    }

    updateUser(): void {
        if (!this.user) {
            return;
        }

        this.updatingUser = true;
        this.http.put('users/' + this.user.id, this.user).subscribe((result) => {
            this.updatingUser = false;
        });
    }
}

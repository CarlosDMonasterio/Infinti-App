import {Component, OnInit} from '@angular/core';
import {HttpService} from '../../../http.service';
import {User} from '../../../models/User';
import {UserService} from '../../../user.service';

@Component({
    selector: 'app-user-edit',
    templateUrl: './user-edit.component.html',
    styleUrls: ['./user-edit.component.css']
})

export class UserEditComponent implements OnInit {
    loggedInUser: User;
    storedUser: User;

    updating = false;
    update = false;
    editable = false;
    updateSuccess = false;
    updateError = false;

    firstNameEmpty = false;
    lastNameEmpty = false;
    phoneEmpty = false;

    constructor(private http: HttpService, private userService: UserService) {
    }

    ngOnInit(): void {
        //Session credentials
        this.loggedInUser = this.userService.getUser();

        //Database Credentials
        this.http
            .get('users/'+ this.loggedInUser.id).subscribe((result: User) => {
                this.storedUser = result;
            },
        );
    }

    valueChange(value) {
        //Enables Update Button
        this.update = true;

        //Resets confirmation divs in case of consecutive updates.
        this.updateSuccess = false;
        this.updateError = false;
    }

    updateUserInfo(): void {
        this.updating = true;

        //field validation
        while (!this.loggedInUser.firstName || !this.loggedInUser.lastName || !this.loggedInUser.phone){
            if(!this.loggedInUser.firstName){this.firstNameEmpty = true;} else {this.firstNameEmpty = false;}
            if(!this.loggedInUser.lastName){this.lastNameEmpty = true;} else {this.lastNameEmpty = false;}
            if(!this.loggedInUser.phone){this.phoneEmpty = true;} else {this.phoneEmpty = false;}
            this.updating = false;
            return;
        };

        //Clear any empty field error after full validation
        this.firstNameEmpty = false;
        this.lastNameEmpty = false;
        this.phoneEmpty = false;

        //update Session
        this.loggedInUser = this.userService.getUser();
        this.userService.setUser(this.loggedInUser);

        //Update Database
        this.http
            //TODO - Phone number is not saving / Role is not saving / Need to refresh userService with new User Credentials
            .put("users/" + this.loggedInUser.id, this.loggedInUser).subscribe((result: User) => {
                this.updating = false;
                this.update = false;
                this.updateSuccess = true;
            }, error => {
                this.updating = false;
                this.updateError = true;
                this.updateError = true;
            }
        )
    }
}
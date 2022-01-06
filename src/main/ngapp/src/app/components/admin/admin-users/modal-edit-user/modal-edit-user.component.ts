import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {NgbModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap";
import {Router, NavigationEnd,ActivatedRoute} from '@angular/router';

import {User} from "../../../../models/User";
import {Result} from "../../../../models/Result";
import {HttpService} from "../../../../http.service";


@Component({
    selector: 'app-modal-edit-user',
    templateUrl: './modal-edit-user.component.html',
    styleUrls: ['./modal-edit-user.component.css']
})

export class ModalEditUserComponent implements OnInit {
    @Input() userid: number;
    user: User;
    updatedInfo: User;

    reload = false;
    update = false;
    processing = false;
    updateSuccess = false;
    updateError = false;

    firstNameEmpty = false;
    lastNameEmpty = false;
    emailEmpty = false;
    phoneEmpty = false;
    roleEmpty = false;

    constructor(public activeModal: NgbActiveModal, private http: HttpService ) {
    }

    ngOnInit(): void {
        this.getUserInfo();
    }

    getUserInfo(): void {
        this.http
            .get('users/'+ this.userid).subscribe((result: User) => {
                this.user = result;
            },
        );
    };

    valueChange(value) {
        this.update = true; //Displays Update Button
    }

    closeUpdateWindow() {
        if(this.reload == false){
            this.activeModal.close();
        } else {
            window.location.reload();
        }
    }

    updateUserInfo(): void {
        if (this.update == false) {
            this.activeModal.close();
        } else {
            this.reload = true; //Flag to reload user accounts page
            this.processing = true;  //Displays spinner

           //field validation
            while (!this.user.firstName || !this.user.lastName || !this.user.email || !this.user.phone || !this.user.description){
                if(!this.user.firstName){this.firstNameEmpty = true;} else {this.firstNameEmpty = false;}
                if(!this.user.lastName){this.lastNameEmpty = true;} else {this.lastNameEmpty = false;}
                if(!this.user.email){this.emailEmpty = true;} else {this.emailEmpty = false;}
                if(!this.user.phone){this.phoneEmpty = true;} else {this.phoneEmpty = false;}
                if(!this.user.description){this.roleEmpty = true;} else {this.roleEmpty = false;}
                this.processing = false;
                return;
            }

            //Clear any empty field error after full validation
            this.firstNameEmpty = false;
            this.lastNameEmpty = false;
            this.emailEmpty = false;
            this.phoneEmpty = false;
            this.roleEmpty = false;

            //updates to database
            this.http
                //TODO - updateSuccess div is not showing / Phone number is not saving / Role is not saving
                .put("users/" + this.userid, this.user, null).subscribe((result: User) => {
                    this.processing = false;
                    this.updateSuccess = true;
                    this.update = false; //flag to hide update button
                }, error => {
                    this.processing = false;
                    this.updateError = true;
                }
            );
        };
    };
}
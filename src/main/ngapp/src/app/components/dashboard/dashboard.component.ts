import {Component, OnInit} from '@angular/core';
import {HttpService} from '../../http.service';
import {UserService} from '../../user.service';
import {Router} from "@angular/router";
import {User} from "../../models/User";
import {NgbModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap";
import {RegisterPatientComponent} from "./register-patient/register-patient.component";
import {LookupPatientComponent} from "./lookup-patient/lookup-patient.component";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

    currentUser: User = undefined;
    data: any[];

    constructor(private http: HttpService, private user: UserService, private router: Router, private modal: NgbModal) {
    }

    ngOnInit(): void {

        // verify that current user session id is valid. if invalid, it will redirect user to the login page
        this.http.get('accesstoken').subscribe(result => {
        }, error => {
            this.user.clearUser();
        });
        this.currentUser = this.user.getUser(true);
    }

    goTo(link: string): void {
        this.router.navigate(['/' + link]);
    }

    registerPatient(): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false, size: 'lg'};
        const modalRef = this.modal.open(RegisterPatientComponent, options);
        modalRef.result.then(() => {
        });
    }

    lookupPatient(): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false, size: 'md'};
        const modalRef = this.modal.open(LookupPatientComponent, options);
        modalRef.result.then(() => {
        });
    }
}

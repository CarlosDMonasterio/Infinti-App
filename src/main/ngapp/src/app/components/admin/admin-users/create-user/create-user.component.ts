import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpService} from "../../../../http.service";
import {User} from "../../../../models/User";

@Component({
    selector: 'app-create-user',
    templateUrl: './create-user.component.html',
    styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

    user: User;
    emailInvalid: boolean;

    constructor(public activeModal: NgbActiveModal, private http: HttpService) {
        this.user = new User();
    }

    ngOnInit(): void {
    }

    createNewUser(): void {
        this.http.post('users?notify=true', this.user).subscribe((result: User) => {
            this.activeModal.close();
        }, error => {
            console.error(error);
        })
    }
}

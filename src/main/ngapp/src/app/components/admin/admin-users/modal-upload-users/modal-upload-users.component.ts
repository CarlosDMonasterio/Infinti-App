import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpService} from "../../../../http.service";
import {HttpClient, HttpHeaders, HttpParams, HttpRequest} from "@angular/common/http";
import {UserService} from "../../../../user.service";
import {environment} from "../../../../../environments/environment";

@Component({
    selector: 'app-modal-upload-users',
    templateUrl: './modal-upload-users.component.html',
    styleUrls: ['./modal-upload-users.component.css']
})
export class ModalUploadUsersComponent implements OnInit {

    fileName: string;
    emailNotification: boolean;

    constructor(public activeModal: NgbActiveModal, private http: HttpService, private user: UserService, private httpClient: HttpClient) {
    }

    ngOnInit(): void {
    }

    onFileSelected(event): void {
        const file: File = event.target.files[0];

        if (file) {
            this.fileName = file.name;
            const formData = new FormData();
            formData.append("file", file);

            const params = new HttpParams();
            const headers = new HttpHeaders({'X-IH-Authentication-SessionId': this.user.getUser().sessionId})
            const options = {
                headers,
                params,
                reportProgress: true
            }

            const request = new HttpRequest('POST', environment.apiUrl + '/users/import?notify=' + this.emailNotification, formData, options);
            this.httpClient.request(request).subscribe(event => {
                console.log(event);
            })

            // const upload$ = this.http.post("users/import", formData);
            // upload$.subscribe();
        }
    }
}

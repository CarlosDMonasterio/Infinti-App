import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpService} from "../../../../http.service";
import {UserService} from "../../../../user.service";
import {HttpClient, HttpHeaders, HttpParams, HttpRequest} from "@angular/common/http";
import {environment} from "../../../../../environments/environment";
import {District} from "../../../../models/district";

@Component({
    selector: 'app-modal-upload-schools',
    templateUrl: './modal-upload-schools.component.html',
    styleUrls: ['./modal-upload-schools.component.css']
})
export class ModalUploadSchoolsComponent implements OnInit {

    fileName: string;
    @Input() district: District;

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

            const request = new HttpRequest('POST', environment.apiUrl + '/districts/' + this.district.id + '/schools/import', formData, options);
            this.httpClient.request(request).subscribe(event => {
                console.log(event);
            })

            // const upload$ = this.http.post("users/import", formData);
            // upload$.subscribe();
        }
    }

}

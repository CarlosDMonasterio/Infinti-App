import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpService} from "../../../http.service";
import {Patient} from "../../../models/patient";
import {Test} from "../../../models/test";

@Component({
    selector: 'app-lookup-patient',
    templateUrl: './lookup-patient.component.html',
    styleUrls: ['./lookup-patient.component.css']
})
export class LookupPatientComponent implements OnInit {

    identifier: string;
    searching: boolean;
    searchFailed: boolean;
    foundUser: Patient;
    userConfirmed: boolean;
    specimenOptions: string[];
    test: Test;

    constructor(public activeModal: NgbActiveModal, private http: HttpService) {
        this.specimenOptions = ["Saliva", "Nasal Cavity", "Oropharyngeal", "Nasopharyngeal"]
        this.test = new Test();
    }

    ngOnInit(): void {
    }

    searchForPatient(): void {
        this.searching = true;
        this.searchFailed = false;

        this.http.get('patients/' + this.identifier).subscribe((result: Patient) => {
            this.searching = false;

            if (!result) {
                this.searchFailed = true;
                return;
            }
            this.foundUser = result;
        }, error => {
            this.searching = false;
            this.searchFailed = true;
        });
    }

    confirmPatient(): void {
        this.userConfirmed = true;
    }

    // create a new test for patient
    submitSpecimen(): void {
        this.http.post('patients/' + this.foundUser.uuid + '/tests', this.test).subscribe(result => {
            this.activeModal.close();
        });
    }
}

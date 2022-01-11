import {Component, OnInit} from '@angular/core';
import {NgbActiveModal, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {Patient} from '../../../models/patient';
import {HttpService} from '../../../http.service';

@Component({
    selector: 'app-register-patient',
    templateUrl: './register-patient.component.html',
    styleUrls: ['./register-patient.component.css']
})
export class RegisterPatientComponent implements OnInit {

    newPatient: Patient;
    dateStruct: NgbDateStruct;
    minDate: NgbDateStruct;
    maxDate: NgbDateStruct;

    constructor(public activeModal: NgbActiveModal, private http: HttpService) {
        this.newPatient = new Patient();
        this.minDate = {year: 1920, month: 1, day: 1};
        const today = new Date();
        this.maxDate = {year: today.getFullYear(), month: today.getMonth() + 1, day: today.getDate()};
    }

    ngOnInit(): void {
    }

    saveNewPatient(): void {
        this.newPatient.birthDate = String(this.dateStruct.month + '/' + this.dateStruct.day + '/' + this.dateStruct.year);
        this.http.post('patients', this.newPatient).subscribe(result => {
            console.log(result);
            if (!result) {
                return;
            }

            this.activeModal.close(result);
        });
    }
}

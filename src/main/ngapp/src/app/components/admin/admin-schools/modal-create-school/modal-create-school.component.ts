import {Component, Input, OnInit} from '@angular/core';
import {HttpService} from "../../../../http.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {School} from "../../../../models/school";
import {District} from "../../../../models/district";

@Component({
    selector: 'app-modal-create-school',
    templateUrl: './modal-create-school.component.html',
    styleUrls: ['./modal-create-school.component.css']
})
export class ModalCreateSchoolComponent implements OnInit {

    newSchool: School;
    @Input() district: District;

    constructor(private http: HttpService, public activeModal: NgbActiveModal) {
        this.newSchool = new School();
    }

    ngOnInit(): void {
    }

    saveNewSchool(): void {
        this.newSchool.address = this.newSchool.address + ', ' + this.newSchool.city + ', ' + this.newSchool.state + ', ' + this.newSchool.zip;

        this.http.post('schools', this.newSchool).subscribe((result: School) => {
            this.activeModal.close(result);
        });
    }

}

import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpService} from "../../../../http.service";
import {District} from "../../../../models/district";

@Component({
    selector: 'app-modal-create-district',
    templateUrl: './modal-create-district.component.html',
    styleUrls: ['./modal-create-district.component.css']
})

export class ModalCreateDistrictComponent implements OnInit {

    newDistrict: District;

    constructor(private http: HttpService, public activeModal: NgbActiveModal) {
        this.newDistrict = new District();
    }

    ngOnInit(): void {
    }

    saveNewDistrict(): void {
        this.http.post('districts', this.newDistrict).subscribe(result => {
            this.activeModal.close(result);
        })
    }
}

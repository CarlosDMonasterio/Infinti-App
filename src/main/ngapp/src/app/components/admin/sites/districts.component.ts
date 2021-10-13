import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../http.service";
import {NgbModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap";
import {ModalCreateDistrictComponent} from "./modal-create-district/modal-create-district.component";
import {Result} from "../../../models/Result";
import {District} from "../../../models/district";
import {School} from "../../../models/school";
import {Paging} from "../../../models/Paging";
import {ModalCreateSchoolComponent} from "../admin-schools/modal-create-school/modal-create-school.component";
import {ModalUploadSchoolsComponent} from "./modal-upload-schools/modal-upload-schools.component";

@Component({
    selector: 'app-districts',
    templateUrl: './districts.component.html',
    styleUrls: ['./districts.component.css']
})
export class DistrictsComponent implements OnInit {

    districts: District[];
    paging: Paging;
    schools: School[];
    selectedDistrict: District;

    constructor(private http: HttpService, private modalService: NgbModal) {
        this.districts = [];
        this.paging = new Paging();
    }

    ngOnInit(): void {
        this.http.get('districts').subscribe((result: Result<District>) => {
            this.districts = result.requested;
        });
    }

    selectDistrict(district: District): void {
        this.selectedDistrict = district;
        this.getSelectedDistrictSchools();
    }

    addNewDistrict(): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false};
        const modalRef = this.modalService.open(ModalCreateDistrictComponent, options);
        modalRef.result.then((result: District) => {
            this.http.get('districts').subscribe((result: Result<District>) => {
                this.districts = result.requested;
            });
        });
    }

    addNewSchool(): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false, size: 'lg'};
        const modalRef = this.modalService.open(ModalCreateSchoolComponent, options);
        modalRef.componentInstance.district = this.selectedDistrict;
        modalRef.result.then(() => {
        });
    }

    importSchools(): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false};
        const modalRef = this.modalService.open(ModalUploadSchoolsComponent, options);
        modalRef.componentInstance.district = this.selectedDistrict;
    }

    getSelectedDistrictSchools(): void {

        if (!this.selectedDistrict || !this.selectedDistrict.id)
            return;

        console.log(this.selectedDistrict);

        this.http.get('districts/' + this.selectedDistrict.id + '/schools', this.paging).subscribe((result: Result<School>) => {
            this.paging.available = result.available;
            this.schools = result.requested;
        });
    }
}

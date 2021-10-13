import {Component, OnInit} from '@angular/core';
import {School} from "../../../models/school";
import {NgbModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap";
import {Result} from "../../../models/Result";
import {HttpService} from "../../../http.service";
import {ModalCreateSchoolComponent} from "./modal-create-school/modal-create-school.component";
import {Paging} from "../../../models/Paging";

@Component({
    selector: 'app-admin-schools',
    templateUrl: './admin-schools.component.html',
    styleUrls: ['./admin-schools.component.css']
})
export class AdminSchoolsComponent implements OnInit {

    schools: School[];
    schoolPaging: Paging;

    constructor(private http: HttpService, private modalService: NgbModal) {
        this.schoolPaging = new Paging();
    }

    ngOnInit(): void {
        this.getSchools();
    }

    getSchools(): void {
        this.http.get('schools', this.schoolPaging).subscribe((result: Result<School>) => {
            if (!result)
                return;

            this.schools = result.requested;
            this.schoolPaging.available = result.available;
        });
    }

    addNewSchool(): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false, size: 'lg'};
        const modalRef = this.modalService.open(ModalCreateSchoolComponent, options);
        modalRef.result.then(() => {
            this.getSchools();
        });
    }
}

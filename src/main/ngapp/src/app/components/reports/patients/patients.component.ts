import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../http.service";
import {Patient} from "../../../models/patient";
import {Paging} from "../../../models/Paging";
import {Result} from "../../../models/Result";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-patients',
    templateUrl: './patients.component.html',
    styleUrls: ['./patients.component.css']
})
export class PatientsComponent implements OnInit {

    patients: Patient[];
    paging: Paging;
    patientId: string;

    constructor(private http: HttpService, private route: ActivatedRoute, private router: Router) {
        this.paging = new Paging();
    }

    ngOnInit(): void {
        //
        // this.route.data.subscribe((data) => {
        //
        // })

        const param = this.route.snapshot.params['patientId'];
        const urlSplit: string[] = this.router.url.split('/');
        if (urlSplit.length === 4) {
            // get last item for url
            this.patientId = urlSplit[3];
            console.log(this.patientId);
        }

        this.http.get('patients', this.paging).subscribe((result: Result<Patient>) => {
            this.paging.available = result.available;
            this.patients = result.requested;
        });
    }
}

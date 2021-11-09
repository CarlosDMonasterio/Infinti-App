import {Component, OnInit} from '@angular/core';
import {ProfileService} from "../../../profile/profile.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpService} from "../../../../http.service";
import {UserService} from "../../../../user.service";
import {Patient} from "../../../../models/patient";

@Component({
    selector: 'app-patient-details',
    templateUrl: './patient-details.component.html',
    styleUrls: ['./patient-details.component.css']
})
export class PatientDetailsComponent implements OnInit {

    patient: Patient;

    constructor(private profile: ProfileService, private route: ActivatedRoute, private http: HttpService,
                private userService: UserService, private router: Router) {
    }

    ngOnInit(): void {
        this.route.data.subscribe((data) => {
            this.patient = data.patient;
        });
    }

}

import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {HttpService} from "../../../../http.service";

@Injectable()
export class PatientDetailsResolver implements Resolve<any> {

    constructor(private http: HttpService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
        return this.http.get('patients/' + route.params.patientId);
    }
}
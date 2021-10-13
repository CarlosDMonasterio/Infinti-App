import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {HttpService} from "../../http.service";
import {Observable} from "rxjs";

@Injectable()
export class ProfileDetailsResolver implements Resolve<any> {

    constructor(private http: HttpService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return this.http.get('users/' + route.params.id);
    }
}

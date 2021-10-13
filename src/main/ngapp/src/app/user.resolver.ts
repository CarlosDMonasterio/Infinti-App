import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Observable, of} from 'rxjs';
import {HttpService} from "./http.service";

@Injectable()
export class UserResolver implements Resolve<any> {

    constructor(private http: HttpService) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        this.http.get('accesstoken').subscribe(result => {
            console.log(result);
            return of([]);
        }, error => {
            console.log(error);
            return of([]);
        });

        return of([]);
    }
}

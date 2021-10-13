import {Injectable} from '@angular/core';
import {HttpService} from "../http.service";
import {of} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class SchoolService {

    constructor(private http: HttpService) {
    }

    searchDistrictSchools(term: string, id: number) {
        if (term === '')
            return of([]);

        return this.http.get('districts/' + id + '/schools/filter', {filter: term, limit: 8})
    }
}

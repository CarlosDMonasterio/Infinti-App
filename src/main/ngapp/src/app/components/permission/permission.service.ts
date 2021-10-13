import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpService} from "../../http.service";
import {Group} from "../../models/Group";
import {Permission} from "../../models/Permission";

@Injectable({
    providedIn: 'root'
})
export class PermissionService {

    constructor(private http: HttpService) {
    }

    users(term): Observable<any> {
        return this.http.get('users/autocomplete', {val: term});
    }

    groups(term): Observable<Group> {
        return this.http.get('groups/autocomplete', {token: term, limit: 5});
    }

    add(isProject, id, permission: Permission): Observable<Permission> {
        if (isProject)
            return this.http.post('projects/' + id + '/permissions', permission);
        return this.http.post('designs/' + id + '/permissions', permission);
    }

    remove(isProject, id, permissionId): Observable<boolean> {
        if (isProject)
            return this.http.delete('projects/' + id + '/permissions/' + permissionId);
        return this.http.delete('designs/' + id + '/permissions/' + permissionId);
    }

    get(isProject, id): Observable<any> {
        if (isProject)
            return this.http.get('projects/' + id + '/permissions');
        return this.http.get('designs/' + id + '/permissions');
    }
}

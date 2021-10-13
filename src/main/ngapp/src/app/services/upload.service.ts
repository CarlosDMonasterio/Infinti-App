import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserService} from '../user.service';

@Injectable({
    providedIn: 'root'
})
export class UploadService {

    constructor(private http: HttpClient, private user: UserService) {
    }

    // file from event.target.files[0]
    uploadFile(url: string, list: FileList): Observable<HttpEvent<any>> {

        const formData = new FormData();
        for (let i = 0; i < list.length; i += 1) {
            const file: File = list.item(i);
            if (!file) {
                continue;
            }
            formData.append('file', file);
        }

        const params = new HttpParams();
        const headers = new HttpHeaders({'X-IH-Authentication-SessionId': this.user.getUser().sessionId});

        const options = {
            headers,
            params,
            reportProgress: true,
        };

        const req = new HttpRequest('POST', url, formData, options);
        return this.http.request(req);
    }
}

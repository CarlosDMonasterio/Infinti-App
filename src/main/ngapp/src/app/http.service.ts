import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {UserService} from './user.service';
import {Router} from '@angular/router';
import {environment} from '../environments/environment';

@Injectable({
    providedIn: 'root'
})

export class HttpService {

    private readonly apiUrl: string;

    private httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'}),
        params: new HttpParams()
    };

    constructor(private http: HttpClient, private userService: UserService, private router: Router) {
        this.apiUrl = environment.apiUrl;

        // todo : make a call to the server to validate the session id
    }

    private setOptions(options): void {
        this.httpOptions.params = new HttpParams();
        if (!options) {
            return;
        }

        for (const prop in options) {
            if (!options.hasOwnProperty(prop)) {
                continue;
            }

            this.httpOptions.params = this.httpOptions.params.append(prop, options[prop]);
        }
    }

    private setHeaders(redirect?: boolean): void {
        if (this.userService.getUser(redirect)) {
            const sid = this.userService.getUser().sessionId;
            this.httpOptions.headers = new HttpHeaders({
                'Content-Type': 'application/json',
                'X-IH-Authentication-SessionId': sid
            });
        }
    }

    get<T>(api: string, options?, redirect?): Observable<T> {
        this.setOptions(options);
        this.setHeaders(redirect);
        const url = `${this.apiUrl}/${api}`;
        return this.http.get<T>(url, this.httpOptions)
            .pipe(
                catchError(this.handleError<T>())
            );
    }

    post<T>(api: string, payload: T, options?, redirect = true): Observable<any> {
        this.setOptions(options);
        this.setHeaders(redirect);
        const url = `${this.apiUrl}/${api}`;
        return this.http.post<T>(url, payload, this.httpOptions);
    }

    delete<T>(api: string): Observable<any> {
        this.setOptions(undefined);
        this.setHeaders(true);
        const url = `${this.apiUrl}/${api}`;
        return this.http.delete(url, this.httpOptions);
    }

    put<T>(api: string, payload: T, options?): Observable<any> {
        this.setOptions(options);
        this.setHeaders(true);
        const url = `${this.apiUrl}/${api}`;
        return this.http.put(url, payload, this.httpOptions);
    }

    private handleError<T>(result?) {
        return (error: any): Observable<T> => {

            // TODO: send the error to remote logging infrastructure
            if (error.status === 401) {
                this.router.navigate(['/login']);
                return;
            }

            console.error(error); // log to console instead

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }
}

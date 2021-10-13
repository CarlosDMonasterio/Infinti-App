import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {UserService} from '../../user.service';

@Injectable({
    providedIn: 'root'
})
export class AdminGuardGuard implements CanActivate {
    constructor(private user: UserService, private router: Router) {
    }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {


        if (this.user.isAdmin()) {
            return true;
        }

        this.router.navigate(['/']);
        return false;
    }
}

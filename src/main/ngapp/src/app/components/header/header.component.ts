import {Component, OnInit} from '@angular/core';
import {User} from '../../models/User';
import {UserService} from '../../user.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

    loggedInUser: User;

    constructor(private userService: UserService, private router: Router) {
    }

    ngOnInit(): void {
        this.loggedInUser = this.userService.getUser();
    }

    logUserOut(): void {
        this.userService.clearUser();
        this.router.navigate((['/login']));
    }

    goHome(): void {
        this.router.navigate(['/']);
    }

    isAdmin(): boolean {
        return this.loggedInUser.isAdministrator;
    }
}

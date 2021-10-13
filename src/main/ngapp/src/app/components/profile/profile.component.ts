import {Component, OnInit} from '@angular/core';
import {ProfileService} from "./profile.service";
import {User} from "../../models/User";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpService} from "../../http.service";
import {UserService} from "../../user.service";
import {MenuItem} from "../admin/MenuItem";

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

    userMenuOptions: MenuItem[];
    user: User;
    loggedInUser: User;     // the menu is displayed only if viewing own profile
    active: string;

    constructor(private profile: ProfileService, private route: ActivatedRoute, private http: HttpService,
                private userService: UserService, private router: Router) {
    }

    ngOnInit(): void {
        this.userMenuOptions = this.profile.getMenuOptions();
        this.route.data.subscribe((data) => {
            this.user = data.user;
        });
        this.loggedInUser = this.userService.getUser();
        const urlSplit: string[] = this.router.url.split('/');
        if (urlSplit && urlSplit.length)
            this.active = urlSplit[urlSplit.length - 1];
    }

    showSelection(menuItem: MenuItem): void {
        console.log(menuItem);
        this.active = menuItem.id;

        // re-route
        this.router.navigate(['/user/' + this.loggedInUser.id + '/' + menuItem.id]);
    };

    updateUserAccount(): void {
        if (!this.user)
            return;

        this.http.put("users/" + this.user.id, this.user).subscribe((result) => {
        });
    };
}

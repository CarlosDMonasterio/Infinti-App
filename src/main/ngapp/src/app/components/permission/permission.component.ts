import {Component, Input, OnInit} from '@angular/core';
import {Permission} from '../../models/Permission';
import {Observable, of} from 'rxjs';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {User} from '../../models/User';
import {Group} from "../../models/Group";
import {PermissionService} from "./permission.service";

@Component({
    selector: 'app-permission',
    templateUrl: './permission.component.html',
    styleUrls: ['./permission.component.css']
})
export class PermissionComponent implements OnInit {

    @Input() isWrite: boolean;
    @Input() permissions: Permission[];

    title: string;
    searching: boolean;
    searchFailed: boolean;
    model: User;
    add: boolean;
    type: string;
    isProject: boolean;

    constructor(private service: PermissionService) {
        this.searching = false;
        this.searchFailed = false;
        this.add = false;
        this.type = 'user';
    }

    ngOnInit(): void {
        if (this.isWrite) {
            this.title = 'Collaborators';
        } else {
            this.title = 'Observers';
        }

        if (!this.permissions) {
            this.permissions = [];
        }
    }

    searchUsers = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(200),
            distinctUntilChanged(),
            // tap(() => this.searching = true),
            switchMap(term => term.length < 2 ? of([]) :
                this.service.users(term)
                    .pipe(
                        // tap(() => this.searchFailed = false),
                        catchError(() => {
                            this.searchFailed = true;
                            return of([]);
                        }))
            ),
            tap(() => this.searching = false)
        );

    searchGroups = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(200),
            distinctUntilChanged(),
            // tap(() => this.searching = true),
            switchMap(term => term.length < 2 ? of([]) :
                this.service.groups(term)
                    .pipe(
                        // tap(() => this.searchFailed = false),
                        catchError(() => {
                            // this.searchFailed = true;
                            return of([]);
                        }))
            ),
            tap(() => this.searching = false)
        );

    formatter(object: any): string {
        console.log(object);
        return '';
    }

    groupFormatter(group: Group): string {
        return group.display;
    }

    select(item, isUser = true): void {
        const perm = new Permission();
        if (isUser)
            perm.account = item.item;
        else
            perm.group = item.item;
        perm.canWrite = this.isWrite;

        this.addPermission(perm);

        // this.onAdd.emit(perm);
        this.model = undefined;
    }

    remove(item): void {
        this.removePermission(item);
    }

    private addPermission(permission: Permission): void {
    }

    private removePermission(permission: Permission): void {
    }
}

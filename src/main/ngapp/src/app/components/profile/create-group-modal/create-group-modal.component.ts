import {Component, Input, OnInit} from '@angular/core';
import {Group} from "../../../models/Group";
import {HttpService} from "../../../http.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {User} from "../../../models/User";

@Component({
    selector: 'app-create-group-modal',
    templateUrl: './create-group-modal.component.html',
    styleUrls: ['./create-group-modal.component.css']
})
export class CreateGroupModalComponent implements OnInit {

    @Input() group: Group;
    newGroup: Group;
    edit: { error: boolean, name: boolean, description: boolean };
    newUserName: string;
    searching: boolean;
    searchFailed: boolean;
    isEdit: boolean;    // whether we are editing an existing group (true) or creating a new one (false)

    constructor(private http: HttpService, public activeModal: NgbActiveModal) {
        this.edit = {error: false, name: false, description: false}
    }

    ngOnInit(): void {
        if (this.group && this.group.memberCount) {
            // retrieve the group members
            this.http.get("groups/" + this.group.id + "/members").subscribe((result: any) => {
                if (result) {
                    this.newGroup.members = [];
                    for (let i = 0; i < result.length; i += 1)
                        this.newGroup.members.push(result[i])
                }
            });
        }

        this.newGroup = this.group ? Object.assign({}, this.group) : {type: 'PRIVATE'};
        this.isEdit = (this.newGroup.id !== undefined && this.newGroup.id > 0);
    }

    success(result): void {
        if (!result) {
            this.edit.error = true;
            return;
        }
        this.edit.name = false;
        this.edit.description = false;
    };

    // only for creating a new group not updating
    saveNewUserGroup(): void {
        this.edit.error = false;

        if (this.newGroup.id) {
            this.http.put("groups/" + this.newGroup.id, this.newGroup).subscribe((result) => {
                this.success(result);
            });
        } else {
            this.http.post("groups", this.newGroup).subscribe((result) => {
                this.success(result);
                this.activeModal.close(true);
            }, error => {
                console.error(error);
                this.edit.error = true;
            })
        }
    };

    formatter(user: User): string {
        console.log(user);
        return '';
    }

    searchUsers = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term => term.length < 2 ? [] : this.http.get('users/autocomplete', {val: term})
                .pipe(
                    tap(() => this.searchFailed = false),
                    catchError(() => {
                        this.searchFailed = true;
                        return of([]);
                    }))
            ),
            tap(() => this.searching = false)
        );

    selectUser(item): void {
        const user: User = item.item;

        if (!this.newGroup.members)
            this.newGroup.members = [];

        // add immediately when adding to an existing group
        if (this.newGroup.id) {
            this.http.post("groups/" + this.newGroup.id + "/members", user).subscribe();
        }

        this.newGroup.members.push(user);

        // reset
        // this.newUserName = undefined;
        //this.newPublicGroup.type = 'ACCOUNT';
    };

    removeUserFromGroup(user): void {
        if (this.newGroup.id) {
            // remove it now
            this.http.delete("groups/" + this.newGroup.id + "/members/" + user.id).subscribe((result) => {
                if (!result)
                    return;

                let i = this.newGroup.members.indexOf(user);
                if (i !== -1)
                    this.newGroup.members.splice(i, 1);
            })
        } else {
            const index = this.newGroup.members.indexOf(user);
            if (index !== -1)
                this.newGroup.members.splice(index, 1);
        }
    };

    closeAlert(): void {
        this.edit.error = false;
    };
}

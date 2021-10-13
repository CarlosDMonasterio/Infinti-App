import {Component, OnInit} from '@angular/core';
import {Group} from "../../../models/Group";
import {HttpService} from "../../../http.service";
import {UserService} from "../../../user.service";
import {NgbModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap";
import {CreateGroupModalComponent} from "../create-group-modal/create-group-modal.component";
import {User} from "../../../models/User";

@Component({
    selector: 'app-user-groups',
    templateUrl: './user-groups.component.html',
    styleUrls: ['./user-groups.component.css']
})
export class UserGroupsComponent implements OnInit {

    params = {limit: 15, currentPage: 1, available: 0, maxSize: 10};
    loadingGroupsData = false;
    groups: Group[];
    user: User;
    confirmDeleteGroup?: boolean;
    confirmLeaveGroup?: boolean;

    constructor(private http: HttpService, private userService: UserService, private modalService: NgbModal) {
        this.getUserGroups();
        this.user = this.userService.getUser();
        this.confirmDeleteGroup = false;
        this.confirmLeaveGroup = false;
    }

    ngOnInit(): void {
    }

    getUserGroups(): void {
        this.loadingGroupsData = true;
        this.http.get("groups", this.params).subscribe((result: any) => {
            this.groups = result.data;
            this.params.available = result.resultCount;
            this.loadingGroupsData = false;
        });
    };

    pageCounts(currentPage, resultCount): string {
        let maxPageCount = 15;
        let pageNum = ((currentPage - 1) * maxPageCount) + 1;

        // number on this page
        let pageCount = (currentPage * maxPageCount) > resultCount ? resultCount : (currentPage * maxPageCount);
        return pageNum + " - " + (pageCount) + " of " + (resultCount);
    };

    deleteUsersGroup(group): void {
        this.http.delete("groups/" + group.id).subscribe((result) => {
            if (!result)
                return;
            let i = this.groups.indexOf(group);
            if (i !== -1) {
                this.groups.splice(i, 1);
                group.confirmDeleteGroup = false;
            }
        });
    };

    openCreateUserModal(existingGroup?: Group): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false, size: 'md'};
        const modalRef = this.modalService.open(CreateGroupModalComponent, options);
        modalRef.componentInstance.group = existingGroup;

        modalRef.result.then((result) => {
            this.getUserGroups();
        });
    };

    leaveGroup(group): void {
        if (group.id && this.user.id) {
            // remove it now

            this.http.delete("groups/" + group.id + "/members/" + this.user.id).subscribe((result) => {
                this.getUserGroups();
            })
        }
    };
}

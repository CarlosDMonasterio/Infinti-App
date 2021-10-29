import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../http.service";
import {User} from "../../../models/User";
import {Result} from "../../../models/Result";
import {NgbModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap";
import {CreateUserComponent} from "./create-user/create-user.component";
import {ModalUploadUsersComponent} from "./modal-upload-users/modal-upload-users.component";
import {Paging} from "../../../models/Paging";
import {ConfirmComponent} from "../../common/confirm/confirm.component";
import {Observable} from "rxjs";

@Component({
    selector: 'app-admin-users',
    templateUrl: './admin-users.component.html',
    styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {

    loadingAdminAccountsPage = false;
    users: Result<User>;
    paging: Paging;

    constructor(private modalService: NgbModal, private http: HttpService) {
        this.paging = new Paging();
        this.paging.sort = "lastName";
        this.paging.asc = true;
    }

    ngOnInit(): void {
        this.loadAccounts(1);
    }

    addNewUser(): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false};
        const modalRef = this.modalService.open(CreateUserComponent, options);
    }

    showUploadModal(): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false};
        const modalRef = this.modalService.open(ModalUploadUsersComponent, options);
    }

    loadAccounts(page: number): void {
        this.paging.start = ((page - 1) * this.paging.limit);
        this.loadingAdminAccountsPage = true;
        this.http.get('users', this.paging).subscribe((result: Result<User>) => {
            this.users = result;
            this.loadingAdminAccountsPage = false;
        },);
    };

    setSort(sort: string): void {
        if (this.paging.sort !== sort) {
            this.paging.sort = sort;
            this.paging.asc = false;
        } else {
            this.paging.asc = !this.paging.asc;
        }

        this.paging.currentPage = 1;
        this.loadAccounts(1);
    }

    // determine whether to add admin role or remove it
    setUserAccountType(user: User): void {
        let observable: Observable<any>;
        user.updatingType = true;

        if (user.isAdministrator) {
            // remove admin role
            observable = this.http.delete('users/' + user.id + '/roles/ADMINISTRATOR');
        } else {
            // add admin role
            observable = this.http.post('users/' + user.id + '/roles', 'ADMINISTRATOR');
        }

        observable.subscribe(result => {
            user.updatingType = false;
            user.isAdministrator = !user.isAdministrator;
        }, error => {
            user.updatingType = false;
        });
    };

    updateActiveStatus(user): void {
        user.updatingActiveStatus = true;

        if (user.disabled) {
            // enable
            this.http.put("users/" + user.id + "/active", {}).subscribe(result => {
                user.updatingActiveStatus = false;
                user.disabled = !user.disabled;
            }, error => {
                user.updatingActiveStatus = false;
            })
        } else {
            // disable
            this.http.delete("users/" + user.id + "/active").subscribe(result => {
                user.updatingActiveStatus = false;
                user.disabled = !user.disabled;
            }, error => {
                user.updatingActiveStatus = false;
            })
        }
    };

    confirmPassReset(user: User): void {
        const options: NgbModalOptions = {backdrop: 'static', keyboard: false};
        const modalRef = this.modalService.open(ConfirmComponent, options);
        modalRef.componentInstance.resourceName = "Reset Password?"
        modalRef.componentInstance.details = "Please confirm that you will like to reset the password for user '" + user.email + "'";
        modalRef.result.then(result => {
            if (!result || result == false)
                return;

            this.http.post('users/' + user.email + '/password', undefined).subscribe((result: User) => {

            });
        });
    }

    pageCounts(currentPage, resultCount): string {
        const maxPageCount = 15;
        const pageNum = ((currentPage - 1) * maxPageCount) + 1;

        // number on this page
        const pageCount = (currentPage * maxPageCount) > resultCount ? resultCount : (currentPage * maxPageCount);
        return pageNum + " - " + (pageCount) + " of " + (resultCount);
    };
}

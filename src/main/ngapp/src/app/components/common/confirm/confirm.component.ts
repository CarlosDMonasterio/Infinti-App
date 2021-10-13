import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-confirm',
    templateUrl: './confirm.component.html',
    styleUrls: ['./confirm.component.css']
})
export class ConfirmComponent implements OnInit {

    @Input() resourceName: string;
    @Input() details: string;

    constructor(public activeModal: NgbActiveModal) {
    }

    ngOnInit(): void {
    }

    actionConfirmed(): void {
        this.activeModal.close(true);
    }
}

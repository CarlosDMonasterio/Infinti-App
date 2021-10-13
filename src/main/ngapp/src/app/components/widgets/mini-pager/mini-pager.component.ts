import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
    selector: 'app-mini-pager',
    templateUrl: './mini-pager.component.html',
    styleUrls: ['./mini-pager.component.css']
})
export class MiniPagerComponent implements OnInit {

    totalSize: number;

    @Input() set available(available: number) {
        this.totalSize = available;
        this.calculatePages();
    }

    @Input() numberPerPage: number;
    @Output() nextPage: EventEmitter<any> = new EventEmitter<any>();

    params: { currentPage, numberOfPages };

    constructor() {
    }

    ngOnInit(): void {
        this.params = {
            numberOfPages: Math.ceil(this.totalSize / this.numberPerPage),
            currentPage: 1
        };
    }

    calculatePages(): void {
        this.params = {
            numberOfPages: Math.ceil(this.totalSize / this.numberPerPage),
            currentPage: 1
        };
    }

    page(isNext = true): void {
        // cannot go back when already at page 1
        if (this.params.currentPage === 1 && !isNext) {
            return;
        }

        if ((this.params.currentPage === this.params.numberOfPages) && isNext) {
            return;
        }

        this.params.currentPage = isNext ? this.params.currentPage + 1 : this.params.currentPage - 1;
        this.nextPage.emit({isNext, params: this.params});
    }
}

import { Component, OnInit } from '@angular/core';

import { AlertService } from '../services/index_services';

@Component({
    selector: 'app-alert',
    templateUrl: 'alert.component.html'
})

export class AlertComponent implements OnInit{
    message: any;

    constructor(private alertService: AlertService) { }

    ngOnInit(): any {
        this.alertService.getMessage().subscribe(message => { this.message = message; });
    }
}

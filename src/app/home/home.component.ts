import { Component, OnInit } from '@angular/core';
import { ResizedEvent } from 'angular-resize-event';
import { SettingsComponent } from '../settings/settings.component';

import { User } from '../models/index_models';
import { UserService } from '../services/index_services';

@Component({
    // moduleId: module.id,
    templateUrl: 'home.component.html',
    styleUrls: ['./styles_home.css']
})

export class HomeComponent implements OnInit {

    width: number;
    height: number;

    // currentUser: User;
    // users: User[] = [];

    constructor(private userService: UserService,
                private settings: SettingsComponent,
                ) {
        // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }

    ngOnInit(): any {
        // this.loadAllUsers();
        // this.settings.loadLenguageFavouriteJSONFileSettings();

    }

    // deleteUser(id: number): any {
    //     this.userService.delete(id).subscribe(() => { this.loadAllUsers(); });
    // }

    // private loadAllUsers(): any {
    //     this.userService.getAll().subscribe(users => { this.users = users; });
    // }



    onResized(event: ResizedEvent): void{
        this.width = event.newWidth;
        this.height = event.newHeight;
  }
}

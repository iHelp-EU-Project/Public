import { Component, OnInit } from '@angular/core';
import { ResizedEvent } from 'angular-resize-event';


@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {

  width: number;
  height: number;


  constructor() {
  }


  onResized(event: ResizedEvent): void {
    this.width = event.newWidth;
    this.height = event.newHeight;
  }



}

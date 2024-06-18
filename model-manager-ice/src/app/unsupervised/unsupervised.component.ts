import { Component, OnInit } from '@angular/core';
import { ResizedEvent } from 'angular-resize-event';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../services/LanguageApp';
import { TranslationService } from '../services/translation.service';


@Component({
  selector: 'app-unsupervised',
  templateUrl: './unsupervised.component.html',
  styleUrls: ['./unsupervised.component.css']
})
export class UnsupervisedComponent implements OnInit {

  width: number;
  height: number;

  constructor(private translate: TranslateService,
              private transLang: TranslationService) { }

  ngOnInit(): void {
  }

  onResized(event: ResizedEvent): void {
    this.width = event.newWidth;
    this.height = event.newHeight;
  }

}

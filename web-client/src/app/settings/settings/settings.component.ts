import { Component, OnInit } from '@angular/core';
import { InMemorySettingsService } from '../memorysettings.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor(public settings: InMemorySettingsService) { }

  ngOnInit() {

  }

}

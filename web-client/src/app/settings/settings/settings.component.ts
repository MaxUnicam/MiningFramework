import { Component, OnInit } from '@angular/core';
import { InMemorySettingsService } from '../memorysettings.service';
import { ApplicationSettings } from '../models/application-settings';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  settings: ApplicationSettings = null;

  constructor(private settingsService: InMemorySettingsService, private snackBar: MatSnackBar) {
    settingsService.getSettings().subscribe(settings => {
      this.settings = settings;
    }, error => {
      console.log(error);
    })
  }

  ngOnInit() { }

  save() {
    this.settingsService.saveNewSettings(this.settings);
    this.snackBar.open("Saved correctly", null, {
      panelClass: 'snack-bar-color',
      duration: 3000
    });
  }

}

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { SettingsComponent } from './settings/settings.component';

import { InMemorySettingsService } from '../settings/memorysettings.service';

import {
  MatInputModule,
  MatCardModule
} from '@angular/material';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatInputModule
  ],
  providers: [ InMemorySettingsService ],
  declarations: [ SettingsComponent ],
  exports: [ SettingsComponent ]
})
export class SettingsModule { }

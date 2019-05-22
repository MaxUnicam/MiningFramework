import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LayoutComponent } from './layout/layout.component';
import { SideMenuComponent } from './side-menu/side-menu.component';

import {
  MatSidenavModule,
  MatButtonModule,
  MatToolbarModule,
  MatIconModule,
  MatListModule
} from '@angular/material';
import { FooterModule } from '../footer/footer.module';

@NgModule({
  imports: [
    CommonModule,

    MatButtonModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,

    FooterModule
  ],
  declarations: [
    LayoutComponent,
    SideMenuComponent
  ],
  exports: [ LayoutComponent ]
})
export class LayoutModule { }

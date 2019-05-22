import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { CommonModule } from '@angular/common';
import { VisualQueristComponent } from './visual-querist/visual-querist.component';
import { QueryResultsComponent } from './query-results/query-results.component';

import {
  MatButtonModule,
  MatIconModule,
  MatCardModule,
  MatCheckboxModule,
  MatRadioModule,
  MatSelectModule,
  MatInputModule,
  MatProgressBarModule,
  MatTableModule,
  MatPaginatorModule,
  MatDialogModule
} from '@angular/material';

import { ExportDialogComponent } from './export-dialog/export-dialog.component';
import { LogicalPipe } from './pipes/logical.pipe';
import { ConstraintPipe } from './pipes/constraint.pipe';
import { SettingsModule } from '../settings/settings.module';
import { DataProjectorService } from './services/dataprojector.service';
import { DataSelectorService } from './services/dataselector.service';
import { DataProviderService } from './services/dataprovider.service';
import { CamQueristService } from './services/camquerist.service';
import { ReportGeneratorService } from './services/reportgenerator.service';

@NgModule({
  imports: [
    FormsModule,
    CommonModule,

    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatCheckboxModule,
    MatRadioModule,
    MatSelectModule,
    MatInputModule,
    MatProgressBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatDialogModule,

    SettingsModule
  ],
  declarations: [
    VisualQueristComponent,
    QueryResultsComponent,
    ExportDialogComponent,

    LogicalPipe,
    ConstraintPipe
  ],
  exports: [
    VisualQueristComponent,
    QueryResultsComponent
  ],
  entryComponents: [ExportDialogComponent],
  providers: [
    CamQueristService,
    DataProjectorService,
    DataSelectorService,
    DataProviderService,
    ReportGeneratorService
  ]
})
export class QueristModule { }

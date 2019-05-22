import { Component, OnInit } from '@angular/core';

import { MatDialogRef } from '@angular/material';


@Component({
  selector: 'app-export-dialog',
  templateUrl: './export-dialog.component.html',
  styleUrls: ['./export-dialog.component.css']
})
export class ExportDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ExportDialogComponent>) { }

  ngOnInit() {

  }

  public exportJson() {
    this.dialogRef.close('json');
  }

  public exportCsv() {
    this.dialogRef.close('csv');
  }

}

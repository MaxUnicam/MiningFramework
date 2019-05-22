import { Component, OnInit, OnDestroy } from '@angular/core';

import { MatDialog } from '@angular/material';

import { ExportDialogComponent } from '../export-dialog/export-dialog.component';
import { CamQueristService } from '../services/camquerist.service';
import { ReportGeneratorService } from '../services/reportgenerator.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-query-results',
  templateUrl: './query-results.component.html',
  styleUrls: ['./query-results.component.css']
})
export class QueryResultsComponent implements OnInit, OnDestroy {

  public isQuerying: Boolean = false;
  public noDataNorQuery: Boolean = true;

  public data: any[] = [];
  public showActionColumn: boolean = false;
  
  private subscription: Subscription = null;

  constructor(private router: Router,
    public querist: CamQueristService,
    public fileGenerator: ReportGeneratorService,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.showActionColumn = false;

    if (this.querist.query) {
      this.isQuerying = true;
      this.noDataNorQuery = false;
      this.subscription = this.querist.executeQuery().subscribe(
        (value: any) => { 
          this.data.push(value)
          if (value.to && !this.showActionColumn) {
            this.showActionColumn = true;
          }
        },
        (msg) => console.log('Error executing query: ', msg),
        () => this.isQuerying = false
      );
    }
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public downloadData() {
    const dialogRef = this.dialog.open(ExportDialogComponent, {
      width: '500px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result == 'json') {
        this.exportJson();
      } else if (result == 'csv') {
        this.exportCsv();
      }
    });
  }

  public showAnalyzeButton(item: any) {
    return (item.to) ? true : false;
  }

  public analyzeContract(item: any) {
    if (!item && !item.to) {
      return;
    }

    const params = { hash: item.to };
    this.router.navigate(['/home'], { queryParams: params });
  }

  private exportJson() {
    const data = this.querist.queryResult;
    const fileContent = this.fileGenerator.generateJson(data);
    const uri = encodeURI("data:application/json;charset=utf-8," + fileContent);
    this.startFileDownload(uri, "querying-ethereum-export.json");
  }

  private exportCsv() {
    const data = this.querist.queryResult;
    const properties = this.querist.query.desiredProperties;
    const fileContent = this.fileGenerator.generateCsv(data, properties);
    const uri = encodeURI("data:text/csv;charset=utf-8," + fileContent);
    this.startFileDownload(uri, "querying-ethereum-export.csv");
  }

  private startFileDownload(encodedUri: string, fileName: string) {
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", fileName);
    link.hidden = true;
    document.body.appendChild(link); // Required for FF
    link.click();
  }

}

import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AnalysisResult } from 'src/app/shared/models/analysisResult';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {

  @Input()
  result: AnalysisResult = null;

  @Output()
  analysisReset: EventEmitter<boolean> = new EventEmitter();

  constructor() { }

  ngOnInit() { }

  resetAnalysis() {
    this.analysisReset.emit(true);
  }

  public dowloadXesFile() {
    if (!this.result)
      return;

    var binary = atob(this.result.xesContent);
    const blob = new Blob([binary], { type: 'application/octet-stream' });
    const url = window.URL.createObjectURL(blob);
    // Needed to choose the filename
    var anchor = document.createElement("a");
    anchor.download = "log.xes";
    anchor.href = url;
    anchor.click();
  }

  public dowloadModel() {
    if (!this.result)
      return;

    var binary = atob(this.result.pnmlContent);
    const blob = new Blob([binary], { type: 'application/octet-stream' });
    const url = window.URL.createObjectURL(blob);
    // Needed to choose the filename
    var anchor = document.createElement("a");
    anchor.download = "model.pnml";
    anchor.href = url;
    anchor.click();
  }

}

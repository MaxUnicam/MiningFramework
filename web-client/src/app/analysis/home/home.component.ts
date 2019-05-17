import { Component, OnInit, ViewChild } from '@angular/core';
import { ServerStubService } from 'src/app/shared/services/server-stub.service';
import { DiscoveryAlgorithm } from 'src/app/shared/models/discoveryAlgorithm';
import { FormControl, Validators, FormGroupDirective, NgForm } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { AnalysisResult } from 'src/app/shared/models/analysisResult';
import { MatStepper } from '@angular/material/stepper';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public ethereumContractHash: string = '';
  public algorithm: DiscoveryAlgorithm = DiscoveryAlgorithm.InductiveMiner;

  public getModel: boolean = false;
  public getLog: boolean = false;
  public getMeasures: boolean = true;

  public isAnalyzing: boolean = false;
  public showResults: boolean = false;

  public results: AnalysisResult = null;

  public matcher = new MyErrorStateMatcher();
  public contractFormControl = new FormControl('', [
    Validators.required
  ]);

  @ViewChild("stepper")
  stepper: MatStepper;

  constructor(private serverStub: ServerStubService) { }

  ngOnInit() { }

  get inductive(): DiscoveryAlgorithm {
    return DiscoveryAlgorithm.InductiveMiner;
  }

  get heuristic(): DiscoveryAlgorithm {
    return DiscoveryAlgorithm.HeuristicMiner;
  }

  public startAnalysis() {
    this.isAnalyzing = true;
    this.serverStub.analyzeContract(this.ethereumContractHash, this.algorithm, this.getLog, this.getModel).subscribe(result => {
      this.isAnalyzing = false;
      this.results = result;
      this.showResults = true;
    }, error => {
      this.isAnalyzing = false;
      console.log(error);
    });
  }

  public clearAnalysisResults() {
    this.showResults = false;
    this.results = null;
    this.stepper.reset();
  }

}


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
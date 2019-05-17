import { Injectable } from '@angular/core';
import { SettingsService } from './settings.service';
import { Observable } from 'rxjs';
import { AnalysisResult } from '../models/analysisResult';
import { HttpClient } from '@angular/common/http';
import { DiscoveryAlgorithm } from '../models/discoveryAlgorithm';

@Injectable({
  providedIn: 'root'
})
export class ServerStubService {

  private serverEndpoint: string = null;

  constructor(private settings: SettingsService, private httpClient: HttpClient) {
    this.serverEndpoint = 'http://localhost:8080';
  }

  public analyzeContract(contract: string, algorithm: DiscoveryAlgorithm, returnXes = false, returnPnml = false): Observable<AnalysisResult> {
    const alg = this.getAlgorithmString(algorithm);
    const url = `${this.serverEndpoint}/api/v1/processmining/analyze?contract=${contract}&algorithm=${alg}&returnXes=${returnXes}&returnPnml=${returnPnml}`;
    return this.httpClient.get<AnalysisResult>(url);
  }

  private getAlgorithmString(algorithm: DiscoveryAlgorithm) {
    switch (algorithm) {
      case DiscoveryAlgorithm.HeuristicMiner:
        return "heuristic";
      case DiscoveryAlgorithm.InductiveMiner:
      default:
        return "inductive";
    }
  }

}

import { Injectable } from '@angular/core';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { ApplicationSettings } from './models/application-settings';
import { ConfigurationService } from '../shared/services/configuration.service';

@Injectable()
export class InMemorySettingsService {

  private appSettings: ApplicationSettings = null;

  private settingsStream: BehaviorSubject<ApplicationSettings>;

  constructor(config: ConfigurationService) {
    this.appSettings = {
      ethereumNodeUrl: config.ethereumEndpoint,
      apiServerUrl: config.apiEndpoint,
      queryStartBlock: 7806279,
      numberOfBlocks: 20
    };
    this.settingsStream = new BehaviorSubject<ApplicationSettings>(this.appSettings);
  }

  getSettings(): Observable<ApplicationSettings> {
    return of(this.appSettings);
  }

  getSettingsStream(): Observable<ApplicationSettings> {
    return this.settingsStream;
  }

  saveNewSettings(settings: ApplicationSettings) {
    this.appSettings = settings;
    this.settingsStream.next(settings);
  }

}

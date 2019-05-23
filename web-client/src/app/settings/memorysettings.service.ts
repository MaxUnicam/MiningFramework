import { Injectable } from '@angular/core';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { ApplicationSettings } from './models/application-settings';

@Injectable()
export class InMemorySettingsService {

  private appSettings: ApplicationSettings = null;

  private settingsStream: BehaviorSubject<ApplicationSettings>;

  constructor() {
    this.appSettings = {
      ethereumNodeUrl: 'https://mainnet.infura.io/v3/419c7f687f3b41f0bc9e0e647b9ab911',
      apiServerUrl: 'http://localhost:8080',
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

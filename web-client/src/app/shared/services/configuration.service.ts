import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ConfigurationService {

    public apiEndpoint: string = null;

    public ethereumEndpoint: string = null;

    constructor() {
        this.loadDynamicEnvironment();
    }

    private loadDynamicEnvironment() {
        const dynamicEnvrionmentConst = '__env';
        const browserWindow = window || {};
        const browserWindowEnv = browserWindow[dynamicEnvrionmentConst] || {};
        for (const key in browserWindowEnv) {
            if (browserWindowEnv.hasOwnProperty(key)) {
                this[key] = window[dynamicEnvrionmentConst][key];
            }
        }
    }

}

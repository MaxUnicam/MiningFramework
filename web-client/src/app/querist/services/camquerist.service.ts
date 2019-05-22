import { Injectable } from '@angular/core';
import { Observable, Observer } from 'rxjs';

import { Query } from '../models/query';
import { Constraint } from '../models/constraint';
import { Transaction } from '../models/transaction';
import { Account } from '../models/account';

import { DataProjectorService } from './dataprojector.service';
import { DataSelectorService } from './dataselector.service';
import { DataProviderService } from './dataprovider.service';
import { InMemorySettingsService } from 'src/app/settings/memorysettings.service';
import { Block } from '../models/block';

@Injectable()
export class CamQueristService {

  public query: Query;
  public queryResult: any[] = [];

  constructor(private projector: DataProjectorService,
    private selector: DataSelectorService,
    private provider: DataProviderService,
    private settings: InMemorySettingsService) { }

  executeQuery(query?: Query): Observable<any> {
    return new Observable((observer) => {
      this.queryResult = [];

      if (!query) {
        query = this.query;
        if (!query) {
          observer.complete();
          return;
        }
      }

      let endBlock = this.settings.queryStartBlock as number;
      endBlock += this.settings.numberOfBlocks as number;

      if (query.sourceType === 'Transaction') {
        this.provider.getTransactions(this.settings.queryStartBlock, endBlock).subscribe(
          (transaction: Transaction) => this.validate(transaction, query.constraints, query.desiredProperties, observer),
          (msg) => console.log('Error Getting Transaction: ', msg),
          () => observer.complete()
        );
      } else if (query.sourceType === 'Account') {
        this.provider.getAccount(query.constraints[0].value).subscribe(
          (account: Account) => this.validate(account, query.constraints, query.desiredProperties, observer),
          (msg) => console.log('Error Getting Account balance: ', msg),
          () => observer.complete()
        );
      } else {
        this.provider.getBlocks(this.settings.queryStartBlock, endBlock).subscribe(
          (block: Block) => this.validate(block, query.constraints, query.desiredProperties, observer),
          (msg) => console.log('Error Getting Block: ', msg),
          () => observer.complete()
        );
      }
    });
  }

  saveQuery(query: Query) {
    this.query = query;
  }

  private validate(item: any, constraints: Constraint[], properties: string[], observer: Observer<any>) {
    if (this.selector.isValid(item, constraints)) {
      const values = this.projector.getValues(item, properties);
      const t = {};
      for (let j = 0; j < values.length; j++) {
        t[properties[j] as string] = values[j];
      }

      this.queryResult.push(t);
      observer.next(t);
    }
  }

}

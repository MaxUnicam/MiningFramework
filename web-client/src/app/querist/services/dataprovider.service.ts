import { Account } from '../models/account';
import { Transaction } from '../models/transaction';
import { Block } from '../models/block';

import { Injectable } from '@angular/core';

import Web3 from 'web3';
import { Observable, from } from 'rxjs';
import { map } from 'rxjs/operators';

import { BigNumber } from 'bignumber.js';
import { InMemorySettingsService } from 'src/app/settings/memorysettings.service';


@Injectable()
export class DataProviderService {

  // ./parity --jsonrpc-cors all
  // REFERENCES: https://github.com/miguelmota/ethereum-input-data-decoder

  private web3: Web3;

  constructor(settingsService: InMemorySettingsService) {
    settingsService.getSettings().subscribe(settings => {
      this.web3 = new Web3(new Web3.providers.HttpProvider(settings.ethereumNodeUrl));
      if (this.web3)
        this.web3.eth.getBlockNumber().then(number => console.log("Last block " + number));
    })
  }

  getBlockNumber(): Observable<number> {
    return from(this.web3.eth.getBlockNumber());
  }

  getBlock(number: number | string): Observable<Block> {
    return from(this.web3.eth.getBlock(number)).pipe(
      map(value => {
        let block = {} as Block;
        Object.assign(block, value);
        block.difficulty = this.normalizeEth(value.difficulty);
        block.totalDifficulty = this.normalizeEth(value.totalDifficulty);
        return block;
      })
    )
  }

  getBlocks(start: number, end: number): Observable<Block> {
    return Observable.create(async (observer) => {
      for (let i = start; i < end; i++) {
        const block = await this.getBlock(i).toPromise();
        observer.next(block);
      }
      observer.complete();
    });
  }

  getTransaction(hash: string): Observable<Transaction> {
    return from(this.web3.eth.getTransaction(hash)).pipe(
      map(value => {
        let transaction = {} as Transaction;
        Object.assign(transaction, value);
        transaction.value = this.normalizeEth(value.value);
        return transaction;
      })
    );
  }

  getTransactions(start: number, end: number): Observable<Transaction> {
    return Observable.create(async (observer) => {
      for (let i = start; i < end; i++) {
        const block = await this.getBlock(i).toPromise();
        for (let j = 0; j < block.transactions.length; j++) {
          const hash = block.transactions[j];
          const item = await this.getTransaction(hash).toPromise();
          observer.next(item);
        }
      }

      observer.complete();
    });

  }

  getAccount(hash: string): Observable<Account> {
    return from(this.web3.eth.getBalance(hash)).pipe(
      map(value => { return { hash: hash, balance: this.normalizeEth(value) } })
    )
  }


  private normalizeEth(value: number | string | BigNumber): BigNumber {
    if (!value) {
      return new BigNumber(0);
    }

    return new BigNumber(value).dividedBy(new BigNumber('1000000000000000000'));
  }

}

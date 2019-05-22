import { Block } from '../models/block';
import { Transaction } from '../models/transaction';
import { Account } from '../models/account';

import { Injectable } from '@angular/core';


@Injectable()
export class DataProjectorService {

  getProperties(source: any): string[] {
    if (!source) {
      return [];
    }

    return Object.keys(source);
  }

  getBlockProperties(): string[] {
    const app: Block = {
      number: 0,
      hash: '',
      parentHash: '',
      nonce: '',
      sha3Uncles: '',
      logsBloom: '',
      transactionsRoot: '',
      stateRoot: '',
      miner: '',
      difficulty: null,
      totalDifficulty: null,
      size: 0,
      extraData: '',
      gasLimit: 0,
      gasUsed: 0,
      timestamp: 0,
      transactions: [''],
      uncles: ['']
    };
    return this.getProperties(app);
  }

  getTransactionProperties(): string[] {
    const app: Transaction = {
      hash: '',
      nonce: 0,
      blockHash: '',
      blockNumber: 0,
      transactionIndex: 0,
      from: '',
      to: '',
      value: null,
      gas: 0,
      gasPrice: null,
      input: ''
    };
    return this.getProperties(app);
  }

  getAccountProperties(): string[] {
    const app: Account = {
      hash: '',
      balance: null
    };
    return this.getProperties(app);
  }

  getValues(source: any, properties: string[]): any[] {
    if (!source || !properties) {
      return [];
    }

    const values: any[] = [];
    properties.forEach(prop => {
        values.push(source[prop as string]);
    });
    return values;
  }

  count(source: any): number {
    if (!source) {
      return 0;
    }

    if (Array.isArray(source)) {
      return (source as Array<any>).length;
    }

    return 1;
  }

}

import { BigNumber } from 'bignumber.js';

export interface Transaction {

  hash: string;

  nonce: number;

  blockHash: string;

  blockNumber: number;

  transactionIndex: number;

  from: string;

  to: string;

  value: BigNumber;

  gas: number;

  gasPrice: BigNumber;

  input: string;

}

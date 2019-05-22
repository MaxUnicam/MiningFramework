import { Injectable } from '@angular/core';


@Injectable()
export class InMemorySettingsService {

  public ethereumNodeUrl: string;

  public queryStartBlock: number;

  public numberOfBlocks: number;

  constructor() {
    // this.ethereumNodeUrl = 'http://localhost:8545';
    this.ethereumNodeUrl = 'https://mainnet.infura.io/v3/419c7f687f3b41f0bc9e0e647b9ab911';
    // this.queryStartBlock = 7000000;
    this.queryStartBlock = 7806279;
    this.numberOfBlocks = 20;
  }

}

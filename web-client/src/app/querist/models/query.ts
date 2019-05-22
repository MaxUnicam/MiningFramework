import { Constraint } from './constraint';

export interface Query {

  sourceType: string;

  desiredProperties: string[];

  constraints: Constraint[];

}

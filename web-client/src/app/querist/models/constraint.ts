export class Constraint {

  property: string;

  operator: string;

  value: any;

  logicalOperator?: LogicalOperator = null;

}


export enum LogicalOperator {
  And,
  Or
}

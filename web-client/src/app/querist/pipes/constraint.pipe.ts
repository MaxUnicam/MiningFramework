import { Pipe, PipeTransform } from '@angular/core';

import { Constraint } from '../models/constraint';

@Pipe({
  name: 'constraint'
})
export class ConstraintPipe implements PipeTransform {

  transform(value: Constraint): string {
    if (!value) {
      return '';
    }

    if (this.isFirst(value)) {
      return 'Primo';
    }

    if (this.isLast(value)) {
      return 'Ultimo';
    }

    return value.property + ' ' + value.operator + ' ' + value.value;
  }


  private isLast(value: Constraint): boolean { return this.isEdge(value, 'last'); }

  private isFirst(value: Constraint): boolean { return this.isEdge(value, 'first'); }

  private isEdge(value: Constraint, prop: string): boolean {
    if (!value.property || !value.operator || !value.value) {
      return false;
    }

    if (value.property.toLowerCase() !== prop.toLocaleLowerCase() || value.operator !== '=') {
      return false;
    }

    return value.value === 1;
  }

}

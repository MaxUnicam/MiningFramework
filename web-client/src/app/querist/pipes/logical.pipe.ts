import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'logical'
})
export class LogicalPipe implements PipeTransform {

    transform(value): any[] {
      const keys = [];
      for (const enumMember in value) {
        if (!isNaN(parseInt(enumMember, 0))) {
          keys.push({key: parseInt(enumMember, 0), value: value[enumMember]});
        }
      }

      return keys;
    }

}

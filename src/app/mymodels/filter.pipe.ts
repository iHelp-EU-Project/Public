import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(value: any, searchValue): any {

    if (!searchValue) {
        return value;
    }

    console.log(value);
    const val = value.filter((v) => v.value.model_id.name.toLowerCase().indexOf(searchValue.toLowerCase()) > -1);
    return val;

  }

}

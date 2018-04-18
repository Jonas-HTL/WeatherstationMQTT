import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

/*
  Generated class for the RestProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class RestProvider {
constructor(private http: HttpClient) {
    
  }

   public getDataByDate(date : String){
    const req = this.http.post('http://localhost:8080/server/api/rest/', date).subscribe((res) => {
      console.log(res);
      }, (err) => {
      console.log(err);
     });
   }
}

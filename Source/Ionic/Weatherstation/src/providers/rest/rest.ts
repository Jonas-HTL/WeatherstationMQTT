import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClientModule } from '@angular/common/http'; 
import { HttpModule } from '@angular/http';

/*
  Generated class for the RestProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/

export interface GetData{
  ws_id: String,
  year: String
}

export interface Weatherstation{
  ws_id: String,
  villagename: String
}

@Injectable()
export class RestProvider {

  constructor(public http: HttpClient) {
  }

     public getTemperatureAvgPerVillage(data: GetData){
     return this.http.post<String>('http://localhost:8080/server/api/rest/GetTemperatureAvgPerId', JSON.stringify(data));
   }
   
  public getRainAvgPerVillage(data: GetData){
    return this.http.post<String>('http://localhost:8080/server/api/rest/GetRainAvgPerId', JSON.stringify(data));
  }

  public getWindVelocityMinPerVillage(data: GetData){
    return this.http.post<String>('http://localhost:8080/server/api/rest/GetWindVelocityMinPerId', JSON.stringify(data));
  }


  public getWindVelocityMaxPerVillage(data: GetData){
    return this.http.post<String>('http://localhost:8080/server/api/rest/GetWindVelocityMaxPerId', JSON.stringify(data));
  }


  public getPreasureAvgPerVillage(data: GetData){
    return this.http.post<String>('http://localhost:8080/server/api/rest/GetPreasureAvgPerId', JSON.stringify(data));
  }

  
  getWeatherstation(): any {
     return this.http.get<Weatherstation[]>('http://localhost:8080/server/api/rest/GetAllWeatherstations');
  }

}

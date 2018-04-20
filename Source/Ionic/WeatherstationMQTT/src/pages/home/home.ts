import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { DatepickerOptions } from 'ng2-datepicker';

export interface Weatherstation {
	Id: number,
  Village: string,
	Address: string,
}
export interface Wind{
  maxVelocity: number,
  minVelocity: number
}
export interface Rain{
  Id:number,
  dailyAVG: number,
}
export interface Air{
  Id: number,
  dailyPreassureAVG: number
}
export interface Temperatur{
  Id: number,
  dailyAVG: number
}

export interface Data{
  wind: Wind,
  rain: Rain,
  air: Air,
  temperatur: Temperatur
}

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

options: DatepickerOptions = {
  minYear: 2016,
  maxYear: 2018,
  displayFormat: 'W \\Week',
  barTitleFormat: 'MMMM YYYY',
  dayNamesFormat: 'dd',
  firstCalendarDay: 0, // 0 - Sunday, 1 - Monday
  barTitleIfEmpty: 'Click to select a date'
};

date:string;
currentStation:number
weatherstations:Weatherstation[]=[]
weatherstationData:Data[]=[]

  constructor(public navCtrl: NavController) {
    this.getWeatherstations();
  }
  getWeatherstations(){

  }
  getDataPerWeekByWeatherstation(weatherstationId:number){
      //send Date and current Station
      
  }
 sendData(){
   console.log(this.date.toString)
 }

}

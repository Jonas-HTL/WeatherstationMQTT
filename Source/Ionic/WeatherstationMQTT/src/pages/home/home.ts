import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { DatepickerOptions } from 'ng2-datepicker';

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

date:string
  constructor(public navCtrl: NavController) {

  }
 sendData(){
   console.log(this.date)
 }

}

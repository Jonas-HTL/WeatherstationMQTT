import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  date: Date = new Date();
    settings = {
        bigBanner: true,
        timePicker: false,
        format: 'w',
        defaultOpen: true
    }
  constructor(public navCtrl: NavController) {

  }
  onDateSelect(){
    
  }

}

import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { DataDistributorProvider } from '../../providers/data-distributor/data-distributor';
import { RestProvider } from '../../providers/rest/rest';


export interface Rain {
  type: string,
  id_ws: string,
  amount: number,
  time: string
}

export interface Weatherstation {
  ws_id: String,
  villagename: String
}

export interface Temperature {
  type: string,
  id_ws: string,
  time: string,
  temp: number
}

export interface Wind {
  type: string,
  id_ws: string,
  time: string,
  int: number,
  dir: string
}

export interface Air {
  type: string,
  id_ws: string,
  time: string,
  press: number,
  hum: number
}

export interface Filter {
  ws_id: string,
  type: number
}

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})
export class AboutPage {

  air = <Air>{ type: "", id_ws: "", time: "", press: 0, hum: 0 }
  wind = <Wind>{ type: "", id_ws: "", time: "", int: 0, dir: "" }
  temperature = <Temperature>{ type: "", id_ws: "", time: "", temp: 0 }
  rain = <Rain>{ type: "", id_ws: "", time: "", amount: 0 }

  weatherstations: Weatherstation[];
  currentWeatherstation: string;

  constructor(public navCtrl: NavController, public dataProvider: DataDistributorProvider, public rest: RestProvider) {
    this.getAllWeatherstations();
  }

  getAllWeatherstations() {
    this.rest.getWeatherstation().subscribe(res => this.weatherstations = res);
  }
  public sendMessage() {
    var data = <Filter>{ ws_id: this.currentWeatherstation, type: 10 }
    this.dataProvider.dataTransfer.next(data);
  }

  buttonClicked() {
    this.sendMessage();
    var air: Air
    this.dataProvider.readAir((value) => {
      this.air = value[0];
      //console.log(value[0]);
    });
    var wind: Wind
    this.dataProvider.readWind((value) => {
      this.wind = value[0];
      // console.log(JSON.stringify(value))
    });
    var temperature: Temperature
    this.dataProvider.readTemperatures((value) => {
      this.temperature = value[0];
    });
    var rain: Rain
    this.dataProvider.readRain((value) => {
      this.rain = value[0];
      //console.log(rain)
    })
  } 
}

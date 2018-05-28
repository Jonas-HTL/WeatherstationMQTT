import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { DataDistributorProvider } from '../../providers/data-distributor/data-distributor';
import { RestProvider } from '../../providers/rest/rest';




export interface Rain {
	type: string
	
}

export interface Weatherstation{
  ws_id: String,
  villagename: String
}

export interface Temperature{
	type: string,
	id_ws: string,
	time: string,
	temp: number
}

export interface Wind{
	type: string,
	id_ws: string,
	time: string,
	int: number,
	dir: string
}

export interface Air{
	type: string,
	id_ws:string,
	time: string,
	press: number,
	hum: number
}

export interface Filter{
  ws_id: string,
  type: number
}


@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})
export class AboutPage {
  air = <Air>{type:"3", id_ws:"3", time:"22:00", press:32, hum:32}
	wind = <Wind>{type:"1", id_ws:"001", time:"16:00",int:5, dir:"south"}
	temperature = <Temperature>{type:"2", id_ws:"001", time:"17:00", temp:25}

  testWeatherstation1 = <Weatherstation>{villagename:"Schmidham", ws_id:"1"};
  testWeatherstation2 = <Weatherstation>{villagename:"Walchen", ws_id:"2"};
  testWeatherstation3 = <Weatherstation>{villagename:"Spielberg", ws_id:"3"};
 
  weatherstations:Weatherstation[];
  currentWeatherstation:Weatherstation = <Weatherstation>{villagename:"Schmidham", ws_id:"1"};
  
  constructor(public navCtrl: NavController,public dataProvider: DataDistributorProvider,public rest:RestProvider) {
     this.weatherstations=[this.testWeatherstation1, this.testWeatherstation2,this.testWeatherstation3];
    this.currentWeatherstation=this.testWeatherstation1;
    this.getAllWeatherstations();
  
    
  }

  getAllWeatherstations(){
    this.rest.getWeatherstation().subscribe(res=>this.weatherstations=res);
  }
  public sendMessage(){
    var data= <Filter>{ws_id:this.currentWeatherstation.ws_id, type:10}
    this.dataProvider.dataTransfer.next(data);
  }

  buttonClicked(){
    this.sendMessage();
    var air:Air
       this.dataProvider.readAir((value) =>{
         this.air = value;
         console.log(JSON.stringify(value))
    });
		var wind:Wind
       this.dataProvider.readWind((value) =>{
         this.wind = value;
         console.log(JSON.stringify(value))
    });
		var temperature:Temperature
       this.dataProvider.readTemperatures((value) =>{
         this.temperature = value;
         console.log(JSON.stringify(value))
    });
  }
  
}

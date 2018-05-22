import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { WebsocketProvider } from '../websocket/websocket';

/*
  Generated class for the DataDistributorProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/


const URL = 'ws://167.99.131.124:8025/websocket/weatherstation';


export interface Rain {
	type: string
	
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

@Injectable()
export class DataDistributorProvider {
temperaturesCb: (value: any) => void;
	windCb: (value: any) => void;
	airCb: (value: any) => void;
	rainCb: (value: any) => void;

  public dataTransfer: Subject<any>;
 
  public temperatures: Temperature[] = [];
	public wind: Wind[]=[];
	public air: Air[]=[];
	public rain: Rain[]=[];

  constructor(wsService: WebsocketProvider) {
		this.dataTransfer = <Subject<any>>wsService
			.connect(URL)
			.map((response: MessageEvent): any => {
				var obj = JSON.parse(response.data);
				return obj;
			});

      this.dataTransfer.subscribe((value) => {
        if (value.type == "2") {		//Temperatur	
					this.temperaturesCb(this.temperatures);
				}
				 if (value.type == "1") {			//Wind
					this.windCb(this.wind);
				}
				 if (value.type == "3") {		//Air
				 this.air=[];
				 this.air.push(value);
					this.airCb(this.air);
				}
				 if (value.type == "4") {		//Rain
					this.rainCb(this.rain);
				}
        else {
				console.log(JSON.stringify(value));
			  }			
      });
  }

  public readWind(callback: (value: any) => void) {
		this.windCb = callback;
	}
	 public readTemperatures(callback: (value: any) => void) {
		this.temperaturesCb = callback;
	}
	public readAir(callback: (value: any) => void) {
		this.airCb = callback;
	}

}

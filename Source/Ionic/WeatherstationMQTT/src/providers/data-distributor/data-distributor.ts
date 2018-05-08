import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs/Rx';
import { WebsocketProvider } from '../websocket/websocket';

/*
  Generated class for the DataDistributorProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
const MQTT_URL = 'ws://172.18.251.94:8000/mqtt';


export interface Message {
	type: string,
	text: string,
}

@Injectable()
export class DataDistributorProvider {

  public dataTransfer: Subject<any>;
  public messagesCb: (value: any) => void;
  public messages: Message[] = [];


  constructor(wsService: WebsocketProvider) {
		this.dataTransfer = <Subject<any>>wsService
			.connect(MQTT_URL)
			.map((response: MessageEvent): any => {
				var obj = JSON.parse(response.data);
				return obj;
			});

      this.dataTransfer.subscribe((value) => {
        if (value.type == "151") {
					this.messagesCb(this.messages);
				}
        else {
				console.log(value);
			  }			
      });
  }

  public readMessages(callback: (value: any) => void) {
		this.messagesCb = callback;
	}
 
}

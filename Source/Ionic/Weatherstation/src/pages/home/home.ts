import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { RestProvider } from '../../providers/rest/rest';


export interface GetData{
  ws_id: String,
  year: String
}

export interface Weatherstation{
  ws_id: String,
  villagename: String
}

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  testWeatherstation1 = <Weatherstation>{villagename:"Schmidham", ws_id:"1"};
  testWeatherstation2 = <Weatherstation>{villagename:"Walchen", ws_id:"2"};
  testWeatherstation3 = <Weatherstation>{villagename:"Spielberg", ws_id:"3"};
 
  weatherstations:Weatherstation[];
  currentWeatherstation:Weatherstation = <Weatherstation>{villagename:"Schmidham", ws_id:"1"};
  myDate:String;



  public lineChartData:Array<any> = [
  {data: [59,32,25,16,58,89,27,45,33,55,65,18,12,93,49,97,3,56,69,71,7,43,99,75,100,15,84,90,34,23,67,80,40,51,2,82,13,88,95,61,50,38,72,48,70,35,62,60,9,26,73,20,94], label: 'Temperatur'},
  {data: [4,56,6,67,64,70,55,17,23,91,38,13,19,48,25,36,32,28,26,71,86,10,15,50,30,20,79,1,47,87,43,11,98,39,76,3,33,65,8,22,21,89,58,84,35,59,82,7,99,88,2,42,29,52], label: 'Rain'},
];
public lineChartLabels:Array<any> = ['January','','','','February','','','','March','','','','April','','','','May','','','','June','','','','July','','','','August','','','','May','','','','June','','','','August','','','','September','','','','Oktober','','','November', 'December'];
public lineChartOptions:any = {
  elements: { point: { radius: 0.5 }},
  showXLabels: 12,
  responsive: true,
  
};
public lineChartColors:Array<any> = [
  { // grey
    backgroundColor: 'rgba(148,159,177,0.2)',
    borderColor: 'rgba(148,159,177,1)',
    pointBackgroundColor: 'rgba(148,159,177,1)',
    pointBorderColor: '#fff',
    pointHoverBackgroundColor: '#fff',
    pointHoverBorderColor: 'rgba(148,159,177,0.8)'
  },
  { // dark grey
    backgroundColor: 'rgba(77,83,96,0.2)',
    borderColor: 'rgba(77,83,96,1)',
    pointBackgroundColor: 'rgba(77,83,96,1)',
    pointBorderColor: '#fff',
    pointHoverBackgroundColor: '#fff',
    pointHoverBorderColor: 'rgba(77,83,96,1)'
  },
  { // grey
    backgroundColor: 'rgba(148,159,177,0.2)',
    borderColor: 'rgba(148,159,177,1)',
    pointBackgroundColor: 'rgba(148,159,177,1)',
    pointBorderColor: '#fff',
    pointHoverBackgroundColor: '#fff',
    pointHoverBorderColor: 'rgba(148,159,177,0.8)'
  }
];
public lineChartLegend:boolean = true;
public lineChartType:string = 'line';

public randomize():void {
  let _lineChartData:Array<any> = new Array(this.lineChartData.length);
  for (let i = 0; i < this.lineChartData.length; i++) {
    _lineChartData[i] = {data: new Array(this.lineChartData[i].data.length), label: this.lineChartData[i].label};
    for (let j = 0; j < this.lineChartData[i].data.length; j++) {
      _lineChartData[i].data[j] = Math.floor((Math.random() * 100) + 1);
    }
  }
  this.lineChartData = _lineChartData;
}

// events
public chartClicked(e:any):void {
  console.log(e);
}

public chartHovered(e:any):void {
  console.log(e);
}

  constructor(public navCtrl: NavController, public rest:RestProvider) {
    this.weatherstations=[this.testWeatherstation1, this.testWeatherstation2,this.testWeatherstation3];
    this.currentWeatherstation=this.testWeatherstation1;
    this.getAllWeatherstations();
  }
  
  getAllWeatherstations(){
    this.rest.getWeatherstation().subscribe(res=>this.weatherstations=res);
  }
  getData = <GetData>{ws_id:this.currentWeatherstation.ws_id, year:this.myDate};

  getTemperatureAvgPerVillage(){
    this.rest.getTemperatureAvgPerVillage(this.getData);
  }
  getRainAvgPerVillage(){
    this.rest.getTemperatureAvgPerVillage(this.getData);
  }

  buttonClicked(){
    console.log(this.currentWeatherstation.ws_id, this.myDate);
    this.lineChartData=[{},{}];
    this.lineChartData=[
      {data: [this.getRainAvgPerVillage]},
      {data:[this.getTemperatureAvgPerVillage]}
    ]
  }
 
}

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
  //testWeatherstation1 = <Weatherstation>{villagename:"Schmidham", ws_id:"1"};
  //testWeatherstation2 = <Weatherstation>{villagename:"Walchen", ws_id:"2"};
  //testWeatherstation3 = <Weatherstation>{villagename:"Spielberg", ws_id:"3"};
 
  weatherstations:Weatherstation[];
  //currentWeatherstation:Weatherstation = <Weatherstation>{villagename:"Schmidham", ws_id:"1"};
  currentWeatherstation:string;
  myDate:String;



  public lineChartData:Array<any> = [
  {data: [], label: 'Temperatur'},
  {data: [], label: 'Rain'},
];
public lineChartLabels:Array<any> = ['January','','','','February','','','','March','','','','April','','','','May','','','','June','','','','July','','','','August','','','','September','','','','Oktober','','','','November','','','', 'December'];
public lineChartOptions:any = {
  elements: { point: { radius: 0.5 }},
  scaleBeginAtZero: false,
    responsive: true,
    scaleStartValue : -25 ,
  showXLabels: 12,
 
  
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
  this.lineChartData =_lineChartData;
}

// events
public chartClicked(e:any):void {
  console.log(e);
}

public chartHovered(e:any):void {
  console.log(e);
}

  

  constructor(public navCtrl: NavController, public rest:RestProvider) {
    
   // this.weatherstations=[this.testWeatherstation1, this.testWeatherstation2,this.testWeatherstation3];
 
    //this.currentWeatherstation=this.testWeatherstation1;
    this.getAllWeatherstations();
  }
  
  getAllWeatherstations(){
    this.rest.getWeatherstation().subscribe(res=>this.weatherstations=res);
  }
  getData = <GetData>{ws_id:this.currentWeatherstation, year:this.myDate};
  values2:number[]
  getTemperatureAvgPerVillage(){
    this.getData.ws_id=this.currentWeatherstation;
    this.getData.year = this.myDate;
    let _lineChartData:Array<any> = new Array(this.lineChartData.length);
    this.rest.getTemperatureAvgPerVillage(this.getData).subscribe(res=>{
      _lineChartData[0] = {data: res,label: this.lineChartData[0].label};
      _lineChartData[1] = {data: this.lineChartData[1].data, label: this.lineChartData[1].label};
      this.lineChartData = _lineChartData;
    }
  );
  }
 values:number[]=[];
  getRainAvgPerVillage(){
    this.getData.ws_id=this.currentWeatherstation;
    this.getData.year = this.myDate;
    let _lineChartData:Array<any> = new Array(this.lineChartData.length);
    this.rest.getRainAvgPerVillage(this.getData).subscribe(res=>{
      _lineChartData[0] = {data: this.lineChartData[0].data,label: this.lineChartData[0].label};
      _lineChartData[1] = {data: res, label: this.lineChartData[1].label};
      this.lineChartData = _lineChartData;
      }
    ); 
  }
 
 
  buttonClicked(){

    this.getRainAvgPerVillage();
    this.getTemperatureAvgPerVillage();
  
   
  }
}
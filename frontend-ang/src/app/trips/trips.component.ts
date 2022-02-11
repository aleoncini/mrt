import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MapGeocoder } from '@angular/google-maps';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { GooglekeyService } from '../endpoints/googlekey.service';
import { TripsService } from '../endpoints/trips.service';
import { Key } from '../models/key';
import { Trip } from '../models/trip';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.scss']
})
export class TripsComponent implements OnInit {
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  thirdFormGroup: FormGroup;
  // key: Key;
  googleMap: any;
  romeCoord = environment.romeOfficePosition;
  center: {
    lat: number;
    lng: number;
  };
  zoom = 9;
  // apiLoaded: Observable<boolean>;

  constructor(private _formBuilder: FormBuilder, private tripsService: TripsService, private googleKeyService: GooglekeyService, private httpClient: HttpClient) {
    this.firstFormGroup = this._formBuilder.group({
      firstCtrl: ['', Validators.required],
    });
    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required],
    });
    this.thirdFormGroup = this._formBuilder.group({
      thirdCtrl: ['', Validators.required],
    });
    // this.key = {name: "", value: ""}
    // this.key = { name: "", value: environment.googleKey }

    // this.apiLoaded = httpClient.jsonp('https://maps.googleapis.com/maps/api/js?key=' + this.key.value, 'callback')
    //     .pipe(
    //       map(() => true),
    //       catchError(() => of(false)),
    //     );

    this.center = this.romeCoord
  }

  ngOnInit(): void { }

  saveTrip() {
    let tripDate: Date = this.firstFormGroup.controls['firstCtrl'].value
    let distance: number = 0
    let trip: Trip = {
      day: tripDate.getDay(),
      month: tripDate.getMonth(),
      year: tripDate.getFullYear(),
      destination: this.secondFormGroup.controls['secondCtrl'].value,
      distance: distance,
      purpose: this.thirdFormGroup.controls['thirdCtrl'].value,
      rhid: ""
    }
    this.tripsService.CreateTrip(trip).subscribe(() => console.log("Trip added."));
  }

  seachPlace() {
    let destination = this.secondFormGroup.controls['secondCtrl'].value;
    let directions = this.httpClient.get<any>("https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=" + destination + "&key=" + environment.googleKey);
    this.httpClient.get<any>("https://maps.google.com/maps/api/geocode/json?address=" + destination + "&key=" + environment.googleKey).subscribe((res) => {
      this.center = res.results[0].geometry.location;
    });
    directions.subscribe((res) => {
      console.log(res);
    });
  }
}

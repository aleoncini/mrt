import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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
  key: Key | undefined;
  rome_office_position = {lat: 41.909832599799316, lng: 12.452532095955236};

  constructor(private _formBuilder: FormBuilder, private tripsService: TripsService) {
    this.firstFormGroup = this._formBuilder.group({
      firstCtrl: ['', Validators.required],
    });
    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required],
    });
    this.thirdFormGroup = this._formBuilder.group({
      thirdCtrl: ['', Validators.required],
    });
  }

  ngOnInit(): void {

  }

  // initKey() {
  //   this.tripsService.getKey().subscribe(response => this.key = { ...response.body! });
  // };

  saveTrip(trip: Trip){
    this.tripsService.CreateTrip(trip).subscribe(() => console.log("Trip added."));
  }
}

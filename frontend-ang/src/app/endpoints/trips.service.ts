import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Trip } from '../models/trip';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TripsService {

  private address = environment.url + ":" + environment.port + "/trips/"


  constructor(private http: HttpClient) { }


  GetAllTrips(): Observable<Trip[]> {
    return this.http.get<Trip[]>(this.address)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting all the trips')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  GetTripsByDate(year: number, month: number): Observable<Trip[]> {
    return this.http.get<Trip[]>(this.address + year + "/" + month)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting these trips')
          console.error(err);
          return throwError(() => err);
        })
      )
  }

  GetTripById(id: string): Observable<Trip> {
    return this.http.get<Trip>(this.address + id)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting this trip')
          console.error(err);
          return throwError(() => err);
        })
      )
  }

  CreateTrip(trip: Trip): Observable<Trip> {
    return this.http.post<Trip>("/trips", trip)
      .pipe(
        catchError((err) => {
          console.log('error caught in creating these trip')
          console.error(err);
          return throwError(() => err);
        })
      );
  }
}

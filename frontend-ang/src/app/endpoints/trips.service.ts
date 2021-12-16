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
    return this.http.post<Trip>(this.address, trip)
      .pipe(
        catchError((err) => {
          console.log('error caught in creating these trip')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  UpdateTrip(id: string, trip: Trip){
    this.http.put<string>(this.address + id, {id, trip})
    .pipe(
      catchError((err) => {
        console.log('error caught while updating this trip')
        console.error(err);
        return throwError(() => err);
      })
    );
  }

  DeleteTrip(id: string){
    this.http.delete<string>(this.address + id)
    .pipe(
      catchError((err) => {
        console.log('error caught while deleting this trip')
        console.error(err);
        return throwError(() => err);
      })
    );
  }

  GetTripCount(): Observable<number>{
    return this.http.get<number>(this.address + "count")
    .pipe(
      catchError((err) => {
        console.log('error caught while getting trips count')
        console.error(err);
        return throwError(() => err);
      })
    )
  }
}

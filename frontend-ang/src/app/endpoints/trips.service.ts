import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { Key } from '../models/key';
import { Trip } from '../models/trip';

@Injectable({
  providedIn: 'root'
})
export class TripsService {

  constructor(private http: HttpClient) { }

  getKey(): Observable<HttpResponse<Key>> {
    return this.http.get<Key>(
      "/key", { observe: 'response' });
  }

  saveTrip(trip: Trip): Observable<Trip>{
    return this.http.post<Trip>("/trips", trip)
    .pipe(
      catchError(this.handleError('Add Trip Error'))
    );
  }

  private handleError(error: any): any {
    console.error('An error occurred', error);
 }
}

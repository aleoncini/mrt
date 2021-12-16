import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Location } from '../models/location';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  private address = environment.url + ":" + environment.port + "/locations/"

  constructor(private http: HttpClient) { }

  GetAllLocations(): Observable<Location[]> {
    return this.http.get<Location[]>(this.address)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting all the locations')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  GetLocationById(id: string): Observable<Location> {
    return this.http.get<Location>(this.address + id)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting the location')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  CreateLocation(location: Location): Observable<Location> {
    return this.http.post<Location>(this.address, location)
      .pipe(
        catchError((err) => {
          console.log('error caught while creating the location')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  UpdateLocation(id: string) {
    return this.http.put<Location>(this.address, location)
      .pipe(
        catchError((err) => {
          console.log('error caught while updating the location')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  DeleteLocation(id: string) {
    return this.http.delete<Location>(this.address + id)
      .pipe(
        catchError((err) => {
          console.log('error caught while deleting the location')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  GetLocationsByName(name: string) {
    return this.http.get<Location>(this.address + "search/" + name)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting the locations')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  GetLocationsByDistance(distance: number) {
    return this.http.get<Location>(this.address + "range/" + distance)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting the locations')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  GetLocationsNumber() {
    return this.http.get<Location>(this.address + "count")
      .pipe(
        catchError((err) => {
          console.log('error caught in getting the locations number')
          console.error(err);
          return throwError(() => err);
        })
      );
  }
}

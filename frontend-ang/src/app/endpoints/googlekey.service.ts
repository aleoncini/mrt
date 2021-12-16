import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Key } from '../models/key';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GooglekeyService {

  private address = environment.url + ":" + environment.port + "/key"

  constructor(private http: HttpClient) { }

  GetGoogleKey(): Observable<Key> {
    return this.http.get<Key>(
      this.address)
      .pipe(
        catchError((err) => {
          console.log('error caught in service')
          console.error(err);
          return throwError(() => err);
        })
      );;
  }
}

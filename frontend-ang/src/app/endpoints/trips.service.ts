import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';



@Injectable({
  providedIn: 'root'
})
export class TripsService {

  url: string = "";
  constructor(private http: HttpClient) { }

  getKey(): Observable<HttpResponse<Key>> {
    return this.http.get<Key>(
      this.url, { observe: 'response' });
  }
}

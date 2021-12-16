import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {

  private address = environment.url + ":" + environment.port + "/reports/"


  constructor(private http: HttpClient) { }

  GetAllTrips(year: number, month: number): Observable<string> {
    return this.http.post<string>(this.address + year + "/" + month, {year, month})
      .pipe(
        catchError((err) => {
          console.log('error caught while building')
          console.error(err);
          return throwError(() => err);
        })
      );
  }
}

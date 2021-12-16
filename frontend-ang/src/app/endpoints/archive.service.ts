import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ArchiveService {

  private address = environment.url + ":" + environment.port + "/archive/"

  constructor(private http: HttpClient) { }

  GetPdf(docName: string): Observable<File> {
    return this.http.get<File>(this.address + "pdf/" + docName)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting the pdf')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  GetReportsByYear(year: number): Observable<number[]>{
    return this.http.get<number[]>(this.address + year)
      .pipe(
        catchError((err) => {
          console.log('error caught in getting the pdfs')
          console.error(err);
          return throwError(() => err);
        })
      );
  }

  DeleteReport(docName: string){
    return this.http.delete(this.address + docName)
    .pipe(
      catchError((err) => {
        console.log('error caught while deleting the document')
        console.error(err);
        return throwError(() => err);
      })
    )
  }
}

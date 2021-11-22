import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';

export interface Trip {
  date: number;
  size: number;
  version: number;
}

const ELEMENT_DATA: Trip[] = [
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
  { date: 1, size: 2, version: 3 },
];

@Component({
  selector: 'app-archive',
  templateUrl: './archive.component.html',
  styleUrls: ['./archive.component.scss']
})
export class ArchiveComponent implements OnInit {
  displayedColumns: string[] = ['date', 'size', 'version'];
  dataSource = new MatTableDataSource(ELEMENT_DATA);
  constructor() { }

  ngOnInit(): void {
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}

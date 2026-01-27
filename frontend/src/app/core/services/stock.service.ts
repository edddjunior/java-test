import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Stock } from '../models/stock.model';
import { ApiResponse } from '../models/api.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class StockService {
  private readonly apiUrl = `${environment.stockApiUrl}/api/v1/stock`;

  constructor(private readonly http: HttpClient) {}

  checkStock(productId: string): Observable<Stock> {
    return this.http
      .get<ApiResponse<Stock>>(`${this.apiUrl}/${productId}`)
      .pipe(map((response) => response.data));
  }
}

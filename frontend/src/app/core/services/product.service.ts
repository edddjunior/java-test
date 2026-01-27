import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Product, ProductRequest } from '../models/product.model';
import { ApiResponse, PagedResponse } from '../models/api.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly apiUrl = `${environment.productApiUrl}/api/v1/products`;
  private readonly defaultPageSize = 8;

  constructor(private readonly http: HttpClient) {}

  getAll(page = 0, size = this.defaultPageSize): Observable<PagedResponse<Product>> {
    return this.http
      .get<ApiResponse<PagedResponse<Product>>>(`${this.apiUrl}?page=${page}&size=${size}`)
      .pipe(map((response) => response.data));
  }

  getById(id: string): Observable<Product> {
    return this.http
      .get<ApiResponse<Product>>(`${this.apiUrl}/${id}`)
      .pipe(map((response) => response.data));
  }

  create(product: ProductRequest): Observable<Product> {
    return this.http
      .post<ApiResponse<Product>>(this.apiUrl, product)
      .pipe(map((response) => response.data));
  }

  update(id: string, product: ProductRequest): Observable<Product> {
    return this.http
      .put<ApiResponse<Product>>(`${this.apiUrl}/${id}`, product)
      .pipe(map((response) => response.data));
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

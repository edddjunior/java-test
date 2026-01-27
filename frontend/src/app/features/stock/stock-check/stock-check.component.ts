import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StockService } from '../../../core/services/stock.service';
import { ProductService } from '../../../core/services/product.service';
import { Stock } from '../../../core/models/stock.model';
import { Product } from '../../../core/models/product.model';

interface ErrorWithMessage {
  userMessage?: string;
}

@Component({
  selector: 'app-stock-check',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stock-check.component.html',
  styleUrl: './stock-check.component.css'
})
export class StockCheckComponent implements OnInit {
  products: Product[] = [];
  selectedProductId = '';
  stock: Stock | null = null;
  loading = false;
  loadingProducts = true;
  error = '';

  constructor(
    private readonly stockService: StockService,
    private readonly productService: ProductService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  checkStock(): void {
    if (!this.selectedProductId) {
      this.stock = null;
      return;
    }

    this.loading = true;
    this.error = '';
    this.stock = null;

    this.stockService.checkStock(this.selectedProductId).subscribe({
      next: (stock) => {
        this.stock = stock;
        this.loading = false;
      },
      error: (err: ErrorWithMessage) => {
        this.error = err.userMessage ?? 'Erro ao consultar estoque.';
        this.loading = false;
      }
    });
  }

  private loadProducts(): void {
    this.productService.getAll(0, 100).subscribe({
      next: (response) => {
        this.products = response.content;
        this.loadingProducts = false;
      },
      error: (err: ErrorWithMessage) => {
        this.error = err.userMessage ?? 'Erro ao carregar produtos.';
        this.loadingProducts = false;
      }
    });
  }
}

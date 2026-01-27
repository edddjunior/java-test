import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { Product } from '../../../core/models/product.model';

interface ErrorWithMessage {
  userMessage?: string;
}

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  loading = true;
  error = '';
  successMessage = '';
  page = 0;
  totalElements = 0;
  totalPages = 0;
  showDeleteModal = false;
  productToDelete: Product | null = null;
  updatingStock: string | null = null;

  private successTimeout: ReturnType<typeof setTimeout> | null = null;

  constructor(private readonly productService: ProductService) {}

  ngOnInit(): void {
    this.load();
  }

  ngOnDestroy(): void {
    if (this.successTimeout) {
      clearTimeout(this.successTimeout);
    }
  }

  load(): void {
    this.loading = true;
    this.error = '';
    this.productService.getAll(this.page).subscribe({
      next: (response) => {
        this.products = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err: ErrorWithMessage) => {
        this.error = err.userMessage ?? 'Erro ao carregar produtos.';
        this.loading = false;
      }
    });
  }

  updateStock(product: Product, delta: number): void {
    const newQuantity = product.stockQuantity + delta;
    if (newQuantity < 0) {
      return;
    }

    this.updatingStock = product.id;
    this.clearMessages();

    this.productService.update(product.id, {
      name: product.name,
      description: product.description,
      price: product.price,
      stockQuantity: newQuantity
    }).subscribe({
      next: (updated) => {
        product.stockQuantity = updated.stockQuantity;
        this.updatingStock = null;
        this.showSuccessMessage(`Estoque: ${product.name} → ${newQuantity}`);
      },
      error: (err: ErrorWithMessage) => {
        this.error = err.userMessage ?? 'Erro ao atualizar estoque.';
        this.updatingStock = null;
      }
    });
  }

  confirmDelete(product: Product): void {
    this.productToDelete = product;
    this.showDeleteModal = true;
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.productToDelete = null;
  }

  deleteConfirmed(): void {
    if (!this.productToDelete) {
      return;
    }

    const productName = this.productToDelete.name;
    this.productService.delete(this.productToDelete.id).subscribe({
      next: () => {
        this.showDeleteModal = false;
        this.productToDelete = null;
        this.showSuccessMessage(`"${productName}" excluído.`);
        this.load();
      },
      error: (err: ErrorWithMessage) => {
        this.error = err.userMessage ?? 'Erro ao excluir produto.';
        this.showDeleteModal = false;
        this.productToDelete = null;
      }
    });
  }

  changePage(newPage: number): void {
    this.page = newPage;
    this.load();
  }

  private showSuccessMessage(message: string): void {
    this.successMessage = message;
    if (this.successTimeout) {
      clearTimeout(this.successTimeout);
    }
    this.successTimeout = setTimeout(() => {
      this.successMessage = '';
      this.successTimeout = null;
    }, 3000);
  }

  private clearMessages(): void {
    this.error = '';
    this.successMessage = '';
  }
}

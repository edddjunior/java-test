import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { ProductRequest } from '../../../core/models/product.model';

interface ErrorWithMessage {
  userMessage?: string;
}

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit {
  product: ProductRequest = { name: '', description: '', price: 0, stockQuantity: 0 };
  isEdit = false;
  saving = false;
  loadingProduct = false;
  error = '';

  private productId = '';

  constructor(
    private readonly productService: ProductService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.params['id'];
    if (idParam) {
      this.productId = idParam;
      this.isEdit = true;
      this.loadProduct();
    }
  }

  save(): void {
    if (!this.isFormValid()) {
      return;
    }

    this.saving = true;
    this.error = '';

    const request$ = this.isEdit
      ? this.productService.update(this.productId, this.product)
      : this.productService.create(this.product);

    request$.subscribe({
      next: () => {
        this.router.navigate(['/products']);
      },
      error: (err: ErrorWithMessage) => {
        this.error = err.userMessage ?? 'Erro ao salvar produto.';
        this.saving = false;
      }
    });
  }

  private loadProduct(): void {
    this.loadingProduct = true;
    this.productService.getById(this.productId).subscribe({
      next: (product) => {
        this.product = {
          name: product.name,
          description: product.description,
          price: product.price,
          stockQuantity: product.stockQuantity
        };
        this.loadingProduct = false;
      },
      error: (err: ErrorWithMessage) => {
        this.error = err.userMessage ?? 'Erro ao carregar produto.';
        this.loadingProduct = false;
      }
    });
  }

  private isFormValid(): boolean {
    return Boolean(this.product.name) && this.product.price > 0 && this.product.stockQuantity >= 0;
  }
}

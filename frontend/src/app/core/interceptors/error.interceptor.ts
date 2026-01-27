import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let message = 'Erro desconhecido';

      if (error.status === 0) {
        message = 'Não foi possível conectar ao servidor. Verifique sua conexão.';
      } else if (error.status === 429) {
        message = 'Muitas requisições. Aguarde um momento e tente novamente.';
      } else if (error.status === 404) {
        message = error.error?.message || 'Recurso não encontrado.';
      } else if (error.status === 400) {
        message = error.error?.message || 'Dados inválidos.';
      } else if (error.status === 409) {
        message = error.error?.message || 'Conflito de dados. O recurso foi modificado.';
      } else if (error.status >= 500) {
        message = 'Erro no servidor. Tente novamente mais tarde.';
      } else if (error.error?.message) {
        message = error.error.message;
      }

      return throwError(() => ({ ...error, userMessage: message }));
    })
  );
};

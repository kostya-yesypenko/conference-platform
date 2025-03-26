import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PageEvent } from '@angular/material/paginator';
import {Page} from "../model/page";

export abstract class BaseCrudService<T> {
	protected url = '';

	protected constructor(
		theUrl: string,
		protected http: HttpClient,
	) {
		this.url = theUrl;
	}

	public handleError(error: any) {
		return throwError(error);
	}

	public abstract fromObject(entity: T): T;

	page(event?: PageEvent, search?: string, sort?: string[], expandFields?: string, isUnPaged = false) {
		let params = new HttpParams();

		if (search) {
			params = params.append('search', search);
		}

		if (sort) {
			for (const sortValue of sort) {
				params = params.append('sort', sortValue);
			}
		}

		if (event) {
			params = params.append('index', `${event.pageIndex}`);
			params = params.append('size', `${event.pageSize}`);
		}

		if (expandFields) {
			params = params.append('expand_fields', expandFields);
		}

		params = params.append('isUnPaged', `${isUnPaged}`);

		return this.http.get<Page<T>>(`${this.url}`, {params}).pipe(
			catchError(error => this.handleError(error))
		);
	}

  create(entity: T): Observable<T> {
    return this.http.post<T>(this.url, entity).pipe(
      map((response: T) => this.fromObject(response) as T),
      catchError(error => this.handleError(error))
    );
  }

  update(entity: T,id:number): Observable<T> {
    return this.http.put(`${this.url}/${id}`, entity)
      .pipe(
        map((response: any) => this.fromObject(response) as T),
        catchError(error => this.handleError(error))
      );
  }


  delete(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`)
      .pipe(
        catchError(error => this.handleError(error))
      );
  }

  getById(id: number, expandFields?: string[]): Observable<T> {
    const url = `${this.url}/${id}`;
    let params = new HttpParams();

    if (expandFields) {
      const expandValues: string = expandFields.join(',');
      params = params.append('expand_fields', expandValues);
    }

    return this.http.get<T>(url, {params})
      .pipe(
        map((response: any) => this.fromObject(response) as T),
        catchError(error => this.handleError(error))
      );
  }

}

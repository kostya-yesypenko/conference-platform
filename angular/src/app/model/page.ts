export interface Page<T> {
  items: Array<T>;
  size: number;
  empty: boolean;
  index: number;
  totalElements: number;
  last: boolean;
  first: boolean;
}

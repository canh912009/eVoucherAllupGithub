import {
  Category,
} from '@/types';
import { API_ENDPOINTS } from './api-endpoints';
import { crudFactory } from './curd-factory-ev';

export const categoryClient = {
  ...crudFactory<Category, any, Partial<Category>>(API_ENDPOINTS.CATEGORY),
};

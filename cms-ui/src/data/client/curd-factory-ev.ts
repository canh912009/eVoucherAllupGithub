import type { PathOptions, PaginatorInfoEV, BaseResponse } from '@/types';
import { HttpClient } from './http-client-ev';

export function crudFactory<Type, QueryParams, InputType>(endpoint: string) {
  return {
    all(params: QueryParams) {
      return HttpClient.get<Type[]>(endpoint, params);
    },

    paginated(pathOps: PathOptions, params: QueryParams) {
      return HttpClient.get<PaginatorInfoEV<Type>>(
        `${endpoint}/${pathOps.page}/${pathOps.pageSize}`,
        params
      );
    },

    // If endpoint is different
    _paginated(_endpoint: string, pathOps?: PathOptions, params?: QueryParams) {
      return pathOps
        ? HttpClient.get<PaginatorInfoEV<Type>>(`${_endpoint}/${pathOps.page}/${pathOps.pageSize}`, params)
        : HttpClient.get<PaginatorInfoEV<Type>>(`${_endpoint}`, params)
    },

    get(slug: string) {
      return HttpClient.get<BaseResponse<Type>>(`${endpoint}/${slug}`, {});
    },

    create(data: InputType) {
      return HttpClient.post<Type>(endpoint, data);
    },

    update({ id, ...input }: Partial<InputType> & { id?: string } & { roleCode?: string }) {
      return input.roleCode
        ? HttpClient.put<BaseResponse<Type>>(`${endpoint}/${input.roleCode}`, input)
        : HttpClient.put<BaseResponse<Type>>(`${endpoint}/${id}`, input);
    },

    updateNoId({ id, ...input }: Partial<InputType> & { id?: string } & { roleCode?: string }) {
      return HttpClient.put<BaseResponse<Type>>(`${endpoint}`, input);
    },

    patch({ id, ...input }: Partial<InputType> & { id?: string }) {
      return id
        ? HttpClient.patch<BaseResponse<Type>>(`${endpoint}/${id}`, input)
        : HttpClient.patch<BaseResponse<Type>>(`${endpoint}`, input);
    },

    delete({ id }: { id: string }) {
      return HttpClient.delete<boolean>(`${endpoint}/${id}`);
    },

    updateAdminInfo({ id, ...input }: Partial<InputType> & { id?: string }) {
      return HttpClient.put<BaseResponse<Type>>(`${endpoint}/${id}`, input);
    },
  };
}

import camelCaseKeys from 'camelcase-keys';
import { MappedPaginatorInfoEV, PaginatorInfoEV, PathOptions } from '@/types';
import { PAGE_SIZE } from './constants';

export const mapPaginatorData = (
  pathOps: PathOptions,
  obj: PaginatorInfoEV<any> | undefined
): MappedPaginatorInfoEV | null => {
  if (!obj) return null;

  const { data, ...formattedValues } = camelCaseKeys(obj);

  formattedValues.perPage = pathOps?.pageSize ?? PAGE_SIZE;
  formattedValues.currentPage = pathOps?.page ?? 1;
  if (formattedValues.totalCount % formattedValues.perPage) {
    formattedValues.lastPage =
      formattedValues.totalCount / formattedValues.perPage;
  } else {
    formattedValues.lastPage =
      formattedValues.totalCount / formattedValues.perPage + 1;
  }

  return {
    ...formattedValues,
    hasMorePages: formattedValues.lastPage !== formattedValues.currentPage,
  };
};

export const updatePaginatorFE = (pathOps: PathOptions, dataList: any[]) => {
  if (!dataList?.length) return { sliceData: [], pageInfo: null };

  /**
   * Slice list data by page current
   * */
  const { page, pageSize } = pathOps;
  const startIdx = (page! - 1) * pageSize!;
  const endIdx = page! * pageSize!;

  const sliceData = dataList?.slice(startIdx, endIdx);

  /**
   * Set paginator info
   **/
  const pageInfo = mapPaginatorData(pathOps, {
    data: sliceData,
    totalCount: dataList?.length,
  } as PaginatorInfoEV<any>);

  return {
    sliceData,
    pageInfo,
  };
};

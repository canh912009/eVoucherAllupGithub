import Router, { useRouter } from 'next/router';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';

import {
  BaseResponse,
  ExternalPinUploadData,
  ExternalPinUpload,
  ExternalPinUploadPaginator,
  ExternalPinUploadQueryOptions,
  PathOptions,

} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';
import { externalPinUpdateClient } from './client/crud-client';

export const useExternalPinUploadsQuery = (
  pathOps: PathOptions,
  options: Partial<ExternalPinUploadQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<ExternalPinUploadPaginator, Error>(
    [API_ENDPOINTS.EXTERNAL_PIN_UPLOAD_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return externalPinUpdateClient._paginated(
        API_ENDPOINTS.EXTERNAL_PIN_UPLOAD_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    externalPinUploads: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useExternalPinUploadDataQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<ExternalPinUpload>, Error>(
    [API_ENDPOINTS.EXTERNAL_PIN_UPLOAD, id],
    () => externalPinUpdateClient.get(id)
  );

  return {
    externalPinUpload: data?.data,
    error,
    loading: isLoading,
  };
};

export const createExternalPinUploadMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(externalPinUpdateClient.create, {
    onSuccess: async () => {
      // const generateRedirectUrl = Routes.goods + '/upload-new-pin/';
      Router.reload();
      // await Router.push(generateRedirectUrl, undefined, {
      //   locale: Config.defaultLanguage,
      // });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.EXTERNAL_PIN_UPLOAD);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
}
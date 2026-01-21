import Card from '@/components/common/card';
import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import VNPTEPayProviderList from '@/components/vnpt-epay/vnpt-epay-brand-list';
import Search from '@/components/vnpt-epay/vnpt-epay-search';
import {useBalanceQuery, useProvidersVNPTEpayListQuery} from '@/data/vnpt-epay';
import {VNPTEPayBalance, VNPTEPayProvidersQueryOptions as SearchValue} from '@/types';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useState } from 'react';
import Button from "@/components/ui/button";
import {formatNumber} from "@/utils/common-utils";
import {API_ENDPOINTS} from "@/data/client/api-endpoints";
import {useQueryClient} from "react-query";

export default function VNPTEPayProviders() {
  const { t } = useTranslation();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { providersList, loading, paginatorInfo, error } = useProvidersVNPTEpayListQuery(
    { page, pageSize: PAGE_SIZE },
    searchOptions
  );
  const {
    balance,
    loading: balanceLoading,
    error: balanceError
  } = useBalanceQuery();

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  function handlePagination(current: number) {
    setPage(current);
  }

  function handleSearch(data: Partial<SearchValue>) {
    setSearchOptions(data);
    setPage(1);
  }
  const queryClient = useQueryClient();
  const handleRefresh = async () => {
    await queryClient.invalidateQueries([API_ENDPOINTS.VNPT_EPAY_BALANCE]);
  };

  return (
    <>
      <div className="mb-2 flex items-center justify-between w-full px-9 py-2 ">
        <div className="flex items-center w-full justify-end">
          <h1 className="text-3xl font-semibold text-heading text-red-500 px-8">
            {balanceLoading
              ? t('Loading balance...')
              : balanceError
                ? t('Error loading balance')
                : t(`Remaining balance : ${formatNumber(balance)} VND`)}
          </h1>
        </div>
        <div className="flex-shrink-0">
          <Button
            className="bg-red-500 hover:bg-red-600 rounded-xl"
            aria-label="Refresh"
            onClick={handleRefresh}
            disabled={balanceLoading}
          >
            {balanceLoading ? t('Refreshing...') : t('Refresh')}
          </Button>
        </div>
      </div>
      <Search onSearch={handleSearch} />
      <VNPTEPayProviderList
        data={providersList}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

VNPTEPayProviders.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

VNPTEPayProviders.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

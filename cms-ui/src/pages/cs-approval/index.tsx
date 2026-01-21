import CsList from '@/components/cs/cs-list';
import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import {useCsExtendListQuery, useCsListQuery} from '@/data/cs';
import { CsExtendQueryOptions as SearchValue, SortOrder} from '@/types';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useState } from 'react';
import Search from "@/components/cs/cs-extend-search";

export default function CsExtend() {
  const { t } = useTranslation();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { csExtendList, loading, paginatorInfo, error } = useCsExtendListQuery(
    { page, pageSize: PAGE_SIZE },
    searchOptions
  );

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  function handleSearch(data: Partial<SearchValue>) {
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  return (
    <>
      <Search onSearch={handleSearch} />
      <CsList
        csList={csExtendList}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
        extendRequest={true}
      />
    </>
  );
}

CsExtend.Layout = Layout;
CsExtend.authenticate = {
  permissions: [p.ROLE_ADMIN],
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

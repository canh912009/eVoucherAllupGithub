import CsList from '@/components/cs/cs-list';
import Search from '@/components/cs/cs-search';
import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useCsListQuery } from '@/data/cs';
import { CsQueryOptions as SearchValue, SortOrder } from '@/types';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useState } from 'react';

export default function Cs() {
  const { t } = useTranslation();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({
    sort: 'deliveryDate',
    direction: SortOrder.Desc,
  });
  const [page, setPage] = useState(1);
  const [orderBy, setOrder] = useState('id');
  const [sortedBy, setSort] = useState<SortOrder>(SortOrder.Desc);
  const { csList, loading, paginatorInfo, error } = useCsListQuery(
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

  function handleOrder(newOrder: string) {
    let sort = SortOrder.Asc;
    if (newOrder == orderBy) {
      sort = sortedBy == SortOrder.Asc ? SortOrder.Desc : SortOrder.Asc;
    }

    setSort(sort);
    setOrder(newOrder);

    setSearchOptions({
      ...searchOptions,
      sort: newOrder,
      direction: sort,
    });
  }

  return (
    <>
      <Search onSearch={handleSearch} />
      <CsList
        csList={csList}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
        onOrder={(newOrder) => handleOrder(newOrder)}
      />
    </>
  );
}

Cs.Layout = Layout;
Cs.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

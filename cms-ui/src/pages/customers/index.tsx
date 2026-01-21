import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import Search from '@/components/customer/customer-search';
import CustomerList from '@/components/customer/customer-list';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { CustomerQueryOptions as SearchValue } from '@/types';
import { useCustomersQuery } from '@/data/customer';
import {PAGE_SIZE, PERMISSIONS_EV as p} from '@/utils/constants';

export default function Customers() {
  const { t } = useTranslation();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { customers, loading, paginatorInfo, error } = useCustomersQuery(
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
      <CustomerList
        customers={customers}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

Customers.Layout = Layout;
Customers.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER]
  // permissions: adminOnly,
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

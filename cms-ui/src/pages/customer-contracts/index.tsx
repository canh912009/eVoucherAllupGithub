import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import Search from '@/components/customer-contract/customer-contract-search';
import CustomerContractList from '@/components/customer-contract/customer-contract-list';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { CustomerQueryOptions as SearchValue } from '@/types';
import { useCustomerContractsQuery } from '@/data/customer-contract';
import { useRouter } from 'next/router';
import {PAGE_SIZE, PERMISSIONS_EV as p} from '@/utils/constants';

export default function CustomerContracts() {
  const { t } = useTranslation();
  const { locale } = useRouter();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { customerContracts, loading, paginatorInfo, error } =
    useCustomerContractsQuery({ page, pageSize: PAGE_SIZE }, searchOptions);

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
      <CustomerContractList
        customerContracts={customerContracts}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

CustomerContracts.Layout = Layout;
CustomerContracts.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER]
};


export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

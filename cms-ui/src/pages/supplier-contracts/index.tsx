import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import Search from '@/components/supplier-contract/supplier-contract-search';
import SupplierContractList from '@/components/supplier-contract/supplier-contract-list';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { SupplierQueryOptions as SearchValue } from '@/types';
import { useSupplierContractsQuery } from '@/data/supplier-contract';
import { useRouter } from 'next/router';
import {PAGE_SIZE, PERMISSIONS_EV as p} from '@/utils/constants';

export default function SupplierContracts() {
  const { t } = useTranslation();
  const { locale } = useRouter();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { supplierContracts, loading, paginatorInfo, error } = useSupplierContractsQuery(
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
      <SupplierContractList
        supplierContracts={supplierContracts}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

SupplierContracts.Layout = Layout;
SupplierContracts.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER]
};


export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

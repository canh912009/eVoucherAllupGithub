import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import Search from '@/components/vnpt-epay/vnpt-epay-pin-search';
import SupplierList from '@/components/supplier/supplier-list';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { VNPTEPayProvidersQueryOptions as SearchValue } from '@/types';
import { adminOnly, allowedRolesEV } from '@/utils/auth-utils';
import { useSuppliersQuery } from '@/data/supplier';
import { useRouter } from 'next/router';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { usePINListQuery } from '@/data/vnpt-epay';
import VNPTEPayPINList from '@/components/vnpt-epay/vnpt-epay-pin-list';
import VNPTEPayBalanceComponent from '@/components/vnpt-epay/vnpt-epay-balance';
import VNPTEPayPINPurchaseComponent from '@/components/vnpt-epay/vnpt-epay-pin-purchase';

export default function VNPTEPayPINPurchasePage() {
  const { t } = useTranslation();
  const { locale } = useRouter();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { pinList, loading, paginatorInfo, error } = usePINListQuery(
    { page, pageSize: PAGE_SIZE },
    searchOptions
  );

  // if (loading) return <Loader text={t('common:text-loading')} />;
  // if (error) return <ErrorMessage message={error.message} />;

  function handleSearch(data: Partial<SearchValue>) {
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  return (
    <>
      <VNPTEPayPINPurchaseComponent />
      <VNPTEPayBalanceComponent />
      <Search onSearch={handleSearch} />

       <VNPTEPayPINList
        data={pinList}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

VNPTEPayPINPurchasePage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

VNPTEPayPINPurchasePage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

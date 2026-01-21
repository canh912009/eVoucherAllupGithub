import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { GoodQueryOptions } from '@/types';
import { adminOnly, allowedRolesEV } from '@/utils/auth-utils';
import { useGoodsQuery } from '@/data/good';
import { useRouter } from 'next/router';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import LinkButton from '@/components/ui/link-button';
import GoodsList from '@/components/good/good-list';
import GoodSearch from '@/components/good/good-search';

export default function Good() {
  const { t } = useTranslation();
  const { locale } = useRouter();

  const [searchOptions, setSearchOptions] = useState<Partial<GoodQueryOptions>>({});
  const [page, setPage] = useState(1);
  const { goods, loading, paginatorInfo, error } = useGoodsQuery(
    { page, pageSize: PAGE_SIZE },
    searchOptions
  );

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  function handleSearch(data: Partial<GoodQueryOptions>) {
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  return (
    <>
      <GoodSearch onSearch={handleSearch} />
      <GoodsList
        goods={goods}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}
Good.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER, p.ROLE_BRAND]
};
Good.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

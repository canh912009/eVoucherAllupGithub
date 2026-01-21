import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import BrandsList from '@/components/brands/brands-list';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { BrandsQueryOptions as SearchValue} from '@/types';
import { useBrandsQuery } from '@/data/brands';
import { useRouter } from 'next/router';
import {PAGE_SIZE, PERMISSIONS_EV as p} from '@/utils/constants';
import BrandSearch from '@/components/brands/brands-search';

export default function Brands() {
  const { t } = useTranslation();
  const { locale } = useRouter();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { brands, loading, paginatorInfo, error } = useBrandsQuery(
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
      <BrandSearch onSearch={handleSearch} />
      <BrandsList
        brands={brands}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}
Brands.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER]
};
Brands.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

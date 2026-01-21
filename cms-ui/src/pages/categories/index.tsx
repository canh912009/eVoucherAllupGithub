import CategoryList from '@/components/categories/category-list';
import CategorySearch from '@/components/categories/category-search';
import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useCategoriesQuery } from '@/data/categories';
import { CategoryQueryOptions } from '@/types';
import { adminOnly } from '@/utils/auth-utils';
import { PAGE_SIZE } from '@/utils/constants';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';

export default function Category() {
  const { t } = useTranslation();

  const [searchOptions, setSearchOptions] = useState<
    Partial<CategoryQueryOptions>
  >({});
  const [page, setPage] = useState(1);
  const { categories, loading, paginatorInfo, error } = useCategoriesQuery(
    { page, pageSize: PAGE_SIZE },
    searchOptions
  );

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  function handleSearch(data: Partial<CategoryQueryOptions>) {
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }
  return (
    <>
      <CategorySearch onSearch={handleSearch} />
      <CategoryList
        categories={categories}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

Category.authenticate = {
  permissions: adminOnly, //[p.ROLE_ADMIN, p.ROLE_OPERATOR]
};

Category.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

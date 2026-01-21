import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import Search from '@/components/menu-group/menu-group-search';
// import SupplierList from '@/components/supplier/supplier-list';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {MenuGroupQueryOptions as SearchValue} from '@/types';
import { adminOnly } from '@/utils/auth-utils';
import { useMenuGroupsQuery } from '@/data/menu-group';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import SupplierList from "@/components/supplier/supplier-list";
import MenuGroupList from "@/components/menu-group/menu-group-list";

export default function MenuGroups() {
  const { t } = useTranslation();
  const { locale } = useRouter();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { menuGroups, loading, paginatorInfo, error } = useMenuGroupsQuery(
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
      <MenuGroupList
        menuGroups={menuGroups}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

MenuGroups.authenticate = {
  permissions: adminOnly,
};

MenuGroups.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

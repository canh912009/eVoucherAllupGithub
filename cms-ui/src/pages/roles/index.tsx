import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import Search from '@/components/role/role-search';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {RoleQueryOptions as SearchValue} from '@/types';
import { adminOnly } from '@/utils/auth-utils';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import {useRolesQuery} from "@/data/role";
import RoleList from "@/components/role/role-list";

export default function Roles() {
  const { t } = useTranslation();
  const { locale } = useRouter();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { roles, loading, paginatorInfo, error } = useRolesQuery(
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
      <RoleList
        roles={roles}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

Roles.authenticate = {
  permissions: adminOnly,
};

Roles.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

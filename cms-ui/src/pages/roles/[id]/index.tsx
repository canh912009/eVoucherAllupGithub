import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import {adminOnly, getAuthCredentials} from '@/utils/auth-utils';
import {useRoleQuery} from "@/data/role";
import RoleDetail from "@/components/role/role-detail";

const RolePage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { role, loading, error } = useRoleQuery(query?.id as string);
  // const { token, permissions } = getAuthCredentials();
  // const {role, loading, error} = useRoleQuery(permissions ? permissions[0] : "");

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return <RoleDetail data={role} />;
};

RolePage.authenticate = {
  permissions: adminOnly,
};
RolePage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common','table'])),
  },
});

export default RolePage;

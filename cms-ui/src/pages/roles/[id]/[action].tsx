import Layout from '@/components/layouts/owner';
import CreateOrUpdateMenuGroupForm from '@/components/menu-group/menu-group-form';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import {adminOnly} from "@/utils/auth-utils";
import {useRoleQuery} from "@/data/role";
import CreateOrUpdateRoleForm from "@/components/role/role-form";

export default function UpdateRolePage() {
  const { query, locale } = useRouter();
  const { t } = useTranslation();
  const { role, loading, error } = useRoleQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading">
          {t('Edit Role')}
        </h1>
      </div>
      <CreateOrUpdateRoleForm initialValues={role} />
    </>
  );
}

UpdateRolePage.Layout = Layout;
UpdateRolePage.authenticate = {
  permissions: adminOnly,
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

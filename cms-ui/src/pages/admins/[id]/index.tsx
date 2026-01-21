import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { adminOnly, allowedRolesEV } from '@/utils/auth-utils';
import AdminDetail from '@/components/admin/admin-detail';
import { useAdminQuery } from '@/data/admin';

const AccountPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { admin, loading, error } = useAdminQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <>
      <div className="flex md:py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading uppercase text-red-600">
          {t('ACCOUNT INFORMATION')}
        </h1>
      </div>
      <AdminDetail data={admin} />
    </>
  )
};

AccountPage.authenticate = {
  // permissions: adminOnly,
  permissions: allowedRolesEV,
};
AccountPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common'])),
  },
});

export default AccountPage;

import CreateOrUpdateAdminForm from '@/components/admin/admin-form';
import Layout from '@/components/layouts/owner';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';

export default function CreateAdminPage() {
  const { t } = useTranslation();
  return (
    <>
      <div className="flex md:py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading uppercase text-red-600">
          {t('Create New Account')}
        </h1>
      </div>
      <CreateOrUpdateAdminForm />
    </>
  );
}
CreateAdminPage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});
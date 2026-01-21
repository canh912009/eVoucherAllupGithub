import Layout from '@/components/layouts/owner';
import CreateOrUpdateStoreForm from '@/components/store/store-form';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useStoreQuery } from '@/data/store';
import { useRouter } from 'next/router';
import {PERMISSIONS_EV} from "@/utils/constants";

export default function UpdateStorePage() {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { store, loading, error } = useStoreQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('Edit Chained Store Information')}
        </h1>
      </div>
      <CreateOrUpdateStoreForm initialValues={store} />
    </>
  );
}
UpdateStorePage.Layout = Layout;
UpdateStorePage.authenticate = {
  permissions: [PERMISSIONS_EV.ROLE_ADMIN, PERMISSIONS_EV.ROLE_OPERATOR],
};


export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

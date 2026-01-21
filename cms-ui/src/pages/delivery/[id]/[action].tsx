import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import CreateOrUpdateDeliveryForm from '@/components/delivery/delivery-form';
import { useDeliveryQuery } from '@/data/delivery';
import { PERMISSIONS_EV as p } from '@/utils/constants';

export default function UpdateDeliveryPage() {
  const { query } = useRouter();
  const { t } = useTranslation();

  const { delivery, loading, error } = useDeliveryQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-4">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('Edit Delivery')}
        </h1>
      </div>
      <CreateOrUpdateDeliveryForm initialValues={delivery} />
    </>
  );
}
UpdateDeliveryPage.Layout = Layout;

UpdateDeliveryPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

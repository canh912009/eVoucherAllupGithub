import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { useDeliveryQuery } from '@/data/delivery';
import { useRouter } from 'next/router';
import DeliveryDetail from '@/components/delivery/delivery-detail';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { PERMISSIONS_EV as p } from '@/utils/constants';

const DeliveryPage = () => {
  const { t } = useTranslation();
  const { query } = useRouter();
  const { delivery, loading, error } = useDeliveryQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-4">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('View Delivery Information')}
        </h1>
      </div>
      <DeliveryDetail initialValues={delivery} />
    </>
  );
};
DeliveryPage.Layout = Layout;

DeliveryPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default DeliveryPage;

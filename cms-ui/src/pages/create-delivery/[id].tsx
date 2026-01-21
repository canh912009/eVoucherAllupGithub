import Layout from '@/components/layouts/owner';
import Loader from '@/components/ui/loader/loader';
import ErrorMessage from '@/components/ui/error-message';
import CreateOrUpdateDeliveryForm from '@/components/delivery/delivery-form';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useCampaignQuery } from '@/data/campaign';
import { useRouter } from 'next/router';
import { adminOnly } from '@/utils/auth-utils';

const CreateDeliveryPage = () => {
  const { t } = useTranslation();
  const { query } = useRouter();
  const {
    campaign,
    loading: loadingCampaign,
    error: errorCampaign,
  } = useCampaignQuery(query.id as string);

  if (loadingCampaign) return <Loader text={t('common:text-loading')} />;
  if (errorCampaign) return <ErrorMessage message={errorCampaign.message} />;

  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-4">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('Create Delivery')}
        </h1>
      </div>
      {/*<CreateOrUpdateDeliveryForm campaign={campaign} />*/}
    </>
  );
};
CreateDeliveryPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

CreateDeliveryPage.authenticate = {
  permissions: adminOnly,
};

export default CreateDeliveryPage;

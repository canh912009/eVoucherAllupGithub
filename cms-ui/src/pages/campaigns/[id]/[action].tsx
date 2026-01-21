import Layout from '@/components/layouts/owner';
import CreateOrUpdateCampaignForm from '@/components/campaign/campaign-form';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useCampaignQuery } from '@/data/campaign';
import { useRouter } from 'next/router';
import { PERMISSIONS_EV as p } from '@/utils/constants';

export default function UpdateCampaignPage() {
  const { query } = useRouter();
  const { t } = useTranslation();
  const {
    campaign,
    loading: loadingCampaign,
    error: errorCampaign,
  } = useCampaignQuery(query.id as string);

  if (loadingCampaign) return <Loader text={t('common:text-loading')} />;
  if (errorCampaign) return <ErrorMessage message={errorCampaign.message} />;

  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('Edit Campaign')}
        </h1>
      </div>
      <CreateOrUpdateCampaignForm initialValues={campaign} />
    </>
  );
}
UpdateCampaignPage.Layout = Layout;

UpdateCampaignPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

import Layout from '@/components/layouts/owner';
import CreateOrUpdateCampaignForm from '@/components/campaign/campaign-form';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { PERMISSIONS_EV as p } from '@/utils/constants';

export default function CreateCampaignPage() {
  const { t } = useTranslation();
  return (
    <>
      <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('Create Campaign')}
        </h1>
      </div>
      {/*<CreateOrUpdateCampaignForm />*/}
    </>
  );
}
CreateCampaignPage.Layout = Layout;

CreateCampaignPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { adminOnly } from '@/utils/auth-utils';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import {useProviderQuery} from "@/data/vnpt-epay";
import ProviderDetail from "@/components/vnpt-epay/provider-detail";
import {useProviderXpayQuery} from "@/data/xpay";

const VnptEpayProviderPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { provider, loading, error } = useProviderXpayQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <>
      <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
        <h1 className="text-lg font-semibold uppercase text-red-500">
          {t('Provider Information')}
        </h1>
      </div>

      <ProviderDetail initialValues={provider} isXPAY={true} />
    </>
  );
};

VnptEpayProviderPage.authenticate = {
  permissions: adminOnly,
};
VnptEpayProviderPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common'])),
  },
});

export default VnptEpayProviderPage;

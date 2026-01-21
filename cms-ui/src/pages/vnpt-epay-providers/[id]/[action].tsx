import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import CreateOrUpdateProviderForm from "@/components/vnpt-epay/provider-form";
import {useProviderQuery} from "@/data/vnpt-epay";

export default function UpdateProviderPage() {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { provider, loading, error } = useProviderQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold uppercase text-red-500">
          {t('Edit Provider')}
        </h1>
      </div>
      <CreateOrUpdateProviderForm initialValues={provider} />
    </>
  );
}
UpdateProviderPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

import Layout from '@/components/layouts/owner';
import CreateOrUpdateGooodForm from '@/components/good/good-form';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useGoodQuery } from '@/data/good';
import { useRouter } from 'next/router';
import {PERMISSIONS_EV} from "@/utils/constants";

export default function UpdateGoodPage() {
  const { query, locale } = useRouter();
  const { t } = useTranslation();
  const { good, loading, error } = useGoodQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <>
      <CreateOrUpdateGooodForm initialValues={good} />
    </>
  );
}
UpdateGoodPage.Layout = Layout;
UpdateGoodPage.authenticate = {
  permissions: [PERMISSIONS_EV.ROLE_ADMIN, PERMISSIONS_EV.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

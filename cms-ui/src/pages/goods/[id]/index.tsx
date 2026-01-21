import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useGoodQuery } from '@/data/good';
import { useRouter } from 'next/router';
import { adminOnly } from '@/utils/auth-utils';
import GoodDetail from '@/components/good/good-detail';
import {PERMISSIONS_EV as p} from "@/utils/constants";

const GoodPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { good, loading, error } = useGoodQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <GoodDetail data={good} />
    </>
  )
};

GoodPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER]
};
GoodPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common'])),
  },
});

export default GoodPage;

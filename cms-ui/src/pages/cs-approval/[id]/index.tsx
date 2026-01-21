import CsDetail from '@/components/cs/cs-detail';
import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import {useCsExtendQuery, useCsQuery} from '@/data/cs';
import { PERMISSIONS_EV as p } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import CsExtendDetail from "@/components/cs/cs-extend-detail";

const CsExtendPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const {
    csExtend,
    loading: loadingCs,
    error: errorCs,
  } = useCsExtendQuery(query.id as string);

  if (loadingCs) return <Loader text={t('common:text-loading')} />;
  if (errorCs) return <ErrorMessage message={errorCs.message} />;

  return (
    <>
      <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
        {t('Admin Approval')}
      </h1>
      <CsExtendDetail initialValues={csExtend} />
    </>
  );
};

CsExtendPage.Layout = Layout;

CsExtendPage.authenticate = {
  permissions: [p.ROLE_ADMIN],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default CsExtendPage;

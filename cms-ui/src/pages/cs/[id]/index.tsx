import CsDetail from '@/components/cs/cs-detail';
import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useCsQuery } from '@/data/cs';
import { PERMISSIONS_EV as p } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';

const CsPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const {
    cs,
    loading: loadingCs,
    error: errorCs,
  } = useCsQuery(query.id as string);

  if (loadingCs) return <Loader text={t('common:text-loading')} />;
  if (errorCs) return <ErrorMessage message={errorCs.message} />;

  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('Detailed PIN Information')}
        </h1>
      </div>
      <CsDetail initialValues={cs} />
    </>
  );
};

CsPage.Layout = Layout;

CsPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default CsPage;

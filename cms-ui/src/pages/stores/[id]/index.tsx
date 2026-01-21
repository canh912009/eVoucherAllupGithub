import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useStoreQuery } from '@/data/store';
import { useRouter } from 'next/router';
import { adminOnly } from '@/utils/auth-utils';
import StoreDetail from '@/components/store/store-detail';
import {PERMISSIONS_EV as p} from "@/utils/constants";

const StorePage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { store, loading, error } = useStoreQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading uppercase text-red-500">
          {t('View Chained Store Information')}
        </h1>
      </div>
      <StoreDetail initialValues={store} />
    </>
  );
};

StorePage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER]
};
StorePage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

export default StorePage;

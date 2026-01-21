import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { useBrandInfoQuery } from '@/data/brands';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import DetailBrandForm from '@/components/brands/brands-form-detail';
import { PERMISSIONS_EV as p } from '@/utils/constants';

const BrandsPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { brands, loading, error } = useBrandInfoQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <div className="bg-white">
      <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
        <h1 className="ml-4 text-lg font-semibold text-heading text-red-500   ">
          {t('View Brand').toUpperCase()}
        </h1>
      </div>
      <DetailBrandForm initialValues={brands} viewPage={true} />
    </div>
  );
};

BrandsPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER],
};

BrandsPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common'])),
  },
});

export default BrandsPage;

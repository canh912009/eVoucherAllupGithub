import Layout from '@/components/layouts/owner';
import CreateOrUpdateBrandForm from '@/components/brands/brands-form';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useUpdateBrandQuery } from '@/data/brands';
import { useRouter } from 'next/router';
import BrandStoreListQr from '@/components/brands/brands-store-list-qr';
import { PERMISSIONS_EV as p } from '@/utils/constants';

export default function UpdateBrandPage() {
  const { query } = useRouter();
  const action = query.action?.toString();

  const { t } = useTranslation();
  const { brands, loading, error } = useUpdateBrandQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  let content;

  if (action === 'store-list-qr') {
    content = <BrandStoreListQr data={brands} />;
  } else {
    content = (
      <div className="bg-white">
        <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
          <h1 className="ml-4 text-lg font-semibold uppercase text-heading text-red-500">
            {t('Edit Brand')}
          </h1>
        </div>
        <CreateOrUpdateBrandForm initialValues={brands} />
      </div>
    );
  }

  return <div>{content}</div>;
}

UpdateBrandPage.Layout = Layout;

UpdateBrandPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

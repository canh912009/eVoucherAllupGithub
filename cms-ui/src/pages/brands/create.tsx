import Layout from '@/components/layouts/owner';
import CreateOrUpdateBrandsForm from '@/components/brands/brands-form';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { PERMISSIONS_EV as p } from '@/utils/constants';

export default function CreateBrandPage() {
  const { t } = useTranslation();
  return (
    <div className="bg-white">
      <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
        <h1 className="ml-4 text-lg font-semibold uppercase text-heading text-red-500">
          {t('Create Brand')}
        </h1>
      </div>
      <CreateOrUpdateBrandsForm />
    </div>
  );
}

CreateBrandPage.Layout = Layout;
CreateBrandPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

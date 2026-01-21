import Layout from '@/components/layouts/owner';
import CreateOrUpdateSupplierForm from '@/components/supplier/supplier-form';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useSupplierQuery } from '@/data/supplier';
import { useRouter } from 'next/router';
import CreateSupplierPage from "@/pages/suppliers/create";
import {PERMISSIONS_EV as p} from "@/utils/constants";
import SupplierPage from "@/pages/suppliers/[id]/index";

export default function UpdateSupplierPage() {
  const { query, locale } = useRouter();
  const { t } = useTranslation();
  const { supplier, loading, error } = useSupplierQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading">
          {t('Edit Supplier')}
        </h1>
      </div>
      <CreateOrUpdateSupplierForm initialValues={supplier} />
    </>
  );
}
UpdateSupplierPage.Layout = Layout;
UpdateSupplierPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER]
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

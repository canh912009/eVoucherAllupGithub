import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useSupplierQuery } from '@/data/supplier';
import { useRouter } from 'next/router';
import SupplierFormDetail from "@/components/supplier/supplier-form-detail";
import {PERMISSIONS_EV as p} from "@/utils/constants";

const SupplierPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { supplier, loading, error } = useSupplierQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <div className="bg-white">
      <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
        <h1 className="text-lg font-semibold text-heading text-red-500 ml-4   ">
          {t('View Supplier').toUpperCase()}
        </h1>
      </div>
      <SupplierFormDetail initialValues={supplier} viewPage={true}/>
    </div>
  )

};

SupplierPage.Layout = Layout;
SupplierPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER]
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common'])),
  },
});

export default SupplierPage;

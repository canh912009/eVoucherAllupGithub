import Layout from '@/components/layouts/owner';
import CreateOrUpdateSupplierForm from '@/components/supplier/supplier-form';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {PERMISSIONS_EV as p} from "@/utils/constants";
import SupplierPage from "@/pages/suppliers/[id]";

export default function CreateSupplierPage() {
  const { t } = useTranslation();
  return (
    <div className="bg-white">
      <div className="flex border-b border-dashed border-border-base md:py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading text-red-500 ml-4 uppercase">
          {t('Create Supplier')}
        </h1>
      </div>
      <CreateOrUpdateSupplierForm />
    </div>
  );
}
CreateSupplierPage.Layout = Layout;
CreateSupplierPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER]
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

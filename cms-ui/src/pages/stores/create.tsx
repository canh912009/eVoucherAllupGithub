import Layout from '@/components/layouts/owner';
import CreateOrUpdateStoreForm from '@/components/store/store-form';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import UpdateStorePage from "@/pages/stores/[id]/[action]";
import {PERMISSIONS_EV} from "@/utils/constants";

export default function CreateStorePage() {
  const { t } = useTranslation();
  return (
    <>
      <div className="flex border-b border-dashed border-border-base md:py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading text-red-500 uppercase">
          {t('Create Chained Store')}
        </h1>
      </div>
      <CreateOrUpdateStoreForm />
    </>
  );
}
CreateStorePage.Layout = Layout;
CreateStorePage.authenticate = {
  permissions: [PERMISSIONS_EV.ROLE_ADMIN, PERMISSIONS_EV.ROLE_OPERATOR],
};


export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

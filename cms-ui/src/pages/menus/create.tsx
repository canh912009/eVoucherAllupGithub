import Layout from '@/components/layouts/owner';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {adminOnly} from "@/utils/auth-utils";
import CreateOrUpdateMenuForm from "@/components/menu/menu-form";

export default function CreateMenuPage() {
  const { t } = useTranslation();
  return (
    <>
      <div className="flex border-b border-dashed border-border-base md:py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading">
          {t('Add Menu')}
        </h1>
      </div>
      <CreateOrUpdateMenuForm />
    </>
  );
}
CreateMenuPage.Layout = Layout;
CreateMenuPage.authenticate = {
  permissions: adminOnly,
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

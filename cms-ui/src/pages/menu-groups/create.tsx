import Layout from '@/components/layouts/owner';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {adminOnly} from "@/utils/auth-utils";
import CreateOrUpdateMenuGroupForm from "@/components/menu-group/menu-group-form";

export default function CreateMenuGroupPage() {
  const { t } = useTranslation();
  return (
    <>
      <div className="flex border-b border-dashed border-border-base md:py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading">
          {t('Add MenuGroup')}
        </h1>
      </div>
      <CreateOrUpdateMenuGroupForm />
    </>
  );
}
CreateMenuGroupPage.Layout = Layout;
CreateMenuGroupPage.authenticate = {
  permissions: adminOnly,
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

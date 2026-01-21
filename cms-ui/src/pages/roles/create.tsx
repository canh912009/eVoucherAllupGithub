import Layout from '@/components/layouts/owner';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {adminOnly} from "@/utils/auth-utils";
import CreateOrUpdateRoleForm from "@/components/role/role-form";

export default function CreateRolePage() {
  const { t } = useTranslation();
  return (
    <>
      <div className="flex border-b border-dashed border-border-base md:py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading">
          {t('Add Role')}
        </h1>
      </div>
      <CreateOrUpdateRoleForm />
    </>
  );
}
CreateRolePage.Layout = Layout;
CreateRolePage.authenticate = {
  permissions: adminOnly,
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

import Layout from '@/components/layouts/owner';
import CreateOrUpdateGoodForm from '@/components/good/good-form';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {PERMISSIONS_EV} from "@/utils/constants";
import UpdateGoodPage from "@/pages/goods/[id]/[action]";

export default function CreateGoodPage() {
  const { t } = useTranslation();
  return (
    <>
      <CreateOrUpdateGoodForm />
    </>
  );
}
CreateGoodPage.Layout = Layout;
CreateGoodPage.authenticate = {
  permissions: [PERMISSIONS_EV.ROLE_ADMIN, PERMISSIONS_EV.ROLE_OPERATOR],
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

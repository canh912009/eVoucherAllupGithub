import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { adminOnly } from '@/utils/auth-utils';
import {useMenuGroupQuery} from "@/data/menu-group";
import MenuGroupDetail from "@/components/menu-group/menu-group-detail";

const MenuGroupPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { menuGroup, loading, error } = useMenuGroupQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return <MenuGroupDetail data={menuGroup} />;
};

MenuGroupPage.authenticate = {
  permissions: adminOnly,
};
MenuGroupPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common','table'])),
  },
});

export default MenuGroupPage;

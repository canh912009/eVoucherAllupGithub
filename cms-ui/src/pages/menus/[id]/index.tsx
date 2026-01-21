import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { adminOnly } from '@/utils/auth-utils';
import {useMenuQuery} from "@/data/menu";
import MenuDetail from "@/components/menu/menu-detail";

const MenuPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { menu, loading, error } = useMenuQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return <MenuDetail data={menu} />;
};

MenuPage.authenticate = {
  permissions: adminOnly,
};
MenuPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common'])),
  },
});

export default MenuPage;

import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import {adminOnly} from "@/utils/auth-utils";
import {useMenuQuery} from "@/data/menu";
import CreateOrUpdateMenuForm from "@/components/menu/menu-form";

export default function UpdateMenuPage() {
  const { query, locale } = useRouter();
  const { t } = useTranslation();
  const { menu, loading, error } = useMenuQuery(query.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold text-heading">
          {t('Edit Menu ')}
        </h1>
      </div>
      <CreateOrUpdateMenuForm initialValues={menu} />
    </>
  );
}

UpdateMenuPage.Layout = Layout;
UpdateMenuPage.authenticate = {
  permissions: adminOnly,
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

import Layout from '@/components/layouts/owner';
import CreateOrUpdateCustomerForm from '@/components/customer/customer-form';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';

export default function CreateCustomerPage() {
  const { t } = useTranslation();
  return (
    <div className="bg-white">
      <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
        <h1 className="text-lg font-semibold text-heading text-red-500 ml-4">
          {t('Create Customer').toUpperCase()}
        </h1>
      </div>
      <CreateOrUpdateCustomerForm />
    </div>
  );
}
CreateCustomerPage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

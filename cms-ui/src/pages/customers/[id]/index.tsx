import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useCustomerQuery } from '@/data/customer';
import { useRouter } from 'next/router';
import CustomerDetail from '@/components/customer/customer-detail';
import CreateOrUpdateCustomerForm from "@/components/customer/customer-form";
import CustomerFormDetail from "@/components/customer/customer-form-detail";

const CustomerPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { customer, loading, error } = useCustomerQuery(query?.id as string);

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <div className="bg-white">
      <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
        <h1 className="text-lg font-semibold text-heading text-red-500 ml-4  ">
          {t('View Customer').toUpperCase()}
        </h1>
      </div>
      <CustomerFormDetail initialValues={customer} viewPage={true}/>
    </div>
  );
};

CustomerPage.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['common'])),
  },
});

export default CustomerPage;

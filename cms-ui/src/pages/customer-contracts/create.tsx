import Layout from '@/components/layouts/owner';
import CreateOrUpdateCustomerContractForm from '@/components/customer-contract/customer-contract-form';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { PERMISSIONS_EV as p } from '@/utils/constants';

export default function CreateCustomerContractPage() {
  const { t } = useTranslation();

  return (
    <>
      <div className="flex border-b border-dashed border-border-base sm:py-8 md:py-5">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('Create Customer Contract')}
        </h1>
      </div>
      <CreateOrUpdateCustomerContractForm />
    </>
  );
}
CreateCustomerContractPage.Layout = Layout;

CreateCustomerContractPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common'])),
  },
});

import Layout from '@/components/layouts/owner';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useSupplierContractQuery } from '@/data/supplier-contract';
import { useRouter } from 'next/router';
import SupplierContractDetail from '@/components/supplier-contract/supplier-contract-detail';
import { PERMISSIONS_EV as p } from '@/utils/constants';

const SupplierContractPage = () => {
  const { query } = useRouter();
  const { t } = useTranslation();
  const { supplierContract, loading, error } = useSupplierContractQuery(
    query?.id as string
  );

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <>
      <div className="flex border-b border-dashed border-border-base py-5 sm:py-8">
        <h1 className="text-lg font-semibold uppercase text-heading text-red-500">
          {t('View Supplier Contract')}
        </h1>
      </div>
      <SupplierContractDetail initialValues={supplierContract} />
    </>
  );
};

SupplierContractPage.Layout = Layout;

SupplierContractPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default SupplierContractPage;

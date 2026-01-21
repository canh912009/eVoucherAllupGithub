import Layout from '@/components/layouts/owner';
import Button from '@/components/ui/button';
import UrBoxGoodDetail from '@/components/urbox/urbox-good-detail';
import { getGoodList } from '@/data/urbox';
import { UrBoxGood } from '@/types';
import { PERMISSIONS_EV as p } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';

const UrBoxGoodDetailPage = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const { id: brandId, good: goodId } = router.query;
  const [good, setGood] = useState<UrBoxGood>();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getGoodList(brandId as string);
        // console.log(data);
        setGood(data.find((item) => item.id === goodId));
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [brandId]);

  return (
    <>
      {good && <UrBoxGoodDetail initialValues={good} />}

      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>
      </div>
    </>
  );
};

UrBoxGoodDetailPage.Layout = Layout;

UrBoxGoodDetailPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default UrBoxGoodDetailPage;

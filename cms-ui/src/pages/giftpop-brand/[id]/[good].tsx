import GiftPopGoodDetail from '@/components/giftpop/giftpop-good-detail';
import Layout from '@/components/layouts/owner';
import Button from '@/components/ui/button';
import { getGoodList } from '@/data/giftpop';
import { GiftPopGood } from '@/types';
import { PERMISSIONS_EV as p } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';

const GiftPopGoodDetailPage = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const { id: brandId, good: goodId } = router.query;
  const [good, setGood] = useState<GiftPopGood>();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getGoodList(brandId as string);
        // console.log(data);
        setGood(data.find((item) => item.goodsId === goodId));
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [brandId]);

  return (
    <>
      {good && <GiftPopGoodDetail initialValues={good} />}

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

GiftPopGoodDetailPage.Layout = Layout;

GiftPopGoodDetailPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default GiftPopGoodDetailPage;

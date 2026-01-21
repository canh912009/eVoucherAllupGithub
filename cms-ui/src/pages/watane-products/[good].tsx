import Layout from '@/components/layouts/owner';
import Button from '@/components/ui/button';
import {getGoodDetails, getGoodList} from '@/data/watane';
import {UrBoxGood, WataneGood} from '@/types';
import { PERMISSIONS_EV as p } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import WataneGoodDetai from "@/components/watane/watane-good-detail";

const WataneGoodDetailPage = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const [good, setGood] = useState<WataneGood>();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getGoodDetails(router.query.good as string);
        // console.log(data);
        // @ts-ignore
        setGood(data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  return (
    <>
      {good && <WataneGoodDetai initialValues={good} />}

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

WataneGoodDetailPage.Layout = Layout;

WataneGoodDetailPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default WataneGoodDetailPage;

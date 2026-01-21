import Card from '@/components/common/card';
import Layout from '@/components/layouts/owner';
import Button from '@/components/ui/button';
import { useModalState } from '@/components/ui/modal/modal.context';
import UrBoxGoodList from '@/components/urbox/urbox-good-list';
import { getGoodList } from '@/data/watane';
import {
  Brands,
  MappedPaginatorInfoEV,
  WataneGood
} from '@/types';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { updatePaginatorFE } from '@/utils/data-mappers-ev';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import WataneGoodList from "@/components/watane/watane-good-list";

type IProps = {
  checkAddWatane?: boolean;
  selectedBrand?: Brands;
};

const WataneGoodsPage = ({ checkAddWatane = false, selectedBrand }: IProps) => {
  const { data } = useModalState();
  const router = useRouter();
  const { query } = useRouter();
  const { t } = useTranslation();

  const IS_PAGINATION = true; 
  const [goods, setGoods] = useState<WataneGood[]>([]);
  const [goodSlices, setGoodSlices] = useState<WataneGood[]>([]);

  const [page, setPage] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  function handlePagination(current: number) {
    // console.log('current', current);
    setPage(current);
  }

  const [selectedItems, setSelectedItems] = useState<WataneGood[]>([]);
  function handleSaveWataneGood() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveWatane(selectedItems);
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getGoodList();
        // console.log('data', data);
        setGoods(data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (IS_PAGINATION) {
      // console.log('update paginator', page, goods.length);
      const data = updatePaginatorFE(
        { page: page, pageSize: PAGE_SIZE },
        goods
      );
      setGoodSlices(data.sliceData);
      setPaginatorInfo(data.pageInfo!);
    }
  }, [page, goods]);

  return (
    <div className="flex flex-wrap">
      <Card className="w-full sm:w-full md:w-full">
        <div className="mb-8 flex w-full flex-wrap items-center justify-between">
          <h2 className="text-4xl font-semibold uppercase text-red-500">
            {t('Watane Products')}
          </h2>
        </div>

        {checkAddWatane ? (
          <WataneGoodList
            data={IS_PAGINATION ? goodSlices : goods}
            paginatorInfo={paginatorInfo}
            onPagination={handlePagination}
            isPagination={IS_PAGINATION}
            checkAddUrBox={checkAddWatane}
            selectedItems={selectedItems}
            setSelectedItems={setSelectedItems}
          />
        ) : (
          <WataneGoodList
            data={IS_PAGINATION ? goodSlices : goods}
            paginatorInfo={paginatorInfo}
            onPagination={handlePagination}
            isPagination={IS_PAGINATION}
            checkAddUrBox={checkAddWatane}
          />
        )}

        <div className="mb-4 text-end">
          {checkAddWatane ? (
            selectedItems.length > 0 && (
              <Button
                size="medium"
                className="bg-red-600 hover:bg-red-700"
                onClick={handleSaveWataneGood}
              >
                {'Save'}
              </Button>
            )
          ) : (
            <Button
              variant="outline"
              onClick={router.back}
              className="me-4 hover:bg-red-800"
              type="button"
            >
              {t('form:button-label-back')}
            </Button>
          )}
        </div>
      </Card>
    </div>
  );
};

WataneGoodsPage.Layout = Layout;

WataneGoodsPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default WataneGoodsPage;

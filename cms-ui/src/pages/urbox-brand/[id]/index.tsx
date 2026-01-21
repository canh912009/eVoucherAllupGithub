import Card from '@/components/common/card';
import Layout from '@/components/layouts/owner';
import Button from '@/components/ui/button';
import { useModalState } from '@/components/ui/modal/modal.context';
import UrBoxGoodList from '@/components/urbox/urbox-good-list';
import { getGoodList } from '@/data/urbox';
import {
  Brands,
  MappedPaginatorInfoEV,
  UrBoxGood
} from '@/types';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { updatePaginatorFE } from '@/utils/data-mappers-ev';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';

type IProps = {
  checkAddUrBox?: boolean;
  selectedBrand?: Brands;
};

const UrBoxGoodPage = ({ checkAddUrBox = false, selectedBrand }: IProps) => {
  // console.log(selectedBrand)
  const { data } = useModalState();
  const router = useRouter();
  const { query } = useRouter();
  const { t } = useTranslation();
  const brandCode = checkAddUrBox ? selectedBrand?.brandCode : query?.id;
  const [brandName, setBrandName] = useState('');

  const IS_PAGINATION = true; 
  const [goods, setGoods] = useState<UrBoxGood[]>([]);
  const [goodSlices, setGoodSlices] = useState<UrBoxGood[]>([]);

  const [page, setPage] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  function handlePagination(current: number) {
    // console.log('current', current);
    setPage(current);
  }

  const [selectedItems, setSelectedItems] = useState<UrBoxGood[]>([]);
  function handleSaveUrBoxGood() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveUrBox(selectedItems);
  }

  useEffect(() => {
    // console.log('first get all good', query?.id);
    const fetchData = async () => {
      try {
        const data = await getGoodList(brandCode as string);
        // console.log('data', data);
        setGoods(data);
        if (data) {
          setBrandName('(' + data[0].brand_name + ' - ' + brandCode + ')');
        }
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
            {t('UrBox Products')} {brandName}
          </h2>
        </div>

        {checkAddUrBox ? (
          <UrBoxGoodList
            data={IS_PAGINATION ? goodSlices : goods}
            paginatorInfo={paginatorInfo}
            onPagination={handlePagination}
            isPagination={IS_PAGINATION}
            checkAddUrBox={checkAddUrBox}
            selectedItems={selectedItems}
            setSelectedItems={setSelectedItems}
          />
        ) : (
          <UrBoxGoodList
            data={IS_PAGINATION ? goodSlices : goods}
            paginatorInfo={paginatorInfo}
            onPagination={handlePagination}
            isPagination={IS_PAGINATION}
            checkAddUrBox={checkAddUrBox}
          />
        )}

        <div className="mb-4 text-end">
          {checkAddUrBox ? (
            selectedItems.length > 0 && (
              <Button
                size="medium"
                className="bg-red-600 hover:bg-red-700"
                onClick={handleSaveUrBoxGood}
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

UrBoxGoodPage.Layout = Layout;

UrBoxGoodPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default UrBoxGoodPage;

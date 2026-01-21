import Card from '@/components/common/card';
import GiftPopGoodList from '@/components/giftpop/giftpop-good-list';
import Layout from '@/components/layouts/owner';
import Button from '@/components/ui/button';
import { getGoodList } from '@/data/giftpop';
import {Brands, GiftPopGood, MappedPaginatorInfoEV, PaginatorInfoEV} from '@/types';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import {useModalState} from "@/components/ui/modal/modal.context";
import { TrashIcon } from '@/components/icons/trash';
import { CloseIcon } from '@/components/icons/close-icon';

type IProps = {
  checkAddGiftPop?: boolean;
  selectedBrand?: Brands;
};

const GiftPopGoodPage = ({ checkAddGiftPop = false, selectedBrand }: IProps) => {
  // console.log(selectedBrand)
  const { data } = useModalState();
  const router = useRouter();
  const { query } = useRouter();
  const { t } = useTranslation();
  const brandCode = checkAddGiftPop ? selectedBrand?.brandCode : query?.id

  const IS_PAGINATION = true;
  const [goods, setGoods] = useState<GiftPopGood[]>([]);
  const [goodSlices, setGoodSlices] = useState<GiftPopGood[]>([]);

  const [page, setPage] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  function handlePagination(current: number) {
    // console.log('current', current);
    setPage(current);
  }

  const [selectedItems, setSelectedItems] = useState<GiftPopGood[]>([]);
  function handleSaveGiftPopGood() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveGiftpop(selectedItems);
  }

  function updatePaginator(goods: GiftPopGood[]) {
    // console.log('updatePaginator', goods.length);

    /**
     * Slice list data by page current
     * */
    let pathOps = { page: page, pageSize: PAGE_SIZE };
    const sliceData = goods.slice(
      (pathOps.page - 1) * pathOps.pageSize,
      pathOps.page * pathOps.pageSize
    );
    // console.log('updatePaginator sliceData', sliceData);
    setGoodSlices(sliceData);

    /**
     * Set paginator info
     **/
    let obj: PaginatorInfoEV<any> = {} as PaginatorInfoEV<GiftPopGood>;
    obj.data = sliceData;
    obj.totalCount = goods.length;
    let pageInfo = mapPaginatorData(pathOps, obj);
    // console.log('updatePaginator pageInfo', pageInfo);
    setPaginatorInfo(pageInfo!);
  }

  useEffect(() => {
    // console.log('first get all good', query?.id);
    const fetchData = async () => {
      try {
        const data = await getGoodList(brandCode  as string);
        setGoods(data);
        setGoodSlices(data);
        if (IS_PAGINATION) {
          updatePaginator(data);
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
      updatePaginator(goods);
    }
  }, [page]);

  return (
    <div className="flex flex-wrap">
      <Card className="w-full sm:w-full md:w-full">
        <div className="mb-8 flex w-full flex-wrap items-center justify-between">
          <h2 className="uppercase text-4xl font-semibold text-heading text-red-500">
            {t('GiftPop Good List')} {'(' + brandCode + ')'}
          </h2>
          {/* <CloseIcon className="w-8 h-8 text-right" /> */}
        </div>

        { checkAddGiftPop
          ? <GiftPopGoodList
              data={IS_PAGINATION ? goodSlices : goods}
              paginatorInfo={paginatorInfo}
              onPagination={handlePagination}
              isPagination={IS_PAGINATION}

              checkAddGiftPop={checkAddGiftPop}
              selectedItems={selectedItems}
              setSelectedItems={setSelectedItems}
            />
          : <GiftPopGoodList
              data={IS_PAGINATION ? goodSlices : goods}
              paginatorInfo={paginatorInfo}
              onPagination={handlePagination}
              isPagination={IS_PAGINATION}

              checkAddGiftPop={checkAddGiftPop}
            />
        }

        <div className="mb-4 text-end">
          {checkAddGiftPop
              ? selectedItems.length > 0 && <Button
              size="medium"
              className="bg-red-600 hover:bg-red-700"
              onClick={handleSaveGiftPopGood}
          >
            {('Save')}
          </Button>
              : <Button
                  variant="outline"
                  onClick={router.back}
                  className="me-4 hover:bg-red-800"
                  type="button"
              >
                {t('form:button-label-back')}
              </Button>
          }
        </div>
      </Card>
    </div>
  );
};

GiftPopGoodPage.Layout = Layout;

GiftPopGoodPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

export default GiftPopGoodPage;

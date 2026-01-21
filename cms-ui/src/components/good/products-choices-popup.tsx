import Spinner from '@/components/ui/loader/spinner/spinner';
import { useTranslation } from 'next-i18next';
import { useCategoriesQuery } from '@/data/categories';
import {PAGE_SIZE, PAGE_SIZE_MAX, SYSTEM_BRAND_TYPE} from '@/utils/constants';
import {Brands, Category, GoodQueryOptions, Good, CommonYesNoEnum,} from '@/types';
import { useState } from 'react';
import Card from '../common/card';
import Button from '@/components/ui/button';
import { useModalState } from '../ui/modal/modal.context';
import {useGoodsChoicePopupQuery, useGoodsQuery} from "@/data/good";
import GoodSearch from "@/components/good/good-search";
import GoodsList from "@/components/good/good-list";

const ProductsChoicesType: React.FC = () => {
  const { t } = useTranslation('common');
  const { data } = useModalState();

  const [searchOptions, setSearchOptions] = useState<Partial<GoodQueryOptions>>({
    'validYn': CommonYesNoEnum.YES,
    'system': SYSTEM_BRAND_TYPE.INTERNAL ,
    'isExpired': CommonYesNoEnum.NO,
  });
  const [page, setPage] = useState(1);
  const { goods, loading, paginatorInfo, error } = useGoodsChoicePopupQuery(
      { page, pageSize: PAGE_SIZE },
      searchOptions
  );

  const [selectedItems, setSelectedItems] = useState<Good[]>([]);

  function handleSaveGoods() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveGoodsChoicesType(selectedItems);
  }

  function handleSearch(data: Partial<GoodQueryOptions>) {
    // console.log('handleSearch, data = ', data)
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  if (loading || !goods)
    return (
      <div className="relative flex items-center justify-center h-96 w-96 bg-light">
        <Spinner text={t('common:text-loading')} />
      </div>
    );
  return (
    <div className="flex flex-wrap">
      <Card className="w-full sm:w-full md:w-full">
        <article className="relative z-[51] w-full h-full max-w-6xl bg-light md:rounded-xl xl:min-w-[1152px]">

          <div className="mb-2 flex flex-wrap">
            <div className="flex w-full flex-wrap font-semibold px-4 py-2">
              <h1 className="text-lg uppercase text-red-500 font-bold">
                {t('Add & Modified (Choices Product)')}
              </h1>
            </div>
          </div>

          <GoodSearch onSearch={handleSearch} isPopupChoiceType={true}/>
          <GoodsList
              goods={goods}
              paginatorInfo={paginatorInfo}
              onPagination={handlePagination}
              selectedItems={selectedItems}
              setSelectedItems={setSelectedItems}
          />

          <div className="mt-5 flex flex-row-reverse flex-wrap">
            <Button
              size="medium"
              className="bg-red-600 hover:bg-red-700"
              onClick={handleSaveGoods}
            >
              {t('Save')}
            </Button>
          </div>
        </article>
      </Card>
    </div>
  );
};

export default ProductsChoicesType;

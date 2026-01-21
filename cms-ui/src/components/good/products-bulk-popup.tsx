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

interface GoodsBULKPopupProps {
  categoryCodeBULKSelected: string;
  brandIdBULKSelected: string;
}

const GoodsBULKPopup: React.FC<GoodsBULKPopupProps> = ({
                 categoryCodeBULKSelected,
                 brandIdBULKSelected}) => {

  const { t } = useTranslation('common');
  const { data } = useModalState();

  const [searchOptions, setSearchOptions] = useState<Partial<GoodQueryOptions>>({
    categoryCode: categoryCodeBULKSelected,
    brandId: brandIdBULKSelected ,
    validYn: CommonYesNoEnum.YES,
    isExpired: CommonYesNoEnum.NO
  });
  const [page, setPage] = useState(1);
  const { goods, loading, paginatorInfo, error } = useGoodsQuery(
      { page, pageSize: PAGE_SIZE },
      searchOptions
  );

  const [selectedItems, setSelectedItems] = useState<Good[]>([]);

  function handleSaveGoodsBULK() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveGoodsBULK(selectedItems);
  }

  function handleSearch(data: Partial<GoodQueryOptions>) {
    // console.log('handleSearch, data = ', data)
    setSearchOptions(prevOptions => ({
      ...prevOptions,
      ...data,
      categoryCode: categoryCodeBULKSelected,
      brandId: brandIdBULKSelected,
      validYn: CommonYesNoEnum.YES ,
      isExpired: CommonYesNoEnum.NO
    }));
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
            <div className="flex w-full flex-wrap font-semibold px-4 ">
              <div className="w-full">
                <h1 className="text-lg uppercase text-red-500 font-bold">
                  {t('Select Products')}
                </h1>
              </div>
              <div className="w-full">
                <h1 className=" ">
                  Products has Category code : {categoryCodeBULKSelected},  brand ID : {brandIdBULKSelected}
                </h1>
              </div>
            </div>
          </div>

          <GoodSearch
              onSearch={handleSearch}
              isPopupBulkType={true}/>
          <GoodsList
              goods={goods}
              paginatorInfo={paginatorInfo}
              onPagination={handlePagination}
              selectedItems={selectedItems}
              setSelectedItems={setSelectedItems}
          />

          { selectedItems.length > 0 && <div className="mt-5 flex flex-row-reverse flex-wrap">
            <Button
              size="medium"
              className="bg-red-600 hover:bg-red-700"
              onClick={handleSaveGoodsBULK}
            >
              {t('Save')}
            </Button>
          </div> }
        </article>
      </Card>
    </div>
  );
};

export default GoodsBULKPopup;

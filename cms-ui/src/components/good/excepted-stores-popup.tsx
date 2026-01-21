import Spinner from '@/components/ui/loader/spinner/spinner';
import { useTranslation } from 'next-i18next';
import { PAGE_SIZE, PAGE_SIZE_MAX } from '@/utils/constants';
import { Brands, Store, StoreQueryOptions } from '@/types';

import { useState } from 'react';
import Card from '../common/card';
import Button from '@/components/ui/button';
import { useModalState } from '../ui/modal/modal.context';
import { useStoresQuery } from '@/data/store';
import ExceptedStoresList from './excepted-stores-list-popup';
import ExceptedStoresSearch from './excepted-store-search-popup';

interface ExceptedStoresPopupProps {
  // productSlug: string;
  selectedBrand?: Brands
}
const ExceptedStoresPopup: React.FC<ExceptedStoresPopupProps> = ({ selectedBrand }) => {
  // console.log('[ExceptedStoresPopup] selectedBrand = ', selectedBrand)
  const { t } = useTranslation('common');
  const { data } = useModalState();

  const [searchOptions, setSearchOptions] = useState<Partial<StoreQueryOptions>>({});
  const [page, setPage] = useState(1);
  const { stores, loading, paginatorInfo } = (selectedBrand ? useStoresQuery(
    { page, pageSize: PAGE_SIZE },
    {
      ...searchOptions,
      'brandName': selectedBrand?.brandName,
      'validYn': 'Y'
    }
  ) : {
    stores: [],
    loading: false,
    paginatorInfo: null,
  })

  const [selectedItems, setSelectedItems] = useState<Store[]>([]);

  function handleSaveExceptedStores() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveExceptedStores(selectedItems);
  }

  function handleSearch(data: Partial<StoreQueryOptions>) {
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  if (loading || !stores)
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
                {t('Modfied Except Stores')}
              </h1>
            </div>
          </div>

          <ExceptedStoresSearch onSearch={handleSearch} />
          <ExceptedStoresList stores={stores}
            paginatorInfo={paginatorInfo}
            onPagination={handlePagination}

            selectedItems={selectedItems}
            setSelectedItems={setSelectedItems}
          />
          {!selectedBrand && (<>
            <span className="px-5 w-full text-xs text-red-500 text-start md:w-1/4">
              {t('*You need select your Brand first!')}
            </span>
          </>)
          }
          <div className="mt-5 flex flex-row-reverse flex-wrap">
            <Button
              size="medium"
              className="bg-red-600 hover:bg-red-700"
              onClick={handleSaveExceptedStores}
            >
              {t('Save')}
            </Button>
          </div>
        </article>
      </Card>
    </div>
  );
};

export default ExceptedStoresPopup;

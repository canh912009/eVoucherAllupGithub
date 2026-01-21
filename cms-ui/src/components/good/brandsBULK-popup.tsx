import { useTranslation } from 'next-i18next';
import { PAGE_SIZE, PAGE_SIZE_MAX } from '@/utils/constants';
import {Brands, BrandsQueryOptions as SearchValue, CommonYesNoEnum, Store, StoreQueryOptions} from '@/types';

import { useState } from 'react';
import {useRouter} from "next/router";
import {useBrandsQuery} from "@/data/brands";
import Loader from "@/components/ui/loader/loader";
import ErrorMessage from "@/components/ui/error-message";
import BrandSearch from "@/components/brands/brands-search";
import BrandsList from "@/components/brands/brands-list";
import Button from "@/components/ui/button";
import {useModalState} from "@/components/ui/modal/modal.context";
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import Card from "@/components/common/card";
import {SYSTEM_GROUP_IN_EX} from "@/components/common/status-code-badge";

interface BrandsBULKPopupProps {
  categoryCodeBULKSelected: string;
}
const BrandsBULKPopup: React.FC<BrandsBULKPopupProps> = ({ categoryCodeBULKSelected }) => {
  const { t } = useTranslation();
  const { locale } = useRouter();
  const { data } = useModalState();
  const [selectedItems, setSelectedItems] = useState<Brands[]>([]);

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({
      validYn:  CommonYesNoEnum.YES,
      systems:  SYSTEM_GROUP_IN_EX.map(item => item.code).join(','),
      categoryCode: categoryCodeBULKSelected} );
  const [page, setPage] = useState(1);
  const { brands, loading, paginatorInfo, error } = useBrandsQuery(
      { page, pageSize: PAGE_SIZE },
      searchOptions
  );

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  function handleSaveBrandsBULK() {
      // console.log('selectedItems handleSaveBrandsBULK', selectedItems)
      // return
    return data?.handleSaveBrandsBULK(selectedItems);
  }

  function handleSearch(data: Partial<SearchValue>) {
    setSearchOptions(prevOptions => ({
        ...prevOptions,
        ...data,
        validYn: CommonYesNoEnum.YES,
        systems:  SYSTEM_GROUP_IN_EX.map(item => item.code).join(','),
        categoryCode: categoryCodeBULKSelected }));
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  return (
      <Card className="w-full sm:w-full md:w-full">
        <div className="mb-2 flex flex-wrap">
          <div className="flex w-full flex-wrap font-semibold px-4 ">
              <h1 className="text-lg uppercase text-red-500 font-bold  w-full ">
                  {t('Select Brands')}
              </h1>
              <h1 className="  w-full ">
                  Brands has Category code : {categoryCodeBULKSelected}
              </h1>
          </div>
        </div>
        <BrandSearch onSearch={handleSearch} popupType={true}/>
        <BrandsList
            brands={brands}
            paginatorInfo={paginatorInfo}
            onPagination={handlePagination}

            selectedItems={selectedItems}
            setSelectedItems={setSelectedItems}
            popupType={true}
        />
        <div className="mt-5 flex flex-row-reverse flex-wrap">
            {selectedItems.length > 0 && <Button
                    size="medium"
                    className="bg-red-600 hover:bg-red-700"
                    onClick={handleSaveBrandsBULK}
                >
                    {t('Save')}
                </Button>
            }
        </div>
      </Card>
  );
};

export default BrandsBULKPopup;



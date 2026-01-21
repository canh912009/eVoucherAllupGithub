import Spinner from '@/components/ui/loader/spinner/spinner';
import { useTranslation } from 'next-i18next';
import { useCategoriesQuery } from '@/data/categories';
import { PAGE_SIZE, PAGE_SIZE_MAX } from '@/utils/constants';
import {Category, CategoryQueryOptions, CommonYesNoEnum} from '@/types';
import CategoryList from '../categories/category-list';
import { useState } from 'react';
import CategorySearch from '../categories/category-search';
import Card from '../common/card';
import Button from '@/components/ui/button';
import { useModalState } from '../ui/modal/modal.context';

interface CategoryPopupProps {
  // productSlug: string;
}
const CategoriesPopup: React.FC<CategoryPopupProps> = ({ }) => {
  const { t } = useTranslation('common');
  const { data } = useModalState();

  const [searchOptions, setSearchOptions] = useState<Partial<CategoryQueryOptions>>({ validYn:  CommonYesNoEnum.YES});
  const [page, setPage] = useState(1);
  const { categories, loading, paginatorInfo } = useCategoriesQuery(
    { page, pageSize: PAGE_SIZE },
      { ...searchOptions, validYn: "Y" }
  );

  const [selectedItems, setSelectedItems] = useState<Category[]>([]);

  function handleSaveCategories() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveCategories(selectedItems);
  }

  function handleSearch(data: Partial<CategoryQueryOptions>) {
    // console.log('handleSearch, data = ', data)
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  if (loading || !categories)
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
                {t('Select Categories')}
              </h1>
            </div>
          </div>

          <CategorySearch onSearch={handleSearch} bulkPopup={true}/>
          <CategoryList categories={categories}
            paginatorInfo={paginatorInfo}
            onPagination={handlePagination}

            selectedItems={selectedItems}
            setSelectedItems={setSelectedItems}
          />

          { selectedItems.length > 0 && <div className="mt-5 flex flex-row-reverse flex-wrap">
            <Button
              size="medium"
              className="bg-red-600 hover:bg-red-700"
              onClick={handleSaveCategories}
            >
              {t('Save')}
            </Button>
          </div> }
        </article>
      </Card>
    </div>
  );
};

export default CategoriesPopup;

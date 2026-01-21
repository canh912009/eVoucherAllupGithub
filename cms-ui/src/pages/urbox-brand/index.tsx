import Card from '@/components/common/card';
import Layout from '@/components/layouts/owner';
import Button from '@/components/ui/button';
import { useModalState } from '@/components/ui/modal/modal.context';
import UrBoxBrandList from '@/components/urbox/urbox-brand-list';
import Search from '@/components/urbox/urbox-search';
import { getBrandList } from '@/data/urbox';
import {
  MappedPaginatorInfoEV,
  UrBoxBrandQueryOptions as SearchValue,
  UrBoxBrand,
} from '@/types';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { updatePaginatorFE } from '@/utils/data-mappers-ev';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useEffect, useState } from 'react';

type IProps = {
  checkAddUrBox?: boolean;
};

export default function UrBoxBrandPage({
  checkAddUrBox = false,
}: Readonly<IProps>) {
  const { data } = useModalState();
  const IS_PAGINATION = true;
  const [brands, setBrands] = useState<UrBoxBrand[]>([]); // Total
  const [brandFilters, setBrandFilters] = useState<UrBoxBrand[]>([]); // After search
  const [brandSlices, setBrandSlices] = useState<UrBoxBrand[]>([]); // Display (10 records)

  const [page, setPage] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  function handlePagination(current: number) {
    // console.log('current', current);
    setPage(current);
  }

  const [selectedItems, setSelectedItems] = useState<UrBoxBrand[]>([]);
  function handleSaveUrBoxBrand() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveUrBox(selectedItems);
  }

  function handleSearch(data: Partial<SearchValue>) {
    // console.log('handleSearch', data, brands.length);
    const filterBrands = brands.filter(
      (brand) =>
        brand.title
          ?.toLowerCase()
          .includes(data.brandName?.toLowerCase() ?? '') &&
        brand.cat_title
          ?.toLowerCase()
          .includes(data.categoryTitle?.toLowerCase() ?? '')
    );
    // console.log('handleSearch filter', filterBrands.length);
    setBrandFilters(filterBrands);
    setPage(1);
  }

  useEffect(() => {
    // console.log('first get all brand');
    const fetchData = async () => {
      try {
        const data = await getBrandList();
        setBrands(data);
        setBrandFilters(data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (IS_PAGINATION) {
      // console.log('update paginator', page, brandFilters.length);
      const data = updatePaginatorFE(
        { page: page, pageSize: PAGE_SIZE },
        brandFilters
      );
      setBrandSlices(data.sliceData);
      setPaginatorInfo(data.pageInfo!);
    }
  }, [page, brandFilters]);

  return (
    <>
      <Search onSearch={handleSearch} />

      <Card className="w-full sm:w-full md:w-full">
        {checkAddUrBox ? (
          <UrBoxBrandList
            data={IS_PAGINATION ? brandSlices : brandFilters}
            paginatorInfo={paginatorInfo ?? null}
            onPagination={handlePagination}
            isPagination={IS_PAGINATION}
            checkAddUrBox={checkAddUrBox}
            selectedItems={selectedItems}
            setSelectedItems={setSelectedItems}
          />
        ) : (
          <UrBoxBrandList
            data={IS_PAGINATION ? brandSlices : brandFilters}
            paginatorInfo={paginatorInfo ?? null}
            onPagination={handlePagination}
            isPagination={IS_PAGINATION}
            checkAddUrBox={checkAddUrBox}
          />
        )}

        {checkAddUrBox && selectedItems.length > 0 && (
          <div className="mb-4 text-end">
            <Button
              size="medium"
              className="bg-red-600 hover:bg-red-700"
              onClick={handleSaveUrBoxBrand}
            >
              {'Save'}
            </Button>
          </div>
        )}
      </Card>
    </>
  );
}

UrBoxBrandPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

UrBoxBrandPage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

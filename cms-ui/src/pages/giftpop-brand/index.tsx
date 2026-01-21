import Card from '@/components/common/card';
import GiftPopBrandList from '@/components/giftpop/giftpop-brand-list';
import Search from '@/components/giftpop/giftpop-search';
import Layout from '@/components/layouts/owner';
import { getBrandList } from '@/data/giftpop';
import {
  GiftPopBrand,
  MappedPaginatorInfoEV,
  PaginatorInfoEV,
  GiftPopBrandQueryOptions as SearchValue, Campaign, Category,
} from '@/types';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useEffect, useState } from 'react';
import Button from "@/components/ui/button";
import {useModalState} from "@/components/ui/modal/modal.context";

type IProps = {
  checkAddGiftPop?: boolean;
};

export default function GiftPopBrandPage({ checkAddGiftPop }: IProps) {
  const { data } = useModalState();
  const IS_PAGINATION = true;
  const [brands, setBrands] = useState<GiftPopBrand[]>([]); // Total
  const [brandFilters, setBrandFilters] = useState<GiftPopBrand[]>([]); // After search
  const [brandSlices, setBrandSlices] = useState<GiftPopBrand[]>([]); // Display (10 records)

  const [page, setPage] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  function handlePagination(current: number) {
    // console.log('current', current);
    setPage(current);
  }

  const [selectedItems, setSelectedItems] = useState<GiftPopBrand[]>([]);
  function handleSaveGiftPopBrand() {
    // console.log('selectedItems', selectedItems)
    // return
    return data?.handleSaveGiftpop(selectedItems);
  }


  function handleSearch(data: Partial<SearchValue>) {
    // console.log('handleSearch', data, brands.length);
    const filterBrands = brands.filter(
      (brand) =>
        brand.brandCode
          .toLowerCase()
          .includes(data.brandCode?.toLowerCase() ?? '') &&
        brand.brandName
          .toLowerCase()
          .includes(data.brandName?.toLowerCase() ?? '')
    );
    // console.log('handleSearch filter', filterBrands.length);
    setBrandFilters(filterBrands);
    setPage(1);
  }

  function updatePaginator(brands: GiftPopBrand[]) {
    // console.log('updatePaginator', brands.length);

    /**
     * Slice list data by page current
     * */
    let pathOps = { page: page, pageSize: PAGE_SIZE };
    const sliceData = brands.slice(
      (pathOps.page - 1) * pathOps.pageSize,
      pathOps.page * pathOps.pageSize
    );
    // console.log('updatePaginator sliceData', sliceData);
    setBrandSlices(sliceData);

    /**
     * Set paginator info
     **/
    let obj: PaginatorInfoEV<any> = {} as PaginatorInfoEV<GiftPopBrand>;
    obj.data = sliceData;
    obj.totalCount = brands.length;
    let pageInfo = mapPaginatorData(pathOps, obj);
    // console.log('updatePaginator pageInfo', pageInfo);
    setPaginatorInfo(pageInfo!);
  }

  useEffect(() => {
    // console.log('first get all brand');
    const fetchData = async () => {
      try {
        const data = await getBrandList();
        setBrands(data);
        setBrandFilters(data);
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
      // console.log('update paginator', page, brandFilters.length);
      updatePaginator(brandFilters);
    }
  }, [page, brandFilters]);

  return (
    <>
      <Search onSearch={handleSearch} />

      <Card className="w-full sm:w-full md:w-full">
        {
          checkAddGiftPop
              ?  <GiftPopBrandList
                  data={IS_PAGINATION ? brandSlices : brandFilters}
                  paginatorInfo={paginatorInfo ?? null}
                  onPagination={handlePagination}
                  isPagination={IS_PAGINATION}

                  checkAddGiftPop={checkAddGiftPop}
                  selectedItems={selectedItems}
                  setSelectedItems={setSelectedItems}
              />
              :  <GiftPopBrandList
                  data={IS_PAGINATION ? brandSlices : brandFilters}
                  paginatorInfo={paginatorInfo ?? null}
                  onPagination={handlePagination}
                  isPagination={IS_PAGINATION}

                  checkAddGiftPop={checkAddGiftPop}
              />
        }

        {checkAddGiftPop &&
            selectedItems.length > 0 && <div className="mb-4 text-end">
              <Button
                  size="medium"
                  className="bg-red-600 hover:bg-red-700"
                  onClick={handleSaveGiftPopBrand}
              >
                {('Save')}
              </Button>
            </div>
        }

      </Card>
    </>
  );
}

GiftPopBrandPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

GiftPopBrandPage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

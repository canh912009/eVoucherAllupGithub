import { useState } from 'react';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import Search from '@/components/raw-settlement/raw-search';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {
  RawSettlementQueryOptions as SearchValue,
  SortOrder
} from '@/types';
import {PAGE_SIZE, PERMISSIONS_EV as p, SYSTEM_BRAND_TYPE} from '@/utils/constants';
import RawSettlementList from "@/components/raw-settlement/raw-list";
import {useRawSettlementQuery} from "@/data/raw-settlement";

export default function RawSettlementPage() {
  const { t } = useTranslation();

  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const [orderBy, setOrder] = useState('');
  const [sortedBy, setSort] = useState<SortOrder>(SortOrder.None);

  const { raws, loading, paginatorInfo, error } = useRawSettlementQuery(
    { page, pageSize: PAGE_SIZE },
    searchOptions
  );

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  function handleSearch(data: Partial<SearchValue>) {
    const transformedData = {
      ...data,
      companyName: data.companyName?.value,
      campaignName: data.campaignName?.campaignName ?? "",  // data.campaignName : campaign object get by select input name in raw-search.tsx
      publishName: data.publishName?.publishName ?? ""  // data.publishName : publish object get by select input name in raw-search.tsx
    };
    setSearchOptions(transformedData);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  function handleOrder(newOrder: string) {
    let sort = SortOrder.Asc
    if(newOrder == orderBy) {
      sort = (sortedBy == SortOrder.Asc) ? SortOrder.Desc : SortOrder.Asc
    }

    setSort(sort)
    setOrder(newOrder)

    // toast.success("Sorted By :  " + sort)

    setSearchOptions({
      ...searchOptions,
      sort: newOrder,
      direction: sort
    });
  }

  return (
    <>
      <Search onSearch={handleSearch} searchValuesCsv={searchOptions}/>
      <RawSettlementList
          rawSettlement={raws}
          paginatorInfo={paginatorInfo}
          onPagination={handlePagination}
          onOrder={(newOrder) => handleOrder(newOrder)}
      />
    </>
  );
}

RawSettlementPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER, p.ROLE_SUPPLIER, p.ROLE_BRAND],
};

RawSettlementPage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

import Layout from '@/components/layouts/owner';
import Search from '@/components/stock/stock-search';
import {
  StockQueryOptions as SearchValue,
} from '@/types';
import {PAGE_SIZE_STOCK, PERMISSIONS_EV as p} from '@/utils/constants';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {useEffect, useState} from 'react';
import StockList from "@/components/stock/stock-list";
import {getStockListSummary, useStockAllQuery, useStockListQuery} from '@/data/stock';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useTranslation } from 'next-i18next';
import {format} from "date-fns";
import {parse} from "json2csv";
import {stockClient} from "@/data/client/crud-client";
import {API_ENDPOINTS} from "@/data/client/api-endpoints";

export default function StockPage() {
  const { t } = useTranslation();
  const [summaryData, setSummaryData] = useState<any[]>([]);
  const [downloadEnabled, setDownloadEnabled] = useState(false);
  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({
    insertedAtFrom: format(new Date(), 'yyyy-MM-dd')}
  );
  const [page, setPage] = useState(1);

  const { stockList, loading, paginatorInfo, error } = useStockListQuery(
    { page, pageSize: PAGE_SIZE_STOCK },
    searchOptions
  );

  useEffect(() => {
    fetchSummaryData();
  }, [searchOptions.insertedAtFrom]);

  const handleCsvDownload = async () => {
    if (stockList.length <= 0) {
      setDownloadEnabled(false);
      return;
    }

    setDownloadEnabled(true);
    try {
      const response = await stockClient._paginated(
        API_ENDPOINTS.STOCK_LIST,
        undefined,
        searchOptions
      );

      if (response?.data?.length > 0) {
        // Transform the data to remove unnecessary fields and rename diff fields
        const transformedData = response.data.map((item, index) => {
          // Remove unnecessary fields
          const { regId, regDt, updtId, updtDt, id, diff1d, diff7d, diff30d,
            supplierId, supplierName, brandId, brandName, goodsId, goodsName,
            expireTime, remainDays, totalQuantity, totalAmount, insertedAt,  ...rest } = item;

          // Return with renamed diff fields and No.
          return {
            "No.": index + 1,
            ...rest,
            "Supplier Name": supplierName,
            "Brand Name": brandName,
            "Brand Id": brandId,
            "Good Name": goodsName,
            "Good Id": goodsId,
            "Expire Date": expireTime ? format(new Date(expireTime), 'yyyy-MM-dd') : '',
            "Remain Days": remainDays,
            "Total Quantity": totalQuantity,
            "Total Amount": totalAmount,
            "Decreasing Quantity (vs. yesterday)": diff1d,
            "Decreasing Quantity (vs. last week)": diff7d,
            "Decreasing Quantity (vs. month)": diff30d
          };
        });

        const csv = parse(transformedData);
        const utf8BOM = '\uFEFF';
        const blob = new Blob([utf8BOM + csv], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);

        const hideLink = document.createElement('a');
        hideLink.href = url;
        hideLink.download = `StockList_${searchOptions?.insertedAtFrom 
          ? format(new Date(searchOptions?.insertedAtFrom), 'yyyy-MM-dd') 
          : format(new Date(), 'yyyy-MM-dd')}.csv`;
        hideLink.style.display = 'none';
        document.body.appendChild(hideLink);
        hideLink.click();

        // Cleanup
        URL.revokeObjectURL(url);
        document.body.removeChild(hideLink);
      }
    } catch (error) {
      console.error('Error downloading CSV:', error);
    } finally {
      setDownloadEnabled(false);
    }
  };

  const fetchSummaryData = async () => {
    try {
      const data = await getStockListSummary({
        insertedAtFrom: searchOptions.insertedAtFrom
      });
      setSummaryData(data || []);
    } catch (error) {
      console.error('Error fetching summary data:', error);
    }
  };

  function handleSearch(data: Partial<SearchValue>) {
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;
  if (downloadEnabled) return <Loader text="Downloading..." />;

  return (
    <>
      <Search onSearchCSV={handleSearch} onDownCSV={handleCsvDownload} />
      <StockList
        Stocks={stockList}
        summaryData={summaryData}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
      />
    </>
  );
}

StockPage.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

StockPage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});
import {adminOnly} from "@/utils/auth-utils";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useRouter } from "next/router";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import Layout from '@/components/layouts/owner';
import {CommonApproveStatusAction, DashboardTimeQueryOptions as SearchValue} from "@/types";
import DashboardQuery from "@/components/dashboard/dashboard-query";
import {getCurrentDateString} from "@/utils/common-utils";
import useInputTimeout from "@/utils/use-input-timeout";
import {useSuppliersQuery} from "@/data/supplier";
import {PAGE_SIZE} from "@/utils/constants";
import Pagination from "@/components/ui/pagination";
import ItemTable from "@/components/dashboard/item-table";
import BrandTable from "@/components/dashboard/brand-table";
import StoreTableComponent from "@/components/dashboard/store-table-component";


export default function SupplierStatisticsPage() {
  const { t } = useTranslation();
  const { locale } = useRouter();
  const initTimeSearch: Partial<SearchValue> = {
    startDate: "2023-01-01",
    endDate: getCurrentDateString()
  }
  const [searchTimeOptions, setSearchTimeOptions] = useState<Partial<SearchValue>>(initTimeSearch);;
  const [page, setPage] = useState(1);

  const { inputText: supplierName, onInputChange: handleInputSelect } =
    useInputTimeout();
  const { suppliers, loading: loadingSuppliers } = useSuppliersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      supplierName: supplierName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );

  function onChangeStartTime(selectedDate: string | null) {
    const newDataQuery = { ...searchTimeOptions, startDate: selectedDate };
    // @ts-ignore
    setSearchTimeOptions(newDataQuery);
  }

  function onChangeEndTime(selectedDate: string | null) {
    const newDataQuery = { ...searchTimeOptions, endDate: selectedDate };
    // @ts-ignore
    setSearchTimeOptions(newDataQuery);
  }

  const [queryOnlySupplierId, setQueryOnlySupplierId] = useState<Partial<SearchValue>>({})
  const handleChangeSelect = (supplier: any) => {
    const newDataQuery = {/* ...searchOptions,*/ supplierId: supplier?.id };
    setQueryOnlySupplierId(newDataQuery);
  };

  return (
    <div className="w-full sm:w-full md:w-full -m-2">
      <div className="m-2" >
        <DashboardQuery
          title={"Select Supplier"}
          showSelectBox={true}
          onChangeStartTime={onChangeStartTime}
          onChangeEndTime={onChangeEndTime}
          optionsSelectInput={suppliers}
          handleInputSelect={handleInputSelect}
          handleChangeSelect={handleChangeSelect}
        />
      </div>

      <div className="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4  mt-4">
        <div className="w-full md:w-1/2 shadow-md bg-white p-4  h-full md:h-1/2">
          <ItemTable supplierIdObject={queryOnlySupplierId} />
        </div>
        <div className="w-full md:w-1/2 shadow-md bg-white p-4 h-full md:h-1/2 ">
          <BrandTable supplierIdObject={queryOnlySupplierId} />
        </div>
      </div>

      <div className="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4 mt-4 ">
        <StoreTableComponent supplierIdObject={queryOnlySupplierId} searchTimeOptions={searchTimeOptions}/>
      </div>
    </div>
  );
}
SupplierStatisticsPage.authenticate = {
  permissions: adminOnly,
};
SupplierStatisticsPage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

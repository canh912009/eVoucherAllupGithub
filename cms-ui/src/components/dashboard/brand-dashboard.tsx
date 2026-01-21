import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import Layout from '@/components/layouts/owner';
import {CommonApproveStatusAction, DashboardTimeQueryOptions as SearchValue} from "@/types";
import {getCurrentDateString} from "@/utils/common-utils";
import {useState} from "react";
import useInputTimeout from "@/utils/use-input-timeout";
import {useSuppliersQuery} from "@/data/supplier";
import {PAGE_SIZE} from "@/utils/constants";
import DashboardQuery, {getCurrentDate, getFirstDateCurrentYear} from "@/components/dashboard/dashboard-query";
import ItemTable from "@/components/dashboard/item-table";
import StoreTableComponent from "@/components/dashboard/store-table-component";

export default function DashboardBrand() {
  const { t } = useTranslation();
  const { locale } = useRouter();
  const initTimeSearch: Partial<SearchValue> = {
    startDate: getFirstDateCurrentYear(),
    endDate: getCurrentDate(),
  };
  const [searchTimeOptions, setSearchTimeOptions] = useState<Partial<SearchValue>>(initTimeSearch);;
  const [page, setPage] = useState(1);

  const { inputText: supplierName, onInputChange: handleInputSelect } =
    useInputTimeout();

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
          title={"Brand Statistics"}
          onChangeStartTime={onChangeStartTime}
          onChangeEndTime={onChangeEndTime}
          // optionsSelectInput={suppliers}
          handleInputSelect={handleInputSelect}
          handleChangeSelect={handleChangeSelect}
        />
      </div>

      <ItemTable supplierIdObject={queryOnlySupplierId} />

      <div className="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4 mt-4 ">
        <StoreTableComponent
          supplierIdObject={queryOnlySupplierId}
          searchTimeOptions={searchTimeOptions}
          brandDashboard={true}/>
      </div>
    </div>
  );
}
DashboardBrand.Layout = Layout;


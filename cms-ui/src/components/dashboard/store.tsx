import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import Layout from '@/components/layouts/owner';
import StoreChart from "@/components/dashboard/store-chart";
import DashboardQuery, {getCurrentDate, getFirstDateCurrentYear} from "@/components/dashboard/dashboard-query";
import {DashboardTimeQueryOptions as SearchValue} from "@/types";
import {useState} from "react";

export default function StoreDashboard() {
  const { t } = useTranslation();
  const { locale } = useRouter();

  const initTimeSearch: Partial<SearchValue> = {
    startDate: getFirstDateCurrentYear(),
    endDate: getCurrentDate(),
  };
  const [searchTimeOptions, setSearchTimeOptions] = useState<Partial<SearchValue>>(initTimeSearch);;

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

  return (
    <div className="w-full sm:w-full md:w-full -m-2">
      <div className="m-2" >
        <DashboardQuery
          title={"Store Statistics"}
          onChangeStartTime={onChangeStartTime}
          onChangeEndTime={onChangeEndTime}
        />
      </div>

      <div className="flex flex-col md:flex-row shadow-md bg-white p-4  md:space-x-4 mt-4 ">
        <StoreChart
          searchTimeOptions={searchTimeOptions}
        />
      </div>
    </div>
  );
}
StoreDashboard.Layout = Layout;


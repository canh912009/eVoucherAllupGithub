import CampaignTable from '@/components/dashboard/campaign-table';
import DashboardQuery, {
  getCurrentDate,
  getFirstDateCurrentYear,
} from '@/components/dashboard/dashboard-query';
import DeliveryTable from '@/components/dashboard/delivery-table';
import Layout from '@/components/layouts/owner';
import { DashboardTimeQueryOptions as SearchValue } from '@/types';
import { useState } from 'react';

export default function DashboardCustomer() {
  const initTimeSearch: Partial<SearchValue> = {
    startDate: getFirstDateCurrentYear(),
    endDate: getCurrentDate(),
  };

  const [searchOptions, setSearchOptions] =
    useState<Partial<SearchValue>>(initTimeSearch);

  function onChangeStartTime(selectedDate: string | null) {
    const newDataQuery = { ...searchOptions, startDate: selectedDate };
    // @ts-ignore
    setSearchOptions(newDataQuery);
  }

  function onChangeEndTime(selectedDate: string | null) {
    const newDataQuery = { ...searchOptions, endDate: selectedDate };
    // @ts-ignore
    setSearchOptions(newDataQuery);
  }

  return (
    <div className="-m-2 w-full sm:w-full md:w-full">
      <div className="m-2">
        <DashboardQuery
          title={'Customer Statistics'}
          onChangeStartTime={onChangeStartTime}
          onChangeEndTime={onChangeEndTime}
        />
      </div>

      <div className="mt-4 flex flex-col space-y-4 md:flex-row md:space-x-4 md:space-y-0">
        <div className="h-full w-full bg-white p-4 shadow-md md:h-1/2">
          <CampaignTable options={searchOptions} />
        </div>
      </div>

      <div className="mt-4 flex flex-col space-y-4 md:flex-row md:space-x-4 md:space-y-0">
        <div className="h-full w-full bg-white p-4 shadow-md md:h-1/2">
          <DeliveryTable options={searchOptions} />
        </div>
      </div>
    </div>
  );
}

DashboardCustomer.Layout = Layout;

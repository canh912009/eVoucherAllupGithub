import CampaignTable from '@/components/dashboard/campaign-table';
import DashboardQuery, { getCurrentDate, getFirstDateCurrentYear } from '@/components/dashboard/dashboard-query';
import DeliveryTable from '@/components/dashboard/delivery-table';
import Layout from '@/components/layouts/owner';
import { useCustomersQuery } from '@/data/customer';
import {
  CommonApproveStatusAction,
  DashboardTimeQueryOptions as SearchValue,
} from '@/types';
import { adminOnly } from '@/utils/auth-utils';
import { PAGE_SIZE } from '@/utils/constants';
import useInputTimeout from '@/utils/use-input-timeout';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useState } from 'react';

export default function CustomerStatisticsPage() {
  const initTimeSearch: Partial<SearchValue> = {
    startDate: getFirstDateCurrentYear(),
    endDate: getCurrentDate(),
  };

  const [searchOptions, setSearchOptions] =
    useState<Partial<SearchValue>>(initTimeSearch);

  const { inputText: customerName, onInputChange: handleInputSelect } =
    useInputTimeout();
  const { customers, loading: loadingCustomers } = useCustomersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      customerName: customerName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );

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

  const handleChangeSelect = (customer: any) => {
    if (customer?.id) {
      const newDataQueryCampaign = { ...searchOptions, customerId: customer.id };
      setSearchOptions(newDataQueryCampaign);
    }
  };

  return (
    <div className="-m-2 w-full sm:w-full md:w-full">
      <div className="m-2">
        <DashboardQuery
          title={'Select Customer'}
          showSelectBox={true}
          onChangeStartTime={onChangeStartTime}
          onChangeEndTime={onChangeEndTime}
          optionsSelectInput={customers}
          handleInputSelect={handleInputSelect}
          handleChangeSelect={handleChangeSelect}
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
CustomerStatisticsPage.authenticate = {
  permissions: adminOnly,
};
CustomerStatisticsPage.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

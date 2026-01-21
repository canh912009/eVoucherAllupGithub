import {Code, CommonApproveStatusAction, Customer, CustomerQueryOptions as SearchValue} from '@/types';
import {PAGE_SIZE, PAGE_SIZE_MAX} from '@/utils/constants';
import {useState} from "react";
import {useCustomersQuery} from "@/data/customer";
import SelectInput from "@/components/ui/select-input-autocomplete";
import {useTranslation} from "next-i18next";
import useInputTimeout from "@/utils/use-input-timeout";
import {useForm} from "react-hook-form";
import {useCustomerCampaignQuery} from "@/data/dashboard-admin";
import dynamic from "next/dynamic";
const ReactEcharts = dynamic(() => import('echarts-for-react'), {
  ssr: false,
  loading: () => <div>Loading Chart...</div>
})

const AdminCustomerCampaign = () => {
  const { t } = useTranslation();

  const { inputText: customerName, onInputChange: handleInputChangeCustomer } =
    useInputTimeout();
  const { customers, loading } = useCustomersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      customerName: customerName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );
  const initCustomerId: Partial<SearchValue> = {
    customerId: "",
  }
  const [customerId, setCustomerId] = useState(initCustomerId);
  const {customersCampaign, isLoading, error} = useCustomerCampaignQuery(customerId);

  const handleChangeCustomer = (option: any) => {
    setValue('customer', option);
    // setValue('customerId', option?.id);
    const newCustomerId = { ...customerId, customerId: option?.id };
    setCustomerId(newCustomerId)
  };
  const {
    register,
    handleSubmit,
    control,
    setValue,
    setError,
    formState: { errors },
  } = useForm<{
    customer: Customer;
  }>({});
  const onSubmit = async (values: any) => {};


  const campaignNames = customersCampaign?.map((item) => item.campaignName);

  const othersCount = customersCampaign?.map((item) => item.othersCount );
  const othersAmount = customersCampaign?.map((item) => item.othersAmount  );

  const usedCount = customersCampaign?.map((item) => item.usedCount );
  const usedAmount = customersCampaign?.map((item) => item.usedAmount );

  const unusedCount = customersCampaign?.map((item) => item.unusedCount  );
  const unusedAmount = customersCampaign?.map((item) => item.unusedAmount  );

  const options = {
    title: {
      text: 'Campaign Status',
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
      },
    },
    legend: {left: '30%' },
    // legend: {
    //   data: ['Count', 'Amount'],
    // },
    xAxis: {
      type: 'category',
      data: campaignNames,
    },
    yAxis: [
      {
        type: 'value',
        name: 'Count',
      },
      {
        type: 'value',
        name: 'Amount',
      },
    ],
    series: [
      {
        name: 'othersCount',
        type: 'bar',
        stack: 'Count',
        data: othersCount,
        emphasis: {
          focus: 'series'
        },
        itemStyle: {
          color: '#98A2B3', // Custom color for this series
        },
        yAxisIndex: 0
      },
      {
        name: 'usedCount',
        type: 'bar',
        stack: 'Count',
        data: usedCount,
        emphasis: {
          focus: 'series'
        },
        itemStyle: {
          color: '#475467', // Custom color for this series
        },
        yAxisIndex: 0
      },
      {
        name: 'unusedCount',
        type: 'bar',
        stack: 'Count',
        data: unusedCount,
        emphasis: {
          focus: 'series'
        },
        itemStyle: {
          color: 'black', // Custom color for this series
        },
        yAxisIndex: 0
      },
      {
        name: 'othersAmount',
        type: 'bar',
        stack: 'Amount',
        data: othersAmount,
        emphasis: {
          focus: 'series'
        },
        itemStyle: {
          color: '#FEB273', // Custom color for this series
        },
        yAxisIndex: 1
      },
      {
        name: 'usedAmount',
        type: 'bar',
        stack: 'Amount',
        data: usedAmount,
        emphasis: {
          focus: 'series'
        },
        itemStyle: {
          color: '#FB6514', // Custom color for this series
        },
        yAxisIndex: 1
      },
      {
        name: 'unusedAmount',
        type: 'bar',
        stack: 'Amount',
        data: unusedAmount,
        emphasis: {
          focus: 'series'
        },
        itemStyle: {
          color: 'red', // Custom color for this series
        },
        yAxisIndex: 1
      },
    ],
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex w-full">
        <div className="flex w-full flex-wrap px-4 ">
          <label className="w-full py-2 text-heading md:w-1/4 font-bold">
            {t('Select customer : ')}
          </label>
          <div className="w-full md:w-3/4 ">
            <SelectInput
              name="customer"
              options={customers}
              isLoading={loading}
              getOptionLabel={(option: any) =>
                option.customerName + ' - ID : ' + option.id
              }
              getOptionValue={(option: any) => option.id}
              onInputChange={handleInputChangeCustomer}
              onChange={handleChangeCustomer}
              placeholder={t('common:filter-by-group-placeholder')}
              control={control}
              isClearable={true}
            />
          </div>
        </div>
      </div>
      <div className="w-full">
        <ReactEcharts option={options} className="mt-6 " />
      </div>
    </form>
  );
};

export default AdminCustomerCampaign;

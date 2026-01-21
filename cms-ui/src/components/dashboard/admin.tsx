import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { DashboardTimeQueryOptions as SearchValue} from "@/types";
import DashboardQuery from "@/components/dashboard/dashboard-query";
import {useState} from "react";
import {useCustomerChartQuery, useSupplierChartQuery} from "@/data/dashboard-admin";
import AdminCustomerTable from "@/components/dashboard/admin-customer-table";
import AdminCustomerCampaign from "@/components/dashboard/admin-customer-campaign";
import {getCurrentDateString} from "@/utils/common-utils";
import dynamic from "next/dynamic";
const ReactEcharts = dynamic(() => import('echarts-for-react'), {
  ssr: false,
  loading: () => <div>Loading Chart...</div>
})

export default function Dashboard() {
  const { t } = useTranslation();
  const { locale } = useRouter();
  const initTimeSearch: Partial<SearchValue> = {
    startDate: "2023-01-01",
    endDate: getCurrentDateString()
  }
  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>(initTimeSearch);
  const [page, setPage] = useState(1);

  const { supplierData } = useSupplierChartQuery(searchOptions)
  const optionSupplierChart = {
    title: {
      text: 'Supplier Status',
      textStyle: {
        fontSize: 15,  // Kích thước tiêu đề
        fontWeight: 'bold'  // Độ đậm của tiêu đề (bold)
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {},
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    yAxis: {
      type: 'category',
      data: supplierData.map(item => item.supplierName),
      axisLabel: {
        formatter: (value: string) => {
          // Kiểm tra nếu chiều dài của văn bản vượt quá 3 từ thì rút gọn nó
          if (value.length > 15) {
            return value.slice(0, 15) + '...'; // Rút gọn và thêm dấu ba chấm
          }
          return value; // Giữ nguyên nếu không vượt quá 3 từ
        },
      },
    },
    series:
      {
        name: 'data suppliers ',
        type: 'bar',
        data: supplierData.map(item => item.itemCount),
      },
  };

  const { customerData } = useCustomerChartQuery(searchOptions)
  const optionCustomerChart  = {
    title: {
      text: 'Customer Status',
      textStyle: {
        fontSize: 15,  // Kích thước tiêu đề
        fontWeight: 'bold'  // Độ đậm của tiêu đề (bold)
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        crossStyle: {
          color: '#999'
        }
      }
    },
    // toolbox: {
    //   feature: {
    //     dataView: { show: true, readOnly: false },
    //     magicType: { show: true, type: ['line', 'bar'] },
    //     restore: { show: true },
    //     saveAsImage: { show: true }
    //   }
    // },
    legend: {
      data: ['campaignCount', 'voucherAmount'/*, 'Temperature'*/]
    },
    xAxis: [
      {
        type: 'category',
        data: customerData.map(item => item.customerName),
        axisPointer: {
          type: 'shadow'
        }
      }
    ],
    yAxis: [
      {
        type: 'value',
        // name: 'voucherAmount',
        // min: 0,
        // max: 200,
        // interval: 20,
        // axisLabel: {
        //   formatter: '{value} ml'
        // }
      },
      // {
      //   type: 'value',
      //   name: 'Temperature',
      //   min: 0,
      //   max: 25,
      //   interval: 5,
      //   axisLabel: {
      //     formatter: '{value} °C'
      //   }
      // }
    ],
    series: [
      {
        name: 'campaignCount',
        type: 'bar',
        // tooltip: {
        //   valueFormatter: function (value) {
        //     return value as number + ' ml';
        //   }
        // },
        data: customerData.map(item => item.campaignCount),
      },
      {
        name: 'voucherAmount',
        type: 'bar',
        // tooltip: {
        //   valueFormatter: function (value) {
        //     return value as number + ' ml';
        //   }
        // },
        data: customerData.map(item => item.voucherAmount),
      },
      /*{
        name: 'Temperature',
        type: 'line',
        yAxisIndex: 1,
        // tooltip: {
        //   valueFormatter: function (value) {
        //     return value as number + ' °C';
        //   }
        // },
        data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
      }*/
    ]
  };

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
    <div className="w-full sm:w-full md:w-full -m-2">
      <div className="m-2" >
        <DashboardQuery
          title={"Supplier & Customer Statistics"}
          onChangeStartTime={onChangeStartTime}
          onChangeEndTime={onChangeEndTime}/>
      </div>

      <div className="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4">
        <div className="w-full md:w-1/2 shadow-md bg-white p-4  h-full md:h-1/2"><ReactEcharts option={optionSupplierChart} /></div>
        <div className="w-full md:w-1/2 shadow-md bg-white p-4 h-full md:h-1/2 "><ReactEcharts option={optionCustomerChart} /></div>
      </div>

      <div className="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4 mt-4 ">
        <div className="w-full md:w-1/2 shadow-md bg-white p-4 h-full md:h-1/2">
          <h1 className="w-full font-bold ">Customer Status</h1>
          <AdminCustomerTable />
          {/*<ReactEcharts option={optionCustomerChart} />*/}
        </div>
        <div className="w-full md:w-1/2 shadow-md bg-white p-4  h-full md:h-1/2">
          <AdminCustomerCampaign  />
        </div>
      </div>
    </div>
  )
}

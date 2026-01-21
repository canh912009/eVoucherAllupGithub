import * as echarts from 'echarts';
import {useCustomerChartQuery, useGoodChartQuery} from "@/data/dashboard-admin";
import dynamic from "next/dynamic";
const ReactEcharts = dynamic(() => import('echarts-for-react'), {
  ssr: false,
  loading: () => <div>Loading Chart...</div>
})

type IProps = {
  storeName?: string;
  storeId?: string;
  searchTimeOptions: any;
};

const StoreChart = ({ storeName, storeId, searchTimeOptions } : IProps) => {
  // const data = [
  //   {
  //     "exchangeDate": "2023-05-21",
  //     "goodName": "item1",
  //     "usedCount": 2,
  //     "goodCode": 1,
  //   },
  //   {
  //     "exchangeDate": "2023-05-21",
  //     "goodName": "item2",
  //     "usedCount": 1,
  //     "goodCode": 2,
  //   },
  //   {
  //     "exchangeDate": "2023-05-22",
  //     "goodName": "item1",
  //     "usedCount": 2,
  //     "goodCode": 1,
  //   },
  //   {
  //     "exchangeDate": "2023-05-22",
  //     "goodName": "item2",
  //     "usedCount": 2,
  //     "goodCode": 2,
  //   },
  //   {
  //     "exchangeDate": "2023-05-21",
  //     "goodName": "item3",
  //     "usedCount": 3,
  //     "goodCode": 3,
  //   },
  //   {
  //     "exchangeDate": "2023-05-22",
  //     "goodName": "item3",
  //     "usedCount": 1,
  //     "goodCode": 3,
  //   },
  //   {
  //     "exchangeDate": "2023-05-23",
  //     "goodName": "item2",
  //     "usedCount": 3,
  //     "goodCode": 2,
  //   },
  //   {
  //     "exchangeDate": "2023-05-23",
  //     "goodName": "item3",
  //     "usedCount": 4,
  //     "goodCode": 3,
  //   }
  // ];

  searchTimeOptions.storeId = storeId
  const { data } = useGoodChartQuery(searchTimeOptions)

// Tạo một Set để lấy các giá trị unique của exchangeDate
  const exchangeDateSet = new Set<string>();
  data.forEach(item => {
    exchangeDateSet.add(item.exchangeDate);
  });

// Chuyển Set thành một mảng
  const exchangeDates = Array.from(exchangeDateSet);

// Tạo dữ liệu series
  const seriesData = data.reduce((result, item) => {
    const goodName = item.goodName;
    const goodCode = item.goodCode;
    const exchangeDate = item.exchangeDate;
    const usedCount = item.usedCount;

    // Tìm hoặc tạo series tương ứng cho mỗi goodName
    let series = result.find(seriesItem => seriesItem.name === goodName);
    if (!series) {
      series = {
        name: goodName,
        type: 'line',
        data: [],
      };
      result.push(series);
    }

    // Tìm chỉ số của exchangeDate trong mảng exchangeDates
    const dateIndex = exchangeDates.indexOf(exchangeDate);

    // Chèn giá trị usedCount vào mảng data của series tương ứng
    series.data[dateIndex] = usedCount;

    return result;
  }, [] as echarts.EchartsOption.Series[]);

// Tạo biểu đồ
  const option = {
    // title: {
    //   text: {storeName }
    // },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: seriesData.map(seriesItem => seriesItem.name),
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: exchangeDates
    },
    yAxis: {
      type: 'value'
    },
    series: seriesData,
  };

  return (
    <div className="p-4">
      <h1 className="w-full font-bold">{storeName}</h1>
      {option.series.length > 0
        ? <ReactEcharts option={option} />
        : <h1 className="w-full italic">No have chart data</h1>
      }
    </div>
  );
};

export default StoreChart;

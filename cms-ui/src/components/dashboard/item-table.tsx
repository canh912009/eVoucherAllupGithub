import {
  Admin,
  Code,
  CommonApproveStatusAction,
  Customer,
  CustomerQueryOptions as SearchValue,
  MappedPaginatorInfoEV
} from '@/types';
import {PAGE_SIZE , PAGE_SIZE_MAX} from '@/utils/constants';
import {useState} from "react";
import {useCustomersQuery} from "@/data/customer";
import SelectInput from "@/components/ui/select-input-autocomplete";
import {useTranslation} from "next-i18next";
import useInputTimeout from "@/utils/use-input-timeout";
import {useForm} from "react-hook-form";
import {useCustomerCampaignQuery, useItemTableQuery} from "@/data/dashboard-admin";
import Text from "zrender/src/graphic/Text";
import {Table} from "@/components/ui/table";
import Pagination from "@/components/ui/pagination";
import dynamic from "next/dynamic";
const ReactEcharts = dynamic(() => import('echarts-for-react'), {
  ssr: false,
  loading: () => <div>Loading Chart...</div>
})

type IProps = {
  supplierIdObject: any;
};

const ItemTable = ({ supplierIdObject } : IProps) => {
  const [page, setPage] = useState(1);
  const { itemTableData, loading, paginatorInfo,  } = useItemTableQuery(
    { page, pageSize: PAGE_SIZE },
    supplierIdObject
  );
  const columnsItemTable = [
    {
      title: 'No',
      dataIndex: 'index',
      key: 'index',
      align: 'left',
      width: 0.5,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="text-xs">Item name</span>,
      dataIndex: 'itemName',
      key: 'itemName',
      align: 'left',
      width: 2,
      render: (itemName: string) => (
        <span className="truncate whitespace-nowrap">{itemName}</span>
      ),
    },
    {
      title: <span className="text-xs">Item code</span>,
      dataIndex: 'itemCode',
      key: 'itemCode',
      align: 'left',
      width: 1,
      render: (itemCode: string) => (
        <span className="truncate whitespace-nowrap">{itemCode}</span>
      ),
    },
    {
      title: <span className="text-xs">Used voucher count</span>,
      dataIndex: 'usedCount',
      key: 'usedCount',
      width: 1.2,
      align: 'left',
      ellipsis: true,
      render: (usedCount: string) => (
        <span className="truncate whitespace-nowrap">{usedCount}</span>
      ),
    },
    {
      title: <span className="text-xs">Sales Amount</span>,
      dataIndex: 'usedAmount',
      key: 'usedAmount',
      width: 1,
      align: 'left',
      ellipsis: true,
      render: (usedAmount: string) => (
        <span className="truncate whitespace-nowrap ">{usedAmount}</span>
      ),
    }
  ];

  function handlePagination(current: number) {
    setPage(current);
  }

  return (
    <>
      <h1 className="w-full font-bold ">Items</h1>
      <div className="mb-6 overflow-hidden rounded shadow  mt-4">
        <Table
          //@ts-ignore
          columns={columnsItemTable}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">
                {('table:empty-table-data')}
              </div>
            </div>
          )}
          data={itemTableData}
          rowKey="index"
          // scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-end">
          <Pagination
            total={paginatorInfo.totalCount}
            current={paginatorInfo.currentPage}
            pageSize={PAGE_SIZE}
            onChange={handlePagination}
          />
        </div>
      )}
    </>
  );
};

export default ItemTable;

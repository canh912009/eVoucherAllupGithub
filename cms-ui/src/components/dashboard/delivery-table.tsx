import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { useDeliveryTableQuery } from '@/data/dashboard-admin';
import { DashboardTimeQueryOptions as SearchValue } from '@/types';
import { PAGE_SIZE_MIN } from '@/utils/constants';
import { useState } from 'react';

type IProps = {
  options: Partial<SearchValue>;
};

const DeliveryTable = ({ options }: IProps) => {
  const [page, setPage] = useState(1);
  const { deliveryTableData, loading, paginatorInfo, error } =
    useDeliveryTableQuery({ page, pageSize: PAGE_SIZE_MIN }, options);

  const columnsDeliveryTable = [
    {
      title: 'No',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 50,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span>Delivery name</span>,
      dataIndex: 'deliveryName',
      key: 'deliveryName',
      align: 'left',
      // width: 200,
      render: (deliveryName: string) => (
        <span className="truncate whitespace-nowrap">{deliveryName}</span>
      ),
    },
    {
      title: <span>Voucher count</span>,
      dataIndex: 'voucherCount',
      key: 'voucherCount',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (voucherCount: number) => (
        <span className="truncate whitespace-nowrap">{voucherCount}</span>
      ),
    },
    {
      title: <span>Total Send Success Count</span>,
      dataIndex: 'totalSendSuccessCount',
      key: 'totalSendSuccessCount',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (totalSendSuccessCount: number) => (
        <span className="truncate whitespace-nowrap ">
          {totalSendSuccessCount}
        </span>
      ),
    },
    {
      title: <span>Total Send Fail Count</span>,
      dataIndex: 'totalSendFailCount',
      key: 'totalSendFailCount',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (totalSendFailCount: number) => (
        <span className="truncate whitespace-nowrap ">
          {totalSendFailCount}
        </span>
      ),
    },
  ];

  function handlePagination(current: number) {
    setPage(current);
  }

  return (
    <>
      <h1 className="w-full font-bold ">Delivery Status</h1>
      <div className="mb-6 mt-4 overflow-hidden rounded shadow">
        <Table
          //@ts-ignore
          columns={columnsDeliveryTable}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">
                {/* {'table:empty-table-data'} */}
              </div>
            </div>
          )}
          data={deliveryTableData}
          rowKey="deliveryId"
          scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-end">
          <Pagination
            total={paginatorInfo.totalCount}
            current={paginatorInfo.currentPage}
            pageSize={PAGE_SIZE_MIN}
            onChange={handlePagination}
          />
        </div>
      )}
    </>
  );
};

export default DeliveryTable;

import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { MappedPaginatorInfoEV, CustomerTableItem } from '@/types';
import { PAGE_SIZE } from '@/utils/constants';
import {useState} from "react";
import {useCustomerTableQuery} from "@/data/dashboard-admin";

const AdminCustomerTable = () => {
  const [page, setPage] = useState(1);

  const { customerTableData, loading, paginatorInfo, error } = useCustomerTableQuery(
    { page, pageSize: PAGE_SIZE }
  );

  function handlePagination(current: number) {
    setPage(current);
  }

  const columns = [
    {
      title: 'No',
      dataIndex: 'index',
      key: 'index',
      align: 'left',
      width: 0.3,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="text-xs">Customer name</span>,
      dataIndex: 'customerName',
      key: 'customerName',
      align: 'left',
      width: 2,
      render: (customerName: string) => (
        <span className="truncate whitespace-nowrap">{customerName}</span>
      ),
    },
    {
      title: <span className="text-xs">Campaign Count</span>,
      dataIndex: 'campaignCount',
      key: 'campaignCount',
      width: 1,
      align: 'left',
      ellipsis: true,
      render: (campaignCount: string) => (
        <span className="truncate whitespace-nowrap">{campaignCount}</span>
      ),
    },
    {
      title: <span className="text-xs">Voucher Count</span>,
      dataIndex: 'voucherCount',
      key: 'voucherCount',
      width: 1,
      align: 'left',
      ellipsis: true,
      render: (voucherCount: string) => (
        <span className="truncate whitespace-nowrap">{voucherCount}</span>
      ),
    },
    {
      title: <span className="text-xs">Sales Amount</span>,
      dataIndex: 'salesAmount',
      key: 'salesAmount',
      width: 1,
      align: 'left',
      ellipsis: true,
      render: (salesAmount: string) => (
        <span className="truncate whitespace-nowrap ">{salesAmount}</span>
      ),
    }
  ];

  return (
    <>
      <div className="mb-6 overflow-hidden rounded shadow">
        <Table
          //@ts-ignore
          columns={columns}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">
                {('table:empty-table-data')}
              </div>
            </div>
          )}
          data={customerTableData}
          rowKey="index"
          scroll={{ x: 1000 }}
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

export default AdminCustomerTable;

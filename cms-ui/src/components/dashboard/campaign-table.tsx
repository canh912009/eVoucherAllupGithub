import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { useCampaignTableQuery } from '@/data/dashboard-admin';
import { DashboardTimeQueryOptions as SearchValue } from '@/types';
import { PAGE_SIZE_MIN } from '@/utils/constants';
import { useState } from 'react';

type IProps = {
  options: Partial<SearchValue>;
};

const CampaignTable = ({ options }: IProps) => {
  const [page, setPage] = useState(1);
  const { campaignTableData, loading, paginatorInfo, error } =
    useCampaignTableQuery({ page, pageSize: PAGE_SIZE_MIN }, options);

  const columnsCampaignTable = [
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
      title: <span>Campaign name</span>,
      dataIndex: 'campaignName',
      key: 'campaignName',
      align: 'left',
      // width: 200,
      render: (campaignName: string) => (
        <span className="truncate whitespace-nowrap">{campaignName}</span>
      ),
    },
    {
      title: <span>Delivery Count</span>,
      dataIndex: 'deliveryCount',
      key: 'deliveryCount',
      align: 'center',
      width: 200,
      render: (deliveryCount: number) => (
        <span className="truncate whitespace-nowrap">{deliveryCount}</span>
      ),
    },
    {
      title: <span>Total voucher count</span>,
      dataIndex: 'totalVoucherCount',
      key: 'totalVoucherCount',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (totalVoucherCount: number) => (
        <span className="truncate whitespace-nowrap">{totalVoucherCount}</span>
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
      <h1 className="w-full font-bold ">Campaign Status</h1>
      <div className="mb-6 mt-4 overflow-hidden rounded shadow">
        <Table
          //@ts-ignore
          columns={columnsCampaignTable}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">
                {/* {'table:empty-table-data'} */}
              </div>
            </div>
          )}
          data={campaignTableData}
          rowKey="campaignId"
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

export default CampaignTable;

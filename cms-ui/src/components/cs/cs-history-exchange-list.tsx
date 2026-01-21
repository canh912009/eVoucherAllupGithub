import Link from '@/components/ui/link';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { CsHistoryExchange, MappedPaginatorInfoEV } from '@/types';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import {VOUCHER_TYPE_CODES, VoucherStatusCodeBadge} from '@/components/common/status-code-badge';

type IProps = {
  data: CsHistoryExchange[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
  voucherTypeCode?: string;
};

const CsHistoryExchangeList = ({
  data,
  paginatorInfo,
  onPagination,
  voucherTypeCode,
}: IProps) => {
  const { t } = useTranslation();

  const columns = [
    {
      title: 'NO',
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
      title: <span className="uppercase">Store Name</span>,
      dataIndex: 'storeName',
      key: 'storeName',
      width: 120,
      align: 'left',
      ellipsis: true,
      render: (storeName: string, object: any) => (
        <Link
          href={Routes?.stores?.details(object?.storeId)}
          className="e_not_link"
        >
          <span className="truncate whitespace-nowrap">{storeName}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">Store ID</span>,
      dataIndex: 'storeId',
      key: 'storeId',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (storeId: string) => (
        <span className="truncate whitespace-nowrap">{storeId}</span>
      ),
    },
    {
      title: <span className="uppercase">Exchange Date</span>,
      dataIndex: 'exchangeDate',
      key: 'exchangeDate',
      width: 130,
      align: 'center',
      ellipsis: true,
      render: (exchangeDate: string) => (
        <span className="truncate whitespace-nowrap">{exchangeDate}</span>
      ),
    },
    {
      title: <span className="uppercase">Store Staff</span>,
      dataIndex: 'storeStaff',
      key: 'storeStaff',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (storeStaff: string) => (
        <span className="truncate whitespace-nowrap">{storeStaff}</span>
      ),
    },
    {
      title: <span className="uppercase">Voucher Status</span>,
      dataIndex: 'pinStatus',
      key: 'pinStatus',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (pinStatus: string) => (
        <div
          className={`items-center justify-start space-x-3 rtl:space-x-reverse`}
        >
          {<VoucherStatusCodeBadge statusCode={pinStatus} />}
        </div>
      ),
    },
    {
      title: <span className="uppercase">Exchange Type</span>,
      dataIndex: 'exchangeType',
      key: 'exchangeType',
      width: 130,
      align: 'center',
      ellipsis: true,
      render: (exchangeType: string) => (
        <span className="truncate whitespace-nowrap">{exchangeType}</span>
      ),
    },
    voucherTypeCode === VOUCHER_TYPE_CODES.LC
    ? {
      title: <span className="uppercase">remaining count</span>,
      dataIndex: 'remainingCount',
      key: 'remainingCount',
      width: 160,
      align: 'right',
      ellipsis: true,
      render: (remainingCount: string) => (
        <span className="truncate whitespace-nowrap">{remainingCount}</span>
      ),
    }
    : {
      title: <span className="uppercase">Exchange Amount</span>,
      dataIndex: 'exchangeAmount',
      key: 'exchangeAmount',
      width: 160,
      align: 'right',
      ellipsis: true,
      render: (exchangeAmount: string) => (
        <span className="truncate whitespace-nowrap">{exchangeAmount}</span>
      ),
    },
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
                {t('table:empty-table-data')}
              </div>
            </div>
          )}
          data={data}
          rowKey="id"
          scroll={{ x: 1000, y: 400 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-center">
          <Pagination
            total={paginatorInfo.totalCount}
            current={paginatorInfo.currentPage}
            pageSize={PAGE_SIZE}
            onChange={onPagination}
          />
        </div>
      )}
    </>
  );
};

export default CsHistoryExchangeList;

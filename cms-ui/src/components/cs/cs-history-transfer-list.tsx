import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { Cs, CsHistoryTransfer, MappedPaginatorInfoEV } from '@/types';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import {
  VoucherStatusCodeBadge,
  VoucherTransferStatusCodeBadge,
} from '@/components/common/status-code-badge';
import Badge from '@/components/ui/badge/badge';

type IProps = {
  csData?: Cs | null;
  data: CsHistoryTransfer[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
};

const CsHistoryTransferList = ({
  csData,
  data,
  paginatorInfo,
  onPagination,
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
    // {
    //   title: <span className="uppercase">Target</span>,
    //   dataIndex: 'type',
    //   key: 'type',
    //   width: 150,
    //   align: 'left',
    //   ellipsis: true,
    //   render: (type: string) => (
    //     <span className="truncate whitespace-nowrap">{type}</span>
    //   ),
    // },
    {
      title: <span className="uppercase">From Voucher UUID</span>,
      dataIndex: 'voucherUUID',
      key: 'voucherUUID',
      width: 300,
      align: 'center',
      ellipsis: true,
      render: (voucherUUID: string, object: any) => (
        <a
          href={Routes?.cs?.details(object?.voucherUUID)}
          className="e_not_link"
          target="_blank"
          rel="noopener noreferrer"
        >
          {csData?.voucherUUID === voucherUUID ? (
            <Badge text={voucherUUID} color={'bg-red-500'} />
          ) : (
            <span className="truncate whitespace-nowrap">{voucherUUID}</span>
          )}
        </a>
      ),
    },
    {
      title: <span className="uppercase">To Voucher UUID</span>,
      dataIndex: 'toVoucherUUID',
      key: 'toVoucherUUID',
      width: 300,
      align: 'center',
      ellipsis: true,
      render: (toVoucherUUID: string, object: any) => (
        <a
          href={Routes?.cs?.details(object?.toVoucherUUID)}
          className="e_not_link"
          target="_blank"
          rel="noopener noreferrer"
        >
          <div
            className={`items-center justify-start space-x-3 rtl:space-x-reverse`}
          ></div>
          {csData?.voucherUUID === toVoucherUUID ? (
            <Badge text={toVoucherUUID} color={'bg-red-500'} />
          ) : (
            <span className="truncate whitespace-nowrap">{toVoucherUUID}</span>
          )}
        </a>
      ),
    },
    {
      title: <span className="uppercase">Transfer date</span>,
      dataIndex: 'transferDate',
      key: 'transferDate',
      width: 160,
      align: 'center',
      ellipsis: true,
      render: (transferDate: string) => (
        <span className="truncate whitespace-nowrap">{transferDate}</span>
      ),
    },
    {
      title: <span className="uppercase">Target name</span>,
      dataIndex: 'targetName',
      key: 'targetName',
      width: 130,
      align: 'center',
      ellipsis: true,
      render: (targetName: string) => (
        <span className="truncate whitespace-nowrap">{targetName}</span>
      ),
    },
    {
      title: <span className="uppercase">Target number</span>,
      dataIndex: 'targetNumber',
      key: 'targetNumber',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (targetNumber: string) => (
        <span className="truncate whitespace-nowrap">{targetNumber}</span>
      ),
    },
    {
      title: <span className="uppercase">PIN</span>,
      dataIndex: 'pin',
      key: 'pin',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (pin: string) => (
        <span className="truncate whitespace-nowrap">{pin}</span>
      ),
    },
    {
      title: <span className="uppercase">Voucher status</span>,
      dataIndex: 'pinStatus',
      key: 'pinStatus',
      width: 150,
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
      title: <span className="uppercase">Transfer Status</span>,
      dataIndex: 'transferStatusCode',
      key: 'transferStatusCode',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (transferStatusCode: string) => (
        <div
          className={`items-center justify-start space-x-3 rtl:space-x-reverse`}
        >
          {<VoucherTransferStatusCodeBadge statusCode={transferStatusCode} />}
        </div>
      ),
    },
    {
      title: <span className="uppercase">Access Link</span>,
      dataIndex: 'accessLink',
      key: 'accessLink',
      width: 180,
      align: 'left',
      ellipsis: true,
      render: (accessLink: string) => (
        <a
          href={accessLink}
          className="e_link"
          target="_blank"
          rel="noopener noreferrer"
        >
          <span className="truncate whitespace-nowrap">{accessLink}</span>
        </a>
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

export default CsHistoryTransferList;

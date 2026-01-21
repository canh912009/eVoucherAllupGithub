import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { CsHistoryPurchase, MappedPaginatorInfoEV } from '@/types';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { VoucherStatusCodeBadge } from '@/components/common/status-code-badge';

type IProps = {
  data: CsHistoryPurchase[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
};

const CsHistoryPurchaseList = ({
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
    {
      title: <span className="uppercase">Voucher UUID</span>,
      dataIndex: 'voucherUUID',
      key: 'voucherUUID',
      width: 180,
      align: 'left',
      ellipsis: true,
      render: (voucherUUID: string, object: any) => (
        <a
          href={Routes?.cs?.details(object?.voucherUUID)}
          className="e_not_link"
          target="_blank"
          rel="noopener noreferrer"
        >
          <span className="truncate whitespace-nowrap">{voucherUUID}</span>
        </a>
      ),
    },
    {
      title: <span className="uppercase">Target name</span>,
      dataIndex: 'targetName',
      key: 'targetName',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (targetName: string) => (
        <span className="truncate whitespace-nowrap">{targetName}</span>
      ),
    },
    {
      title: <span className="uppercase">Campaign Name</span>,
      dataIndex: 'campaignName',
      key: 'campaignName',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (campaignName: string) => (
        <span className="truncate whitespace-nowrap">{campaignName}</span>
      ),
    },
    {
      title: <span className="uppercase">Target Number</span>,
      dataIndex: 'targetNumber',
      key: 'targetNumber',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (targetNumber: string) => (
        <span className="truncate whitespace-nowrap">{targetNumber}</span>
      ),
    },
    {
      title: <span className="uppercase">Campaign Id</span>,
      dataIndex: 'campaignId',
      key: 'campaignId',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (campaignId: string) => (
        <span className="truncate whitespace-nowrap">{campaignId}</span>
      ),
    },
    {
      title: <span className="uppercase">Access Link</span>,
      dataIndex: 'accessLink',
      key: 'accessLink',
      width: 220,
      align: 'center',
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
    {
      title: <span className="uppercase">Delivery Name</span>,
      dataIndex: 'deliveryName',
      key: 'deliveryName',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (deliveryName: string) => (
        <span className="truncate whitespace-nowrap">{deliveryName}</span>
      ),
    },
    {
      title: <span className="uppercase">pin</span>,
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
      title: <span className="uppercase">Delivery Id</span>,
      dataIndex: 'deliveryId',
      key: 'deliveryId',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (deliveryId: string) => (
        <span className="truncate whitespace-nowrap">{deliveryId}</span>
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
      title: <span className="uppercase">Product Name</span>,
      dataIndex: 'productName',
      key: 'productName',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (productName: string) => (
        <span className="truncate whitespace-nowrap">{productName}</span>
      ),
    },
    {
      title: <span className="uppercase">Delivery Date</span>,
      dataIndex: 'deliveryDate',
      key: 'deliveryDate',
      width: 180,
      align: 'center',
      ellipsis: true,
      render: (deliveryDate: string) => (
        <span className="truncate whitespace-nowrap">{deliveryDate}</span>
      ),
    },
    {
      title: <span className="uppercase">Start Date</span>,
      dataIndex: 'startDate',
      key: 'startDate',
      width: 180,
      align: 'center',
      ellipsis: true,
      render: (startDate: string) => (
        <span className="truncate whitespace-nowrap">{startDate}</span>
      ),
    },
    {
      title: <span className="uppercase">End Date</span>,
      dataIndex: 'endDate',
      key: 'endDate',
      width: 180,
      align: 'center',
      ellipsis: true,
      render: (endDate: string) => (
        <span className="truncate whitespace-nowrap">{endDate}</span>
      ),
    },
    {
      title: <span className="uppercase">Exchange Date</span>,
      dataIndex: 'exchangeDate',
      key: 'exchangeDate',
      width: 180,
      align: 'center',
      ellipsis: true,
      render: (exchangeDate: string) => (
        <span className="truncate whitespace-nowrap">{exchangeDate}</span>
      ),
    },
    {
      title: <span className="uppercase">Message Type </span>,
      dataIndex: 'messageType',
      key: 'messageType',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (messageType: string) => (
        <span className="truncate whitespace-nowrap">{messageType}</span>
      ),
    },
    {
      title: <span className="uppercase">Result</span>,
      dataIndex: 'result',
      key: 'result',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (result: string) => (
        <span className="truncate whitespace-nowrap">{result}</span>
      ),
    },
    {
      title: <span className="uppercase">Pin Password</span>,
      dataIndex: 'pinPassword',
      key: 'pinPassword',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (pinPassword: string) => (
        <span className="truncate whitespace-nowrap">{pinPassword}</span>
      ),
    },
    {
      title: <span className="uppercase">Voucher Type Code</span>,
      dataIndex: 'voucherTypeCode',
      key: 'voucherTypeCode',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (voucherTypeCode: string) => (
        <span className="truncate whitespace-nowrap">{voucherTypeCode}</span>
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

export default CsHistoryPurchaseList;

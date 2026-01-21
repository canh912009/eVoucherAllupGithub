import Link from '@/components/ui/link';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import {
  DeliveryReceiveType,
  DeliveryVoucher,
  MappedPaginatorInfoEV,
} from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';

type IProps = {
  isPagination?: boolean;
  deliveryType?: DeliveryReceiveType | undefined;
  vouchers: DeliveryVoucher[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
};

const DeliveryVoucherList = ({
  isPagination = false,
  deliveryType,
  vouchers,
  paginatorInfo,
  onPagination,
}: IProps) => {
  const { t } = useTranslation();

  const commonColumns = [
    {
      title: <span className="uppercase">Voucher UUID</span>,
      dataIndex: 'ev',
      key: 'ev',
      align: 'center',
      width: 200,
      render: (ev: string, object: any) => (
        <Link
          href={Routes?.cs?.details(object?.ev)}
          className="e_not_link"
          // @ts-ignore
          target="_blank"
          rel="noopener noreferrer"
        >
          <span className="truncate whitespace-nowrap">{ev}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">Price</span>,
      dataIndex: 'price',
      key: 'price',
      width: 60,
      align: 'right',
      ellipsis: true,
      render: (price: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(price)}
        </span>
      ),
    },
    {
      title: <span className="uppercase">External PIN No</span>,
      dataIndex: 'extPin',
      key: 'extPin',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (extPin: string) => (
        <span className="truncate whitespace-nowrap">{extPin}</span>
      ),
    },
    {
      title: <span className="uppercase">Expiration date</span>,
      dataIndex: 'expirationDate',
      key: 'expirationDate',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (expirationDate: string) => (
        <span className="truncate whitespace-nowrap">{expirationDate}</span>
      ),
    },
    {
      title: <span className="uppercase">Access Link</span>,
      dataIndex: 'shortLink',
      key: 'shortLink',
      width: 160,
      align: 'left',
      ellipsis: true,
      render: (shortLink: string) => (
        <a
          href={shortLink}
          className="e_link"
          target="_blank"
          rel="noopener noreferrer"
        >
          <span className="truncate whitespace-nowrap">{shortLink}</span>
        </a>
      ),
    },
    {
      title: <span className="uppercase">OTP</span>,
      dataIndex: 'otp',
      key: 'otp',
      width: 60,
      align: 'center',
      ellipsis: true,
      render: (otp: string) => (
        <span className="truncate whitespace-nowrap">{otp}</span>
      ),
    },
  ];

  // Define the columns based on the delivery type
  const columns =
    (deliveryType === DeliveryReceiveType.PAPER || deliveryType === DeliveryReceiveType.DOWNLOAD )
      ? [
          ...commonColumns,
          {
            title: <span className="uppercase">Serial Number</span>,
            dataIndex: 'serialNo',
            key: 'serialNo',
            align: 'center',
            ellipsis: true,
            width: 120,
            render: (serialNo: string) => (
              <span className="truncate whitespace-nowrap">{serialNo}</span>
            ),
          },
          {
            title: <span className="uppercase">Activation Link</span>,
            dataIndex: 'activationUrl',
            key: 'activationUrl',
            width: 160,
            align: 'left',
            ellipsis: true,
            render: (activationUrl: string) => (
              <a
                href={activationUrl}
                className="e_link"
                target="_blank"
                rel="noopener noreferrer"
              >
                <span className="truncate whitespace-nowrap">
                  {activationUrl}
                </span>
              </a>
            ),
          },
          {
            title: <span className="uppercase">Activation Date</span>,
            dataIndex: 'activationDate',
            key: 'activationDate',
            align: 'center',
            ellipsis: true,
            width: 140,
            render: (activationDate: string) => (
              <span className="truncate whitespace-nowrap">{activationDate}</span>
            ),
          },
        ]
      : commonColumns;

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
          data={vouchers}
          rowKey="id"
          scroll={isPagination ? { x: 1000 } : { x: 1000, y: 450 }}
        />
      </div>

      {isPagination && !!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-end">
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

export default DeliveryVoucherList;

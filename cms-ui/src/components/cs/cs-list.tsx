import Link from '@/components/ui/link';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import TitleWithSort from '@/components/ui/title-with-sort';
import { Routes } from '@/config/routes';
import { Cs, MappedPaginatorInfoEV, SortOrder } from '@/types';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { useState } from 'react';
import { VoucherStatusCodeBadge } from '@/components/common/status-code-badge';

type IProps = {
  csList: Cs[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
  onOrder?: (current: string) => void;
  extendRequest?: boolean;
};

type SortingObjType = {
  sort: SortOrder;
  column: string | null;
};

type StatusCodeProps = {
  statusCode?: string;
  className?: string;
};

const CsList = ({ csList, paginatorInfo, onPagination, onOrder, extendRequest = false }: IProps) => {
  const { t } = useTranslation();

  const [sortingObj, setSortingObj] = useState<SortingObjType>({
    sort: SortOrder.None,
    column: '', //orderBy
  });

  const onHeaderClick = (column: string | null) => ({
    onClick: () => {
      (sortingObj.sort =
        sortingObj.column === column
          ? sortingObj.sort === SortOrder.Asc
            ? SortOrder.Desc
            : SortOrder.Asc
          : SortOrder.Asc),
        (sortingObj.column = column);
      if(onOrder) onOrder(column!);
    },
  });

  const columns = [
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 80,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Voucher UUID</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'voucherUUID'
          }
          isActive={sortingObj.column === 'voucherUUID'}
        />
      ),
      dataIndex: 'voucherUUID',
      key: 'voucherUUID',
      width: 300,
      align: 'center',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('voucherUUID'),
      render: (voucherUUID: string, object: any) => (
        <Link href={Routes?.cs?.details(object?.voucherUUID)}
              className="e_not_link"
              // @ts-ignore
              target="_blank"
              rel="noopener noreferrer">
          <span className="truncate whitespace-nowrap">{voucherUUID}</span>
        </Link>
      ),
    },
    extendRequest && {
      title: <span className="uppercase">Voucher UUID</span>,
      dataIndex: 'ev',
      key: 'ev',
      width: 300,
      align: 'center',
      ellipsis: true,
      render: (ev: string, object: any) => (
        <Link href={Routes?.csApproval?.details(object?.ev)}
              className="e_not_link"
              // @ts-ignore
              target="_blank"
              rel="noopener noreferrer">
          <span className="truncate whitespace-nowrap">{ev}</span>
        </Link>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Voucher Type</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'voucherType'
          }
          isActive={sortingObj.column === 'voucherType'}
        />
      ),
      dataIndex: 'voucherType',
      key: 'voucherType',
      width: 110,
      align: 'center',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('voucherType'),
      render: (voucherType: string) => (
        <span className="truncate whitespace-nowrap">{voucherType}</span>
      ),
    },
    extendRequest && {
      title: <span className="uppercase">Request Type</span>,
      dataIndex: 'requestType',
      key: 'requestType',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (requestType: string) => (
        <span className="truncate whitespace-nowrap">{requestType ?? "EXTEND"}</span>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Campaign Name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'campaignName'
          }
          isActive={sortingObj.column === 'campaignName'}
        />
      ),
      dataIndex: 'campaignName',
      key: 'campaignName',
      width: 160,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('campaignName'),
      render: (campaignName: string) => (
        <span className="truncate whitespace-nowrap">{campaignName}</span>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Product Id</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'productId'
          }
          isActive={sortingObj.column === 'productId'}
        />
      ),
      dataIndex: 'productId',
      key: 'productId',
      width: 140,
      align: 'center',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('productId'),
      render: (productId: string) => (
        <span className="truncate whitespace-nowrap">{productId}</span>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Product Name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'productName'
          }
          isActive={sortingObj.column === 'productName'}
        />
      ),
      dataIndex: 'productName',
      key: 'productName',
      width: 160,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('productName'),
      render: (productName: string) => (
        <span className="truncate whitespace-nowrap">{productName}</span>
      ),
    },
    extendRequest && {
      title:  <span className="uppercase">Delivery ID</span>,
      dataIndex: 'publishId',
      key: 'publishId',
      width: 120,
      align: 'left',
      ellipsis: true,
      render: (publishId: string) => (
        <span className="truncate whitespace-nowrap">{publishId}</span>
      ),
    },
    extendRequest && {
      title:  <span className="uppercase">Delivery Name</span>,
      dataIndex: 'publishName',
      key: 'publishName',
      width: 140,
      align: 'left',
      ellipsis: true,
      render: (publishName: string) => (
        <span className="truncate whitespace-nowrap">{publishName}</span>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Delivery Name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'deliveryName'
          }
          isActive={sortingObj.column === 'deliveryName'}
        />
      ),
      dataIndex: 'deliveryName',
      key: 'deliveryName',
      width: 140,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('deliveryName'),
      render: (deliveryName: string) => (
        <span className="truncate whitespace-nowrap">
          {deliveryName}
        </span>
      ),
    },
    extendRequest && {
      title:  <span className="uppercase">customer ID</span>,
      dataIndex: 'customerId',
      key: 'customerId',
      width: 120,
      align: 'left',
      ellipsis: true,
      render: (customerId: string) => (
        <span className="truncate whitespace-nowrap">{customerId}</span>
      ),
    },
    extendRequest && {
      title:  <span className="uppercase">customer name</span>,
      dataIndex: 'customerName',
      key: 'customerName',
      width: 150,
      align: 'left',
      ellipsis: true,
      render: (customerName: string) => (
        <span className="truncate whitespace-nowrap">{customerName}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Target Name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'targetName'
          }
          isActive={sortingObj.column === 'targetName'}
        />
      ),
      dataIndex: 'targetName',
      key: 'targetName',
      width: 140,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('targetName'),
      render: (targetName: string) => (
        <span className="truncate whitespace-nowrap">{targetName}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Target Number</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'targetNumber'
          }
          isActive={sortingObj.column === 'targetNumber'}
        />
      ),
      dataIndex: 'targetNumber',
      key: 'targetNumber',
      width: 140,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('targetNumber'),
      render: (targetNumber: string) => (
        <span className="truncate whitespace-nowrap">{targetNumber}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">target Email</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'targetEmail'
          }
          isActive={sortingObj.column === 'targetEmail'}
        />
      ),
      dataIndex: 'targetEmail',
      key: 'targetEmail',
      width: 200,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('targetEmail'),
      render: (targetEmail: string) => (
        <span className="truncate whitespace-nowrap">{targetEmail}</span>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Pin</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'pin'
          }
          isActive={sortingObj.column === 'pin'}
        />
      ),
      dataIndex: 'pin',
      key: 'pin',
      width: 100,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('pin'),
      render: (pin: string) => (
        <span className="truncate whitespace-nowrap">{pin}</span>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Voucher Status</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'pinStatus'
          }
          isActive={sortingObj.column === 'pinStatus'}
        />
      ),
      dataIndex: 'pinStatus',
      key: 'pinStatus',
      width: 150,
      align: 'center',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('pinStatus'),
      render: (pinStatus: string, record: any) => (
        <div
          className={`items-center justify-start space-x-3 rtl:space-x-reverse`}
        >
          {<VoucherStatusCodeBadge statusCode={pinStatus} />}
        </div>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Access Link</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'accessLink'
          }
          isActive={sortingObj.column === 'accessLink'}
        />
      ),
      dataIndex: 'accessLink',
      key: 'accessLink',
      width: 180,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('accessLink'),
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
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">transaction Id</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'transactionId'
          }
          isActive={sortingObj.column === 'transactionId'}
        />
      ),
      dataIndex: 'transactionId',
      key: 'transactionId',
      width: 250,
      align: 'left',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('transactionId'),
      render: (transactionId: string) => (
        <span className="truncate whitespace-nowrap">{transactionId}</span>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Delivery Date</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'deliveryDate'
          }
          isActive={sortingObj.column === 'deliveryDate'}
        />
      ),
      dataIndex: 'deliveryDate',
      key: 'deliveryDate',
      width: 180,
      align: 'center',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('deliveryDate'),
      render: (deliveryDate: string) => (
        <span className="truncate whitespace-nowrap">{deliveryDate}</span>
      ),
    },
    !extendRequest && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Expire Date</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc &&
            sortingObj.column === 'expireDate'
          }
          isActive={sortingObj.column === 'expireDate'}
        />
      ),
      dataIndex: 'expireDate',
      key: 'expireDate',
      width: 180,
      align: 'center',
      ellipsis: true,
      onHeaderCell: () => onHeaderClick('expireDate'),
      render: (expireDate: string) => (
        <span className="truncate whitespace-nowrap">{expireDate}</span>
      ),
    },
    extendRequest && {
      title:  <span className="uppercase">request date</span>,
      dataIndex: 'requestDate',
      key: 'requestDate',
      width: 150,
      align: 'left',
      ellipsis: true,
      render: (requestDate: string) => (
        <span className="truncate whitespace-nowrap">{requestDate}</span>
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
          data={csList}
          rowKey="voucherUUID"
          scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
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

export default CsList;

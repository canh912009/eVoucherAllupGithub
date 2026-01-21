import ActionButtons from '@/components/common/action-buttons';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import Link from '@/components/ui/link';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, Stock } from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import {PAGE_SIZE, PAGE_SIZE_STOCK} from '@/utils/constants';
import {format} from "date-fns";
import {formatNumberWithCommas} from "@/utils/common-utils";

type IProps = {
  Stocks: Stock[] | undefined;
  summaryData: any
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
};

const StockList = ({ Stocks, summaryData, paginatorInfo, onPagination }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();

  const renderValidYn = (validYn: string) => {
    if (validYn === 'Y') return 'Yes';
    if (validYn === 'N') return 'No';
    return '';
  };

  const columns = [
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 100,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="uppercase">supplier name</span>,
      dataIndex: 'supplierName',
      key: 'supplierName',
      width: 200,
      align: 'left',
      ellipsis: true,
      render: (supplierName: string, object: any) => (
        <Link
          href={Routes?.suppliers?.details(object?.supplierId)}
          className="text-[#5E5ADB]"
          // className="e_not_link text-#5E5ADB"
        >
          <span className="truncate whitespace-nowrap">{supplierName}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">brand Name</span>,
      dataIndex: 'brandName',
      key: 'brandName',
      width: 200,
      align: 'left',
      ellipsis: true,
      render: (brandName: string, object: any) => (
        <Link
          href={Routes?.brands?.details(object?.brandId)}
          className="text-[#5E5ADB]"
          // className="e_not_link text-#5E5ADB"
        >
          <span className="truncate whitespace-nowrap">{brandName}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">good Name</span>,
      dataIndex: 'goodsName',
      key: 'goodsName',
      width: 200,
      align: 'left',
      ellipsis: true,
      render: (goodsName: string, object: any) => (
        <Link
          href={Routes?.goods?.details(object?.goodsId)}
          className="text-[#5E5ADB]"
          // className="e_not_link text-#5E5ADB"
        >
          <span className="truncate whitespace-nowrap">{goodsName}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">good id</span>,
      dataIndex: 'goodsId',
      key: 'goodsId',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (goodsId: string, object: any) => (
        <span className="truncate whitespace-nowrap">{goodsId}</span>
      ),
    },
    {
      title: <span className="uppercase">expire date</span>,
      dataIndex: 'expireTime',
      key: 'expireTime',
      width: 120,
      align: 'left',
      ellipsis: true,
      render: (expireTime: string, object: any) => {
        const formattedDate = expireTime ? format(new Date(expireTime), 'yyyy-MM-dd') : '';
        return <span className="truncate whitespace-nowrap">{formattedDate}</span>;
      },
    },
    {
      title: <span className="uppercase">Remain Days</span>,
      dataIndex: 'remainDays',
      key: 'remainDays',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (remainDays: string | number) => {
        const days = Number(remainDays);
        const textColor = days < 90 ? 'text-red-500 font-bold' : 'text-black';
        return (
          <span className={`truncate whitespace-nowrap ${textColor}`}>
            {days}
          </span>
        );
      },
    } ,
    {
      title: <span className="uppercase">Total Quantity</span>,
      dataIndex: 'totalQuantity',
      key: 'totalQuantity',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (totalQuantity: string | number) => {
        const days = Number(totalQuantity);
        const textColor = days < 50 ? 'text-red-500 font-bold' : 'text-black';
        return (
          <span className={`truncate whitespace-nowrap ${textColor}`}>
            {days}
          </span>
        );
      },
    } ,
    {
      title: <span className="uppercase">total Amount</span>,
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (totalAmount: string, object: any) => {
        return <span className="truncate whitespace-nowrap">{totalAmount}</span>;
      },
    },
    {
      title: <span className="uppercase">Decreasing Quantity (vs. yesterday)</span>,
      dataIndex: 'diff1d',
      key: 'diff1d',
      width: 300,
      align: 'center',
      ellipsis: true,
      render: (diff1d: string, object: any) => {
        return <span className="truncate whitespace-nowrap">{diff1d}</span>;
      },
    },
    {
      title: <span className="uppercase">Decreasing Quantity (vs. last week)</span>,
      dataIndex: 'diff7d',
      key: 'diff7d',
      width: 300,
      align: 'center',
      ellipsis: true,
      render: (diff7d: string, object: any) => {
        return <span className="truncate whitespace-nowrap">{diff7d}</span>;
      },
    },
    {
      title: <span className="uppercase">Decreasing Quantity (vs. month)</span>,
      dataIndex: 'diff30d',
      key: 'diff30d',
      width: 300,
      align: 'center',
      ellipsis: true,
      render: (diff30d: string, object: any) => {
        return <span className="truncate whitespace-nowrap">{diff30d}</span>;
      },
    },

  ];

  return (
    <>
    <div className="mt-4 overflow-hidden rounded shadow">
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
        data={Stocks}
        rowKey="id"
        scroll={{x: 1000, y: 'calc(100vh - 200px)'}}
        summary={() => {
          if (!summaryData) return null;
          return (
            <Table.Summary fixed>
              <Table.Summary.Row className="font-bold text-lg">
                <Table.Summary.Cell index={0} align="center">
                  Total
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} colSpan={6}/> {/* Trống cho các cột không cần tính tổng */}
                <Table.Summary.Cell index={7} align="center">
                  {formatNumberWithCommas(summaryData.totalQuantity)}
                </Table.Summary.Cell>
                <Table.Summary.Cell index={8} align="center">
                  {formatNumberWithCommas(summaryData.totalAmount)}
                </Table.Summary.Cell>
                <Table.Summary.Cell index={9} align="center">
                  {formatNumberWithCommas(summaryData.totalDiff1d)}
                </Table.Summary.Cell>
                <Table.Summary.Cell index={10} align="center">
                  {formatNumberWithCommas(summaryData.totalDiff7d)}
                </Table.Summary.Cell>
                <Table.Summary.Cell index={11} align="center">
                  {formatNumberWithCommas(summaryData.totalDiff30d)}
                </Table.Summary.Cell>
              </Table.Summary.Row>
            </Table.Summary>
          );
        }}
      />
    </div>

    {!!paginatorInfo?.totalCount && (
      <div className="flex items-center justify-end">
        <Pagination
          total={paginatorInfo.totalCount}
          current={paginatorInfo.currentPage}
          pageSize={PAGE_SIZE_STOCK}
          onChange={onPagination}
        />
      </div>
    )}
    </>
  );
};

export default StockList;

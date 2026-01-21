import Link from '@/components/ui/link';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, VNPTEPayProvider } from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import React from "react";

type IProps = {
  data: VNPTEPayProvider[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
  isXpay?: boolean;
};

const getButtonColor = (value: number) => {
  switch(value) {
    case 5000000: return 'bg-emerald-600';
    case 3000000: return 'bg-amber-600';
    case 2000000: return 'bg-cyan-600';
    case 1000000: return 'bg-blue-800';
    case 500000: return 'bg-teal-500';
    case 300000: return 'bg-red-500';
    case 200000: return 'bg-yellow-500';
    case 100000: return 'bg-gray-500';
    case 50000: return 'bg-gray-700';
    case 30000: return 'bg-pink-500';
    case 20000: return 'bg-indigo-500';
    case 10000: return 'bg-purple-500';
    default: return 'bg-gray-500';
  }
};

const VNPTEPayProviderList = ({ data, paginatorInfo, onPagination, isXpay = false }: IProps) => {
  const { t } = useTranslation();

  const columns = [
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'left',
      width: 50,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="uppercase">Provider code</span>,
      dataIndex: 'providerCd',
      key: 'providerCd',
      width: 150,
      align: 'left',
      ellipsis: true,
      render: (providerCd: number, object: any) => (
        <Link
          href={isXpay ? Routes?.xpayProvider?.details(object?.providerCd) : Routes?.vnptEpayProvider?.details(object?.providerCd)}
          className="text-[#5E5ADB]"
          // className="e_not_link text-#5E5ADB"
        >
          <span className="truncate whitespace-nowrap">{providerCd}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">provider name</span>,
      dataIndex: 'providerNm',
      key: 'providerNm',
      width: 150,
      align: 'left',
      ellipsis: true,
      render: (providerNm: string) => (
        <span className="truncate whitespace-nowrap">{providerNm}</span>
      ),
    },
    {
      title: <span className="uppercase">provider type</span>,
      dataIndex: 'providerType',
      key: 'providerType',
      width: 150,
      align: 'left',
      ellipsis: true,
      render: (providerType: number) => (
        <span className="truncate whitespace-nowrap">
          {providerType}
        </span>
      ),
    },
    {
      title: <span className="uppercase">valid</span>,
      dataIndex: 'validYn',
      key: 'validYn',
      width: 80,
      align: 'left',
      ellipsis: true,
      render: (validYn: number) => (
        <span className="truncate whitespace-nowrap">
          {validYn}
        </span>
      ),
    },
    {
      title: <span className="uppercase">allowed card faces</span>,
      dataIndex: 'allowedCardFaces',
      key: 'allowedCardFaces',
      width: 500,
      align: 'left',
      ellipsis: true,
      render: (allowedCardFaces: any) => (
        <div className="overflow-x-auto">
          <div className="flex whitespace-nowrap">
          {JSON.parse(allowedCardFaces)?.map((face: any) => (
            <span
              key={face}
              className={`${getButtonColor(face)} text-white py-1 px-3 rounded-xl text-sm flex-shrink-0 mx-1`}
            >
              {face >= 1000000
                ? `${face / 1000000}M`
                : formatNumber(face / 1000, true)}
            </span>
          ))}
          </div>
        </div>
      ),
    },
    {
      title: <span className="uppercase">allowed actions</span>,
      dataIndex: 'allowedActions',
      key: 'allowedActions',
      width: 200,
      align: 'left',
      ellipsis: true,
      render: (allowedActions: number) => (
        <span className="truncate whitespace-nowrap">
          {allowedActions}
        </span>
      ),
    },
  ];

  return (
    <>
      <div className="mb-6 w-full overflow-hidden rounded shadow">
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
          scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex w-full items-center justify-center">
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

export default VNPTEPayProviderList;

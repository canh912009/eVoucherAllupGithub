import { ExternalPinUploadData, ExternalPinUpload, MappedPaginatorInfoEV } from "@/types";
import { useRouter } from "next/router";
import { useTranslation } from "react-i18next";
import { useState } from 'react';
import { Table } from '@/components/ui/table';
import Pagination from '@/components/ui/pagination';
import { PAGE_SIZE } from '@/utils/constants';
import Button from '@/components/ui/button';


type IProps = {
  externalPins: ExternalPinUploadData[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
  externalPinUpload?: ExternalPinUpload;
};

export default function ExternalPinUploadDetail({ externalPins, paginatorInfo, onPagination, externalPinUpload }: IProps) {

  const { t } = useTranslation();
  const router = useRouter();

  // console.log('paginatorInfo = ', paginatorInfo)
  // paginatorInfo?.totalCount = externalPins?.length

  const columns = [
    {
      title: <span className="uppercase">ID</span>,
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      width: 40,
      render: (id: string) => (
        <span className="truncate whitespace-nowrap">{id}</span>
      ),
    },
    {
      title: <span className="uppercase">External PIN No</span>,
      dataIndex: 'externalPinNo',
      key: 'externalPinNo',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (externalPinNo: string) => (
        <span className="truncate whitespace-nowrap">{externalPinNo}</span>
      ),
    },
    {
      title: <span className="uppercase">Product ID</span>,
      dataIndex: 'goodsId',
      key: 'goodsId',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (goodsId: string) => (
        <span className="truncate whitespace-nowrap">{goodsId}</span>
      ),
    },
    {
      title: <span className="uppercase">Upload ID</span>,
      dataIndex: 'externalPinUpload',
      key: 'externalPinUpload',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: () => (
        <span className="truncate whitespace-nowrap">{externalPinUpload?.id}</span>
      ),
    },
    {
      title: <span className="uppercase">Voucher Status</span>,
      dataIndex: 'status',
      key: 'status',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (status: string) => (
        <span className="truncate whitespace-nowrap">{status}</span>
      ),
    },
    {
      title: <span className="uppercase">Registration Date</span>,
      dataIndex: 'regDt',
      key: 'regDt',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (regDt: string) => (
        <span className="truncate whitespace-nowrap">{regDt}</span>
      ),
    },
    {
      title: <span className="uppercase">Expired Date</span>,
      dataIndex: 'expireTime',
      key: 'expireTime',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (expireTime: string) => (
        <span className="truncate whitespace-nowrap">{expireTime}</span>
      ),
    },
    {
      title: <span className="uppercase">Password</span>,
      dataIndex: 'password',
      key: 'password',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (password: string) => (
        <span className="truncate whitespace-nowrap">{password}</span>
      ),
    },
  ]


  return (
    <>
      <div className="mb-5 overflow-hidden rounded shadow">
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
          data={externalPins}
          rowKey="id"
          scroll={{ x: 1000, y: 450 }}
        />
      </div>

      {/* TODO */}
      {/* <div className="mb-6">
        {externalPins?.length > 0 && (
          // {!!paginatorInfo?.totalCount && (
          <div className="flex items-center justify-end">
            <Pagination
              total={externalPins.length}
              current={paginatorInfo?.currentPage}
              pageSize={3}
              onChange={onPagination}
            />
          </div>
        )}
      </div> */}

      <div className="text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 bg-red-700 hover:bg-red-700"
          type="button"
        >
          {t('Return')}
        </Button>
      </div>
    </>
  );
}
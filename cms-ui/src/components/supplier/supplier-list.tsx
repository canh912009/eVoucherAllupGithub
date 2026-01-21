import ActionButtons from '@/components/common/action-buttons';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import Badge from '@/components/ui/badge/badge';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, Supplier } from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import Link from "@/components/ui/link";

type IProps = {
  suppliers: Supplier[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

const SupplierList = ({ suppliers, paginatorInfo, onPagination }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();

  const columns = [
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 40,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="uppercase">Supplier name</span>,
      dataIndex: 'supplierName',
      key: 'supplierName',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (supplierName: string, object: any) => (
        <Link
          href={Routes?.suppliers?.details(object?.id)}
          className="text-[#5E5ADB]"
          // className="e_not_link text-#5E5ADB"
        >
          <span className="truncate whitespace-nowrap">{supplierName}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">Supplier id</span>,
      dataIndex: 'id',
      key: 'id',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (supplierName: string) => (
        <span className="truncate whitespace-nowrap">
          {supplierName}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Tax code</span>,
      dataIndex: 'taxcode',
      key: 'taxcode',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (taxcode: string) => (
        <span className="truncate whitespace-nowrap">
          {taxcode}
        </span>
      ),
    },
    {
      title: <span className="uppercase">contact name</span>,
      dataIndex: 'primaryContactName',
      key: 'primaryContactName',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (primaryContactName: string) => (
        <span className="truncate whitespace-nowrap">{primaryContactName}</span>
      ),
    },
    {
      title: <span className="uppercase">contact email</span>,
      dataIndex: 'primaryContactEmail',
      key: 'primaryContactEmail',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (primaryContactEmail: string) => (
        <span className="truncate whitespace-nowrap">{primaryContactEmail}</span>
      ),
    },
    {
      title: <span className="uppercase">contact phone</span>,
      dataIndex: 'primaryContactMobile',
      key: 'primaryContactMobile',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (primaryContactMobile: string) => (
        <span className="truncate whitespace-nowrap">
          {primaryContactMobile}
        </span>
      ),
    },
    {
      // REQ, APPRV, REJCT
      title: <span className="uppercase">approve status</span>,
      dataIndex: 'approveStatusCode',
      key: 'approveStatusCode',
      align: 'center',
      width: 100,
      render: (approveStatusCode: string, record: any) => (
        <div
          className={`items-center justify-start space-x-3 rtl:space-x-reverse`}
        >
          {approveStatusCode === 'REQ' && (
            <Badge text='Requesting' color="bg-yellow-600" />
          )}
          {approveStatusCode === 'APPRV' && (
            <Badge text='Approved' color="bg-accent" />
          )}
          {approveStatusCode === 'REJCT' && (
            <Badge text='Rejected' color="bg-red-800" />
          )}
        </div>
      ),
    },
    {
      title: <span className="uppercase">Modified date</span>,
      dataIndex: 'updtDt',
      key: 'updtDt',
      width: 140,
      align: 'center',
      ellipsis: true,
      render: (updtDt: string) => (
        <span className="truncate whitespace-nowrap">{updtDt}</span>
      ),
    },
    // {
    //   title: t('table:table-item-actions'),
    //   key: 'actions',
    //   align: 'center',
    //   width: 200,
    //   render: (data: Supplier) => {
    //     return (
    //       <ActionButtons
    //         id={data?.id}
    //         approveModalView={data?.approveStatusCode === 'REQ' && "APPROVE_SUPPLIER"}
    //         disapproveModalView={data?.approveStatusCode === 'REQ' && "DISAPPROVE_SUPPLIER"}
    //         editUrl={`${Routes.suppliers.editWithoutLang(data?.id)}`}
    //         detailsUrl={Routes?.suppliers?.details(data?.id)}
    //         deleteModalView="DELETE_SUPPLIER"
    //         customLocale={router?.locale}
    //       />
    //     );
    //   },
    // },
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
          data={suppliers}
          rowKey="id"
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

export default SupplierList;

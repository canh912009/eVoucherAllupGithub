import ActionButtons from '@/components/common/action-buttons';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, Customer } from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import ApproveStatusCodeBadge from '../common/approve-status-code-badge';
import Link from "@/components/ui/link";

type IProps = {
  customers: Customer[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

const CustomerList = ({ customers, paginatorInfo, onPagination }: IProps) => {
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
      title: 'CUSTOMER NAME',
      dataIndex: 'customerName',
      key: 'customerName',
      width: 150,
      align: 'center',
      ellipsis: true,
      // render: (customerName: string) => (
      //   <span className="truncate whitespace-nowrap">{customerName}</span>
      // ),
      render: (customerName: string, object: any) => (
        <Link
          href={Routes?.customers?.details(object?.id)}
          className="text-[#5E5ADB]"
          // className="e_not_link text-#5E5ADB"
        >
          <span className="truncate whitespace-nowrap">{customerName}</span>
        </Link>
      ),
    },
    {
      title: 'CUSTOMER ID',
      dataIndex: 'id',
      key: 'id',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (id: string) => (
        <span className="truncate whitespace-nowrap">{id}</span>
      ),
    },
    {
      title: 'CUSTOMER TYPE',
      dataIndex: 'customerTypeCode',
      key: 'customerTypeCode',
      width: 100,
      align: 'left',
      ellipsis: true,
      render: (customerTypeCode: string) => (
        <span className="truncate whitespace-nowrap font-bold">{customerTypeCode}</span>
      ),
    },
    {
      title: 'TAX CODE',
      dataIndex: 'taxcode',
      key: 'taxcode',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (taxcode: string) => (
        <span className="truncate whitespace-nowrap">{taxcode}</span>
      ),
    },
    {
      title: 'REPRESENTATIVE EMAIL',
      dataIndex: 'representativeMail',
      key: 'representativeMail',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (representativeMail: string) => (
        <span className="truncate whitespace-nowrap">
          {representativeMail}
        </span>
      ),
    },
    {
      title: 'REPRESENTATIVE PHONE',
      dataIndex: 'representativeMobile',
      key: 'representativeMobile',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (representativeMobile: string) => (
        <span className="truncate whitespace-nowrap">
          {representativeMobile}
        </span>
      ),
    },
    {
      // REQ, APPRV, REJCT
      title: t('APPROVE STATUS'),
      dataIndex: 'approveStatusCode',
      key: 'approveStatusCode',
      align: 'center',
      width: 100,
      render: (approveStatusCode: string, record: any) => (
        <div
          className={`items-center justify-start space-x-3 rtl:space-x-reverse`}
        >
          {<ApproveStatusCodeBadge approveStatusCode={approveStatusCode} />}
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
    //   render: (data: Customer) => {
    //     return (
    //       <ActionButtons
    //         id={data?.id}
    //         approveModalView={
    //           data?.approveStatusCode === 'REQ' && 'APPROVE_CUSTOMER'
    //         }
    //         disapproveModalView={
    //           data?.approveStatusCode === 'REQ' && 'DISAPPROVE_CUSTOMER'
    //         }
    //         editUrl={`${Routes.customers.editWithoutLang(data?.id)}`}
    //         detailsUrl={Routes?.customers?.details(data?.id)}
    //         deleteModalView="DELETE_CUSTOMER"
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
          data={customers}
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

export default CustomerList;

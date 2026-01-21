import ActionButtons from '@/components/common/action-buttons';
import Pagination from '@/components/ui/pagination';
import Link from '@/components/ui/link';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, CustomerContract} from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import ApproveStatusCodeBadge from '../common/approve-status-code-badge';

type IProps = {
  customerContracts: CustomerContract[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

const CustomerContractList = ({ customerContracts, paginatorInfo, onPagination }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();

  const columns = [
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 30,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="uppercase">Customer contract name</span>,
      dataIndex: 'contractName',
      key: 'contractName',
      width: 140,
      align: 'left',
      ellipsis: true,
      render: (contractName: string, object: any) => (
        <Link
          href={Routes?.customerContracts?.details(object?.id)}
          className="e_not_link"
        >
          <span className="truncate whitespace-nowrap">{contractName}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">Customer contract Id</span>,
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
      title: <span className="uppercase">Customer Id</span>,
      dataIndex: 'customerId',
      key: 'customerId',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (customerId: string) => (
        <span className="truncate whitespace-nowrap">{customerId}</span>
      ),
    },
    {
      title: <span className="uppercase">Customer name</span>,
      dataIndex: 'customerName',
      key: 'customerName',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (customerName: string) => (
        <span className="truncate whitespace-nowrap">{customerName}</span>
      ),
    },
    // {
    //   title: 'Start date',
    //   dataIndex: 'startDate',
    //   key: 'startDate',
    //   width: 140,
    //   align: 'center',
    //   ellipsis: true,
    //   render: (startDate: string) => (
    //     <span className="truncate whitespace-nowrap">{startDate}</span>
    //   ),
    // },
    // {
    //   title: 'End date',
    //   dataIndex: 'endDate',
    //   key: 'endDate',
    //   width: 140,
    //   align: 'center',
    //   ellipsis: true,
    //   render: (endDate: number) => (
    //     <span className="truncate whitespace-nowrap">{endDate}</span>
    //   ),
    // },
    {
      title: <span className="uppercase">Approval status</span>,
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
      title: <span className="uppercase">Registration date</span>,
      dataIndex: 'regDt',
      key: 'regDt',
      width: 140,
      align: 'center',
      ellipsis: true,
      render: (regDt: string) => (
        <span className="truncate whitespace-nowrap">{regDt}</span>
      ),
    },
    // {
    //   title: t('table:table-item-actions'),
    //   key: 'actions',
    //   align: 'center',
    //   width: 200,
    //   render: (data: CustomerContract) => {
    //     return (
    //       <ActionButtons
    //         id={data?.id}
    //         approveModalView={data?.approveStatusCode === 'REQ' && "APPROVE_CUSTOMER_CONTRACT"}
    //         disapproveModalView={data?.approveStatusCode === 'REQ' && "DISAPPROVE_CUSTOMER_CONTRACT"}
    //         editUrl={`${Routes.customerContracts.editWithoutLang(data?.id)}`}
    //         detailsUrl={Routes?.customerContracts?.details(data?.id)}
    //         deleteModalView="DELETE_CUSTOMER_CONTRACT"
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
          data={customerContracts}
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

export default CustomerContractList;

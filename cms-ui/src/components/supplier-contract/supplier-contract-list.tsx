import ActionButtons from '@/components/common/action-buttons';
import Pagination from '@/components/ui/pagination';
import Link from '@/components/ui/link';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, SupplierContract } from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import ApproveStatusCodeBadge from '../common/approve-status-code-badge';

type IProps = {
  supplierContracts: SupplierContract[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

const SupplierContractList = ({
  supplierContracts,
  paginatorInfo,
  onPagination,
}: IProps) => {
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
    // {
    //   title: t('table:table-item-id'),
    //   dataIndex: 'id',
    //   key: 'id',
    //   align: 'center',
    //   width: 100,
    // },
    {
      title: <span className="uppercase">Supplier contract name</span>,
      dataIndex: 'contractName',
      key: 'contractName',
      width: 140,
      align: 'left',
      ellipsis: true,
      render: (contractName: string, object: any) => (
        <Link
          href={Routes?.supplierContracts?.details(object?.id)}
          className="e_not_link"
        >
          <span className="truncate whitespace-nowrap">{contractName}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">Supplier contract Id</span>,
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
      title: <span className="uppercase">Supplier Id</span>,
      dataIndex: 'supplierId',
      key: 'supplierId',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (supplierId: string) => (
        <span className="truncate whitespace-nowrap">{supplierId}</span>
      ),
    },
    {
      title: <span className="uppercase">Supplier name</span>,
      dataIndex: 'supplierName',
      key: 'supplierName',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (supplierName: string) => (
        <span className="truncate whitespace-nowrap">{supplierName}</span>
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
    //   render: (data: SupplierContract) => {
    //     return (
    //       <ActionButtons
    //         id={data?.id}
    //         approveModalView={
    //           data?.approveStatusCode === 'REQ' && 'APPROVE_SUPPLIER_CONTRACT'
    //         }
    //         disapproveModalView={
    //           data?.approveStatusCode === 'REQ' &&
    //           'DISAPPROVE_SUPPLIER_CONTRACT'
    //         }
    //         editUrl={`${Routes.supplierContracts.editWithoutLang(data?.id)}`}
    //         detailsUrl={Routes?.supplierContracts?.details(data?.id)}
    //         deleteModalView="DELETE_SUPPLIER_CONTRACT"
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
          data={supplierContracts}
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

export default SupplierContractList;

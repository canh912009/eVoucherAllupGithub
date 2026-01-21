import ActionButtons from '@/components/common/action-buttons';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import Link from '@/components/ui/link';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, Store } from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';

type IProps = {
  stores: Store[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
};

const StoreList = ({ stores, paginatorInfo, onPagination }: IProps) => {
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
      width: 50,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="uppercase">Store ID</span>,
      dataIndex: 'id',
      key: 'id',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (id: string, object: any) => (
        <Link href={Routes?.stores?.details(object?.id)} className="e_not_link">
          <span className="truncate whitespace-nowrap">{object?.id}</span>
        </Link>
      ),
    },
    {
      title: <span className="uppercase">Store name</span>,
      dataIndex: 'storeName',
      key: 'storeName',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (storeName: string) => (
        <span className="truncate whitespace-nowrap">{storeName}</span>
      ),
    },
    {
      title: <span className="uppercase">Brand name</span>,
      dataIndex: 'brandName',
      key: 'brandName',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (brandName: string) => (
        <span className="truncate whitespace-nowrap">{brandName}</span>
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
    {
      title: <span className="uppercase">Region</span>,
      dataIndex: 'region',
      key: 'region',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (region: string) => (
        <span className="truncate whitespace-nowrap">{region}</span>
      ),
    },
    {
      title: <span className="uppercase">active</span>,
      dataIndex: 'validYn',
      key: 'validYn',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (validYn: string) => (
        <span className="truncate whitespace-nowrap">
          {renderValidYn(validYn)}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Modification date</span>,
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
    //   render: (data: Store) => {
    //     return (
    //       <ActionButtons
    //         id={data?.id}
    //         editUrl={`${Routes.stores.editWithoutLang(data?.id)}`}
    //         detailsUrl={Routes?.stores?.details(data?.id)}
    //         deleteModalView="DELETE_STORE"
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
          data={stores}
          rowKey="id"
          scroll={{ x: 1000, y: 400 }}
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

export default StoreList;

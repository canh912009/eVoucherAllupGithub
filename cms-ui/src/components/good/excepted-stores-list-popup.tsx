import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { MappedPaginatorInfoEV, Store } from '@/types';
import { useTranslation } from 'next-i18next';
import { PAGE_SIZE } from '@/utils/constants';
import Checkbox from '@/components/ui/checkbox/checkbox';

type IProps = {
  stores: Store[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;

  selectedItems?: Store[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<Store[]>>;
};

const ExceptedStoresList = ({
  stores,
  paginatorInfo,
  onPagination,
  selectedItems,
  setSelectedItems,
}: IProps) => {

  // console.log('stores = ', stores)
  const { t } = useTranslation();

  const renderValidYn = (validYn: string) => {
    if (validYn === 'Y') return 'Yes';
    if (validYn === 'N') return 'No';
    return '';
  };

  const handleCheckboxChange = (store: Store) => {
    setSelectedItems?.((prevSelectedItems) => {
      if (prevSelectedItems.some((item) => item.id === store.id)) {
        // Item is already selected, so remove it from the selection
        return prevSelectedItems.filter((item) => item.id !== store.id);
      } else {
        // Item is not selected, so add it to the selection
        return [...prevSelectedItems, store];
      }
    });
  };

  const columns = [
    selectedItems!! && setSelectedItems
      ? {
        title: '',
        width: 35,
        align: 'center',
        key: 'index',
        ellipsis: true,
        render: (store: Store) => (
          <Checkbox
            name={`store_${store.id}`}
            label={t('')}
            checked={selectedItems?.some((item) => item.id === store.id)}
            onChange={() => handleCheckboxChange(store)}
          />
        ),
      } :
      {
        title: 'NO',
        dataIndex: 'index',
        key: 'index',
        align: 'center',
        width: 50,
        render: (index: number) => {
          const currentPage = paginatorInfo?.currentPage ?? 1;
          const perPage = paginatorInfo?.perPage ?? 0;
          return (currentPage - 1) * perPage + index + 1;
        },
      },
    {
      title: <span className="uppercase">Store ID</span>,
      dataIndex: 'id',
      key: 'id',
      width: 140,
      align: 'center',
      ellipsis: true,
      render: (id: string, object: any) => (
        <span className="truncate whitespace-nowrap">{object?.id}</span>
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
      width: 100,
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
      width: 100,
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
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (updtDt: string) => (
        <span className="truncate whitespace-nowrap">{updtDt}</span>
      ),
    },
  ];

  return (
    <>
      <div className="mb-2 overflow-hidden rounded shadow">
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

export default ExceptedStoresList;


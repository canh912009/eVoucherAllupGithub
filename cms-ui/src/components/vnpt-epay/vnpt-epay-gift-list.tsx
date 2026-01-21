import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { MappedPaginatorInfoEV, VNPTEPayGift } from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';

type IProps = {
  data: VNPTEPayGift[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
};

const VNPTEPayGiftList = ({ data, paginatorInfo, onPagination }: IProps) => {
  const { t } = useTranslation();

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
      title: <span className="uppercase">ID</span>,
      dataIndex: 'id',
      key: 'id',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (id: string) => (
        <span className="truncate whitespace-nowrap">{id}</span>
      ),
    },
    {
      title: <span className="uppercase">Gift title</span>,
      dataIndex: 'giftTitle',
      key: 'giftTitle',
      width: 250,
      align: 'left',
      ellipsis: true,
      render: (giftTitle: string) => (
        <span className="truncate whitespace-nowrap">{giftTitle}</span>
      ),
    },
    {
      title: <span className="uppercase">Brand Name</span>,
      dataIndex: 'brandName',
      key: 'brandName',
      width: 250,
      align: 'left',
      ellipsis: true,
      render: (brandName: string) => (
        <span className="truncate whitespace-nowrap">{brandName}</span>
      ),
    },
    {
      title: <span className="uppercase">Price</span>,
      dataIndex: 'price',
      key: 'price',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (price: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(price)}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Quantity</span>,
      dataIndex: 'quantity',
      key: 'quantity',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (quantity: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(quantity)}
        </span>
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
          data={data}
          rowKey="id"
          scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-center">
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

export default VNPTEPayGiftList;

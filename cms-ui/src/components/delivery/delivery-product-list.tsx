import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import Radio from '@/components/ui/radio/radio';
import { MappedPaginatorInfoEV, Good } from '@/types';
import { useTranslation } from 'next-i18next';
import { PAGE_SIZE } from '@/utils/constants';

type IProps = {
  goods: Good[] | undefined;
  goodId?: string;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
  onSelectedGoodIdChange?: (goodId: string) => void;
};

const DeliveryGoodsList = ({
  goods,
  goodId,
  paginatorInfo,
  onPagination,
  onSelectedGoodIdChange,
}: IProps) => {
  const { t } = useTranslation();

  const handleClickGood = (e: any) => {
    let goodIdClicked = e.target.value;

    // console.log('goodIdClicked', goodIdClicked);

    // Call the callback function to pass the goodId value to the parent component
    onSelectedGoodIdChange?.(goodIdClicked);
  };

  const columns = [
    {
      title: '',
      dataIndex: 'id',
      key: 'id',
      width: 30,
      align: 'center',
      ellipsis: true,
      render: (id: number) =>
        goodId ? (
          <Radio
            id={`good_${id}`}
            name="goodsId"
            value={id}
            checked={parseInt(goodId) === id}
            disabled={true}
          />
        ) : (
          <Radio
            id={`good_${id}`}
            name="goodsId"
            value={id}
            onClick={handleClickGood}
          />
        ),
    },
    {
      title: <span className="uppercase">Product ID</span>,
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      width: 100,
    },
    {
      title: <span className="uppercase">Product name</span>,
      dataIndex: 'goodsName',
      key: 'goodsName',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (goodsName: string) => (
        <span className="truncate whitespace-nowrap">{goodsName}</span>
      ),
    },
    {
      title: <span className="uppercase">Supplier ID</span>,
      dataIndex: 'supplierId',
      key: 'supplierId',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (supplierId: string, record: Good) => (
        <span className="truncate whitespace-nowrap">{record?.supplier?.id}</span>
      ),
    },
    {
      title: <span className="uppercase">Brand ID</span>,
      dataIndex: 'brandId',
      key: 'brandId',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (brandId: string, record: Good) => (
        <span className="truncate whitespace-nowrap">{record?.brand?.id}</span>
      ),
    },
    {
      title: <span className="uppercase">Exchange cost</span>,
      dataIndex: 'sellPrice',
      key: 'sellPrice',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (sellPrice: string) => (
        <span className="truncate whitespace-nowrap">{sellPrice}</span>
      ),
    },
    {
      title: <span className="uppercase">Settlement method</span>,
      dataIndex: 'settlementMethodCode',
      key: 'settlementMethodCode',
      width: 140,
      align: 'center',
      ellipsis: true,
      render: (settlementMethodCode: string) => (
        <span className="truncate whitespace-nowrap">
          {settlementMethodCode}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Registration time</span>,
      dataIndex: 'startDate',
      key: 'startDate',
      width: 140,
      align: 'center',
      ellipsis: true,
      render: (startDate: string) => (
        <span className="truncate whitespace-nowrap">{startDate}</span>
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
          data={goods}
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

export default DeliveryGoodsList;

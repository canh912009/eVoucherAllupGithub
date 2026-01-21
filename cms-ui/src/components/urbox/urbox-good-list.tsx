import Link from '@/components/ui/link';
import Pagination from '@/components/ui/pagination';
import Radio from '@/components/ui/radio/radio';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, UrBoxGood, UrBoxGoodType } from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';

type IProps = {
  data: UrBoxGood[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
  isPagination?: boolean;

  checkAddUrBox?: boolean;
  selectedItems?: UrBoxGood[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<UrBoxGood[]>>;
};

const UrBoxGoodList = ({
  data,
  paginatorInfo,
  onPagination,
  isPagination,

  // for popup show radiobox add
  checkAddUrBox,
  selectedItems,
  setSelectedItems,
}: IProps) => {
  // console.log('data', data?.length);
  const { t } = useTranslation();

  const handleCheckboxChange = (good: UrBoxGood) => {
    setSelectedItems?.([good]);
    console.log(good);
  };

  const columns = [
    {
      title: '',
      width: checkAddUrBox ? 50 : 0,

      align: 'center',
      key: 'index',
      ellipsis: true,
      render: (good: UrBoxGood) =>
        checkAddUrBox && (
          <Radio
            id={`goodsId_${good.id}`}
            name={`goodsId_${good.id}`}
            label={t('')}
            checked={selectedItems?.some((item) => item.id === good.id)}
            onChange={() => handleCheckboxChange(good)}
          />
        ),
    },
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
      title: <span className="uppercase">Goods ID</span>,
      dataIndex: 'id',
      key: 'id',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (id: string, object: any) =>
        checkAddUrBox ? (
          <span className="truncate whitespace-nowrap">{id}</span>
        ) : (
          <Link
            href={Routes?.urBoxBrand?.details(object?.brand + '/' + object?.id)}
            className="text-[#5E5ADB]"
            // className="e_not_link text-#5E5ADB"
          >
            <span className="truncate whitespace-nowrap">{id}</span>
          </Link>
        ),
    },
    {
      title: <span className="uppercase">Goods Name</span>,
      dataIndex: 'title',
      key: 'title',
      width: 250,
      align: 'left',
      ellipsis: true,
      render: (title: string) => (
        <span className="truncate whitespace-nowrap">{title}</span>
      ),
    },
    {
      title: <span className="uppercase">Good Type</span>,
      dataIndex: 'type',
      key: 'type',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (type: string) => (
        <span className="truncate whitespace-nowrap">
          {UrBoxGoodType[type as keyof typeof UrBoxGoodType]}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Dislay Type</span>,
      dataIndex: 'code_display_type',
      key: 'code_display_type',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (code_display_type: string, object: any) => (
        <span className="truncate whitespace-nowrap">
          {object?.code_display}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Quantity</span>,
      dataIndex: 'quantity',
      key: 'quantity',
      width: 150,
      align: 'right',
      ellipsis: true,
      render: (quantity: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(quantity)}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Price</span>,
      dataIndex: 'price',
      key: 'price',
      width: 250,
      align: 'right',
      ellipsis: true,
      render: (price: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(price)}
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
          scroll={isPagination ? { x: 1000 } : { x: 1000, y: 400 }}
        />
      </div>

      {isPagination && !!paginatorInfo?.totalCount && (
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

export default UrBoxGoodList;

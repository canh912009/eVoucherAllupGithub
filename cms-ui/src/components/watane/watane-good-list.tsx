import Link from '@/components/ui/link';
import Pagination from '@/components/ui/pagination';
import Radio from '@/components/ui/radio/radio';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import {MappedPaginatorInfoEV, UrBoxGood, UrBoxGoodType, WataneGood, WataneGoodType, WatanePackageType} from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';

type IProps = {
  data: WataneGood[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
  isPagination?: boolean;

  checkAddUrBox?: boolean;
  selectedItems?: WataneGood[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<WataneGood[]>>;
};

const WataneGoodList = ({
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

  const handleCheckboxChange = (good: WataneGood) => {
    setSelectedItems?.([good]);
    // console.log(good);
  };

  const columns = [
    {
      title: '',
      width: checkAddUrBox ? 50 : 0,

      align: 'center',
      key: 'index',
      ellipsis: true,
      render: (good: WataneGood) =>
        checkAddUrBox && (
          <Radio
            id={`goodsId_${good.code}`}
            name={`goodsId_${good.code}`}
            label={t('')}
            checked={selectedItems?.some((item) => item.code === good.code)}
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
      title: <span className="uppercase">code</span>,
      dataIndex: 'code',
      key: 'code',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (code: string, object: any) =>
        checkAddUrBox ? (
          <span className="truncate whitespace-nowrap">{code}</span>
        ) : (
          <Link
            href={Routes?.wataneProducts?.details(object?.code)}
            className="text-[#5E5ADB]"
            // className="e_not_link text-#5E5ADB"
          >
            <span className="truncate whitespace-nowrap">{code}</span>
          </Link>
        ),
    },
    {
      title: <span className="uppercase">Name</span>,
      dataIndex: 'name',
      key: 'name',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (name: string) => (
        <span className="truncate whitespace-nowrap">{name}</span>
      ),
    },
    {
      title: <span className="uppercase">Type</span>,
      dataIndex: 'type',
      key: 'type',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (type: string) => (
        <span className="truncate whitespace-nowrap">
          {WataneGoodType[type as keyof typeof WataneGoodType]}
        </span>
      ),
    },
    {
      title: <span className="uppercase">package Type</span>,
      dataIndex: 'packageType',
      key: 'packageType',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (packageType: string, object: any) => (
        <span className="truncate whitespace-nowrap">
          {WatanePackageType[packageType as keyof typeof WatanePackageType]}
        </span>
      ),
    },
    {
      title: <span className="uppercase">value</span>,
      dataIndex: 'value',
      key: 'value',
      width: 150,
      align: 'right',
      ellipsis: true,
      render: (value: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(value, false, true)}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Price</span>,
      dataIndex: 'price',
      key: 'price',
      width: 150,
      align: 'right',
      ellipsis: true,
      render: (price: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(price, false, true)}
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

export default WataneGoodList;

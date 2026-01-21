import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import Link from '@/components/ui/link';
import {GiftPopBrand, GiftPopGood, MappedPaginatorInfoEV} from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import { Routes } from '@/config/routes';
import Checkbox from "@/components/ui/checkbox/checkbox";
import Radio from "@/components/ui/radio/radio";

type IProps = {
  data: GiftPopGood[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
  isPagination?: boolean;

  checkAddGiftPop?: boolean;
  selectedItems?: GiftPopGood[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<GiftPopGood[]>>;
};

const GiftPopGoodList = ({
  data,
  paginatorInfo,
  onPagination,
  isPagination,

   // for popup show radiobox add
   checkAddGiftPop,
   selectedItems,
   setSelectedItems,
}: IProps) => {
  const { t } = useTranslation();

  const handleCheckboxChange = (goodGiftpop: GiftPopGood) => {
    setSelectedItems?.([goodGiftpop]);
    console.log(goodGiftpop)
  };

  const columns = [
    {
      title: '',
      width: checkAddGiftPop ? 50 : 0,

      align: 'center',
      key: 'index',
      ellipsis: true,
      render: (goodGiftpop: GiftPopGood) => (
          checkAddGiftPop && <Radio
              id={`goodsId_${goodGiftpop.goodsId}`}
              name={`goodsId_${goodGiftpop.goodsId}`}
              label={t('')}
              checked={selectedItems?.some((item) => item.goodsId === goodGiftpop.goodsId)}
              onChange={() => handleCheckboxChange(goodGiftpop)}
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
      dataIndex: 'goodsId',
      key: 'goodsId',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (goodsId: string, object: any) => (
          checkAddGiftPop
              ? <span className="truncate whitespace-nowrap">{goodsId}</span>
              : <Link
                    href={Routes?.giftPopBrand?.details(object?.brandCode + '/' + object?.goodsId)}
                    className="text-[#5E5ADB]"
                    // className="e_not_link text-#5E5ADB"
                >
                  <span className="truncate whitespace-nowrap">{goodsId}</span>
                </Link>
      ),
    },
    {
      title: <span className="uppercase">Goods</span>,
      dataIndex: 'goodsName',
      key: 'goodsName',
      width: 250,
      align: 'left',
      ellipsis: true,
      render: (goodsName: string) => (
        <span className="truncate whitespace-nowrap">{goodsName}</span>
      ),
    },
    {
      title: <span className="uppercase">List Prices</span>,
      dataIndex: 'listPrice',
      key: 'listPrice',
      width: 250,
      align: 'right',
      ellipsis: true,
      render: (listPrice: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(listPrice)}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Sale Prices</span>,
      dataIndex: 'salePrice',
      key: 'salePrice',
      width: 150,
      align: 'right',
      ellipsis: true,
      render: (salePrice: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(salePrice)}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Sale Rate</span>,
      dataIndex: 'saleRate',
      key: 'saleRate',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (saleRate: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(saleRate)}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Used</span>,
      dataIndex: 'useYN',
      key: 'useYN',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (useYN: string) => (
        <span className="truncate whitespace-nowrap">{useYN === 'Y' ? 'YES' : 'NO'}</span>
      ),
    },
    {
      title: <span className="uppercase">Stock (Quantity)</span>,
      dataIndex: 'stock',
      key: 'stock',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (stock: number) => (
        <span className="truncate whitespace-nowrap">
          {formatNumber(stock)}
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

export default GiftPopGoodList;

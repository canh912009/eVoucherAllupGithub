import Link from '@/components/ui/link';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { getUrlPublicAsset } from '@/data/download';
import {Category, GiftPopBrand, MappedPaginatorInfoEV} from '@/types';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';
import Checkbox from "@/components/ui/checkbox/checkbox";
import Radio from "@/components/ui/radio/radio";

type IProps = {
  data: GiftPopBrand[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
  isPagination?: boolean;

  checkAddGiftPop?: boolean;
  selectedItems?: GiftPopBrand[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<GiftPopBrand[]>>;
};

const GiftPopBrandList = ({
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

  const handleCheckboxChange = (brand: GiftPopBrand) => {
    setSelectedItems?.([brand]);
    console.log(brand)
  };

  // brandCode = 'GRAB', 'EPAYGAME', and 'EPAYDATA' -->  disable ko cho chọn lúc tạo brand
  const columns = [
    {
      title: '',
      width: checkAddGiftPop ? 30 : 0,

      align: 'center',
      key: 'index',
      ellipsis: true,
      render: (brand: GiftPopBrand) => (
          checkAddGiftPop && !['GRAB', 'EPAYGAME', 'EPAYDATA'].includes(brand.brandCode) && <Radio
              name={`brandCode_${brand.brandCode}`}
              id={`brandCode_${brand.brandCode}`}
              label={t('')}
              checked={selectedItems?.some((item) => item.brandCode === brand.brandCode)}
              onChange={() => handleCheckboxChange(brand)}
          />
      ),
    },
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
      title: <span className="uppercase">Brands Code</span>,
      dataIndex: 'brandCode',
      key: 'brandCode',
      width: 250,
      align: 'left',
      ellipsis: true,
      render: (brandCode: string, object: any) => (
          !checkAddGiftPop
          ? <Link
              href={Routes?.giftPopBrand?.details(object?.brandCode)}
              className="text-[#5E5ADB]"
              // className="e_not_link text-#5E5ADB"
            >
              <span className="truncate whitespace-nowrap">{brandCode}</span>
            </Link>
          : <span className="truncate whitespace-nowrap">{brandCode}</span>
      ),
    },
    {
      title: <span className="uppercase">Brands</span>,
      dataIndex: 'brandName',
      key: 'brandName',
      width: 250,
      align: 'left',
      ellipsis: true,
      render: (brandName: string) => (
        <span className="truncate whitespace-nowrap">{brandName}</span>
      ),
    },
    // {
    //   title: <span className="uppercase">Logo</span>,
    //   dataIndex: 'brandLogo',
    //   key: 'brandLogo',
    //   width: 250,
    //   align: 'center',
    //   ellipsis: true,
    //   render: (brandLogo: string) => {
    //     if (!brandLogo) {
    //       return null;
    //     }
    //     return (
    //       <div style={{ display: 'flex', justifyContent: 'center' }}>
    //         <img
    //           src={getUrlPublicAsset(brandLogo) ?? ''}
    //           alt=""
    //           width={60}
    //           height={60}
    //         />
    //       </div>
    //     );
    //   },
    // },
  ];

  return (
    <>
      <div className="mb-6 w-full overflow-hidden rounded shadow">
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
        <div className="flex w-full items-center justify-center">
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

export default GiftPopBrandList;

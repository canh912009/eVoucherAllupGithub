import Link from '@/components/ui/link';
import Pagination from '@/components/ui/pagination';
import Radio from '@/components/ui/radio/radio';
import { Table } from '@/components/ui/table';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, UrBoxBrand } from '@/types';
import { PAGE_SIZE } from '@/utils/constants';
import { useTranslation } from 'next-i18next';

type IProps = {
  data: UrBoxBrand[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
  isPagination?: boolean;

  checkAddUrBox?: boolean;
  selectedItems?: UrBoxBrand[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<UrBoxBrand[]>>;
};

const UrBoxBrandList = ({
  data,
  paginatorInfo,
  onPagination,
  isPagination,

  // for popup show radiobox add
  checkAddUrBox,
  selectedItems,
  setSelectedItems,
}: IProps) => {
  const { t } = useTranslation();

  const handleCheckboxChange = (brand: UrBoxBrand) => {
    setSelectedItems?.([brand]);
    console.log(brand);
  };

  const columns = [
    {
      title: '',
      width: checkAddUrBox ? 30 : 0,

      align: 'center',
      key: 'index',
      ellipsis: true,
      render: (brand: UrBoxBrand) =>
        checkAddUrBox && (
          <Radio
            name={`brandCode_${brand.id}`}
            id={`brandCode_${brand.id}`}
            label={t('')}
            checked={selectedItems?.some((item) => item.id === brand.id)}
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
      title: <span className="uppercase">Brands Name</span>,
      dataIndex: 'id',
      key: 'id',
      width: 250,
      align: 'left',
      ellipsis: true,
      render: (id: string, object: any) =>
        !checkAddUrBox ? (
          <Link
            href={Routes?.urBoxBrand?.details(object?.id)}
            className="text-[#5E5ADB]"
            // className="e_not_link text-#5E5ADB"
          >
            <span className="truncate whitespace-nowrap">{object.title}</span>
          </Link>
        ) : (
          <span className="truncate whitespace-nowrap">{object.title}</span>
        ),
    },
    {
      title: <span className="uppercase">Category</span>,
      dataIndex: 'cat_title',
      key: 'cat_title',
      width: 250,
      align: 'left',
      ellipsis: true,
      render: (cat_title: string) => (
        <span className="truncate whitespace-nowrap">{cat_title}</span>
      ),
    },
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

export default UrBoxBrandList;

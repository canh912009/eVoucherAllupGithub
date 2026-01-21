import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import { MappedPaginatorInfoEV, Category } from '@/types';
import { useTranslation } from 'next-i18next';
import { PAGE_SIZE } from '@/utils/constants';
import { Routes } from '@/config/routes';
import Link from "@/components/ui/link";
import Checkbox from '@/components/ui/checkbox/checkbox';
import Button from "@/components/ui/button";
import {useEffect, useState} from "react";

type IProps = {
  categories: Category[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;

  selectedItems?: Category[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<Category[]>>;
};

const CategoryList = ({
  categories,
  paginatorInfo,
  onPagination,
  selectedItems,
  setSelectedItems,
}: IProps) => {
  const { t } = useTranslation();
  const [selectAll, setSelectAll] = useState(false);

  useEffect(() => {
        if (categories && selectedItems) {
            setSelectAll(categories.every(category =>
                selectedItems.some(item => item.categoryCode === category.categoryCode)
            ));
        }
  }, [categories, selectedItems]);

  const handleSelectAll = (checked: boolean) => {
        setSelectAll(checked);
        if (checked) {
            // Thêm tất cả các mục của trang hiện tại vào selectedItems
            setSelectedItems?.((prevItems) => {
                const currentPageItems = categories || [];
                const newSelectedItems = [...prevItems];
                currentPageItems.forEach((item) => {
                    if (!newSelectedItems.some((selectedItem) => selectedItem.categoryCode === item.categoryCode)) {
                        newSelectedItems.push(item);
                    }
                });
                return newSelectedItems;
            });
        } else {
            // Loại bỏ tất cả các mục của trang hiện tại khỏi selectedItems
            setSelectedItems?.((prevItems) =>
                prevItems.filter((item) =>
                    !(categories || []).some((pageItem) => pageItem.categoryCode === item.categoryCode)
                )
            );
        }
  };

  const handleCheckboxChange = (category: Category) => {
    setSelectedItems?.((prevSelectedItems) => {
      const isSelected = prevSelectedItems.some((item) => item.categoryCode === category.categoryCode);
      if (isSelected) {
        const newSelectedItems = prevSelectedItems.filter((item) => item.categoryCode !== category.categoryCode);
        setSelectAll(false);
        return newSelectedItems;
      } else {
        const newSelectedItems = [...prevSelectedItems, category];
        setSelectAll(newSelectedItems.length === categories?.length);
        return newSelectedItems;
      }
    });
  };

  const columns = [
    selectedItems!! && setSelectedItems
      ? {
          title: (
              <Checkbox
                  name="select-all"
                  checked={selectAll}
                  onChange={(e) => handleSelectAll(e.target.checked)}
              />
          ),
        width: 30,

        align: 'center',
        key: 'index',
        ellipsis: true,
        render: (category: Category) => (
          <Checkbox
            name={`categoryCode_${category.categoryCode}`}
            label={t('')}
            checked={selectedItems?.some((item) => item.categoryCode === category.categoryCode)}
            onChange={() => handleCheckboxChange(category)}
          />
        ),
      } :
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
      title: t('CATEGORY CODE'),
      dataIndex: 'categoryCode',
      key: 'categoryCode',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (categoryCode: string) => (
        <span className="truncate whitespace-nowrap">
          {categoryCode}
        </span>
      ),
    },
    selectedItems!! && setSelectedItems
      ? {
        title: t('CATEGORY NAME'),
        dataIndex: 'categoryName',
        key: 'categoryName',
        width: 120,
        align: 'center',
        ellipsis: true,
        render: (categoryName: string, object: any) => (
          <span className="truncate whitespace-nowrap">{categoryName}</span>
        ),
      } : {
        title: t('CATEGORY NAME'),
        dataIndex: 'categoryName',
        key: 'categoryName',
        width: 120,
        align: 'center',
        ellipsis: true,
        render: (categoryName: string, object: any) => (
          <Link
            href={Routes?.category?.details(object?.categoryCode)}
            className="text-[#5E5ADB]"
          >
            <span className="truncate whitespace-nowrap">{categoryName}</span>
          </Link>
        ),
      }
    ,
    {
      title: 'VALID',
      dataIndex: 'validYn',
      key: 'validYn',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (validYn: string) => (
        <span className="truncate whitespace-nowrap">
          {validYn}
        </span>
      ),
    },
    {
      title: t('UPDATE DATE'),
      dataIndex: 'updtDt',
      key: 'updtDt',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (updtDt: string) => (
        <span className="truncate whitespace-nowrap">
          {updtDt}
        </span>
      ),
    },
    {
      title: 'REGISTRATION DATE',
      dataIndex: 'regDt',
      key: 'regDt',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (regDt: string) => (
        <span className="truncate whitespace-nowrap">
          {regDt}
        </span>
      ),
    },
  ]

  return (
    <>
      <div className="mb-6 overflow-hidden rounded shadow">
        <Table
          //@ts-ignore
          columns={columns}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">
                {t('No Data')}
              </div>
            </div>
          )}
          data={categories}
          rowKey="categoryCode"
          scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-end">
          <Pagination
            total={paginatorInfo.totalCount}
            current={paginatorInfo.currentPage}
            pageSize={PAGE_SIZE}
            onChange={(page) => {
                setSelectAll(false);
                onPagination(page);
            }}
          />
        </div>
      )}
    </>
  );
}

export default CategoryList;

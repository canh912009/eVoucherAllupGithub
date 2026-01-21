import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import {MappedPaginatorInfoEV, Brands, Store} from '@/types';
import { useTranslation } from 'next-i18next';
import { PAGE_SIZE } from '@/utils/constants';
import Image from 'next/image';
import array from 'yup/lib/array';
import ActionButtons from '@/components/common/action-buttons';
import { Routes } from '@/config/routes';
import router, { useRouter } from 'next/router';
import { getUrlPublicAsset } from '@/data/download';
import { emptyPlaceholder } from '@/utils/placeholders';
import Link from "@/components/ui/link";
import Checkbox from "@/components/ui/checkbox/checkbox";
import {useEffect, useState} from "react";

type IProps = {
  brands: Brands[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
  selectedItems?: Brands[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<Brands[]>>;
  popupType?: boolean;
};

const BrandsList = ({ brands, paginatorInfo, onPagination,
                      selectedItems,
                      setSelectedItems, popupType = false}: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [selectAll, setSelectAll] = useState(false);
  useEffect(() => {
    if (brands && selectedItems) {
      setSelectAll(brands.every(brand =>
          selectedItems.some(item => item.id === brand.id)
      ));
    }
  }, [brands, selectedItems]);

  const handleSelectAll = (checked: boolean) => {
    setSelectAll(checked);
    if (checked) {
      setSelectedItems?.((prevItems) => {
        const currentPageItems = brands || [];
        const newSelectedItems = [...prevItems];
        currentPageItems.forEach((item) => {
          if (!newSelectedItems.some((selectedItem) => selectedItem.id === item.id)) {
            newSelectedItems.push(item);
          }
        });
        return newSelectedItems;
      });
    } else {
      setSelectedItems?.((prevItems) =>
          prevItems.filter((item) =>
              !(brands || []).some((pageBrand) => pageBrand.id === item.id)
          )
      );
    }
  };

  const handleCheckboxChange = (brand: Brands) => {
    setSelectedItems?.((prevSelectedItems) => {
      const isSelected = prevSelectedItems.some((item) => item.id === brand.id);
      if (isSelected) {
        const newSelectedItems = prevSelectedItems.filter((item) => item.id !== brand.id);
        setSelectAll(false);
        return newSelectedItems;
      } else {
        const newSelectedItems = [...prevSelectedItems, brand];
        setSelectAll(newSelectedItems.length === brands?.length);
        return newSelectedItems;
      }
    });
  };

  function increatementIndex(index: number) {
    index = index + 1;
    return index.toString();
  }
  const columns = [
    selectedItems!! && setSelectedItems
        ? {
          title: (
              <Checkbox
                  name="select-all-checkbox"
                  checked={selectAll}
                  onChange={(e) => handleSelectAll(e.target.checked)}
              />
          ),
          width: 25,
          align: 'left',
          key: 'index',
          ellipsis: true,
          render: (brand: Brands) => (
              <Checkbox
                  name={`brand_${brand.id}`}
                  label={t('')}
                  checked={selectedItems?.some((item) => item.id === brand.id)}
                  onChange={() => handleCheckboxChange(brand)}
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
      title: <span className="uppercase ">{t('table:table-item-brand-id')}</span>,
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      width: 60,
      render: (id: string, object: any) => (
          popupType
              ? <span className="truncate whitespace-nowrap"> {id} </span>
              : <Link
                  href={Routes?.brands?.details(object?.id)}
                  className="text-[#5E5ADB]" >
                    <span className="truncate whitespace-nowrap">{id}</span>
                </Link>
          ),
    },
    {
      title: <span className="uppercase ">{t('table:table-item-brand-name')}</span>,
      dataIndex: 'brandName',
      key: 'brandName',
      width: 80,
      align: 'center',
      ellipsis: true,
      render: (brandName: string) => (
        <span className="truncate whitespace-nowrap">
          {brandName}
        </span>
      ),
    },
    {
      title: <span className="uppercase ">{t('table:table-item-supplier-name')}</span>,
      dataIndex: 'supplierName',
      key: 'supplierName',
      width: 80,
      align: 'center',
      ellipsis: true,
    },
    {
      title: <span className="uppercase ">active</span>,
      dataIndex: 'validYn',
      key: 'validYn',
      align: 'center',
      width: 40,
    },
    {
      title: <span className="uppercase ">modification date</span>,
      dataIndex: 'updtDt',
      key: 'updtDt',
      align: 'center',
      width: 80,
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
          data={brands}
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
            onChange={(page) => {
              setSelectAll(false);
              onPagination(page);
            }}
          />
        </div>
      )}
    </>
  );
};

export default BrandsList;

import {Category, Good, MappedPaginatorInfoEV} from "@/types";
import { useRouter } from "next/router";
import { useTranslation } from "react-i18next";
import { Table } from '@/components/ui/table';
import Pagination from '@/components/ui/pagination';
import { PAGE_SIZE } from "@/utils/constants";
import ActionButtons from "../common/action-buttons";
import { Routes } from "@/config/routes";
import Link from '@/components/ui/link';
import LinkButton from '@/components/ui/link-button';
import Checkbox from "@/components/ui/checkbox/checkbox";
import {useEffect, useState} from "react";

type IProps = {
  goods: Good[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
  selectedItems?: Good[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<Good[]>>;
};

const GoodsList = ({ goods,
                     paginatorInfo,
                     onPagination,
                     selectedItems,
                     setSelectedItems, }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();
  useEffect(() => {
    if (goods && selectedItems) {
        setSelectAll(goods.every(good =>
            selectedItems.some(item => item.id === good.id)
        ));
    }
  }, [goods, selectedItems]);

  const rootClassName =
    'ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const handleCheckboxChange = (good: Good) => {
    setSelectedItems?.((prevSelectedItems) => {
        const isSelected = prevSelectedItems.some((item) => item.id === good.id);
        if (isSelected) {
            const newSelectedItems = prevSelectedItems.filter((item) => item.id !== good.id);
            setSelectAll(false);
            return newSelectedItems;
        } else {
            const newSelectedItems = [...prevSelectedItems, good];
            setSelectAll(newSelectedItems.length === goods?.length);
            return newSelectedItems;
        }
    });
  };

  const [selectAll, setSelectAll] = useState(false);
  const handleSelectAll = (checked: boolean) => {
    setSelectAll(checked);
    if (checked) {
        setSelectedItems?.((prevItems) => {
            const currentPageItems = goods || [];
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
                !(goods || []).some((pageGood) => pageGood.id === item.id)
            )
        );
    }
  };

  const columns = [
    selectedItems!! && setSelectedItems
    ?
        {
            title: (
                <Checkbox
                    name="select-all-checkbox"
                    checked={selectAll}
                    onChange={(e) => handleSelectAll(e.target.checked)}
                />
            ),
            width: 30,
            align: 'center' as const,
            key: 'checkbox',
            render: (good: Good) => (
                <Checkbox
                    name={`id_${good.id}`}
                    checked={selectedItems?.some((item) => item.id === good.id)}
                    onChange={() => handleCheckboxChange(good)}
                />
            ),
        }
    :
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
      title: ' ID',
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      width: 50,
      render: (id: string) => (
        <span className="truncate whitespace-nowrap">{id}</span>
      ),
    },
    selectedItems!! && setSelectedItems
      ?
        {
          title: 'PRODUCT NAME',
          dataIndex: 'goodsName',
          key: 'goodsName',
          align: 'center',
          width: 100,
          render: (goodsName: string, object: any) => (
              <span className="truncate whitespace-nowrap">{goodsName}</span>
          ),
        }
      :
        {
          title: 'PRODUCT NAME',
          dataIndex: 'goodsName',
          key: 'goodsName',
          align: 'center',
          width: 100,
          render: (goodsName: string, object: any) => (
              <Link
                  href={Routes?.goods?.details(object?.id)}
                  className="e_not_link"
              >
                <span className="truncate whitespace-nowrap">{goodsName}</span>
              </Link>
          ),
        },
    {
      title: 'PRODUCT CODE',
      dataIndex: 'supplierGoodsId',
      key: 'supplierGoodsId',
      align: 'center',
      width: 100,
      render: (supplierGoodsId: string) => (
        <span className="truncate whitespace-nowrap">{supplierGoodsId}</span>
      ),
    },
    {
      title: 'BRAND NAME',
      dataIndex: 'brandName',
      key: 'brandName',
      align: 'center',
      width: 100,
      render: (brandName: string) => (
        <span className="truncate whitespace-nowrap">{brandName}</span>
      ),
    },
    {
      title: 'SUPPLIER NAME',
      dataIndex: 'supplierName',
      key: 'supplierName',
      align: 'center',
      width: 100,
      render: (supplierName: string) => (
        <span className="truncate whitespace-nowrap">{supplierName}</span>
      ),
    },
    {
      title: 'ACTIVE',
      dataIndex: 'validYn',
      key: 'validYn',
      align: 'center',
      width: 100,
      render: (validYn: string) => (
        <span className="truncate whitespace-nowrap">{validYn}</span>
      ),
    },
    {
      title: 'MODIFICATION DATE',
      dataIndex: 'updtDt',
      key: 'updtDt',
      align: 'center',
      width: 100,
      render: (updtDt: string) => (
        <span className="truncate whitespace-nowrap">{updtDt}</span>
      ),
    },
    // {
    //   title: 'Display Type',
    //   dataIndex: 'displayType',
    //   key: 'displayType',
    //   align: 'center',
    //   width: 40,
    //   render: (displayType: string) => (
    //     <span className="truncate whitespace-nowrap uppercase">{displayType}</span>
    //   ),
    // },
      {
          title: 'SYSTEM',
          dataIndex: 'system',
          key: 'system',
          align: 'center',
          width: 100,
          render: (system: string, object: any) => (
              <span className="truncate whitespace-nowrap">{system}</span>
          ),
      },
      !(selectedItems!! && setSelectedItems) && {
          title: 'UPLOAD VOUCHER PIN',
          dataIndex: 'id',
          key: 'uploadVoucherPin',
          align: 'center',
          width: 100,
          render: (id: string, record: any) => (
              (record.system === 'EXTERNAL') &&
              <LinkButton
                  className="h-7 me-1 bg-gray-300 hover:bg-gray-400"
                  href={`/goods/upload-new-pin/${id}`}
                  variant="outline"
                  aria-label="UploadPIN"
              >
                  {t('Upload PIN')}
              </LinkButton>
          ),
      },

  ]
  return (
    <>
      <div className="mt-4 overflow-hidden rounded shadow">
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
            onChange={(page) => {
                setSelectAll(false);
                onPagination?.(page);
            }}
          />
        </div>
      )}
    </>
  )
};

export default GoodsList;

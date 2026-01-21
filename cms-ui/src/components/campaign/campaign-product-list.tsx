import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import Checkbox from '@/components/ui/checkbox/checkbox';
import { MappedPaginatorInfoEV, Good } from '@/types';
import { useTranslation } from 'next-i18next';
import { PAGE_SIZE, SYSTEM_BRAND_TYPE } from '@/utils/constants';
import { CloseFillIcon } from '../icons/close-fill';
import { useState } from 'react';
import {
  SYSTEM_GROUP_ALL,
  SYSTEM_GROUP_CHOICE_BULK,
  SYSTEM_GROUP_IN_EX
} from "@/components/common/status-code-badge";

type IProps = {
  data: Good[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
  removeOne?: (item: Good) => void;
  removeAll?: () => void;
  selectedItems?: Good[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<Good[]>>;
};

const CampaignGoodsList = ({
  data,
  paginatorInfo,
  onPagination,
  removeOne,
  removeAll,
  selectedItems,
  setSelectedItems,
}: IProps) => {
  const { t } = useTranslation();
  const [systemGroup, setSystemGroup] = useState<string>('');

  const handleCheckboxChange = (good: Good) => {
    if (systemGroup === '') setSystemGroup(good.system);

    setSelectedItems?.((prevSelectedItems) => {
      if (prevSelectedItems.some((item) => item.id === good.id)) {
        // Item is already selected, so remove it from the selection
        const goodFilter = prevSelectedItems.filter(
          (item) => item.id !== good.id
        );
        if (systemGroup !== '' && goodFilter.length === 0) setSystemGroup('');
        return goodFilter;
      } else {
        // Item is not selected, so add it to the selection
        return [...prevSelectedItems, good];
      }
    });
  };

  function showCheckbox(systemGood: string): boolean {
    if (systemGroup === '') return true;

    const internalExternalGroup = SYSTEM_GROUP_IN_EX.map(item => item.code);
    const choiceBulkGroup = SYSTEM_GROUP_CHOICE_BULK.map(item => item.code);

    return (internalExternalGroup.includes(systemGroup) && internalExternalGroup.includes(systemGood)) ||
      (choiceBulkGroup.includes(systemGroup) && choiceBulkGroup.includes(systemGood));
  }

  const columns = [
    selectedItems!! && setSelectedItems
      ? {
          title: '',
          width: 50,
          align: 'center',
          ellipsis: true,
          render: (good: Good) =>
            // (systemGroup === ""  || (systemGroup !== "" && systemGroup === good.system) ) && <Checkbox
            showCheckbox(good.system) && (
              <Checkbox
                name={`goodsId_${good.id}`}
                label={t('')}
                checked={selectedItems?.some((item) => item.id === good.id)}
                onChange={() => handleCheckboxChange(good)}
              />
            ),
        }
      : '',
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
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (goodsName: string, good: Good) => (
        <span
          className={
            showCheckbox(good.system)
              ? 'truncate whitespace-nowrap font-bold'
              : 'truncate whitespace-nowrap italic'
          }
        >
          {goodsName}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Supplier ID</span>,
      dataIndex: 'supplierId',
      key: 'supplierId',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (supplierId: string, record: any) => (
        <span className="truncate whitespace-nowrap">{supplierId ?? record.supplier?.id}</span>
      ),
    },
    {
      title: <span className="uppercase">Brand ID</span>,
      dataIndex: 'brandId',
      key: 'brandId',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (brandId: string, record: any) => (
        <span className="truncate whitespace-nowrap">{brandId ?? record.brand?.id}</span>
      ),
    },
    {
      title: <span className="uppercase">Exchange Cost</span>,
      dataIndex: 'sellPrice',
      key: 'sellPrice',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (sellPrice: string) => (
        <span className="truncate whitespace-nowrap">{sellPrice}</span>
      ),
    },
    {
      title: <span className="uppercase">Settlement Method</span>,
      dataIndex: 'settlementMethodCode',
      key: 'settlementMethodCode',
      width: 180,
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
      dataIndex: 'regDt',
      key: 'regDt',
      width: 180,
      align: 'center',
      ellipsis: true,
      render: (regDt: string) => (
        <span className="truncate whitespace-nowrap">{regDt}</span>
      ),
    },
    {
      title: <span className="uppercase">system</span>,
      dataIndex: 'system',
      key: 'system',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (system: string, good: Good) => (
        <span
          className={
            showCheckbox(good.system)
              ? 'truncate whitespace-nowrap font-bold'
              : 'truncate whitespace-nowrap italic'
          }
        >
          {system}
        </span>
      ),
    },
    removeOne || removeAll
      ? {
          title:
            removeAll && data && data.length > 0 ? (
              <button
                onClick={(event) => {
                  // Prevent the form submission
                  event.preventDefault();
                  return removeAll();
                }}
                className="text-gray-300 transition duration-200 hover:text-red-600 focus:outline-none"
                title={t('common:text-disapprove')}
              >
                <CloseFillIcon width={20} />
              </button>
            ) : (
              ''
            ),
          key: 'actions',
          align: 'center',
          width: 40,
          render: (data: Good) => {
            return (
              <>
                {removeOne ? (
                  <button
                    onClick={() => removeOne(data)}
                    className="text-gray-300 transition duration-200 hover:text-red-600 focus:outline-none"
                    title={t('common:text-disapprove')}
                  >
                    <CloseFillIcon width={20} />
                  </button>
                ) : (
                  ''
                )}
              </>
            );
          },
        }
      : '',
  ];

  console.log("paginatorInfo", paginatorInfo)

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

export default CampaignGoodsList;

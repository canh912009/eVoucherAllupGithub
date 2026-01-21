import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import Checkbox from '@/components/ui/checkbox/checkbox';
import {MappedPaginatorInfoEV, Good, UrBoxBrand} from '@/types';
import { useTranslation } from 'next-i18next';
import { PAGE_SIZE, SYSTEM_BRAND_TYPE } from '@/utils/constants';
import { CloseFillIcon } from '../icons/close-fill';
import {useEffect, useState} from 'react';
import {getBrandList} from "@/data/urbox";
import {updatePaginatorFE} from "@/utils/data-mappers-ev";
import {MoveTopIcon} from "@/components/icons/move-top";
import {MoveUpIcon} from "@/components/icons/move-up";
import {MoveDownIcon} from "@/components/icons/move-down";
import {DeleteIcon} from "@/components/icons/delete-icon";

type IProps = {
  allGoods: Good[] ;
  actionMoveTop? : (item: Good) => void;
  actionMoveUp? : (item: Good) => void;
  actionMoveDown? : (item: Good) => void;
  actionRemoveItem? : (item: Good) => void;
  removeAll?: () => void;
  caseUpdate?: boolean;
  caseView?: boolean;
};

const GoodsListCustomPagination = ({
        allGoods,
        actionMoveTop,
        actionMoveUp,
        actionMoveDown,
        actionRemoveItem,
        removeAll,
        caseUpdate = false,
        caseView = false,
}: IProps) => {

  const { t } = useTranslation();
  const [systemGroup, setSystemGroup] = useState<string>('');

  const [goodsSlices, setGoodsSlices] = useState<Good[]>([]); // Display (10 records)

  const [page, setPage] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  function handlePagination(current: number) {
    // console.log('current', current);
    setPage(current);
  }

  useEffect(() => {
    const dataUpdatePaging = updatePaginatorFE(
        { page: page, pageSize: PAGE_SIZE},
        allGoods || []
    );
    setGoodsSlices(dataUpdatePaging.sliceData);
    setPaginatorInfo(dataUpdatePaging.pageInfo!);
  }, [page, allGoods]);

  useEffect(() => {
    if (paginatorInfo?.totalCount === null || paginatorInfo?.totalCount === 0) {
      setPaginatorInfo((prev) => ({
        ...prev!,
        totalCount: allGoods.length,
      }));
    }
  }, [paginatorInfo, allGoods]);


  const columns = [
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 30,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="uppercase">Product ID</span>,
      dataIndex: caseUpdate ? 'goodsId' : 'id',
      key: caseUpdate || caseView ? 'goodsId' : 'id',
      align: 'center',
      width: 80,
    },
    {
      title: <span className="uppercase">Product name</span>,
      dataIndex: 'goodsName',
      key: 'goodsName',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (goodsName: string, good: any) => (
        <span
          className='truncate whitespace-nowrap italic'
        >
          {caseView ? good?.goods.goodsName : goodsName}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Supplier ID</span>,
      dataIndex: 'supplierId',
      key: 'supplierId',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (supplierId: string, record: any) => (
        <span className="truncate whitespace-nowrap">{supplierId ?? record?.supplierGoodsId }</span>
      ),
    },
    {
      title: <span className="uppercase">Brand ID</span>,
      dataIndex: 'brandId',
      key: 'brandId',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (brandId: string, record: any) => (
        <span className="truncate whitespace-nowrap">{brandId }</span>
      ),
    },
    {
      title: <span className="uppercase">Exchange Cost</span>,
      dataIndex: 'sellPrice',
      key: 'sellPrice',
      width: 100,
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
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (settlementMethodCode: string, record: any) => (
        <span className="truncate whitespace-nowrap">
          { caseUpdate||caseView ? record?.goods?.settlementMethodCode : settlementMethodCode}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Registration time</span>,
      dataIndex: 'regDt',
      key: 'regDt',
      width: 100,
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
      width: 80,
      align: 'center',
      ellipsis: true,
      render: (system: string, good: Good) => (
        <span
          className='truncate whitespace-nowrap italic'
        >
          { caseUpdate||caseView ? good?.goods?.system : system}
        </span>
      ),
    },
    {
      title: removeAll ? <button
        onClick={(event) => {
          event.preventDefault();
          return removeAll();
        }}
        className="text-gray-300 transition duration-200 hover:text-red-600 focus:outline-none md:w-1/4"
        title={"Remove"}
      >
        <DeleteIcon width={15} />
      </button> :  (
        ''
      ),
      key: 'actions',
      align: 'center',
      width: 30,
      render: (data: Good) => {
        return (
            <>
              <div className="w-full sm:w-full md:w-full">
                {/*<button
                    onClick={(event) => {
                      event.preventDefault();
                      actionMoveTop?.(data);
                    }}
                    className="text-gray-300 transition duration-200 hover:text-red-600 focus:outline-none md:w-1/4"
                    title={"MoveTop"}
                >
                  <MoveTopIcon width={15} />
                </button>
                <button
                    onClick={(event) => {
                      event.preventDefault();
                      actionMoveUp?.(data);
                    }}
                    className="text-gray-300 transition duration-200 hover:text-red-600 focus:outline-none md:w-1/4"
                    title={"MoveUp"}
                >
                  <MoveUpIcon width={15} />
                </button>
                <button
                    onClick={(event) => {
                      event.preventDefault();
                      actionMoveDown?.(data);
                    }}
                    className="text-gray-300 transition duration-200 hover:text-red-600 focus:outline-none md:w-1/4"
                    title={"MoveDown"}
                >
                  <MoveDownIcon width={15} />
                </button>*/}
                {actionRemoveItem ? <button
                    onClick={(event) => {
                      event.preventDefault();
                      actionRemoveItem?.(data);
                    }}
                    className="text-gray-300 transition duration-200 hover:text-red-600 focus:outline-none md:w-1/4"
                    title={"Remove"}
                >
                  <DeleteIcon width={15} />
                </button> :  (
                  ''
                )}
              </div>
            </>
        );
      },
    },
  ];

  return (
    <>
      <div className="mb-6 overflow-hidden rounded shadow w-full">
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
          data={goodsSlices}
          rowKey="id"
          scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-end w-full">
          <Pagination
            total={allGoods?.length}
            current={paginatorInfo?.currentPage}
            pageSize={PAGE_SIZE}
            onChange={handlePagination}
          />
        </div>
      )}
    </>
  );
};

export default GoodsListCustomPagination;

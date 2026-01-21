import Pagination from '@/components/ui/pagination';
import {Table} from '@/components/ui/table';
import {Cs, MappedPaginatorInfoEV, RawSettlement, SortOrder} from '@/types';
import {useTranslation} from 'next-i18next';
import {useRouter} from 'next/router';
import {PAGE_SIZE, PERMISSIONS_EV, SYSTEM_BRAND_TYPE} from '@/utils/constants';
// @ts-ignore
import {parse} from 'json2csv';
import Button from "@/components/ui/button";
import TitleWithSort from "@/components/ui/title-with-sort";
import {useState} from "react";
import DownloadCsvDropdown from "@/components/ui/downloadCsv";
import {toast} from "react-toastify";
import {getUserInfo} from "@/utils/auth-utils";

type IProps = {
  rawSettlement: RawSettlement[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
  onOrder: (current: string) => void;
};

type SortingObjType = {
  sort: SortOrder;
  column: string | null;
};

const RawSettlementList = ({ rawSettlement, paginatorInfo, onPagination, onOrder, }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();
  const infoUser = getUserInfo()
  const supplierRole = (infoUser?.roleCode === PERMISSIONS_EV.ROLE_SUPPLIER)

  const [sortingObj, setSortingObj] = useState<SortingObjType>({
    sort: SortOrder.None,
    column: "", //orderBy
  });

  const onHeaderClick = (column: string | null) => ({
    onClick: () => {
      sortingObj.sort = (sortingObj.column === column)
        ? (sortingObj.sort === SortOrder.Asc) ? SortOrder.Desc : SortOrder.Asc
        : SortOrder.Asc,
      sortingObj.column = column
      onOrder(column!);
      // console.log("onHeaderClick", sortingObj);
    },
  });

  const columns = [
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">log id</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'logId'
          }
          isActive={sortingObj.column === 'logId'}
        />
      ),
      // title: <span className="uppercase">log id</span>,
      className: 'cursor-pointer',
      dataIndex: 'logId',
      key: 'logId',
      align: 'center',
      width: 90,
      onHeaderCell: () => onHeaderClick('logId'),
      render: (logId: string) => (
        <span className="truncate whitespace-nowrap">{logId}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">evoucher id</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'ev'
          }
          isActive={sortingObj.column === 'ev'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('ev'),
      dataIndex: 'ev',
      key: 'ev',
      width: 320,
      align: 'center',
      ellipsis: true,
      render: (ev: string) => (
        <span className="truncate whitespace-nowrap">{ev}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">log type</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'settlementLogType'
          }
          isActive={sortingObj.column === 'settlementLogType'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('settlementLogType'),
      dataIndex: 'settlementLogType',
      key: 'settlementLogType',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (settlementLogType: string) => (
        <span className="truncate whitespace-nowrap">{settlementLogType}</span>
      ),
    },
    !supplierRole && {
      title: (
        <TitleWithSort
          title={<span className="uppercase">PUSBLISH NAME</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'publishName'
          }
          isActive={sortingObj.column === 'publishName'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('publishName'),
      dataIndex: 'publishName',
      key: 'publishName',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (publishName: string) => (
        <span className="truncate whitespace-nowrap">{publishName}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">delivery id</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'publishId'
          }
          isActive={sortingObj.column === 'publishId'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('publishId'),
      dataIndex: 'publishId',
      key: 'publishId',
      width: 140,
      align: 'center',
      ellipsis: true,
      render: (publishId: string) => (
        <span className="truncate whitespace-nowrap">{publishId}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">pin</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'pin'
          }
          isActive={sortingObj.column === 'pin'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('pin'),
      dataIndex: 'pin',
      key: 'pin',
      width: 170,
      align: 'center',
      ellipsis: true,
      render: (pin: string) => (
        <span className="truncate whitespace-nowrap">{pin}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">transaction date</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'transactionDate'
          }
          isActive={sortingObj.column === 'transactionDate'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('transactionDate'),
      dataIndex: 'transactionDate',
      key: 'transactionDate',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (transactionDate: string) => (
        <span className="truncate whitespace-nowrap">{transactionDate}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">log Create Date</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'logCreateDate'
          }
          isActive={sortingObj.column === 'logCreateDate'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('logCreateDate'),
      dataIndex: 'logCreateDate',
      key: 'logCreateDate',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (logCreateDate: string) => (
        <span className="truncate whitespace-nowrap">{logCreateDate}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">product name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'goodsName'
          }
          isActive={sortingObj.column === 'goodsName'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('goodsName'),
      dataIndex: 'goodsName',
      key: 'goodsName',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (goodsName: string) => (
        <span className="truncate whitespace-nowrap">{goodsName}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Target</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'settlementTarget'
          }
          isActive={sortingObj.column === 'settlementTarget'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('settlementTarget'),
      dataIndex: 'settlementTarget',
      key: 'settlementTarget',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (settlementTarget: string) => (
        <span className="truncate whitespace-nowrap">{settlementTarget}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Target name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'companyName'
          }
          isActive={sortingObj.column === 'companyName'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('companyName'),
      dataIndex: 'companyName',
      key: 'companyName',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (primaryContactName: string) => (
        <span className="truncate whitespace-nowrap">{primaryContactName}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">brand name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'brandName'
          }
          isActive={sortingObj.column === 'brandName'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('brandName'),
      dataIndex: 'brandName',
      key: 'brandName',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (brandName: string) => (
        <span className="truncate whitespace-nowrap">{brandName}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">store name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'storeName'
          }
          isActive={sortingObj.column === 'storeName'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('storeName'),
      dataIndex: 'storeName',
      key: 'storeName',
      width: 130,
      align: 'center',
      ellipsis: true,
      render: (storeName: string) => (
        <span className="truncate whitespace-nowrap">{storeName}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">manager name</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'managerName'
          }
          isActive={sortingObj.column === 'managerName'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('managerName'),
      dataIndex: 'managerName',
      key: 'managerName',
      width: 130,
      align: 'center',
      ellipsis: true,
      render: (managerName: string) => (
        <span className="truncate whitespace-nowrap">{managerName}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">mobile number</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'userMobileNumber'
          }
          isActive={sortingObj.column === 'userMobileNumber'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('userMobileNumber'),
      dataIndex: 'userMobileNumber',
      key: 'userMobileNumber',
      width: 170,
      align: 'center',
      ellipsis: true,
      render: (userMobileNumber: string) => (
        <span className="truncate whitespace-nowrap">{userMobileNumber}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">user Email</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'userEmail'
          }
          isActive={sortingObj.column === 'userEmail'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('userEmail'),
      dataIndex: 'userEmail',
      key: 'userEmail',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (userEmail: string) => (
        <span className="truncate whitespace-nowrap">{userEmail}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">voucher type</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'voucherTypeCode'
          }
          isActive={sortingObj.column === 'voucherTypeCode'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('voucherTypeCode'),
      dataIndex: 'voucherTypeCode',
      key: 'voucherTypeCode',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (voucherTypeCode: string) => (
        <span className="truncate whitespace-nowrap">{voucherTypeCode}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">settlement method</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'settlementMethodCode'
          }
          isActive={sortingObj.column === 'settlementMethodCode'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('settlementMethodCode'),
      dataIndex: 'settlementMethodCode',
      key: 'settlementMethodCode',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (settlementMethodCode: string) => (
        <span className="truncate whitespace-nowrap">{settlementMethodCode}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">list price</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'listPrice'
          }
          isActive={sortingObj.column === 'listPrice'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('listPrice'),
      dataIndex: 'listPrice',
      key: 'listPrice',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (listPrice: string) => (
        <span className="truncate whitespace-nowrap">{listPrice}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">sales price</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'salesPrice'
          }
          isActive={sortingObj.column === 'salesPrice'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('salesPrice'),
      dataIndex: 'salesPrice',
      key: 'salesPrice',
      width: 130,
      align: 'center',
      ellipsis: true,
      render: (salesPrice: string) => (
        <span className="truncate whitespace-nowrap">{salesPrice}</span>
      ),
    },
    // {
    //   title: <span className="uppercase">discount price</span>,
    //   dataIndex: 'discountAmount',
    //   key: 'discountAmount',
    //   width: 120,
    //   align: 'center',
    //   ellipsis: true,
    //   render: (primaryContactName: string) => (
    //     <span className="truncate whitespace-nowrap">{primaryContactName}</span>
    //   ),
    // },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">commission rate</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'commissionRate'
          }
          isActive={sortingObj.column === 'commissionRate'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('commissionRate'),
      dataIndex: 'commissionRate',
      key: 'commissionRate',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (commissionRate: string) => (
        <span className="truncate whitespace-nowrap">{commissionRate}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">vat include</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'vatIncludeYn'
          }
          isActive={sortingObj.column === 'vatIncludeYn'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('vatIncludeYn'),
      dataIndex: 'vatIncludeYn',
      key: 'vatIncludeYn',
      width: 130,
      align: 'center',
      ellipsis: true,
      render: (vatIncludeYn: string) => (
        <span className="truncate whitespace-nowrap">{vatIncludeYn}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">send code</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'sendCost'
          }
          isActive={sortingObj.column === 'sendCost'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('sendCost'),
      dataIndex: 'sendCost',
      key: 'sendCost',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (sendCost: string) => (
        <span className="truncate whitespace-nowrap">{sendCost}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">settlement complete</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'settlementCompleteYn'
          }
          isActive={sortingObj.column === 'settlementCompleteYn'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('settlementCompleteYn'),
      dataIndex: 'settlementCompleteYn',
      key: 'settlementCompleteYn',
      width: 210,
      align: 'center',
      ellipsis: true,
      render: (settlementCompleteYn: string) => (
        <span className="truncate whitespace-nowrap">{settlementCompleteYn}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">settlement complete date</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'settlementCompleteDate'
          }
          isActive={sortingObj.column === 'settlementCompleteDate'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('settlementCompleteDate'),
      dataIndex: 'settlementCompleteDate',
      key: 'settlementCompleteDate',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (settlementCompleteDate: string) => (
        <span className="truncate whitespace-nowrap">{settlementCompleteDate}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">settlement expect reason code</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'settlementExceptReasonCode'
          }
          isActive={sortingObj.column === 'settlementExceptReasonCode'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('settlementExceptReasonCode'),
      dataIndex: 'settlementExceptReasonCode',
      key: 'settlementExceptReasonCode',
      width: 280,
      align: 'center',
      ellipsis: true,
      render: (settlementExceptReasonCode: string) => (
        <span className="truncate whitespace-nowrap">{settlementExceptReasonCode}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">settlement expect reason</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'settlementExceptReason'
          }
          isActive={sortingObj.column === 'settlementExceptReason'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('settlementExceptReason'),
      dataIndex: 'settlementExceptReason',
      key: 'settlementExceptReason',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (settlementExceptReason: string) => (
        <span className="truncate whitespace-nowrap">{settlementExceptReason}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Parent EV</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'parentVoucherEv'
          }
          isActive={sortingObj.column === 'parentVoucherEv'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('parentVoucherEv'),
      dataIndex: 'parentVoucherEv',
      key: 'parentVoucherEv',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (parentVoucherEv: string) => (
        <span className="truncate whitespace-nowrap">{parentVoucherEv}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Original EV</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'originalEv'
          }
          isActive={sortingObj.column === 'originalEv'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('originalEv'),
      dataIndex: 'originalEv',
      key: 'originalEv',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (originalEv: string) => (
        <span className="truncate whitespace-nowrap">{originalEv}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Serial Number</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'serialNo'
          }
          isActive={sortingObj.column === 'serialNo'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('serialNo'),
      dataIndex: 'serialNo',
      key: 'serialNo',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (serialNo: string) => (
        <span className="truncate whitespace-nowrap">{serialNo}</span>
      ),
    },
    {
      title: (
        <TitleWithSort
          title={<span className="uppercase">Activation Date</span>}
          ascending={
            sortingObj.sort === SortOrder.Asc && sortingObj.column === 'activationDate'
          }
          isActive={sortingObj.column === 'activationDate'}
        />
      ),
      className: 'cursor-pointer',
      onHeaderCell: () => onHeaderClick('activationDate'),
      dataIndex: 'activationDate',
      key: 'activationDate',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (activationDate: string) => (
        <span className="truncate whitespace-nowrap">{activationDate}</span>
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
          data={rawSettlement}
          rowKey="logId"
          scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-end">
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

export default RawSettlementList;

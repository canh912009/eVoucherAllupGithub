import ActionButtons from '@/components/common/action-buttons';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import Badge from '@/components/ui/badge/badge';
import Link from '@/components/ui/link';
import { Routes } from '@/config/routes';
import { Delivery, MappedPaginatorInfoEV } from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import DeliveryStatusCodeBadge from './delivery-status-code-badge';

type IProps = {
  deliveries: Delivery[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

const DeliveryList = ({ deliveries, paginatorInfo, onPagination }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();

  const columns = [
    {
      title: <span className="uppercase">id</span>,
      dataIndex: 'id',
      key: 'id',
      width: 50,
      align: 'left',
      ellipsis: true,
      render: (id: string) => (
          <span className="truncate whitespace-nowrap">{id}</span>
      ),
    },
    {
      title: <span className="uppercase">Delivery name</span>,
      dataIndex: 'publishName',
      key: 'publishName',
      width: 150,
      align: 'left',
      ellipsis: true,
      render: (publishName: string, object: any) => (
        <Link href={Routes?.delivery?.details(object?.id)} className="e_not_link">
          <span className="whitespace-nowrap">{publishName}</span>
        </Link>
      ),
    },
    // {
    //   title: 'Campaign Id',
    //   dataIndex: 'campaignId',
    //   key: 'campaignId',
    //   width: 120,
    //   align: 'center',
    //   ellipsis: true,
    //   render: (item: string, object: object) => (
    //     <span className="truncate whitespace-nowrap">{item}</span>
    //   ),
    // },
    {
      title: <span className="uppercase">Campaign name</span>,
      dataIndex: 'campaignName',
      key: 'campaignName',
      width: 140,
      align: 'left',
      ellipsis: true,
      render: (campaignName: string, object: object) => (
        <span className="truncate whitespace-nowrap">{campaignName}</span>
      ),
    },
    {
      title: <span className="uppercase">Product name</span>,
      dataIndex: 'goodsName',
      key: 'goodsName',
      width: 140,
      align: 'left',
      ellipsis: true,
      render: (goodsName: string, object: object) => (
        <span className="truncate whitespace-nowrap">{goodsName}</span>
      ),
    },
    {
      title: <span className="uppercase">Delivery type</span>,
      dataIndex: 'bookingYn',
      key: 'bookingYn',
      width: 140,
      align: 'left',
      ellipsis: true,
      render: (bookingYn: string, object: object) => (
        <span className="truncate whitespace-nowrap">
          {bookingYn === 'Y' ? 'Reservation' : 'Immediately'}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Delivery status</span>,
      dataIndex: 'statusCode',
      key: 'statusCode',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (statusCode: string, record: any) => (
        <div
          className={`items-center justify-start space-x-3 rtl:space-x-reverse`}
        >
          {<DeliveryStatusCodeBadge statusCode={statusCode} />}
        </div>
      ),
    },
    {
      title: <span className="uppercase">Target Type</span>,
      dataIndex: 'smsType',
      key: 'smsType',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (smsType: string, object: object) => (
          <span className="truncate whitespace-nowrap">{smsType}</span>
      ),
    },
    {
      title: <span className="uppercase">Approval date</span>,
      dataIndex: 'approveDate',
      key: 'approveDate',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (item: string, object: object) => (
        <span
          style={{ cursor: 'pointer' }}
          className="truncate whitespace-nowrap"
        >
          {item}
        </span>
      ),
    },
    {
      title: <span className="uppercase">Modification date</span>,
      dataIndex: 'updtDt',
      key: 'updtDt',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (item: string, object: object) => (
        <span
          style={{ cursor: 'pointer' }}
          className="truncate whitespace-nowrap"
        >
          {item}
        </span>
      ),
    },
    // {
    //   title: t('table:table-item-actions'),
    //   key: 'actions',
    //   align: 'center',
    //   width: 200,
    //   render: (data: Delivery) => {
    //     return (
    //       <ActionButtons
    //         id={data?.id}
    //         approveModalView={
    //           data?.statusCode === 'WAIT_APPRV' && 'APPROVE_DELIVERY'
    //         }
    //         deleteModalView="DELETE_DELIVERY"
    //         // detailsUrl={Routes?.delivery?.details(data?.id)}
    //         customLocale={router?.locale}
    //       />
    //     );
    //   },
    // },
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
          data={deliveries}
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
            onChange={onPagination}
          />
        </div>
      )}
    </>
  );
};

export default DeliveryList;

import ActionButtons from '@/components/common/action-buttons';
import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import LinkButton from '@/components/ui/link-button';
import Link from '@/components/ui/link';
import { Routes } from '@/config/routes';
import { MappedPaginatorInfoEV, Campaign } from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import StatusCodeBadge from '../common/status-code-badge';

type IProps = {
  campaigns: Campaign[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

const CampaignList = ({ campaigns, paginatorInfo, onPagination }: IProps) => {
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
    // {
    //   title: t('table:table-item-id'),
    //   dataIndex: 'id',
    //   key: 'id',
    //   align: 'center',
    //   width: 60,
    // },
    {
      title: <span className="uppercase">Campaign name</span>,
      dataIndex: 'campaignName',
      key: 'campaignName',
      width: 160,
      align: 'left',
      ellipsis: true,
      render: (campaignName: string, object: any) => (
        <Link
          href={Routes?.campaigns?.details(object?.id)}
          className="e_not_link"
        >
          <span className="truncate whitespace-nowrap">{campaignName}</span>
        </Link>
      ),
    },
    // {
    //   title: 'Customer Id',
    //   dataIndex: 'customerId',
    //   key: 'customerId',
    //   width: 120,
    //   align: 'center',
    //   ellipsis: true,
    //   render: (customerId: string) => (
    //     <span className="truncate whitespace-nowrap">{customerId}</span>
    //   ),
    // },
    {
      title: <span className="uppercase">Customer name</span>,
      dataIndex: 'customerName',
      key: 'customerName',
      width: 150,
      align: 'left',
      ellipsis: true,
      render: (customerName: string) => (
        <span className="truncate whitespace-nowrap">{customerName}</span>
      ),
    },
    {
      title: <span className="uppercase">Customer type</span>,
      dataIndex: 'customerType',
      key: 'customerType',
      width: 100,
      align: 'left',
      ellipsis: true,
      render: (customerType: string) => (
        <span className="truncate whitespace-nowrap font-bold">{customerType}</span>
      ),
    },
    {
      title: <span className="uppercase">Start date</span>,
      dataIndex: 'startDate',
      key: 'startDate',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (startDate: string) => (
        <span className="truncate whitespace-nowrap">{startDate}</span>
      ),
    },
    {
      title: <span className="uppercase">End date</span>,
      dataIndex: 'endDate',
      key: 'endDate',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (endDate: string) => (
        <span className="truncate whitespace-nowrap">{endDate}</span>
      ),
    },
    {
      title: <span className="uppercase">Modified date</span>,
      dataIndex: 'updtDt',
      key: 'updtDt',
      width: 120,
      align: 'center',
      ellipsis: true,
      render: (updtDt: string) => (
        <span className="truncate whitespace-nowrap">{updtDt}</span>
      ),
    },
    {
      title: <span className="uppercase">Campaign {t('table:table-item-status')}</span>,
      dataIndex: 'statusCode',
      key: 'statusCode',
      align: 'center',
      width: 140,
      render: (statusCode: string, record: any) => (
        <div
          className={`items-center justify-start space-x-3 rtl:space-x-reverse`}
        >
          {<StatusCodeBadge statusCode={statusCode} />}
        </div>
      ),
    },
    // {
    //   title: t('Owned delivery'),
    //   key: 'actions',
    //   align: 'center',
    //   width: 150,
    //   render: (data: Campaign) => {
    //     return (
    //       <>
    //         {' '}
    //         {data.statusCode == 'APPROVED' || data.statusCode == 'PROCESSING' ? (
    //           <LinkButton
    //             variant="outline"
    //             href={`create-delivery/${data.id}`}
    //             className="h-12 w-full bg-red-700 hover:bg-red-800 md:w-auto md:ms-6"
    //           >
    //             <span className="xl:block">Create</span>
    //           </LinkButton>
    //         ) : (
    //           ''
    //         )}
    //       </>
    //     );
    //   },
    // },
    // {
    //   title: t('table:table-item-actions'),
    //   key: 'actions',
    //   align: 'center',
    //   width: 200,
    //   render: (data: Campaign) => {
    //     return (
    //       <ActionButtons
    //         id={data?.id.toString()}
    //         editUrl={`${Routes.campaigns.editWithoutLang(data?.id)}`}
    //         detailsUrl={Routes?.campaigns?.details(data?.id.toString())}
    //         deleteModalView={data?.statusCode === null && 'DELETE_CAMPAIGN'}
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
          data={campaigns}
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

export default CampaignList;

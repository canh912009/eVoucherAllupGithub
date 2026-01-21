import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import {MappedPaginatorInfoEV, DeliveryUser, PublishDetailStatusCode, DeliveryReceiveType} from '@/types';
import { useTranslation } from 'next-i18next';
import { PAGE_SIZE } from '@/utils/constants';
import { CloseFillIcon } from '../icons/close-fill';
import {useModalAction} from "@/components/ui/modal/modal.context";
import {FAILURE_DELIVERY_EMAIL_TYPE, forbiddenCodesResend} from "@/components/cs/cs-detail";

type IProps = {
  data: DeliveryUser[] | undefined;
  isSmsStatus?: boolean;
  targetEmail?: boolean;
  isPagination?: boolean;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
  removeOne?: (item: DeliveryUser) => void;
  removeAll?: () => void;
};

const DeliveryUsersList = ({
  data,
  isSmsStatus = false,
  targetEmail = false,
  isPagination = false,
  paginatorInfo,
  onPagination,
  removeOne,
  removeAll,
}: IProps) => {
  const { t } = useTranslation();

  const {openModal} = useModalAction();
  function handleStatus(modalView: any, initialValues: any) {
    openModal(modalView, initialValues);
  }

  const columnUsers = [
    {
      title: <span className="uppercase">User name</span>,
      dataIndex: 'userNm',
      key: 'userNm',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (userNm: string) => (
        <span className="truncate whitespace-nowrap">{userNm}</span>
      ),
    },
    {
      title: <span className="uppercase">Phone number</span>,
      dataIndex: 'userMobileNum',
      key: 'userMobileNum',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (userMobileNum: string) => (
        <span className="truncate whitespace-nowrap">{userMobileNum}</span>
      ),
    },
    {
      title: <span className="uppercase">Email</span>,
      dataIndex: 'email',
      key: 'email',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (email: string) => (
        <span className="truncate whitespace-nowrap">{email}</span>
      ),
    },
    {
      title: <span className="uppercase">Gender</span>,
      dataIndex: 'gender',
      key: 'gender',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (gender: string) => (
        <span className="truncate whitespace-nowrap">{gender}</span>
      ),
    },
    {
      title: <span className="uppercase">Birthday</span>,
      dataIndex: 'birthday',
      key: 'birthday',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (birthday: string) => (
        <span className="truncate whitespace-nowrap">{birthday}</span>
      ),
    },
    {
      title: <span className="uppercase">Address</span>,
      dataIndex: 'address',
      key: 'address',
      width: 280,
      align: 'center',
      ellipsis: true,
      render: (address: string) => (
        <span className="truncate whitespace-nowrap">{address}</span>
      ),
    },
    isSmsStatus && {
      title: <span className="uppercase">Sms Status</span>,
      dataIndex: 'smsStatus',
      key: 'smsStatus',
      width: 160,
      align: 'center',
      ellipsis: true,
      render: (smsStatus: string) => (
        <span className="truncate whitespace-nowrap">{smsStatus}</span>
      ),
    },
    (isSmsStatus || targetEmail ) && {
      title: <span className="uppercase">External PIN No</span>,
      dataIndex: 'externalPinNo',
      key: 'externalPinNo',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (externalPinNo: string) => (
        <span className="truncate whitespace-nowrap">{externalPinNo}</span>
      ),
    },
    targetEmail && {
      title: <span className="uppercase">Email Status</span>,
      dataIndex: 'smsStatus',
      key: 'smsStatus',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (smsStatus: string, record: any) => {
        console.log(record)
        if (FAILURE_DELIVERY_EMAIL_TYPE.includes(smsStatus as PublishDetailStatusCode) ) {
          return (
            <div className="flex items-center justify-center space-x-2">
              <span className="text-red-600 font-bold">{smsStatus}</span>
              <button
                className="w-5 h-5 text-white font-bold rounded-full bg-red-600"
                onClick={() => handleStatus('RESEND_CS', {ev: record?.ev, emailType: true}) }
              >
                ?
              </button>
            </div>
          );
        } else {
          switch (smsStatus) {
            case 'RESULT_SUCCESS':
              return <span className="truncate whitespace-nowrap font-bold text-green-500">{smsStatus}</span>;
            case 'RESULT_PENDING':
              return <span className="truncate whitespace-nowrap font-bold text-black">{smsStatus}</span>;
            default:
              return <span className="truncate whitespace-nowrap font-bold text-gray-500">{smsStatus}</span>;
          }
        }
      }
    },
    (removeOne || removeAll)
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
          render: (data: DeliveryUser) => {
            return (
              <>
                {removeOne ? (
                  <button
                    onClick={(event) => {
                      event.preventDefault();
                      return removeOne(data)
                    }}
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

  return (
    <>
      <div className="overflow-hidden rounded shadow">
        <Table
          //@ts-ignore
          columns={columnUsers}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">{t('No users')}</div>
            </div>
          )}
          data={data}
          rowKey="id"
          scroll={isPagination ? { x: 1000 } : { x: 1000, y: 450 }}
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

export default DeliveryUsersList;

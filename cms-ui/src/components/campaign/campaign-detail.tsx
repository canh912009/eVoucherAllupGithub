import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import { useModalAction } from '@/components/ui/modal/modal.context';
import { Table } from '@/components/ui/table';
import TextArea from '@/components/ui/text-area';
import { Routes } from '@/config/routes';
import { getUrlPublicAsset } from '@/data/download';
import { Campaign, CommonStatusCode, CustomerTypeCode, Good } from '@/types';
import { isPermitted } from '@/utils/auth-utils';
import { PERMISSIONS_EV as p } from '@/utils/constants';
import { emptyPlaceholder } from '@/utils/placeholders';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import StatusCodeBadge from '../common/status-code-badge';
import { WarningCircleIcon } from '../icons/warning-circle-i';
import LinkButton from '../ui/link-button';
import CampaignGoodsList from './campaign-product-list';
import SwitchInput from "@/components/ui/switch-input";
import SwitchDisplay from "@/components/ui/switch-display";

type IProps = {
  initialValues?: Campaign | null;
};

export default function CampaignDetail({ initialValues }: IProps) {
  const rootClassName =
    'bg-gray-100 ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const router = useRouter();
  const { t } = useTranslation();
  const { openModal } = useModalAction();

  const deliveryColumns = [
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 50,
      render: (text: any, record: any, index: number) => {
        return index + 1;
      },
    },
    {
      title: <span className="uppercase">Delivery name</span>,
      dataIndex: 'publishName',
      key: 'publishName',
      width: 200,
      align: 'left',
      ellipsis: true,
      render: (publishName: string) => (
        <span className="truncate whitespace-nowrap">{publishName}</span>
      ),
    },
    // {
    //   title: <span className="uppercase">Product id</span>,
    //   dataIndex: 'goodsId',
    //   key: 'goodsId',
    //   width: 150,
    //   align: 'center',
    //   ellipsis: true,
    //   render: (goodsId: string) => (
    //     <span className="truncate whitespace-nowrap">{goodsId}</span>
    //   ),
    // },
    {
      title: <span className="uppercase">Product name</span>,
      dataIndex: 'goods',
      key: 'goods',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (goods: Good) => (
        <span className="truncate whitespace-nowrap">{goods?.goodsName}</span>
      ),
    },
    {
      title: <span className="uppercase">Delivery type</span>,
      dataIndex: 'bookingYn',
      key: 'bookingYn',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (bookingYn: string) => (
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
      render: (statusCode: string) => (
        <span className="truncate whitespace-nowrap">{statusCode}</span>
      ),
    },
    {
      title: <span className="uppercase">Approval date</span>,
      dataIndex: 'approveDate',
      key: 'approveDate',
      width: 200,
      align: 'center',
      ellipsis: true,
      render: (approveDate: string) => (
        <span className="truncate whitespace-nowrap">{approveDate}</span>
      ),
    },
    {
      title: <span className="uppercase">Modification date</span>,
      dataIndex: 'updtDt',
      key: 'updtDt',
      width: 250,
      align: 'center',
      ellipsis: true,
      render: (updtDt: string) => (
        <span className="truncate whitespace-nowrap">{updtDt}</span>
      ),
    },
  ];

  function handleStatus(modalView: any, id: string) {
    openModal(modalView, id);
  }

  return (
    <div>
      {initialValues?.statusCode === CommonStatusCode.REJECTED && (
        <div className="col-span-12 my-5 flex flex-wrap rounded-lg bg-[#144AB8] px-5 py-4 text-sm text-light">
          <div className="flex w-full">
            <span className="px-2">
              <WarningCircleIcon />
            </span>{' '}
            <span className="text-md px-4 font-semibold">Reject Reason</span>
            <span className="px-6">{initialValues?.rejectReason}</span>
          </div>
        </div>
      )}
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('View Campaign Information')}
              </h1>
            </div>
          </div>

          {initialValues && (
            <div className="flex flex-wrap">
              <div className="mb-5 px-4 sm:w-full md:w-1/2">
                {t('Status')}:{' '}
                {<StatusCodeBadge statusCode={initialValues?.statusCode} />}
              </div>
            </div>
          )}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Customer Name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                id="customer"
                value={initialValues?.customer?.customerName}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract Name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                id="contract"
                value={initialValues?.customerContract?.contractName}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Campaign name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                id="campaignName"
                value={initialValues?.campaignName}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract ID')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                id="customerContractId"
                value={initialValues?.customerContract?.id}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Start date')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                id="startDate"
                value={initialValues?.startDate}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('End date')} <span className="text-red-500">*</span>
              </label>
              <input
                  className={rootClassName}
                  type="text"
                  disabled={true}
                  id="endDate"
                  value={initialValues?.endDate}
                  autoComplete="off"
              />
            </div>
          </div>

          {/*PRODUCT INFORMATION*/}
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Product Information')}
              </h1>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <CampaignGoodsList data={initialValues?.listGoods} />
            </div>
          </div>

          {/*SMS TEMPLATE*/}
          <div className=" flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('SMS TEMPLATE')}
              </h1>
            </div>
          </div>

          <div className="flex w-full flex-wrap px-4">
            <label className="w-full  text-heading md:w-2/12">
              {t('Message Template')} <span className="text-red-500">*</span>
            </label>
            <input
              className={`${rootClassName} `}
              type="text"
              disabled={true}
              id="messageTemplate"
              value={initialValues?.messageTemplate?.name}
              autoComplete="off"
            />
          </div>
          <div className="flex w-full flex-wrap px-4 my-2">
            <label className="w-full  text-heading md:w-2/12">
              {t('Message Template Description')}
            </label>
            <TextArea
              name="messageTemplateDescription"
              value={initialValues?.messageTemplate?.messageString}
              variant="outline"
              disabled={true}
              className="w-full md:w-9/12"
            />
          </div>

          {initialValues?.customer?.customerTypeCode !==
            CustomerTypeCode.CHANNEL && ( <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 ">
                <label className="w-full  text-heading md:w-2/12">
                  {t('Sender name')} <span className="text-red-500">*</span>
                </label>
                <input
                  className={rootClassName}
                  type="text"
                  disabled={true}
                  id="senderName"
                  value={initialValues?.senderName}
                  autoComplete="off"
                />
              </div>
            </div>
          )}
          {/*End SMS TEMPLATE*/}

          {/*GIFT MESSAGE*/}
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('GIFT MESSAGE')}
              </h1>
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4">
            <label className="w-full  text-heading md:w-2/12">
              {t('Enable popup')}
            </label>
            <div className="flex items-center space-x-2 md:w-9/12 my-2">
              <SwitchDisplay checked={initialValues?.showPopupYn === 'Y'} />
              <span className="text-sm text-gray-500 italic">
                {t('Enable to show pop up message when user views the voucher for the first time')}
              </span>
            </div>
          </div>

          {initialValues?.customer?.customerTypeCode !==
            CustomerTypeCode.CHANNEL && ( <div className=" flex flex-wrap">
              <div className="flex w-full flex-wrap px-4">
                <label className="w-full  text-heading md:w-2/12">
                  {t('Voucher subject')} <span className="text-red-500">*</span>
                </label>
                <input
                  className={`${rootClassName} md:w-9/12`}
                  type="text"
                  disabled={true}
                  id="messageSubject"
                  value={initialValues?.messageSubject}
                  autoComplete="off"
                />
              </div>
              <div className="flex w-full flex-wrap px-4 my-2">
                <label className="w-full  text-heading md:w-2/12">
                  {t('Voucher content')}
                </label>
                <TextArea
                  disabled={true}
                  id="messageContent"
                  placeholder={t('Voucher content')}
                  name="messageContent"
                  value={initialValues?.messageContent}
                  variant="outline"
                  className="w-full md:w-9/12"
                />
              </div>
            </div>
          )}

          <div className="  flex flex-wrap">
            <div className="flex w-full flex-wrap px-4">
              <label className="w-full  text-heading md:w-2/12">
                {t('Content Link')}
              </label>
              <input
                className={`${rootClassName} md:w-9/12`}
                type="text"
                id="contentLink"
                disabled={true}
                value={initialValues?.contentLink}
                // placeholder={t('Content link')}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4">
              <label className="w-full  text-heading md:w-2/12">
                {t('Content Image')}
              </label>
              {initialValues?.contentImagePath && (
                <div className="w-full pt-1 md:w-9/12 ">
                  <img
                    src={
                      getUrlPublicAsset(initialValues?.contentImagePath) ??
                      emptyPlaceholder
                    }
                    alt={'Content Image'}
                    width={300}
                    height={300}
                  />
                </div>
              )}
            </div>
          </div>
          {/*End VOUCHER INFORMATION*/}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2 md:w-4/5">
              <h1 className="text-lg font-semibold uppercase">
                {t('Delivery Information')}
              </h1>
            </div>
            {/*{isPermitted([p.ROLE_ADMIN, p.ROLE_OPERATOR]) &&*/}
            {/*  initialValues?.customer?.customerTypeCode !==*/}
            {/*    CustomerTypeCode.CHANNEL &&*/}
            {/*  (initialValues?.statusCode === CommonStatusCode.APPROVED ||*/}
            {/*    initialValues?.statusCode === CommonStatusCode.PROCESSING) && (*/}
            {/*    <div className="flex w-full flex-row-reverse flex-wrap px-4 py-2 md:w-1/5">*/}
            {/*      <LinkButton*/}
            {/*        size="small"*/}
            {/*        href={`/create-delivery/${initialValues?.id}`}*/}
            {/*        className="h-12 w-full bg-black hover:bg-slate-700 md:w-auto md:ms-6"*/}
            {/*      >*/}
            {/*        <span className="xl:block">Add Delivery</span>*/}
            {/*      </LinkButton>*/}
            {/*    </div>*/}
            {/*  )}*/}
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <div className="mb-6 overflow-hidden rounded shadow">
                <Table
                  //@ts-ignore
                  columns={deliveryColumns}
                  emptyText={() => (
                    <div className="flex flex-col items-center py-6">
                      <div className="pt-6 text-sm font-semibold">
                        {t('table:empty-table-data')}
                      </div>
                    </div>
                  )}
                  data={initialValues?.publishes}
                  rowKey="id"
                  scroll={{ x: 1000, y: 400 }}
                />
              </div>
            </div>
          </div>
        </Card>
      </div>
      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>

        {/* Page detail */}
        {initialValues && (
          <>
            {!initialValues?.statusCode && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.campaigns.editWithoutLang(
                    initialValues?.id
                  )}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() => handleStatus('REQ_CAMPAIGN', initialValues.id)}
                >
                  {t('Register')}
                </Button>
              </>
            )}
            {initialValues.statusCode === CommonStatusCode.WAIT_APPRV && (
              <>
                {isPermitted([p.ROLE_ADMIN]) && (
                  <>
                    <Button
                      variant="outline"
                      className="me-4 hover:bg-red-800"
                      onClick={() =>
                        handleStatus('REJECT_CAMPAIGN', initialValues.id)
                      }
                    >
                      {t('Reject')}
                    </Button>
                    <Button
                      className="bg-red-700 me-4 hover:bg-red-800"
                      onClick={() =>
                        handleStatus('APPROVE_CAMPAIGN', initialValues.id)
                      }
                    >
                      {t('Approve')}
                    </Button>
                  </>
                )}
                {isPermitted([p.ROLE_OPERATOR]) && (
                  <Button
                    variant="outline"
                    className="bg-red-700 me-4 hover:bg-red-800"
                    onClick={() =>
                      handleStatus('CANCEL_REQUEST_CAMPAIGN', initialValues.id)
                    }
                  >
                    {t('Cancel Registration')}
                  </Button>
                )}
              </>
            )}
            {initialValues.statusCode === CommonStatusCode.APPROVED && (
              <>
                {isPermitted([p.ROLE_ADMIN]) && (
                  <Button
                    className="bg-red-700 me-4 hover:bg-red-800"
                    onClick={() =>
                      handleStatus('CANCEL_APPROVE_CAMPAIGN', initialValues.id)
                    }
                  >
                    {t('Cancel Approval')}
                  </Button>
                )}
              </>
            )}
            {initialValues.statusCode === CommonStatusCode.REJECTED && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.campaigns.editWithoutLang(
                    initialValues?.id
                  )}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('RE_REQ_CAMPAIGN', initialValues.id)
                  }
                >
                  {t('Re-register')}
                </Button>
              </>
            )}
            {initialValues.statusCode === CommonStatusCode.CANCEL_APPRV && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.campaigns.editWithoutLang(
                    initialValues?.id
                  )}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('RE_REQ_CAMPAIGN', initialValues.id)
                  }
                >
                  {t('Re-register')}
                </Button>
              </>
            )}
            {initialValues.statusCode === CommonStatusCode.PROCESSING && (
              <Button
                className="hidden bg-red-700 me-4 hover:bg-red-800"
                onClick={() =>
                  handleStatus('CANCEL_REQUEST_CAMPAIGN', initialValues.id)
                }
              >
                {t('Cancel Campaign')}
              </Button>
            )}
            {initialValues.statusCode === CommonStatusCode.END && (
              <>
                {initialValues.customer?.customerTypeCode ===
                  CustomerTypeCode.CHANNEL && (
                  <LinkButton
                    variant="outline"
                    href={`${Routes.campaigns.editWithoutLang(
                      initialValues?.id
                    )}`}
                    className="me-4 hover:bg-red-800"
                  >
                    <span className="xl:block">Edit</span>
                  </LinkButton>
                )}
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
}

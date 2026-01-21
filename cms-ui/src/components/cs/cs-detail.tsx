import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import {useModalAction} from '@/components/ui/modal/modal.context';
import {
  Cs,
  DeliveryReceiveType,
  PublishDetailStatusCode,
  VoucherStatusCode,
} from '@/types';
import {useTranslation} from 'next-i18next';
import {useRouter} from 'next/router';
import CsHistoryExchangeList from './cs-history-exchange-list';
import CsHistoryTransferList from './cs-history-transfer-list';
import {useEffect, useState} from 'react';
import CsHistoryPurchaseList from '@/components/cs/cs-history-purchase-list';
import {Routes} from '@/config/routes';
import LinkButton from '../ui/link-button';
import {SYSTEM_GROUP_ALL, VOUCHER_TYPE_CODES} from "@/components/common/status-code-badge";
import {SYSTEM_BRAND_TYPE} from "@/utils/constants";

type IProps = {
  initialValues?: Cs | null;
};

export const forbiddenCodesResend = [
  PublishDetailStatusCode.STRT_PUB,
  PublishDetailStatusCode.END_PUB,
  PublishDetailStatusCode.STRT_GEN_MSG,
  PublishDetailStatusCode.END_GEN_MSG,
  PublishDetailStatusCode.STRT_SND_MSG,
  PublishDetailStatusCode.RESULT_PENDING
];

export const FAILURE_DELIVERY_EMAIL_TYPE = [
  PublishDetailStatusCode.FAIL_PUB,
  PublishDetailStatusCode.FAIL_GEN_MSG,
  PublishDetailStatusCode.FAIL_SND_MSG,
  PublishDetailStatusCode.RESULT_FAIL
];

function showResend(initialValues: any) {
  return !(
    forbiddenCodesResend.includes(initialValues?.publishDetailStatusCode) ||
    (initialValues?.messageType === DeliveryReceiveType.DOWNLOAD && !initialValues?.targetNumber) ||
    (initialValues?.messageType === DeliveryReceiveType.PAPER && !initialValues?.targetNumber)
  );
}

export default function CsDetail({initialValues}: Readonly<IProps>) {
  const rootClassName =
    'bg-gray-100 ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const router = useRouter();
  const {t} = useTranslation();
  const {openModal} = useModalAction();
  const [showPassword, setShowPassword] = useState(false);

  function handleStatus(modalView: any, initialValues: any) {
    openModal(modalView, initialValues);
  }

  // Extend Button
  const [extendTime, setExtendTime] = useState<number>(0);
  const [extendEnable, setExtendEnable] = useState<boolean>(true);
  const [extendDescription, setExtendDescription] = useState<string>("");

  const validExtendSystem = [
    SYSTEM_BRAND_TYPE.BULK,
    SYSTEM_BRAND_TYPE.CHOICE,
    SYSTEM_BRAND_TYPE.INTERNAL,
    SYSTEM_BRAND_TYPE.VNPT_EPAY,
    SYSTEM_BRAND_TYPE.XPAY
  ];
  const validExtendPinStatus = [
    VoucherStatusCode.NORMAL,
    VoucherStatusCode.PART_USED
  ];

  useEffect(() => {
    setExtendTime(initialValues?.requestCount ?? 0)

    if (!validExtendSystem.includes(initialValues?.system as string)) {
      setExtendEnable(false)
      setExtendDescription("Voucher of this type cannot be extended.")
    }
    if (!validExtendPinStatus.includes(initialValues?.pinStatus as VoucherStatusCode)) {
      setExtendEnable(false)
      setExtendDescription("Voucher of this status cannot be extended.")
    }
    if ( initialValues?.underProcess ) {
      setExtendEnable(false)
      setExtendDescription("Request is under approval")
    }
    if(!initialValues?.underProcess && initialValues?.requestCount === 1) setExtendDescription("Voucher is extended once") ;
    if(!initialValues?.underProcess && (initialValues?.requestCount ?? 0) > 1) setExtendDescription(`Voucher is extended ${(initialValues?.requestCount ?? 0)}  times`) ;
  }, [initialValues]);
  // End Extend Button

  return (
    <div>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('PIN Information')}
              </h1>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Voucher UUID')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.voucherUUID}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Target Name')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.targetName}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Voucher Type Code')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.voucherTypeCode}
                autoComplete="off"
              />
            </div>
            {initialValues?.voucherTypeCode !== VOUCHER_TYPE_CODES.CH &&
              initialValues?.voucherTypeCode !== VOUCHER_TYPE_CODES.BK && ( //not CHOICE voucher type
                <div className="flex w-full flex-wrap px-4 md:w-1/2">
                  <label className="w-full py-2 text-heading md:w-1/4">
                    {t('Parent Voucher')}
                  </label>
                  <input
                    className={rootClassName}
                    type="text"
                    disabled={true}
                    value={initialValues?.parentVoucherEv}
                    autoComplete="off"
                  />
                </div>
              )}
          </div>
          {initialValues?.voucherTypeCode !== VOUCHER_TYPE_CODES.CH &&
            initialValues?.voucherTypeCode !== VOUCHER_TYPE_CODES.BK && (
              <div className="mb-5 flex flex-wrap">
                <div className="flex w-full flex-wrap px-4 md:w-1/2">
                  <label className="w-full py-2 text-heading md:w-1/4">
                    {t('Parent type')}
                  </label>
                  <input
                    className={rootClassName}
                    type="text"
                    disabled={true}
                    value={initialValues?.parentSystem}
                    autoComplete="off"
                  />
                </div>
              </div>
            )}
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Serial Number')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.serialNo}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Target email')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.targetEmail}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Campaign name')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.campaignName}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Target Number')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.targetNumber}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Campaign ID')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.campaignId}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Access Link')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.accessLink}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Delivery name')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.deliveryName}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('PIN')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.pin}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Delivery ID')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.deliveryId}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Voucher status')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.pinStatus}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Product name')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.productName}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Delivery date')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.deliveryDate}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Available period')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={
                  initialValues?.startDate + ' - ' + initialValues?.endDate
                }
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Exchange date')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.exchangeDate}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Password')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.pinPassword}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('OTP')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.parentVoucherToken}
                autoComplete="off"
              />
            </div>
          </div>

          {initialValues?.messageType === DeliveryReceiveType.PAPER && (
            <>
              <div className="mb-5 flex flex-wrap">
                <div className="flex w-full flex-wrap px-4 md:w-1/2">
                  <label className="w-full py-2 text-heading md:w-1/4">
                    {t('Activation Link')}
                  </label>
                  <input
                    className={rootClassName}
                    type="text"
                    disabled={true}
                    value={initialValues?.activationUrl}
                    autoComplete="off"
                  />
                </div>
              </div>
              <div className="mb-5 flex flex-wrap">
                <div className="flex w-full flex-wrap px-4 md:w-1/2"></div>
                <div className="flex w-full flex-wrap px-4 md:w-1/2">
                  <label className="w-full py-2 text-heading md:w-1/4">
                    {t('Activation Date')}
                  </label>
                  <input
                    className={rootClassName}
                    type="text"
                    disabled={true}
                    value={initialValues?.activationDate}
                    autoComplete="off"
                  />
                </div>
              </div>
            </>
          )}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Delivery Information')}
              </h1>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Delivery date')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.deliveryDate}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Target type')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                value={initialValues?.messageType}
                autoComplete="off"
              />
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Result')}
              </label>
              {(initialValues?.messageType === DeliveryReceiveType.EMAIL && FAILURE_DELIVERY_EMAIL_TYPE.includes(initialValues?.result as PublishDetailStatusCode) )
                ? <div className="flex items-center justify-center space-x-2 bg-gray-300 p-2 rounded-lg">
                  <span className=" font-bold ">{initialValues?.result}</span>
                  <button
                    className="w-5 h-5 text-white font-bold rounded-full bg-red-600 "
                    onClick={() => handleStatus('RESEND_CS', {ev: initialValues?.voucherUUID, emailType: true})}
                  > ?
                  </button>
                </div>
                : <input
                  className={rootClassName}
                  type="text"
                  disabled={true}
                  value={initialValues?.result}
                  autoComplete="off"
                />
              }
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                { (initialValues?.voucherTypeCode === VOUCHER_TYPE_CODES.CH || initialValues?.voucherTypeCode === VOUCHER_TYPE_CODES.BK )
                  ? t('PURCHASE History')
                  : t('Exchange History')}
              </h1>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              { (initialValues?.voucherTypeCode === VOUCHER_TYPE_CODES.CH || initialValues?.voucherTypeCode === VOUCHER_TYPE_CODES.BK ) ? (
                <CsHistoryPurchaseList
                  data={initialValues?.childOfChoiceVoucherList}
                />
              ) : (
                <CsHistoryExchangeList
                  data={initialValues?.exChangeHistories}
                  voucherTypeCode={initialValues?.voucherTypeCode}
                />
              )}
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Transfer History')}
              </h1>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <CsHistoryTransferList
                csData={initialValues}
                data={initialValues?.transferHistories}
              />
            </div>
          </div>
        </Card>
      </div>
      <div className="flex justify-end items-start">
        <LinkButton
          variant="outline"
          className="me-4 hover:bg-red-800"
          href={`${Routes?.cs.list}`}
        >
          {t('form:button-label-back')}
        </LinkButton>
        {initialValues && (
          <>
            {!(
              initialValues.pinStatus == VoucherStatusCode.DISABLED ||
              initialValues.pinStatus == VoucherStatusCode.EXPIRE ||
              initialValues.pinStatus == VoucherStatusCode.USED
            ) && (
              <Button
                className="bg-slate-900 me-4 hover:bg-slate-600"
                onClick={() =>
                  handleStatus('DISABLE_CS', initialValues.voucherUUID)
                }
              >
                {t('Disable')}
              </Button>
            )}
            {showResend(initialValues) && (
              <Button
                className="bg-red-500 me-4 hover:bg-red-800"
                onClick={() => handleStatus('RESEND_CS', initialValues?.voucherUUID) }
              >
                {t('Resend')}
              </Button>
            )}
            <div className="flex flex-col items-end">
              <Button
                className={`${extendEnable ? "bg-red-600 hover:bg-red-700" : ""}`}
                disabled={!extendEnable}
                onClick={() =>
                  handleStatus('REQUEST_EXTEND_CS', initialValues)
                }
              >
                <span>{t('Extend Expire date')}</span>
                { extendTime > 0 && <span className="bg-gray-800 text-white ml-2 py-1 px-2 rounded-full text-xs">{extendTime}</span> }
              </Button>
              <div className="text-gray-500 text-sm mt-1">
                {extendDescription}
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

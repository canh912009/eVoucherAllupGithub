import Card from '@/components/common/card';
import { DownloadIcon } from '@/components/icons/download-icon1';
import { WarningCircleIcon } from '@/components/icons/warning-circle-i';
import Button from '@/components/ui/button';
import Checkbox from '@/components/ui/checkbox/checkbox';
import { useModalAction } from '@/components/ui/modal/modal.context';
import Radio from '@/components/ui/radio/radio';
import { Routes } from '@/config/routes';
import { uploadClient } from '@/data/client/upload';
import { getUrlPublicAsset } from '@/data/download';
import {
  downloadQRCode,
  downloadVouchersQRCodeZip,
} from '@/data/download-QRCode';
import {
  CommonStatusCode,
  Delivery,
  DeliveryReceiveType,
  DeliveryUploadType,
  DeliveryUser,
  DeliveryVoucher,
  MappedPaginatorInfoEV,
} from '@/types';
import { isPermitted } from '@/utils/auth-utils';
import { PAGE_SIZE, PERMISSIONS_EV as p } from '@/utils/constants';
import { updatePaginatorFE } from '@/utils/data-mappers-ev';
import { emptyPlaceholder } from '@/utils/placeholders';
import { parse } from 'json2csv';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { useEffect, useRef, useState } from 'react';
import { toast } from 'react-toastify';
import LinkButton from '../ui/link-button';
import TextArea from '../ui/text-area';
import DeliveryGoodsList from './delivery-product-list';
import DeliveryStatusCodeBadge from './delivery-status-code-badge';
import DeliveryUsersList from './delivery-users-list';
import DeliveryVoucherList from './delivery-voucher-list';
import { formatNumber } from '@/utils/common-utils';
import SwitchInput from "@/components/ui/switch-input";
import {useForm} from "react-hook-form";
import SwitchDisplay from "@/components/ui/switch-display";
import { downloadVouchersBarcodeZip } from '@/data/download-BarCode';

type IProps = {
  initialValues?: Delivery | null;
};

export default function DeliveryDetail({ initialValues }: Readonly<IProps>) {
  // console.log('initialValues', initialValues);

  const router = useRouter();
  const { t } = useTranslation();
  const { openModal } = useModalAction();
  const targetEmail = (initialValues?.smsType === DeliveryReceiveType.EMAIL)

  const rootClassName =
    'bg-gray-100 ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const smallClassName =
    'bg-gray-100 h-12 flex items-center w-full md:w-1/2 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  let dataVouchers: any[] = [];
  let index = 0;
  if (initialValues?.downloadVouchers) {
    for (const voucher of initialValues.downloadVouchers) {
      index++;
      const modifiedVoucher = {
        campaignId: initialValues?.campaign?.id,
        campaignName: initialValues?.campaign?.campaignName,
        publishId: initialValues?.id,
        publishName: initialValues?.publishName,
        productId: initialValues?.goods?.id,
        productName: initialValues?.goods?.goodsName,
        // ...voucher,
        ev: voucher.ev,
        price: voucher.price,
        extPin: voucher.extPin ? `"${voucher.extPin}"` : '',
        expirationDate: voucher.expirationDate,
        shortLink: voucher.shortLink,
        otp: voucher.otp ? `"${voucher.otp}"` : '',
        ...(initialValues?.smsType === DeliveryReceiveType.PAPER
          ? {
              activationUrl: voucher.activationUrl,
              activationDate: voucher.activationDate,
              serialNo: voucher.serialNo ? `"${voucher.serialNo}"` : '',
            }
          : {}),
        ...((initialValues?.smsType === DeliveryReceiveType.PAPER || initialValues?.smsType === DeliveryReceiveType.DOWNLOAD ) && {
          serialNo: voucher.serialNo ? `"${voucher.serialNo}"` : '',
          imageQrCode: `=HYPERLINK("${index}.png", "${index}.png")`,
        }),
      };
      dataVouchers.push(modifiedVoucher);
    }
  }

  const downloadCsv = async () => {
    try {
      const csv = parse(dataVouchers); // convert the JSON to CSV using the json2csv package
      const utf8BOM = '\uFEFF';
      const blob = new Blob([utf8BOM + csv], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);

      const hideLink = document.createElement('a');
      hideLink.href = url;
      hideLink.download = `Delivery_id${initialValues?.id}_List_Vouchers.csv`;
      hideLink.style.display = 'none';
      document.body.appendChild(hideLink);
      hideLink.click();

      // delete Blob object và a tag when download done
      URL.revokeObjectURL(url);
      document.body.removeChild(hideLink);
    } catch (error) {
      console.error('Error while downloading CSV:', error);
    }
  };

  async function handleDownloadFile() {
    try {
      const response = await uploadClient.downloadFiles(
        initialValues?.uploadFilePath || ''
      );
      const blob = new Blob([response.data], {
        type: 'application/octet-stream',
      });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = initialValues?.uploadFilePath as string;
      link.click();
      window.URL.revokeObjectURL(url);
    } catch (error: any) {
      console.log(error);
      if (error.isAxiosError && !error.response) {
        toast.error('Network Error:' + error?.response?.data.message);
      } else {
        toast.error('Error:' + error?.response?.data.message);
      }
    }
  }

  const handleDownloadQrCode = () => {
    const QR_SIZE = 200;

    if (initialValues?.smsType === DeliveryReceiveType.PAPER) {
      if(dataVouchers[0].activationUrl) {
        const filename = `Delivery_id${initialValues?.id}_Activation_Link_QRCode`;
        downloadQRCode(dataVouchers[0].activationUrl, filename, QR_SIZE);
      } else {
        const filename = `Delivery_id${initialValues?.id}_Short_Link_QRCode`;
        downloadVouchersQRCodeZip(
          initialValues?.downloadVouchers,
          filename,
          QR_SIZE
        );
      }
    } else if (initialValues?.smsType === DeliveryReceiveType.DOWNLOAD) {
      const filename = `Delivery_id${initialValues?.id}_Short_Link_QRCode`;
      downloadVouchersQRCodeZip(
        initialValues?.downloadVouchers,
        filename,
        QR_SIZE
      );
    }
  };

  const handleDownloadBarcode = () => {
    if (
      initialValues?.smsType === DeliveryReceiveType.PAPER ||
      initialValues?.smsType === DeliveryReceiveType.DOWNLOAD
    ) {
      const filename = `Delivery_id${initialValues?.id}_ExtPin_Barcode`;
      downloadVouchersBarcodeZip(initialValues?.downloadVouchers, filename);
    }
  };


  function handleStatus(modalView: any, id: string) {
    openModal(modalView, id);
  }

  // Function to generate unique "id" for each record (For bypass Warning: Each record in table should have a unique `key` prop, or set `rowKey` to an unique primary key )
  const addUniqueIds = (data: any) => {
    return data?.map((record: any, index: number) => ({
      ...record,
      id: index + 1,
    }));
  };

  /* Vouchers Pagination */
  const IS_PAGINATION_VOUCHERS = true;
  const [voucherSlices, setVoucherSlices] = useState<DeliveryVoucher[]>([]);
  const [pageVouchers, setPageVouchers] = useState(1);
  const [paginatorInfoVouchers, setPaginatorInfoVouchers] =
    useState<MappedPaginatorInfoEV>();

  /* Users Pagination */
  const IS_PAGINATION = true;
  const [userFilters, setUserFilters] = useState<DeliveryUser[]>([]); // List users after search
  const [userSlices, setUserSlices] = useState<DeliveryUser[]>([]); // Display users (10) in once page
  const [page, setPage] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  const userPhoneRef = useRef<HTMLInputElement | null>(null);
  const userEmailRef = useRef<HTMLInputElement | null>(null);
  const userNameRef = useRef<HTMLInputElement | null>(null);
  const userExternalPinNoRef = useRef<HTMLInputElement | null>(null);

  const handleKeyUpFilterUsers = () => {
    const userPhone = userPhoneRef.current?.value.trim()!;
    const userName = userNameRef.current?.value.trim()!;
    const userExternalPinNo = userExternalPinNoRef.current?.value.trim()!;

    let filterUsers: DeliveryUser[];
    if (userExternalPinNo) {
      filterUsers = initialValues?.endUsers?.filter(
        (user) =>
          user.userMobileNum.toLowerCase().includes(userPhone) &&
          user.userNm.toLowerCase().includes(userName) &&
          user.externalPinNo?.toLowerCase().includes(userExternalPinNo)
      )!;
    } else {
      filterUsers = initialValues?.endUsers?.filter(
        (user) =>
          user.userMobileNum.toLowerCase().includes(userPhone) &&
          user.userNm.toLowerCase().includes(userName)
      )!;
    }
    // console.log('handleKeyUpFilterUsers filter', filterUsers?.length);
    setUserFilters(filterUsers!);
    setPage(1);
  };

  const handleKeyUpFilterEmailUsers = () => {
    const userEmail = userEmailRef.current?.value.trim()!;
    let emailsUsers: DeliveryUser[];
    emailsUsers = initialValues?.endUsers?.filter(
      (user) => user.email.toLowerCase().includes(userEmail)
    )!;
    setUserFilters(emailsUsers!);
    setPage(1);
  };

  function handlePagination(current: number) {
    // console.log('current', current);
    setPage(current);
  }

  function handlePaginationVouchers(current: number) {
    setPageVouchers(current);
  }

  useEffect(() => {
    if (IS_PAGINATION) {
      setUserFilters(initialValues?.endUsers!);
    }
  }, [initialValues?.endUsers]);

  useEffect(() => {
    if (IS_PAGINATION) {
      const data = updatePaginatorFE(
        { page: page, pageSize: PAGE_SIZE },
        userFilters
      );
      setUserSlices(data.sliceData);
      setPaginatorInfo(data.pageInfo!);
    }
  }, [userFilters, page]);

  useEffect(() => {
    if (IS_PAGINATION_VOUCHERS) {
      const data = updatePaginatorFE(
        { page: pageVouchers, pageSize: PAGE_SIZE },
        initialValues?.downloadVouchers!
      );
      setVoucherSlices(data.sliceData);
      setPaginatorInfoVouchers(data.pageInfo!);
    }
  }, [initialValues?.downloadVouchers, pageVouchers]);

  const {
    control,
  } = useForm<any>({});

  function notDownloadAndPaper() {
    return <>
      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-2 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Allow Duplication')}
          </label>
          <div className="flex w-full md:w-3/4">
            <Checkbox
              disabled={true}
              name="receiverNoDuplicateAllowYn"
              checked={
                initialValues?.receiverNoDuplicateAllowYn === 'Y'
              }
              label={t('Allowed')}
              className="flex items-center font-semibold"
            />
          </div>
        </div>
      </div>

      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-2 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Target details')}{' '}
            <span className="text-red-500">*</span>
          </label>
          <div className="flex w-full md:w-3/4">
            <Radio
              disabled={true}
              className="flex w-full md:w-1/2"
              label={t('File')}
              name="uploadType"
              id="uploadType_file"
              value={DeliveryUploadType.FILE}
              checked={
                initialValues?.uploadType === DeliveryUploadType.FILE
              }
            />
            <Radio
              disabled={true}
              className="flex w-full md:w-1/2"
              label={t('Text')}
              name="uploadType"
              id="uploadType_text"
              checked={
                initialValues?.uploadType === DeliveryUploadType.TEXT
              }
              value={DeliveryUploadType.TEXT}
            />
          </div>
        </div>
        {initialValues?.uploadType == DeliveryUploadType.FILE && (
          <div className="flex w-full flex-wrap px-2 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('File Upload')}{' '}
              <span className="text-red-500">*</span>
            </label>
            <div className="w-full pt-1 md:w-3/4">
              {initialValues?.uploadFilePath && (
                <button onClick={handleDownloadFile}>
                  <DownloadIcon className="w-5 shrink-0"/>
                </button>
              )}
            </div>
            <span className="text-md w-full text-start md:w-3/4 md:pl-[25%]">
                        {initialValues?.uploadFileName}
                      </span>
          </div>
        )}
      </div>
      <div className=" mt-6 flex flex-wrap">
        <div className="flex w-full flex-wrap px-2 py-2 md:w-3/4">
          <h1 className="text-lg font-semibold uppercase">
            {t('Target details')}
          </h1>
        </div>
        <div className="flex-reverse flex w-full flex-wrap px-2 md:w-1/4">
          <label className="w-full py-2 text-heading md:w-1/2">
            {t('Number of Target')}
          </label>
          <input
            disabled={true}
            className={smallClassName}
            type="text"
            id="numberOfTarget"
            value={initialValues?.endUsers?.length}
            autoComplete="off"
          />
        </div>
      </div>

      {IS_PAGINATION && (
        <div className="mb-5 mt-6 flex flex-wrap">
          <div className="flex w-full flex-wrap px-2 md:w-1/4">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Phone number')}
            </label>
            <input
              className={`${smallClassName} bg-white`}
              type="text"
              id="user_userMobileNum"
              autoComplete="off"
              ref={userPhoneRef}
              onKeyUp={handleKeyUpFilterUsers}
            />
          </div>
          <div className="flex w-full flex-wrap px-2 md:w-1/4">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Email')}
            </label>
            <input
              className={`${smallClassName} bg-white`}
              type="text"
              id="user_userEmail"
              autoComplete="off"
              ref={userEmailRef}
              onKeyUp={handleKeyUpFilterEmailUsers}
            />
          </div>
          <div className="flex w-full flex-wrap px-2 md:w-1/4">
            <label className="w-full px-4 py-2 text-heading md:w-1/4">
              {t('Username')}
            </label>
            <input
              className={`${smallClassName} bg-white`}
              type="text"
              id="user_userNm"
              autoComplete="off"
              ref={userNameRef}
              onKeyUp={handleKeyUpFilterUsers}
            />
          </div>
          <div className="flex w-full flex-wrap px-2 md:w-1/4">
            <label className="w-full px-4 py-2 text-heading md:w-1/4">
              {t('External PIN No')}
            </label>
            <input
              className={`${smallClassName} bg-white`}
              type="text"
              id="user_externalPinNo"
              autoComplete="off"
              ref={userExternalPinNoRef}
              onKeyUp={handleKeyUpFilterUsers}
            />
          </div>
        </div>
      )}
      <div className="mb-5 ">
        <DeliveryUsersList
          data={
            IS_PAGINATION
              ? addUniqueIds(userSlices)
              : addUniqueIds(initialValues?.endUsers)
          }
          isSmsStatus={!targetEmail}
          targetEmail={targetEmail}
          isPagination={IS_PAGINATION}
          paginatorInfo={paginatorInfo ?? null}
          onPagination={handlePagination}
        />
      </div>
    </>;
  }

  function typeDownAndPaper() {
    return <>
      <div className="flex w-full flex-wrap px-2 md:w-1/2">
        <label className="w-full py-2 text-heading md:w-1/4">
          {t('Number of vouchers')}{' '}
          <span className="text-red-500">*</span>
        </label>
        <input
          className={rootClassName}
          type="number"
          disabled={true}
          id="numberOfVouchers"
          value={initialValues?.numberOfVouchers}
          placeholder={t('Number Of Vouchers')}
          autoComplete="off"
        />
      </div>
      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-2 py-2 md:w-3/5">
          <h1 className="text-lg font-semibold uppercase text-heading">
            {t('Vouchers')}
          </h1>
        </div>
        {initialValues?.downloadVouchers?.length! > 0 && (
          <div className="flex w-full flex-row-reverse flex-wrap px-4 py-2 md:w-2/5">
            {(initialValues?.smsType === DeliveryReceiveType.PAPER ||
              initialValues?.smsType === DeliveryReceiveType.DOWNLOAD) && (
              <>
                <Button
                  size="medium"
                  onClick={handleDownloadQrCode}
                  className="w-full bg-violet-700 me-2 hover:bg-violet-800 md:w-auto"
                >
                          <span className="text-md xl:block">
                            {t('Download QR')}
                          </span>
                </Button>
                <Button
                    size="medium"
                    onClick={handleDownloadBarcode}
                    className="w-full bg-emerald-700 me-2 hover:bg-emerald-800 md:w-auto"
                  >
                    <span className="text-md xl:block">
                      {t('Download Barcode')}
                    </span>
                  </Button>
              </>
            )}
            <Button
              size="medium"
              onClick={downloadCsv}
              className="w-full bg-red-700 me-2 hover:bg-red-800 md:w-auto"
            >
                      <span className="text-md xl:block">
                        {t('Download details')}
                      </span>
            </Button>
          </div>
        )}
      </div>
      <DeliveryVoucherList
        vouchers={
          IS_PAGINATION_VOUCHERS
            ? addUniqueIds(voucherSlices)
            : initialValues?.downloadVouchers
        }
        deliveryType={initialValues?.smsType}
        isPagination={IS_PAGINATION_VOUCHERS}
        paginatorInfo={paginatorInfoVouchers ?? null}
        onPagination={handlePaginationVouchers}
      />
    </>;
  }

  return (
    <div>
      {initialValues?.statusCode === CommonStatusCode.REJECTED && (
        <div className="col-span-12 my-5 flex flex-wrap rounded-lg bg-[#144AB8] px-5 py-4 text-sm text-light">
          <div className="flex w-full">
            <span className="px-2">
              <WarningCircleIcon/>
            </span>{' '}
            <span className="text-md px-4 font-semibold">Reject Reason</span>
            <span className="px-6">{initialValues?.rejectReason}</span>
          </div>
        </div>
      )}
      <div className="my-5 flex flex-wrap sm:my-6">
        <Card className="w-full sm:w-full md:w-full md:p-4">
          <div className=" flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Delivery Information')}
              </h1>
            </div>
          </div>

          {initialValues && (
            <div className="flex flex-wrap">
              <div className="mb-5 px-2 sm:w-full md:w-1/2">
                {t('Status')}:{' '}
                {
                  <DeliveryStatusCodeBadge
                    statusCode={initialValues?.statusCode}
                  />
                }
              </div>
            </div>
          )}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Campaign name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="campaignName"
                value={initialValues?.campaign?.campaignName}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Campaign ID')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="campaignId"
                value={initialValues?.campaign?.id}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Delivery name')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="publishName"
                value={initialValues?.publishName}
                autoComplete="off"
              />
            </div>
            {initialValues && (
              <div className="flex w-full flex-wrap px-2 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Delivery ID')} <span className="text-red-500">*</span>
                </label>
                <input
                  className={rootClassName}
                  type="text"
                  disabled={true}
                  id="id"
                  value={initialValues?.id}
                  autoComplete="off"
                />
              </div>
            )}
          </div>

          <div className="mb-1 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('gift message ')}
              </h1>
            </div>
          </div>
          <div className="flex w-full flex-wrap px-2">
            <label className="w-full  text-heading md:w-[12.5%]">
              {t('Enable popup')}
            </label>
            <div className="flex items-center space-x-2 md:w-9/12 my-2">
              <SwitchDisplay checked={initialValues?.showPopupYn === 'Y'} disabled={true}/>
              <span className="text-sm text-gray-500 italic">
                {t('Enable to show pop up message when user views the voucher for the first time')}
              </span>
            </div>
          </div>

          <div className=" flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-1 ">
              <label className="w-full py-2 text-heading md:w-[12.5%]">
                {t('Sender Name')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="senderName"
                name="senderName"
                value={initialValues?.senderName}
                autoComplete="off"
              />
            </div>
          </div>

          <div className=" flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-1">
              <label className="w-full text-heading md:w-[12.5%]">
                {t('Voucher subject')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="messageSubject"
                name="messageSubject"
                value={initialValues?.messageSubject}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-2  ">
              <label className="w-full py-2 text-heading md:w-[12.5%]">
                {t('Voucher content')} <span className="text-red-500">*</span>
              </label>
              <TextArea
                disabled={true}
                name="messageContent"
                value={initialValues?.messageContent}
                variant="outline"
                className="w-full md:w-3/4"
              />
            </div>
          </div>

          <div className=" flex flex-wrap py-1">
            <div className="flex w-full flex-wrap px-2 py-1 ">
              <label className="w-full py-2 text-heading md:w-[12.5%]">
                {t('Content Link')}
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="contentLink"
                name="contentLink"
                value={initialValues?.contentLink}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-2  ">
              <label className="w-full py-2 text-heading md:w-[12.5%]">
                {t('Content Image')}
              </label>
              {initialValues?.contentImagePath && (
                <div className="w-full pt-1 md:w-3/4">
                  <img
                    src={
                      getUrlPublicAsset(initialValues?.contentImagePath) ??
                      emptyPlaceholder
                    }
                    alt={'Content'}
                    width={300}
                    height={300}
                  />
                </div>
              )}
            </div>
          </div>

          <div className="mb-5 mt-6 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2 md:w-3/4">
              <h1 className="text-lg font-semibold uppercase text-cyan-600 text-heading">
                {t('Target information')}
              </h1>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2  ">
              <label className="w-full py-2 text-heading md:w-[12.5%]">
                {t('Target type')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4 space-x-5">
                <Radio
                  disabled={true}
                  className="flex w-full "
                  label={t('ZALO')}
                  checked={initialValues?.smsType === DeliveryReceiveType.ZALO}
                  id="smsType_zalo"
                  name="smsType"
                  value={DeliveryReceiveType.ZALO}
                />
                <Radio
                  disabled={true}
                  className="flex w-full  "
                  label={t('SMS')}
                  checked={initialValues?.smsType === DeliveryReceiveType.SMS}
                  id="smsType_sms"
                  name="smsType"
                  value={DeliveryReceiveType.SMS}
                />
                <Radio
                  disabled={true}
                  className="flex w-full  "
                  label={t('Download')}
                  checked={initialValues?.smsType === DeliveryReceiveType.DOWNLOAD}
                  id="smsType_download"
                  name="smsType"
                  value={DeliveryReceiveType.DOWNLOAD}
                />
                <Radio
                  disabled={true}
                  className="flex w-full  "
                  label={t('Paper')}
                  checked={initialValues?.smsType === DeliveryReceiveType.PAPER}
                  id="smsType_paper"
                  name="smsType"
                  value={DeliveryReceiveType.PAPER}
                />
                <Radio
                  disabled={true}
                  className="flex w-full "
                  label={t('Email')}
                  checked={initialValues?.smsType === DeliveryReceiveType.EMAIL}
                  id="smsType_email"
                  name="smsType"
                  value={DeliveryReceiveType.EMAIL}
                />
              </div>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Delivery Type')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                {initialValues?.smsType !== DeliveryReceiveType.DOWNLOAD &&
                  initialValues?.smsType !== DeliveryReceiveType.PAPER && (
                    <Radio
                      disabled={true}
                      className="flex w-full md:w-1/2"
                      label={t('Reservation')}
                      name="bookingYn"
                      id="bookingYn_reservation"
                      value="Y"
                      checked={initialValues?.bookingYn === 'Y'}
                    />
                  )}
                <Radio
                  disabled={true}
                  className="flex w-full md:w-1/2"
                  label={t('Immediately')}
                  name="bookingYn"
                  id="bookingYn_immediately"
                  value="N"
                  checked={initialValues?.bookingYn === 'N'}
                />
              </div>
            </div>
            {initialValues?.bookingYn === 'Y' && (
              <div className="flex w-full flex-wrap px-2 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Delivery Schedule')}{' '}
                  <span className="text-red-500">*</span>
                </label>
                <input
                  disabled={true}
                  className={rootClassName}
                  type="datetime-local"
                  id="bookingDate"
                  name="bookingDate"
                  value={initialValues?.bookingDate}
                />
              </div>
            )}
          </div>

          {initialValues?.smsType !== DeliveryReceiveType.DOWNLOAD &&
            initialValues?.smsType !== DeliveryReceiveType.PAPER && notDownloadAndPaper()}

          {(initialValues?.smsType === DeliveryReceiveType.DOWNLOAD ||
            initialValues?.smsType === DeliveryReceiveType.PAPER) && typeDownAndPaper()}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Contract Information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Sales Price')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="string"
                id="sellPrice"
                value={formatNumber(initialValues?.sellPrice ?? 0)}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('List Price')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="string"
                id="sellListPrice"
                value={formatNumber(initialValues?.sellListPrice ?? 0 )}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Settlement Method')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="string"
                id="sellSettlementMethodCode"
                value={initialValues?.sellSettlementMethodCode}
                autoComplete="off"
              />
            </div>

            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Discount Amount')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="string"
                id="sellDiscountAmount"
                value={formatNumber(initialValues?.sellDiscountAmount ?? 0)}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Commission Rate')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="string"
                id="sellCommissionRate"
                value={initialValues?.sellCommissionRate}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Including VAT')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  disabled={true}
                  className="flex w-full md:w-1/2"
                  label={t('Yes')}
                  name="sellVatIncludeYn"
                  checked={initialValues?.sellVatIncludeYn === 'Y'}
                  id="sellVatIncludeYn_yes"
                  value="Y"
                />
                <Radio
                  disabled={true}
                  className="flex w-full md:w-1/2"
                  label={t('No')}
                  name="sellVatIncludeYn"
                  checked={initialValues?.sellVatIncludeYn === 'N'}
                  id="sellVatIncludeYn_no"
                  value="N"
                />
              </div>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Product Information')}
              </h1>
            </div>
          </div>

          <DeliveryGoodsList
            goods={initialValues?.campaign?.listGoods}
            goodId={initialValues?.goods?.id.toString()}
          />
        </Card>
      </div>
      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4"
          type="button"
        >
          {t('Back')}
        </Button>

        {isPermitted([p.ROLE_ADMIN]) && (
          <>
            {initialValues?.statusCode === CommonStatusCode.WAIT_APPRV && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.delivery.editWithoutLang(initialValues?.id)}`}
                  className="bg-red-700 me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  variant="outline"
                  className="me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('REJECT_DELIVERY', initialValues.id)
                  }
                >
                  {t('Reject')}
                </Button>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('APPROVE_DELIVERY', initialValues.id)
                  }
                >
                  {t('Approve')}
                </Button>
              </>
            )}
            {initialValues?.statusCode === CommonStatusCode.CANCEL && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.delivery.editWithoutLang(initialValues?.id)}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('REQUEST_DELIVERY', initialValues.id)
                  }
                >
                  {t('Re-register')}
                </Button>
              </>
            )}
            {initialValues?.statusCode === CommonStatusCode.APPROVED && (
              <Button
                className="bg-red-700 me-4 hover:bg-red-800"
                onClick={() =>
                  handleStatus('CANCEL_APPROVE_DELIVERY', initialValues.id)
                }
              >
                {t('Cancel Approval')}
              </Button>
            )}
            {initialValues?.statusCode === CommonStatusCode.CANCEL_APPRV && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.delivery.editWithoutLang(initialValues?.id)}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('REQUEST_DELIVERY', initialValues.id)
                  }
                >
                  {t('Re-register')}
                </Button>
              </>
            )}
            {initialValues?.statusCode === CommonStatusCode.REJECTED && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.delivery.editWithoutLang(initialValues?.id)}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('REQUEST_DELIVERY', initialValues.id)
                  }
                >
                  {t('Re-register')}
                </Button>
              </>
            )}
            {initialValues?.statusCode === CommonStatusCode.PUBLISHING && (
              <>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('CANCEL_APPROVE_DELIVERY', initialValues.id)
                  }
                >
                  {t('Cancel Approval')}
                </Button>
              </>
            )}
          </>
        )}

        {isPermitted([p.ROLE_OPERATOR]) && (
          <>
            {initialValues?.statusCode === CommonStatusCode.WAIT_APPRV && (
              <>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('CANCEL_REQUEST_DELIVERY', initialValues.id)
                  }
                >
                  {t('Cancel request')}
                </Button>
              </>
            )}
            {initialValues?.statusCode === CommonStatusCode.CANCEL && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.delivery.editWithoutLang(initialValues?.id)}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('REQUEST_DELIVERY', initialValues.id)
                  }
                >
                  {t('Re-register')}
                </Button>
              </>
            )}
            {initialValues?.statusCode === CommonStatusCode.CANCEL_APPRV && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.delivery.editWithoutLang(initialValues?.id)}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('REQUEST_DELIVERY', initialValues.id)
                  }
                >
                  {t('Re-register')}
                </Button>
              </>
            )}
            {initialValues?.statusCode === CommonStatusCode.REJECTED && (
              <>
                <LinkButton
                  variant="outline"
                  href={`${Routes.delivery.editWithoutLang(initialValues?.id)}`}
                  className="me-4 hover:bg-red-800"
                >
                  <span className="xl:block">Edit</span>
                </LinkButton>
                <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus('REQUEST_DELIVERY', initialValues.id)
                  }
                >
                  {t('Re-register')}
                </Button>
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
}

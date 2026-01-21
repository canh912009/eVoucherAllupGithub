import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import { DatePicker } from '@/components/ui/date-picker';
import Input from '@/components/ui/input';
import Label from '@/components/ui/label';
import Select from '@/components/ui/select/select';
import { useCodeGroupQuery } from '@/data/code-group';
import {Cs, CsQueryOptions as SearchValue, SortOrder} from '@/types';
import {CODE_GROUP, SYSTEM_BRAND_TYPE} from '@/utils/constants';
import cn from 'classnames';
import {differenceInDays, format, subDays} from 'date-fns';
import { useTranslation } from 'next-i18next';
import { useRef, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { csClient } from '@/data/client/crud-client';
import { API_ENDPOINTS } from '@/data/client/api-endpoints';
import { parse } from 'json2csv';
import DownloadCsvDropdown from "@/components/ui/downloadCsv";
import {VOUCHER_TYPE_CODES} from "@/components/common/status-code-badge";
import {toast} from "react-toastify";

type SearchProps = {
  className?: string;
  shadow?: boolean;
  variant?: 'normal' | 'solid' | 'outline';
  inputClassName?: string;
  onSearch: (data: SearchValue) => void;
};

const Search: React.FC<SearchProps> = ({
  className,
  onSearch,
  variant = 'outline',
  shadow = false,
  inputClassName,
  ...rest
}) => {
  const {
    register,
    handleSubmit,
    reset,
    setValue,
    control,
    getValues,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      startDate: '',
      endDate: '',
      deliveryDate: '',
      pinStatus: '',
      pins: '',
      deliveryId: '',
      deliveryName: '',
      campaignId: '',
      campaignName: '',
      targetNumbers: '',
      targetNames: '',
      voucherUUID: '',
      sort: 'deliveryDate',
      direction: SortOrder.Desc,
      serialNo: '',
    },
  });

  const { t } = useTranslation();

  const { codeGroup, loading: codeLoading } = useCodeGroupQuery(
    CODE_GROUP.VOUCHER_STATUS
  );

  const selectInputRef = useRef<any>(null);
  const handleChangePINStatus = (option: any) => {
    setValue('pinStatus', option?.codeId);
  };

  function clear() {
    selectInputRef.current?.clearValue();
    reset();
    onSearch({
      startDate: '',
      endDate: '',
      deliveryDate: '',
      pinStatus: '',
      pins: '',
      deliveryId: '',
      deliveryName: '',
      campaignId: '',
      campaignName: '',
      targetNumbers: '',
      targetNames: '',
      voucherUUID: '',
      sort: 'deliveryDate',
      direction: SortOrder.Desc,
      serialNo: '',
      productId: '',
      productName: '',
      voucherExpireBefore: '',
    });
  }

  const [isDownloading, setIsDownloading] = useState(false);
  const downloadCSV = async (system?: string, voucherType?: string): Promise<void> => {
    const searchValues = getValues();
    let { startDate, endDate } = searchValues;

    if (!endDate) {
      endDate = format(new Date(), 'yyyy-MM-dd');
      startDate = format(subDays(new Date(), 31), 'yyyy-MM-dd');
    } else {
      if (!startDate) {
        startDate = format(subDays(new Date(endDate), 31), 'yyyy-MM-dd');
      } else {
        const daysDifference = differenceInDays(new Date(endDate), new Date(startDate));
        if (daysDifference > 31) {
          toast.error('The period must not exceed 31 days');
          return;
        }
      }
    }

    setIsDownloading(true);
    try {
      const allData = await csClient._paginated(
        API_ENDPOINTS.CS_SEARCH,
        { page: 1, pageSize: 1000000 }, //1M
        Object.assign({}, {...searchValues, startDate, endDate} )
      );
      let csAll = allData?.data ?? [];
      if (csAll.length <= 0) {
        toast.error('Empty data');
        setIsDownloading(false);
        return;
      }

      const filteredType = (system === SYSTEM_BRAND_TYPE.ALL)
        ? csAll
        : csAll.filter(cs => (cs.system === system) && (voucherType ? cs.voucherType === voucherType : true))
      const filteredList = filteredType.map((cs: Cs) => {
        let baseItem: any = {
          system: cs.system,
          voucherUUID: cs.voucherUUID,
          campaignName: cs.campaignName,
          deliveryName: cs.deliveryName,
          productName: cs.productName,
          productId: cs.productId,
          expireDate: cs.expireDate,
          targetNumber: cs.targetNumber ? `"${cs.targetNumber}"` : '',
          targetEmail: cs.targetEmail,
          targetName: cs.targetName,
          pinStatus: cs.pinStatus,
          accessLink: cs.accessLink,
          deliveryDate: cs.deliveryDate
        };

        switch (cs.system) {
          case SYSTEM_BRAND_TYPE.INTERNAL:
            if(cs.voucherType === VOUCHER_TYPE_CODES.PP) {baseItem.remainingBalance = cs.remainingBalance}
            if(cs.voucherType === VOUCHER_TYPE_CODES.LC) {baseItem.remainingCount = cs.remainingCount}
            break;
          case SYSTEM_BRAND_TYPE.BULK:
            baseItem.otp = cs.otp ? `"${cs.otp}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.CHOICE:
            baseItem.otp = cs.otp ? `"${cs.otp}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.EXTERNAL:
            baseItem.pin = cs.pin ? `"${cs.pin}"` : '';
            baseItem.password = cs.password ? `"${cs.password}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.GIFTPOP:
            baseItem.pin = cs.pin ? `"${cs.pin}"` : '';
            baseItem.transactionId = cs.transactionId ? `"${cs.transactionId}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.UR_BOX:
            baseItem.pin = cs.pin ? `"${cs.pin}"` : '';
            baseItem.transactionId = cs.transactionId ? `"${cs.transactionId}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.WATANE:
            baseItem.pin = cs.pin ? `"${cs.pin}"` : '';
            baseItem.transactionId = cs.transactionId ? `"${cs.transactionId}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.VNPT_EPAY:
            baseItem.requestId = cs.requestId;
            baseItem.provider = cs.provider;
            baseItem.faceValue = cs.faceValue;
            baseItem.cardSerial = cs.cardSerial ? `"${cs.cardSerial}"` : '';
            baseItem.cardPin = cs.cardPin ? `"${cs.cardPin}"` : '';
            baseItem.topupNumber = cs.topupNumber ? `"${cs.topupNumber}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.XPAY:
            baseItem.requestId = cs.requestId;
            baseItem.provider = cs.provider;
            baseItem.faceValue = cs.faceValue;
            baseItem.cardSerial = cs.cardSerial ? `"${cs.cardSerial}"` : '';
            baseItem.cardPin = cs.cardPin ? `"${cs.cardPin}"` : '';
            baseItem.topupNumber = cs.topupNumber ? `"${cs.topupNumber}"` : '';
            break;
          default:
            break;
        }

        return baseItem;
      });

      // console.log(filteredList)
      // return

      if(filteredList?.length <= 0) {
        toast.error('Empty list ');
        return
      }

      const csv = parse(filteredList!); // convert the JSON to CSV using the json2csv package
      const utf8BOM = '\uFEFF';
      const blob = new Blob([utf8BOM + csv], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);

      const hideLink = document.createElement('a');
      hideLink.href = url;
      hideLink.download = voucherType
        ? `csList_System${system}_VoucherType${voucherType}_${startDate}_${endDate}.csv`
        : `csList_System${system}_${startDate}_${endDate}.csv` ;
      hideLink.style.display = 'none';
      document.body.appendChild(hideLink);
      hideLink.click();

      // delete Blob object và a tag when download done
      URL.revokeObjectURL(url);
      document.body.removeChild(hideLink);
    } catch (error) {
      console.error('Error while downloading all CS searched :', error);
    } finally {
      setIsDownloading(false);
    }
    // console.log(getValues())
    // console.log(data?.data ?? [])
  }

  return (
    <form
      noValidate
      role="search"
      className={cn('relative w-full items-center', className)}
      onSubmit={handleSubmit(onSearch)}
    >
      <Card className="mb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:!p-4 md:!pt-6">
        <div className="mb-2 flex w-full flex-wrap">
          <div className="flex w-full flex-wrap p-1 md:w-1/2">
            <div className="flex w-full flex-wrap rounded border border-gray-400">
              <Input
                label={t('Campaign ID')}
                {...register('campaignId')}
                placeholder="Add id number..."
                type="number"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.campaignId?.message!)}
              />
              <Input
                label={t('Campaign Name')}
                {...register('campaignName')}
                placeholder="Add text..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.campaignName?.message!)}
              />
              <Input
                label={t('Delivery ID')}
                {...register('deliveryId')}
                placeholder="Add id number..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.deliveryId?.message!)}
              />
              <Input
                label={t('Delivery Name')}
                {...register('deliveryName')}
                placeholder="Add text..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.deliveryName?.message!)}
              />
            </div>
          </div>
          <div className="flex w-full flex-wrap p-1 md:w-1/2">
            <div className="flex w-full flex-wrap rounded border border-gray-400">
              <Input
                label={t('Target Name')}
                {...register('targetNames')}
                placeholder='Add target name (multi ",")...'
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.targetNames?.message!)}
              />
              <Input
                label={t('Target Number')}
                {...register('targetNumbers')}
                placeholder='Add target number (multi ",")...'
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.targetNumbers?.message!)}
              />
              <Input
                label={t('Product Id')}
                {...register('productId')}
                placeholder="Add text..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.productId?.message!)}
              />
              <Input
                label={t('Product Name')}
                {...register('productName')}
                placeholder="Add text..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.productName?.message!)}
              />
            </div>
          </div>
        </div>
        <div className="mb-2 flex w-full flex-wrap">
          <div className="flex w-full flex-wrap p-1 md:w-1/2">
            <div className="flex w-full flex-wrap rounded border border-gray-400">
              <div className="w-full px-2 py-2 md:w-1/2">
                <Label>{t('Delivery date')}</Label>
                <Controller
                  control={control}
                  name="deliveryDate"
                  render={({ field: { onChange, onBlur, value } }) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      placeholderText="yyyy-mm-dd"
                      onChange={(date) => {
                        if (date) {
                          const selectedDate = new Date(date);
                          const formattedDate = format(
                            selectedDate,
                            'yyyy-MM-dd'
                          );
                          onChange(formattedDate);
                        } else {
                          onChange(null);
                        }
                      }}
                      onBlur={onBlur}
                      //@ts-ignore
                      selected={value ? new Date(value) : null}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                    />
                  )}
                />
              </div>
              <div className="w-full px-2 py-2 md:w-1/2">
                <Label>{t('Voucher expire date before')}</Label>
                <Controller
                  control={control}
                  name="voucherExpireBefore"
                  render={({ field: { onChange, onBlur, value } }) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      placeholderText="yyyy-mm-dd"
                      onChange={(date) => {
                        if (date) {
                          const selectedDate = new Date(date);
                          const formattedDate = format(
                            selectedDate,
                            'yyyy-MM-dd'
                          );
                          onChange(formattedDate);
                        } else {
                          onChange(null);
                        }
                      }}
                      onBlur={onBlur}
                      //@ts-ignore
                      selected={value ? new Date(value) : null}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                    />
                  )}
                />
              </div>
              <div className="w-full px-2 py-2 md:w-1/2">
                <Label>{t('Start from')}</Label>
                <Controller
                  control={control}
                  name="startDate"
                  render={({ field: { onChange, onBlur, value } }) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      placeholderText="yyyy-mm-dd"
                      onChange={(date) => {
                        if (date) {
                          const selectedDate = new Date(date);
                          const formattedDate = format(
                            selectedDate,
                            'yyyy-MM-dd'
                          );
                          onChange(formattedDate);
                        } else {
                          onChange(null);
                        }
                      }}
                      onBlur={onBlur}
                      //@ts-ignore
                      selected={value ? new Date(value) : null}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                    />
                  )}
                />
              </div>
              <div className="w-full px-2 py-2 md:w-1/2">
                <Label>{t('End at')}</Label>
                <Controller
                  control={control}
                  name="endDate"
                  render={({ field: { onChange, onBlur, value } }) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      placeholderText="yyyy-mm-dd"
                      onChange={(date) => {
                        if (date) {
                          const selectedDate = new Date(date);
                          const formattedDate = format(
                            selectedDate,
                            'yyyy-MM-dd'
                          );
                          onChange(formattedDate);
                        } else {
                          onChange(null);
                        }
                      }}
                      onBlur={onBlur}
                      //@ts-ignore
                      selected={value ? new Date(value) : null}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                    />
                  )}
                />
              </div>
            </div>
          </div>
          <div className="flex w-full flex-wrap p-1 md:w-1/2">
            <div className="flex w-full flex-wrap rounded border border-gray-400">
              <Input
                label={t('PIN')}
                {...register('pins')}
                placeholder='Add PIN (multi ",")...'
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.pins?.message!)}
              />
              <div className="w-full px-2 py-2 md:w-1/2">
                <Label>{t('Voucher Status')}</Label>
                <Select
                  ref={selectInputRef}
                  isClearable={true}
                  getOptionLabel={(option: any) => option?.codeName}
                  getOptionValue={(option: any) => option?.codeId}
                  onChange={handleChangePINStatus}
                  options={codeGroup?.codes ?? []}
                  isLoading={codeLoading}
                />
              </div>
              <Input
                label={t('Voucher UUID')}
                {...register('voucherUUID')}
                placeholder="Add Voucher UUID..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.voucherUUID?.message!)}
              />
              <Input
                label={t('Serial Number')}
                {...register('serialNo')}
                placeholder="Add Serial Number "
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.serialNo?.message!)}
              />
            </div>
          </div>
        </div>

        <div className="flex w-full justify-end">
          <div className="pt-2">
            <Button
              className="bg-green-500 hover:bg-green-700"
              aria-label="Search"
            >
              {t('Filter')}
            </Button>
            <Button
              className="ml-2 bg-gray-500 hover:bg-gray-700"
              aria-label="Search"
              onClick={clear}
            >
              {t('Clear filter')}
            </Button>
            <DownloadCsvDropdown onDownload={downloadCSV} isDownloading={isDownloading}/>
          </div>
        </div>
      </Card>
    </form>
  );
};

export default Search;

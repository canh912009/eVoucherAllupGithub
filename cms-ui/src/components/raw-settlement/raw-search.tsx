import Select from '@/components/ui/select/select';
import cn from 'classnames';
import {Controller, useForm, useWatch} from 'react-hook-form';
import { useTranslation } from 'next-i18next';
import Card from '@/components/common/card';
import {CommonApproveStatusAction, Delivery, RawSettlementQueryOptions as SearchValue} from '@/types';
import {
  RAW_SETTLEMENT_COMPANY_TYPE,
  RAW_SETTLEMENT_METHOD,
  VOUCHER_TYPE
} from '@/components/common/status-code-badge';
import Button from "@/components/ui/button";
import {DatePicker} from "@/components/ui/date-picker";
import ValidationError from "@/components/ui/form-validation-error";
import {format, subDays, differenceInDays} from 'date-fns';
import SelectInput from "@/components/ui/select-input-autocomplete";
import React, {useEffect, useState} from "react";
import {useCustomersQuery} from "@/data/customer";
import {PAGE_SIZE, PERMISSIONS_EV, SYSTEM_BRAND_TYPE} from "@/utils/constants";
import useInputTimeout from "@/utils/use-input-timeout";
import {useSupplierQuery, useSuppliersQuery} from "@/data/supplier";
import {useCampaignsQuery} from "@/data/campaign";
import {useDeliveriesQuery} from "@/data/delivery";
import {campaignClient, customerClient, deliveryClient, rawSettlement} from "@/data/client/crud-client";
import {toast} from "react-toastify";
import {parse} from "json2csv";
import DownloadCsvDropdown from "@/components/ui/downloadCsv";
import {any} from "prop-types";
import {getUserInfo} from "@/utils/auth-utils";

const classes = {
  root: ' h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0',
  normal:
    'bg-gray-100 border border-border-base focus:shadow focus:bg-light focus:border-accent',
  solid:
    'bg-gray-100 border border-border-100 focus:bg-light focus:border-accent',
  outline: 'border border-border-base focus:border-accent',
  shadow: 'focus:shadow',
};

type SearchProps = {
  className?: string;
  shadow?: boolean;
  variant?: 'normal' | 'solid' | 'outline';
  inputClassName?: string;
  onSearch: (data: SearchValue) => void;
  searchValuesCsv: any;
};

const Search: React.FC<SearchProps> = ({
  className,
  onSearch,
  variant = 'outline',
  shadow = false,
  inputClassName,
  searchValuesCsv,
  ...rest
}) => {
  const {
    register,
    handleSubmit,
    control,
    watch,
    setValue,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      transactionStartDate: '',
      transactionEndDate: '',
      settlementTarget: '',
      companyName: '',
      voucherTypeCode: '',
      settlementMethodCode: '',
    },
  });

  const { t } = useTranslation();
  const infoUser = getUserInfo()
  const supplierRole = (infoUser?.roleCode === PERMISSIONS_EV.ROLE_SUPPLIER)

  const campaign = watch('campaignName');
  const campaignId = watch('campaignId');
  const publish = watch('publishName');
  const publishId = watch('publishId');

  const rootClassName = cn(
    classes.root,
    {
      [classes.normal]: variant === 'normal',
      [classes.solid]: variant === 'solid',
      [classes.outline]: variant === 'outline',
    },
    {
      [classes.shadow]: shadow,
    },
    inputClassName
  );

  function onChangeCompanyType(newValue: any) {
    return setValue('settlementTarget', newValue.code);
  }
  function onChangeCompanyName(newValue: any) {
    return setValue('companyName', newValue);
  }
  function onChangeVoucherType(newValue: any) {
    return setValue('voucherTypeCode', newValue.code);
  }
  function onChangeMethodType(newValue: any) {
    return setValue('settlementMethodCode', newValue.code);
  }

  const { inputText: companyName, onInputChange: hanleInputChangeCorporation } =
    useInputTimeout();

  const { customers } = useCustomersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      customerName: companyName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );
  const customerOptions = customers.map((customer) => ({
    label: "CUSTOMER",
    value: customer.customerName,
  }));
  const { suppliers } = useSuppliersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      supplierName: companyName,
      approveStatusCode: CommonApproveStatusAction.APPRV
    });
  // console.log(" useSuppliersQuery", suppliers)
  const supplierOptions = suppliers.map((supplier) => ({
    label: "SUPPLIER",
    value: supplier.supplierName,
  }));
  const combinedList = [...customerOptions, ...supplierOptions];
  const optionsCompanyName = combinedList.map((item) => ({
    label: item.label,
    value: item.value,
  }));

  const { inputText: campaignName , onInputChange: hanleInputChangeCampaignName } =
    useInputTimeout();
  const handleChangeCampaignName = (option: any) => {
    setValue('campaignName', option );
    setValue('campaignId', option?.id);
    // @ts-ignore
    setValue('publishName', null as unknown as Delivery);
    setValue('publishId', "");
  };
  const { campaigns } = useCampaignsQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      campaignName: campaignName,
    }
  );

  const { inputText: publishName , onInputChange: hanleInputChangePublishName } =
    useInputTimeout();
  const handleChangePublishName = (option: any) => {
    setValue('publishName', option);
    setValue('publishId', option?.id);

    if (option?.campaignId) {
      setValue('campaignId', option?.campaignId);
    }
  };
  const { deliveries } = useDeliveriesQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      // @ts-ignore
      campaignName: campaign?.campaignName ,
      publishName: publishName,
    }
  );

  /**
   * Set campaignName if choose Publish Name
   */
  useEffect(() => {
    const fetchData = async () => {
      try {
        if (!campaignId) {
          return;
        }

        const response = await campaignClient.get(campaignId);
        setValue('campaignName', response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [campaignId]);

  /**
   * Set Publish Name if choose Publish Id
   */
  useEffect(() => {
    const fetchData = async () => {
      try {
        if (!publishId) {
          return;
        }

        const response = await deliveryClient.get(publishId);
        // console.log("deliveryClient", response.data);
        setValue('publishName', response.data);
        setValue('campaignId', response.data?.campaign?.id);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [publishId]);

  const [loadingCsv, setLoadingCsv] = useState(false);
  const downloadCSV = async (system?: string, voucherType?: string) => {
    // console.log("downloadCSV", system, voucherType)
    // return

    let { transactionStartDate, transactionEndDate } = searchValuesCsv;

    if (!transactionEndDate) {
      transactionEndDate = format(new Date(), 'yyyy-MM-dd');
      transactionStartDate = format(subDays(new Date(), 31), 'yyyy-MM-dd');
    } else {
      if (!transactionStartDate) {
        transactionStartDate = format(subDays(new Date(transactionEndDate), 31), 'yyyy-MM-dd');
      } else {
        const daysDifference = differenceInDays(new Date(transactionEndDate), new Date(transactionStartDate));
        if (daysDifference > 31) {
          toast.error('The period must not exceed 31 days');
          return;
        }
      }
    }

    setLoadingCsv(true);
    try {
      const resData = await rawSettlement.all({...searchValuesCsv, transactionStartDate, transactionEndDate}) ;
      let rawsCSV = resData?.data ?? [];

      if (!rawsCSV || rawsCSV.length <= 0) {
        toast.error('Empty data');
        setLoadingCsv(false);
        return;
      }

      const filteredType = (system === SYSTEM_BRAND_TYPE.ALL)
        ? rawsCSV
        : rawsCSV.filter(raw => (raw.system === system) && (voucherType ? raw.voucherTypeCode === voucherType : true))
      const filteredList = filteredType.map((raw: any) => {
        let baseItem: any = ((raw: any) => {
          const {
            system, logId, ev, settlementLogType, publishId, publishName, publishDetailId, transactionDate, logCreateDate, voucherTypeCode, goodsId, goodsName,
            customerId, customerName,
            supplierId, supplierName,
            companyName,
            brandId, brandName,
            storeId, storeName, managerName,
            userMobileNumber,
            staffMobileNumber,
            settlementCompleteYn, settlementCompleteDate, settlementTarget, settlementMethodCode, listPrice, salesPrice,
            discountRate, discountAmount, discountAppliedAmount, settlementAmount, vatIncludeYn, vatAmount,
            commissionRate, commissionAmount, sendCost, settlementExceptReasonCode, settlementExceptReason, remainBalance, campaignId, campaignName,
            serialNo,
            activationDate, originalEv, parentVoucherEv
          } = raw;

          let result: any = {
            system, logId, ev, settlementLogType, publishDetailId, transactionDate, logCreateDate, voucherTypeCode, goodsId, goodsName,
            customerId: customerId ? `"${customerId}"` : '', customerName,
            supplierId: supplierId ? `"${supplierId}"` : '', supplierName,
            companyName,
            brandId: brandId ? `"${brandId}"` : '', brandName,
            storeId: storeId ? `"${storeId}"` : '', storeName, managerName,
            userMobileNumber: userMobileNumber ? `"${userMobileNumber}"` : '',
            staffMobileNumber: staffMobileNumber ? `"${staffMobileNumber}"` : '',
            settlementCompleteYn, settlementCompleteDate, settlementTarget, settlementMethodCode, listPrice, salesPrice,
            discountRate, discountAmount, discountAppliedAmount, settlementAmount, vatIncludeYn, vatAmount,
            commissionRate, commissionAmount, sendCost, settlementExceptReasonCode, settlementExceptReason, remainBalance,
            serialNo: serialNo ? `"${serialNo}"` : '',
            activationDate, originalEv, parentVoucherEv
          };

          if (!supplierRole) {
            result = {
              ...result,
              publishName,
              publishId,
              campaignName,
              campaignId
            };
          }

          return result;
        })(raw);

        switch (raw.system) {
          case SYSTEM_BRAND_TYPE.BULK:
            baseItem.otp = raw.otp ? `"${raw.otp}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.CHOICE:
            baseItem.otp = raw.otp ? `"${raw.otp}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.EXTERNAL:
            baseItem.pin = raw.pin ? `"${raw.pin}"` : '';
            baseItem.password = raw.password ? `"${raw.password}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.GIFTPOP:
            baseItem.pin = raw.pin ? `"${raw.pin}"` : '';
            baseItem.transactionId = raw.transactionId ? `"${raw.transactionId}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.UR_BOX:
            baseItem.pin = raw.pin ? `"${raw.pin}"` : '';
            baseItem.transactionId = raw.transactionId ? `"${raw.transactionId}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.WATANE:
            baseItem.pin = raw.pin ? `"${raw.pin}"` : '';
            baseItem.transactionId = raw.transactionId ? `"${raw.transactionId}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.VNPT_EPAY:
            baseItem.requestId = raw.requestId;
            baseItem.provider = raw.provider;
            baseItem.faceValue = raw.faceValue;
            baseItem.cardSerial = raw.cardSerial ? `"${raw.cardSerial}"` : '';
            baseItem.cardPin = raw.cardPin ? `"${raw.cardPin}"` : '';
            baseItem.topupNumber = raw.topupNumber ? `"${raw.topupNumber}"` : '';
            break;
          case SYSTEM_BRAND_TYPE.XPAY:
            baseItem.requestId = raw.requestId;
            baseItem.provider = raw.provider;
            baseItem.faceValue = raw.faceValue;
            baseItem.cardSerial = raw.cardSerial ? `"${raw.cardSerial}"` : '';
            baseItem.cardPin = raw.cardPin ? `"${raw.cardPin}"` : '';
            baseItem.topupNumber = raw.topupNumber ? `"${raw.topupNumber}"` : '';
            break;
          default:
            break;
        }

        return baseItem;
      });

      // console.log(filteredList)
      // return

      if(filteredList?.length <= 0 ) {
        toast.error('Empty list  ' );
        return
      }

      const csv = parse(filteredList); // convert the JSON to CSV using the json2csv package
      const utf8BOM = '\uFEFF';
      const blob = new Blob([utf8BOM + csv], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);

      const hideLink = document.createElement('a');
      hideLink.href = url;
      hideLink.download = voucherType
        ? `rawSettlement_System${system}_VoucherType${voucherType}_${transactionStartDate}_${transactionEndDate}.csv`
        : `rawSettlement_System${system}_${transactionStartDate}_${transactionEndDate}.csv` ;
      hideLink.style.display = 'none';
      document.body.appendChild(hideLink);
      hideLink.click();

      // delete Blob object và a tag when download done
      URL.revokeObjectURL(url);
      document.body.removeChild(hideLink);
    } catch (error) {
      console.error('Error while downloading CSV:', error);
    } finally {
      setLoadingCsv(false);
    }
  }

  // @ts-ignore
  return (
    <>
      <form
        noValidate
        role="search"
        className={cn('relative w-full items-center', className)}
        onSubmit={handleSubmit(onSearch)}
      >
        <Card className=" mb-8 flex flex-wrap p-1 md:p-1">
          <div className="mb-4 flex md:w-11/12 flex-wrap">
            {/*start date*/}
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Transaction period start')}
              </label>
              <div className="w-full md:w-3/4">
                <Controller
                  control={control}
                  name="transactionStartDate"
                  render={({field: {onChange, onBlur, value}}) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      onChange={(date) => {
                        if (date) {
                          // Đảm bảo rằng date là kiểu Date object
                          const selectedDate = new Date(date);

                          // Format ngày thành chuỗi "YYYY-MM-DD"
                          const formattedDate = format(selectedDate, 'yyyy-MM-dd');
                          onChange(formattedDate);
                        } else {
                          onChange(null);
                        }
                      }}
                      onBlur={onBlur}
                      selected={value ? new Date(value) : null}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                      placeholderText={t('Start date')}
                    />
                  )}
                />
                <ValidationError message={t(errors.transactionStartDate?.message!)}/>
              </div>
            </div>
            {/*End date*/}
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Transaction period end')}
              </label>
              <div className="w-full md:w-3/4">
                <Controller
                  control={control}
                  name="transactionEndDate"
                  render={({field: {onChange, onBlur, value}}) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      onChange={(date) => {
                        if (date) {
                          // Đảm bảo rằng date là kiểu Date object
                          const selectedDate = new Date(date);

                          // Format ngày thành chuỗi "YYYY-MM-DD"
                          const formattedDate = format(selectedDate, 'yyyy-MM-dd');
                          onChange(formattedDate);
                        } else {
                          onChange(null);
                        }
                      }}
                      onBlur={onBlur}
                      selected={value ? new Date(value) : null}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                      placeholderText={t('End date')}
                    />
                  )}
                />
                <ValidationError message={t(errors.transactionEndDate?.message!)}/>
              </div>
            </div>
            {/*Company type*/}
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Company Type')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="customerStatus" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <div className="w-full">
                  {supplierRole
                    ? <input
                      className={`${rootClassName}   cursor-not-allowed bg-gray-100 `}
                      disabled={true  }
                      type="text"
                      id="settlementTarget"
                      {...register('settlementTarget')}
                      value={"SUPPLIER"}
                      autoComplete="off"
                    />
                    : <Select
                      id="settlementTarget"
                      name="settlementTarget"
                      options={RAW_SETTLEMENT_COMPANY_TYPE}
                      getOptionLabel={(option: any) => option.text}
                      getOptionValue={(option: any) => option.code}
                      placeholder={t('All')}
                      isDisabled={supplierRole}
                      onChange={onChangeCompanyType}
                    />
                  }

                </div>
              </div>
            </div>
            {/*Company name*/}
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row ">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Company Name')}
                </h1>
              </div>
              {supplierRole
                ? <input
                    className={`${rootClassName}  md:w-3/4 cursor-not-allowed bg-gray-100 `}
                    disabled={true}
                    type="text"
                    id="companyName"
                    {...register('companyName')}
                    value={infoUser?.adminCorporationName}
                    autoComplete="off"
                  />
                : <div className="w-full md:w-3/4">
                  <SelectInput
                    // {...register('companyName')}
                    id="companyName"
                    name="companyName"
                    options={optionsCompanyName}
                    // disabled={initialValues ? true : false}
                    getOptionLabel={(option: any) => option.label + " - " + option.value}
                    getOptionValue={(option: any) => option.value}
                    onInputChange={hanleInputChangeCorporation}
                    onChange={onChangeCompanyName}
                    control={control}
                    isClearable={true}
                  />
                </div>
              }
            </div>
            {/*Voucher Type*/}
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Voucher Type')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="customerStatus" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <div className="w-full">
                  <Select
                    id="voucherTypeCode"
                    name="voucherTypeCode"
                    options={VOUCHER_TYPE}
                    getOptionLabel={(option: any) => option.text}
                    getOptionValue={(option: any) => option.code}
                    placeholder={t('All')}
                    onChange={onChangeVoucherType}
                  />
                </div>
              </div>
            </div>
            {supplierRole
              ? <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
                {/*empty*/}
              </div>
              : <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Settlement Method')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="customerStatus" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <div className="w-full">
                    <Select
                      id="settlementMethodCode"
                      name="settlementMethodCode"
                      options={RAW_SETTLEMENT_METHOD}
                      getOptionLabel={(option: any) => option.text}
                      getOptionValue={(option: any) => option.code}
                      placeholder={t('All')}
                      onChange={onChangeMethodType}
                    />
                  </div>
                </div>
              </div>}
            {!supplierRole && <>
              {/*campaignName*/}
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Campaign name')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="customerStatus" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <div className="w-full">
                    <SelectInput
                      name="campaignName"
                      options={campaigns}
                      getOptionLabel={(option: any) =>
                        option.campaignName
                      }
                      getOptionValue={(option: any) => option.campaignName}
                      onInputChange={hanleInputChangeCampaignName}
                      onChange={handleChangeCampaignName}
                      placeholder={t('Select or type to search')}
                      control={control}
                      isClearable={true}
                    />
                  </div>
                </div>
              </div>
              {/*campaignId*/}
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Campaign Id')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="customerStatus" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <div className="w-full">
                    <input
                      // @ts-ignore
                      className={`${rootClassName}   cursor-not-allowed bg-gray-100 `}
                      // @ts-ignore
                      disabled={true /*campaign?.campaignName.length > 0*/}
                      type="text"
                      id="campaignId"
                      {...register('campaignId')}
                      autoComplete="off"
                    />
                  </div>
                </div>
              </div>
              {/*publishName*/}
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Publish Name')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="customerStatus" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <div className="w-full">
                    <SelectInput
                      name="publishName"
                      options={deliveries}
                      getOptionLabel={(option: any) =>
                        option.publishName
                      }
                      getOptionValue={(option: any) => option.publishName}
                      onInputChange={hanleInputChangePublishName}
                      onChange={handleChangePublishName}
                      placeholder={t('Select or type to search')}
                      control={control}
                      isClearable={true}
                    />
                  </div>
                </div>
              </div>
              {/*publishId*/}
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Publish Id')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="customerStatus" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <div className="w-full">
                  {/*<Select*/}
                  {/*  id="campaignId"*/}
                  {/*  {...register('campaignId')}*/}
                  {/*  options={campaigns}*/}
                  {/*  getOptionLabel={(option: any) =>*/}
                  {/*    option.id*/}
                  {/*  }*/}
                  {/*  getOptionValue={(option: any) => option.id}*/}
                  {/*  onInputChange={hanleInputChangeCampaignId}*/}
                  {/*  onChange={handleChangeCampaignId}*/}
                  {/*  placeholder={t('common:filter-by-group-placeholder')}*/}
                  {/*  // control={control}*/}
                  {/*  isClearable={true}*/}
                  {/*/>*/}
                  <input
                    // @ts-ignore
                    className={`${rootClassName}   cursor-not-allowed bg-gray-100 `}
                    // @ts-ignore
                    disabled={true /*publish?.publishName.length > 0*/}
                    type="text"
                    id="publishId"
                    {...register('publishId')}
                    autoComplete="off"
                  />
                </div>
              </div>
            </div>
            </> }
          </div>

          <div className="mb-4 flex md:w-1/12 flex-wrap mr-0">
            <Button
              className="w-full bg-red-700 hover:bg-red-800"
              aria-label="Search"
            >
              {t('Search')}
            </Button>
          </div>
        </Card>
        <div className="flex w-full justify-end mb-3 -mt-4">
          { supplierRole
            ? <Button
                onClick={() => downloadCSV(SYSTEM_BRAND_TYPE.ALL)}
                loading={loadingCsv}
                disabled={loadingCsv}
                className=" bg-red-700 hover:bg-red-800"
                aria-label="Download" >
                  {t('Download all')}
              </Button>
            : <DownloadCsvDropdown onDownload={downloadCSV} isDownloading={loadingCsv}/> }
        </div>
      </form>
    </>

  );
};

export default Search;

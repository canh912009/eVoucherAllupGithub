
import { useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';

import { brandsValidationSchema } from '@/components/brands/brands-validation-schema';
import {
  Brands,
  Category,
  CommonApproveStatusAction,
  GiftPopBrand,
  GoodQueryOptions,
  Supplier, UrBoxBrand,
} from '@/types';
import {
  useCreateBrandsMutation,
  useUpdateBrandsMutation,
} from '@/data/brands';
import { getErrorMessage } from '@/utils/form-error';
import SelectInput from '@/components/ui/select-input-autocomplete';
import React, {ChangeEvent, useEffect, useState} from 'react';
import { useSuppliersQuery } from '@/data/supplier';
import {CHANNEL_APP_ID_DEFAULT, CODE_GROUP, PAGE_SIZE, SYSTEM_BRAND_TYPE} from '@/utils/constants';
import TextArea from "@/components/ui/text-area";
import Radio from "@/components/ui/radio/radio";
import { useUploadImageMutation } from '@/data/upload';
import { Routes } from "@/config/routes";
import GoodsList from "@/components/good/good-list";
import StoreList from "@/components/store/store-list";
import useInputTimeout from "@/utils/use-input-timeout";
import { getUrlPublicAsset } from "@/data/download";
import { emptyPlaceholder } from "@/utils/placeholders";
import LinkButton from "@/components/ui/link-button";
import Loader from "@/components/ui/loader/loader";
import {toast} from "react-toastify";
import {useCodeGroupQuery} from "@/data/code-group";
import {tr} from "date-fns/locale";
import {useCodeQuery} from "@/data/code";
import {useModalAction} from "@/components/ui/modal/modal.context";
import {validIPsList} from "@/utils/common-utils";

type IProps = {
  initialValues?: Brands | null;
  viewPage?: boolean;
};

type FormValues = Partial<Brands> & {
  appId?: string;
  serialNumberPrefix?: string;
  serialNumberTotalLength?: number;
}

export const displayTypeDefault = [SYSTEM_BRAND_TYPE.CHOICE, SYSTEM_BRAND_TYPE.BULK, SYSTEM_BRAND_TYPE.VNPT_EPAY, SYSTEM_BRAND_TYPE.XPAY]
export const displayTypeThirdParty = [SYSTEM_BRAND_TYPE.GIFTPOP, SYSTEM_BRAND_TYPE.UR_BOX, SYSTEM_BRAND_TYPE.WATANE]
export const displayTypeMulti = [SYSTEM_BRAND_TYPE.INTERNAL, SYSTEM_BRAND_TYPE.EXTERNAL]

export default function CreateOrUpdateBrandForm({ initialValues, viewPage = false }: IProps) {
  const router = useRouter();
  const { t } = useTranslation();
  const { openModal, closeModal } = useModalAction();
  const rootClassName = viewPage
    ? 'bg-gray-300 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent'
    : ' h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent'
    ;
  const {
    register,
    handleSubmit,
    control,
    watch,
    setError,
    setValue,
    getValues,
    formState: { errors },
  } = useForm<FormValues>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
        ...initialValues,
      }
      : {
        appId: CHANNEL_APP_ID_DEFAULT,
        serialNumberPrefix: CHANNEL_APP_ID_DEFAULT,
        serialNumberTotalLength: 12,
      },
    resolver: yupResolver(brandsValidationSchema),
  });

  const { inputText: supplierName, onInputChange: handleInputChangeSupplier } = useInputTimeout();
  const { suppliers, loading: loadingSuppliers } = useSuppliersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      supplierName: supplierName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );

  // @ts-ignore
  const handleKeyPress = (event) => {
    // Prevent form submission on Enter key press (key code 13)
    if (event.key === 'Enter') {
      event.preventDefault();
    }
  };

  const [typeExternalSupplier, setTypeExternalSupplier] = useState("");
  const { codeGroup: systemTypeCodeQuery, loading: codeLoading } = useCodeGroupQuery(CODE_GROUP.SYSTEM_TYPE);

  const giftPopTypeResult = useCodeQuery(CODE_GROUP.GIFTPOP,"SUPPLIER_ID");
  const urboxTypeResult = useCodeQuery(CODE_GROUP.UR_BOX,"SUPPLIER_ID");
  const wataneTypeResult = useCodeQuery(CODE_GROUP.WATANE,"SUPPLIER_ID");
  const vnptEpayTypeResult = useCodeQuery(CODE_GROUP.VNPT_EPAY,"SUPPLIER_ID");
  const xpayTypeResult = useCodeQuery(CODE_GROUP.XPAY,"SUPPLIER_ID");

  const handleChangeSystemType = (option: any) => {
    setValue('systemTypeCode', option)
  }

  useEffect(() => {
    if (initialValues?.system &&
        systemTypeCodeQuery &&
        systemTypeCodeQuery.codes) {
      let systemTypeInit = systemTypeCodeQuery.codes.find((element: { codeId: string }) => element.codeId === initialValues?.system)
      if (systemTypeInit) {
        setValue('systemTypeCode', systemTypeInit)
      }
    }
    setTypeExternalSupplier(initialValues?.system || null);
    setValue('systemTypeCode', initialValues?.system || null)
  }, [systemTypeCodeQuery, setValue]);

  const supplierTypeMap = {
    // @ts-ignore
    [giftPopTypeResult?.code?.codeName]:  CODE_GROUP.GIFTPOP,
    // @ts-ignore
    [urboxTypeResult?.code?.codeName]: CODE_GROUP.UR_BOX,
    // @ts-ignore
    [wataneTypeResult?.code?.codeName]: CODE_GROUP.WATANE,
    // @ts-ignore
    [vnptEpayTypeResult?.code?.codeName]: CODE_GROUP.VNPT_EPAY,
    // @ts-ignore
    [xpayTypeResult?.code?.codeName]: CODE_GROUP.XPAY,
  };
  const handleChangeSupplier = (newSupplier: Supplier) => {
    setValue('supplier', newSupplier);
    setValue('supplierName', newSupplier?.supplierName);
    setValue('supplierId', newSupplier?.id);

    console.log("handleChangeSupplier", newSupplier)

    // @ts-ignore
    const typeExternalSupplier = supplierTypeMap[newSupplier?.id] || null;
    setTypeExternalSupplier(typeExternalSupplier);
    setValue('systemTypeCode', typeExternalSupplier);
  };

  function handleSelectShowPopup(modalView: any) {
    if (modalView && modalView === "GIFTPOP_BRANCH_CODE") {
      openModal(modalView, { handleSaveGiftpop });
    }
    if (modalView && modalView === "URBOX_BRANCH_CODE") {
      openModal(modalView, { handleSaveUrBox });
    }
  }

  function handleSaveGiftpop(option: GiftPopBrand) {
    setErrorMessageExternalSupplier('')
    setSelectedGiftPop(option)
    // @ts-ignore
    setValue('brandCode', option[0].brandCode)
    // @ts-ignore
    setValue('brandImagePath', option[0].brandLogo);
    // @ts-ignore
    setValue('brandImageName', option[0].brandName);

    closeModal();
  }

  function handleSaveUrBox(option: UrBoxBrand) {
    setErrorMessageExternalSupplier('')
    setSelectedUrbox(option)
    // @ts-ignore
    setValue('brandCode', option[0].id)
    // @ts-ignore
    setValue('brandName', option[0].title)
    // @ts-ignore
    setValue('brandImagePath', option[0].images);
    // @ts-ignore
    setValue('brandImageName', option[0].title);
    // @ts-ignore
    setValue('description', option[0].description);

    closeModal();
  }
  const [selectedGiftPop, setSelectedGiftPop] = useState<GiftPopBrand>();
  const [selectedUrbox, setSelectedUrbox] = useState<UrBoxBrand>();
  const [errorMessageExternalSupplier, setErrorMessageExternalSupplier] = useState<string>();

  const [searchOptions, setSearchOptions] = useState<Partial<GoodQueryOptions>>({});
  const [page, setPage] = useState(1);

  const { mutate: createBrands, isLoading: creating } =
    useCreateBrandsMutation();
  const { mutate: updateBrands, isLoading: updating } =
    useUpdateBrandsMutation();

  /**
   * File
   */

  function handleUpload(e: React.ChangeEvent<HTMLInputElement>, uploadType: any, path: any, name: any) {
    // setValue(path, '');
    // setValue(name, '');
    if (e.target.files) {
      const file = e.target.files[0];

      if (file?.size > 100 * 1024) {
        // 100KB
        // Hiển thị thông báo hoặc xử lý khi tệp tin vượt quá dung lượng cho phép
        alert('File size exceeds 100KB limit!');
        // Xoá tập tin đã chọn (nếu muốn)
        // @ts-ignore
        e.target.value = null;
        return;
      }

      if (file) {
        uploadType(file, {
          onSuccess: (data: any) => {
            setValue(path, data?.path);
            setValue(name, file.name);
          },
          onError: (error: any) => {
            toast.error('Error:' + error?.response?.data.message);
          },
        });
      }
    }
  }
  const brandImagePath = watch('brandImagePath');
  const brandImageName = watch('brandImageName');
  const { mutate: uploadImage, isLoading: uploadingImage } = useUploadImageMutation();
  const handleUploadImageChange = (e: ChangeEvent<HTMLInputElement>) => {
    handleUpload(e, uploadImage, 'brandImagePath', 'brandImageName');
  };

  const brandLogoPath = watch('brandLogoPath');
  const brandLogoName = watch('brandLogoName');
  const { mutate: uploadLogo, isLoading: uploadingLogo } = useUploadImageMutation();
  const handleUploadLogoChange = (e: ChangeEvent<HTMLInputElement>) => {
    handleUpload(e, uploadLogo, 'brandLogoPath', 'brandLogoName');
  };

  const [errorAppId, setErrorAppId] = useState("")
  const [errorSerialNumberPrefix, setSerialNumberPrefix] = useState("")
  const [errorSerialNumberTotalLength, setSerialNumberTotalLength] = useState("")
  const [errorIpWhiteList, setErrorIpWhiteList] = useState("")
  const systemType = (initialValues
    ? initialValues?.system
    : typeExternalSupplier ? watch('systemTypeCode') : watch('systemTypeCode')?.codeId) ?? ""
  const internalSystem: boolean  = systemType === SYSTEM_BRAND_TYPE.INTERNAL

  const onSubmit = async (values: FormValues) => {
    // console.log('onSubmit', values);
    if(internalSystem) {
      if(errorAppId.length > 0) setErrorAppId("")
      if(errorSerialNumberPrefix.length > 0) setSerialNumberPrefix("")
      if(errorSerialNumberTotalLength.length > 0) setSerialNumberTotalLength("")
      if(errorIpWhiteList.length > 0) setErrorIpWhiteList("")

      // @ts-ignore
      const appId = getValues('appId').length > 0 ? getValues('appId') :  CHANNEL_APP_ID_DEFAULT
      // @ts-ignore
      const serialNumberPrefix = getValues('serialNumberPrefix')
      // @ts-ignore
      const serialNumberTotalLength = getValues('serialNumberTotalLength')
      // @ts-ignore
      const ipInput = getValues('ipWhiteList')

      // @ts-ignore
      if(appId === null || appId === undefined || appId?.length <= 0) {
        setErrorAppId("App ID required")
        return
      }
      // @ts-ignore
      if(appId?.length > 50) {
        setErrorAppId("App ID required no more than 50 characters")
        return
      }
      // @ts-ignore
      if(serialNumberPrefix === null || serialNumberPrefix === undefined || serialNumberPrefix?.length <= 0) {
        setSerialNumberPrefix("Serial number prefix required")
        return
      }
      // @ts-ignore
      if(serialNumberPrefix?.length > 10) {
        setSerialNumberPrefix("Serial number prefix required no more than 10 characters")
        return
      }
      // @ts-ignore
      if(serialNumberTotalLength === null || serialNumberTotalLength === undefined || serialNumberTotalLength <= 0) {
        setSerialNumberTotalLength("This required positive number")
        return
      }
      // @ts-ignore
      if(serialNumberTotalLength > 16) {
        setSerialNumberTotalLength("This required no more than 16")
        return
      }
      // @ts-ignore
      if(serialNumberTotalLength - serialNumberPrefix.length < 6) {
        setSerialNumberTotalLength("Serial number total length must be greater than or equal to length of Serial number prefix is 6 ")
        return
      }
      if(ipInput?.length > 0 && !validIPsList(ipInput) ) {
        setErrorIpWhiteList("Check again ip on list")
        return
      }
    }

    const baseInputValues = {
      supplierId: values.supplier?.id,
      supplierName: values.supplier?.supplierName,
      brandName: values.brandName,
      description: values.description,
      defaultBrandYn: values.defaultBrandYn,
      brandImagePath: values.brandImagePath,
      brandImageName: values.brandImageName,
      brandLogoPath: values.brandLogoPath,
      brandLogoName: values.brandLogoName,
      validYn: values.validYn,
      displayType: values.displayType,
      system: typeExternalSupplier ?? values?.systemTypeCode?.codeId ,
      brandCode: typeExternalSupplier ? values?.brandCode : "",
    };

    // Add channel-related fields only if internalSystem is true
    const inputValues = internalSystem ? {
      ...baseInputValues,
      appId: values?.appId ? values.appId : CHANNEL_APP_ID_DEFAULT,
      serialNumberPrefix: values.serialNumberPrefix,
      serialNumberTotalLength: values.serialNumberTotalLength,
      ipWhiteList: values.ipWhiteList,
    } : baseInputValues;
    // console.log('onSubmit, inputValues = ', inputValues);
    // return;

    if(typeExternalSupplier
        && (typeExternalSupplier === CODE_GROUP.GIFTPOP || typeExternalSupplier === CODE_GROUP.UR_BOX )
        && values.brandCode?.length === 0 ) {
      setErrorMessageExternalSupplier(`You must provide Brand Code (${typeExternalSupplier}) ) `)
      // console.log("selectedGiftPop", values.brandCode)
      return
    }

    try {
      if (!initialValues) {
        createBrands({
          ...inputValues,
        });
      } else {
        updateBrands({
          ...inputValues,
          id: initialValues.id,
        });
      }
    } catch (error) {
      const serverErrors = getErrorMessage(error);
      Object.keys(serverErrors?.validation).forEach((field: any) => {
        setError(field.split('.')[1], {
          type: 'manual',
          message: serverErrors?.validation[field][0],
        });
      });
    }
  };

  function displayTypeBySystem() {
    return (
      <>
        { displayTypeMulti.includes(systemType) ? (
          <>
            <Radio
              disabled={viewPage}
              className="flex w-full font-semibold md:w-1/2"
              label={t('Barcode 128')}
              {...register('displayType')}
              id="BARCODE"
              value="BARCODE"
              defaultChecked={ initialValues === undefined  || initialValues === null }
            />
            <Radio
              disabled={viewPage}
              className="flex w-full font-semibold md:w-1/2"
              label={ systemType === SYSTEM_BRAND_TYPE.INTERNAL ? t('QR') : t('Barcode 39') }
              {...register('displayType')}
              id={ systemType === SYSTEM_BRAND_TYPE.INTERNAL ? 'QRCODE' : 'BARCODE_39' }
              value={ systemType === SYSTEM_BRAND_TYPE.INTERNAL ? 'QRCODE' : 'BARCODE_39' }
            />
          </>
        ) : displayTypeDefault.includes(systemType) ? (
          <Radio
            disabled={viewPage}
            className="flex w-full font-semibold md:w-1/2"
            label={t('System default')}
            {...register('displayType')}
            id="DEFAULT"
            value="DEFAULT"
            defaultChecked={ initialValues === undefined  || initialValues === null }
          />
        ) : displayTypeThirdParty.includes(systemType) ? (
            <Radio
              disabled={viewPage}
              className="flex w-full font-semibold  "
              label={t('Decided by integrated system')}
              {...register('displayType')}
              id="THIRD_PARTY"
              value="THIRD_PARTY"
              defaultChecked={ initialValues === undefined  || initialValues === null }
            />
          ) : (
            <h2 className="mt-2 text-gray-500 font-bold">Please choose system first</h2>
          )
        }
      </>
    );
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="w-full sm:w-full md:w-full">
        {/*Brand Information*/}
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 py-2">
            <h1 className="text-base font-semibold uppercase ">
              {t('Brand Information')}
            </h1>
          </div>
        </div>
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2  text-heading md:w-1/4">
              {t('Brand Name: ')}
              <span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              id="brandName"
              {...register('brandName')}
              placeholder={t('Brand Name')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
              {t(errors.brandName?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('System: ')}
              <span className="text-red-500">*</span>
            </label>
            {typeExternalSupplier ? (
              <input
                disabled
                className={`${rootClassName} bg-gray-300`}
                id="systemTypeCode"
                {...register('systemTypeCode')}
                type="text"
                autoComplete="off"
                value={typeExternalSupplier}
              />
            ) : (
              <div className="w-full md:w-3/4 ">
                <SelectInput
                  name="systemTypeCode"
                  disabled={initialValues ? true : false}
                  control={control}
                  getOptionLabel={(option: any) => option?.codeId}
                  getOptionValue={(option: any) => option?.codeId}
                  onChange={handleChangeSystemType}
                  options={systemTypeCodeQuery?.codes ?? []}
                  isLoading={codeLoading}
                  // isClearable
                />
              </div>
            )}
            <span className="w-full text-xs text-red-500 text-start">
              {t(errors.systemTypeCode?.message!)}
            </span>
          </div>
        </div>
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2  text-heading md:w-1/4">
              {t('Supplier name: ')}
              <span className="text-red-500">*</span>
            </label>
            <div className="w-full md:w-3/4">
              <SelectInput
                name="supplier"
                options={suppliers}
                getOptionLabel={(option: any) => option?.supplierName}
                getOptionValue={(option: any) => option?.id} //taxcode
                onInputChange={handleInputChangeSupplier}
                onChange={handleChangeSupplier}
                placeholder={t('common:filter-by-group-placeholder')}
                control={control}
                isClearable={true}
                disabled={initialValues ? true : false}
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4">
                {t(errors.supplier?.message!)}
              </span>
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2   text-heading md:w-1/4">
              {t('Supplier ID: ')}
              <span className="text-red-500">*</span>
            </label>
            <input
              disabled
              className={`${rootClassName} bg-gray-300`}
              type="text"
              id="supplierId"
              {...register('supplierId')}
              autoComplete="off"
              placeholder={initialValues ? initialValues.supplier?.id : t('')}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
              {t(errors.supplier?.message!)}
            </span>
          </div>
        </div>
        {(typeExternalSupplier === CODE_GROUP.GIFTPOP ||
          typeExternalSupplier === CODE_GROUP.UR_BOX) && (
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2  text-heading md:w-1/4">
                {t('Brand Code: ')}
                <span className="text-red-500">*</span>
              </label>
              <input
                disabled
                className={`${rootClassName} bg-gray-300`}
                type="text"
                id="brandCode"
                {...register('brandCode')}
                placeholder={t(' . . .')}
                autoComplete="off"
              />
              {errorMessageExternalSupplier && (
                <>
                  <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                    {t(errorMessageExternalSupplier!)}
                  </span>
                </>
              )}
            </div>
            <div className="flex w-full flex-wrap px-4 py-2  md:w-1/2  ">
              {!initialValues && (
                <Button
                  size="small"
                  className="rounded-2xl bg-blue-800 hover:bg-blue-900"
                  disabled={uploadingImage}
                  onClick={(event) => {
                    event.preventDefault();
                    return typeExternalSupplier === CODE_GROUP.GIFTPOP
                      ? handleSelectShowPopup('GIFTPOP_BRANCH_CODE')
                      : handleSelectShowPopup('URBOX_BRANCH_CODE');
                  }}
                >
                  {t('Browse Brand')}
                </Button>
              )}

              {/*{errorMessageGiftpopOrUrbox && (*/}
              {/*    <>*/}
              {/*      <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">*/}
              {/*        {t(errorMessageGiftpopOrUrbox!)}*/}
              {/*      </span>*/}
              {/*    </>*/}
              {/*)}*/}
            </div>
          </div>
        )}
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Brand Information: ')}
              <span className="text-red-500">*</span>
            </label>
            <TextArea
              id="description"
              // disabled={initialValues ? true : false}
              disabled={viewPage}
              placeholder={t('Description')}
              {...register('description')}
              variant="outline"
              className="w-full pl-0 md:w-3/4"
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
              {t(errors.description?.message!)}
            </span>
          </div>

          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <div className="flex w-full flex-wrap pb-3">
              <label className="w-full py-2 text-heading md:w-1/4 ">
                {t('Display type  ')}
                <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                {displayTypeBySystem()}
              </div>
              <span className="w-full text-xs text-red-500 text-start md:pl-[25%]">
                {t(errors.displayType?.message!)}
              </span>
            </div>

            <div className="flex w-full flex-wrap pb-3">
              <label className="w-full py-2  text-heading md:w-1/4">
                {t('Active ')}
                <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  disabled={viewPage}
                  className="item-top w-full py-3 font-semibold md:w-1/2"
                  label={t('Yes')}
                  {...register('validYn')}
                  id="activeYes"
                  value="Y"
                />
                <Radio
                  disabled={viewPage}
                  className="item-top w-full py-3 font-semibold md:w-1/2"
                  label={t('No')}
                  {...register('validYn')}
                  id="activeNo"
                  value="N"
                />
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.validYn?.message!)}
              </span>
            </div>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <div className="flex w-full flex-wrap pb-3">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand logo: ')}
              </label>
              { [SYSTEM_BRAND_TYPE.CHOICE, SYSTEM_BRAND_TYPE.BULK, SYSTEM_BRAND_TYPE.VNPT_EPAY, SYSTEM_BRAND_TYPE.XPAY, ""].includes(systemType)
                ? <h2 className="mt-2 text-gray-500 font-bold">No logo needed</h2>
                : <div className="w-full pt-1 md:w-3/4">
                  <input
                    id="file_input"
                    type="file"
                    accept=".jpg, .jpeg, .png, .gif, .svg"
                    className={'e_hide-text'}
                    onChange={handleUploadLogoChange}
                  />
                  {uploadingLogo && (
                    <Loader uploadFile={true} text={t('common:text-loading')} />
                  )}
                  {brandLogoName && (
                    <p>
                      Selected logo:{' '}
                      <strong>
                        <em>{brandLogoName}</em>
                      </strong>
                    </p>
                  )}
              </div> }
              {/*<span className="w-full text-xs text-red-500 text-start md:pl-[25%]">*/}
              {/*  {t(errors.brandLogoPath?.message!)}*/}
              {/*</span>*/}
              <div className="w-full md:pl-[25%]">
                {brandLogoPath && (
                  <img
                    src={getUrlPublicAsset(brandLogoPath) ?? emptyPlaceholder}
                    alt={'Brand logo'}
                    width={300}
                    height={300}
                  />
                )}
              </div>
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <div className="flex w-full flex-wrap pb-3">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand image: ')}
                <span className="text-red-500">*</span>
              </label>
              <div className="w-full pt-1 md:w-3/4">
                <input
                  id="file_input"
                  type="file"
                  accept=".jpg, .jpeg, .png, .gif, .svg"
                  className={'e_hide-text'}
                  onChange={handleUploadImageChange}
                />
                {uploadingImage && (
                  <Loader uploadFile={true} text={t('common:text-loading')} />
                )}
                {brandImageName && (
                  <p>
                    Selected image:{' '}
                    <strong>
                      <em>{brandImageName}</em>
                    </strong>
                  </p>
                )}
              </div>
              <span className="w-full text-xs text-red-500 text-start md:pl-[25%]">
                {t(errors.brandImagePath?.message!)}
              </span>
              <div className="w-full md:pl-[25%]">
                {brandImagePath && (
                  <img
                    src={getUrlPublicAsset(brandImagePath) ?? emptyPlaceholder}
                    alt={'Brand Image'}
                    width={300}
                    height={300}
                  />
                )}
              </div>
            </div>
          </div>
        </div>
        {/*End Brand Information*/}

        {/*Channel Information just apply for internalSystem*/}
        {internalSystem && (
          <>
            <div className=" flex flex-wrap border-b border-dashed border-border-base pb-8" />
            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 py-2">
                <h1 className="text-base font-semibold uppercase ">
                  {t('Channel Information')}
                </h1>
              </div>
            </div>
            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('App ID : ')}
                  <span className="text-red-500">*</span>
                </label>
                <input
                  className={rootClassName}
                  type="text"
                  onKeyPress={handleKeyPress}
                  id="appId"
                  {...register('appId')}
                  placeholder={CHANNEL_APP_ID_DEFAULT}
                  autoComplete="off"
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {errorAppId}
                </span>
              </div>
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Authentication Key : ')}
                </label>
                <input
                  className={`${rootClassName} bg-gray-200`}
                  type="text"
                  id="authenticationKey"
                  {...register('authenticationKey')}
                  autoComplete="off"
                  disabled={true}
                />
              </div>
            </div>
            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Serial number prefix')}{' '}
                  <span className="text-red-500">*</span>
                </label>
                <input
                  className={rootClassName}
                  type="text"
                  onKeyPress={handleKeyPress}
                  id="serialNumberPrefix"
                  {...register('serialNumberPrefix')}
                  placeholder={t('AQUA')}
                  autoComplete="off"
                  disabled={viewPage}
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {errorSerialNumberPrefix}
                </span>
              </div>
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Encryption Key : ')}
                </label>
                <input
                  className={`${rootClassName} bg-gray-200`}
                  type="text"
                  id="encryptionKey"
                  {...register('encryptionKey')}
                  autoComplete="off"
                  disabled={true}
                />
              </div>
            </div>
            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Serial number total length')}{' '}
                  <span className="text-red-500">*</span>
                </label>
                <input
                  className={rootClassName}
                  type="number"
                  onKeyPress={handleKeyPress}
                  id="serialNumberTotalLength"
                  {...register('serialNumberTotalLength')}
                  placeholder={t('Type number here')}
                  autoComplete="off"
                  disabled={viewPage}
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {errorSerialNumberTotalLength}
                </span>
              </div>
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Ip white list')}
                </label>
                <TextArea
                  id="ipWhiteList"
                  // disabled={initialValues ? true : false}
                  disabled={viewPage}
                  placeholder={t(
                    'Optional. Leave empty if operator don\'t want to restrict the IP. If input, list separated by comma","'
                  )}
                  {...register('ipWhiteList')}
                  variant="outline"
                  className="w-full pl-0 md:w-3/4"
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {errorIpWhiteList}
                </span>
              </div>
            </div>
          </>
        )}
        {/*End Channel Information*/}

        {/*OWNED PRODUCT LIST*/}
        {initialValues && (
          <div className=" flex flex-wrap border-b border-dashed border-border-base pb-8" />
        )}
        {initialValues && (
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-base font-semibold uppercase ">
                {t('OWNED PRODUCT LIST')}
              </h1>
            </div>
          </div>
        )}
        {initialValues && <GoodsList goods={initialValues?.listGoods} />}
        {/*END OWNED PRODUCT LIST*/}

        {/*OWNED CHAINED STORE LIST*/}
        {initialValues && (
          <div className=" flex flex-wrap border-b border-dashed border-border-base pb-8" />
        )}
        {initialValues && (
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-base font-semibold uppercase ">
                {t('OWNED CHAINED STORE LIST')}
              </h1>
            </div>
          </div>
        )}
        {initialValues && <StoreList stores={initialValues?.stores} />}
        {/*END OWNED CHAINED STORE LIST*/}

        <div className="my-4 text-end ">
          <Button
            variant="outline"
            onClick={router.back}
            className="me-4 hover:bg-red-800"
            type="button"
          >
            {t('Back')}
          </Button>

          {viewPage ? (
            <LinkButton
              href={`${Routes.brands.editWithoutLang(
                initialValues?.id as string
              )}`}
              className="bg-black hover:bg-red-800"
            >
              {t('Edit')}
            </LinkButton>
          ) : (
            <Button
              loading={updating || creating}
              className="bg-red-700 hover:bg-red-800"
              disabled={uploadingImage}
            >
              {initialValues ? t('Update') : t('Register')}
            </Button>
          )}
        </div>
      </div>
    </form>
  );
}

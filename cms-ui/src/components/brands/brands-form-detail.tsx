import { useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';

import { brandsValidationSchema } from '@/components/brands/brands-validation-schema';
import { Brands, CommonApproveStatusAction, GoodQueryOptions, Supplier } from '@/types';
import {
  useCreateBrandsMutation,
  useUpdateBrandsMutation,
} from '@/data/brands';
import { getErrorMessage } from '@/utils/form-error';
import SelectInput from '@/components/ui/select-input-autocomplete';
import React, { useState } from 'react';
import { useSuppliersQuery } from '@/data/supplier';
import {PAGE_SIZE, PERMISSIONS_EV, SYSTEM_BRAND_TYPE} from '@/utils/constants';
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
import {getUserInfo} from "@/utils/auth-utils";
import {displayTypeDefault, displayTypeMulti, displayTypeThirdParty} from "@/components/brands/brands-form";

type IProps = {
  initialValues?: Brands | null;
  viewPage?: boolean;
};

type FormValues = Partial<Brands>

export default function DetailBrandForm({ initialValues, viewPage = false }: IProps) {
  // console.log('initialValues = ', initialValues);
  const router = useRouter();
  const { t } = useTranslation();
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
    formState: { errors },
  } = useForm<FormValues>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
        ...initialValues,
      }
      : {},
    resolver: yupResolver(brandsValidationSchema),
  });

  const { inputText: supplierName, onInputChange: handleInputChangeSupplier } =
    useInputTimeout();
  const { suppliers, loading: loadingSuppliers } = useSuppliersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      supplierName: supplierName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );

  const handleChangeSupplier = (newSupplier: Supplier) => {
    setValue('supplier', newSupplier);
    setValue('supplierName', newSupplier?.supplierName);
    setValue('supplierId', newSupplier?.id);
  };

  const [searchOptions, setSearchOptions] = useState<Partial<GoodQueryOptions>>({});
  const [page, setPage] = useState(1);

  const { mutate: createBrands, isLoading: creating } =
    useCreateBrandsMutation();
  const { mutate: updateBrands, isLoading: updating } =
    useUpdateBrandsMutation();

  /**
   * File
   */
  const brandImagePath = watch('brandImagePath');
  const brandImageName = watch('brandImageName');
  const { mutate: uploadImage } = useUploadImageMutation();
  const systemType = initialValues?.system ?? ""

  const onSubmit = async (values: FormValues) => {
    const inputValues = {
      supplierId: values.supplier?.id,
      supplierName: values.supplier?.supplierName,
      brandName: values.brandName,
      description: values.description,
      defaultBrandYn: values.defaultBrandYn,
      brandImagePath: values.brandImagePath,
      brandImageName: values.brandImageName,
      validYn: values.validYn,
    };

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

  return (
    <div>
      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-4 py-2">
          <h1 className="w-full text-base uppercase font-semibold md:w-[88%]">
            {t('Brand Information')}
          </h1>
          <Button
            variant="outline"
            // TODO
            onClick={() =>
              router.push(
                `${Routes?.brands.details(
                  initialValues?.id as string
                )}/store-list-qr`
              )
            }
            className="px-6 hover:bg-gray-500"
            type="button"
            size="small"
          >
            {t('Download all QR')}
          </Button>
        </div>
      </div>

      {/*Brand Information*/}
      <div className="w-full sm:w-full md:w-full">
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
              value={initialValues?.brandName}
              placeholder={t('Brand Name')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
              {t(errors.brandName?.message!)}
            </span>
          </div>
          {viewPage && (
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2  text-heading md:w-1/4">
                {t('Brand ID: ')}
                <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="id"
                value={initialValues?.id}
                placeholder={t('Brand ID')}
                autoComplete="off"
                disabled
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.brandName?.message!)}
              </span>
            </div>
          )}
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
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplierName?.message!)}
              </span>
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('System *')}
            </label>
            <input
              disabled
              className={`${rootClassName} bg-gray-300`}
              type="text"
              value={initialValues?.system}
              autoComplete="off"
              placeholder={initialValues ? initialValues.system : t('')}
            />
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
              value={initialValues?.supplier?.id}
              autoComplete="off"
              placeholder={initialValues ? initialValues.supplier?.id : t('')}
            />
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Brand Information: ')}
              <span className="text-red-500">*</span>
            </label>
            <TextArea
              id="description"
              disabled={viewPage}
              name="description"
              placeholder={t('Description')}
              value={initialValues?.description}
              variant="outline"
              className="w-full md:w-3/4 pl-0"
            />
          </div>

          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4 ">
              {t('Display type')}
              <span className="text-red-500">*</span>
            </label>
            <div className="flex w-full md:w-3/4">
              {displayTypeMulti.includes(systemType) ? (
                <>
                  <Radio
                    disabled={viewPage}
                    className="flex w-full font-semibold md:w-1/2"
                    label={t('Barcode 128')}
                    {...register('displayType')}
                    id="BARCODE"
                    value="BARCODE"
                    checked={initialValues?.displayType == 'BARCODE'}
                  />
                  <Radio
                    disabled={viewPage}
                    className="flex w-full font-semibold md:w-1/2"
                    label={
                      systemType === SYSTEM_BRAND_TYPE.INTERNAL
                        ? t('QR')
                        : t('Barcode 39')
                    }
                    {...register('displayType')}
                    id={
                      systemType === SYSTEM_BRAND_TYPE.INTERNAL
                        ? 'QRCODE'
                        : 'BARCODE_39'
                    }
                    value={
                      systemType === SYSTEM_BRAND_TYPE.INTERNAL
                        ? 'QRCODE'
                        : 'BARCODE_39'
                    }
                    checked={
                      systemType === SYSTEM_BRAND_TYPE.INTERNAL
                        ? initialValues?.displayType == 'QRCODE'
                        : initialValues?.displayType == 'BARCODE_39'
                    }
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
                  checked={initialValues?.displayType == 'DEFAULT'}
                />
              ) : displayTypeThirdParty.includes(systemType) ? (
                <Radio
                  disabled={viewPage}
                  className="flex w-full font-semibold "
                  label={t('Decided by integrated system')}
                  {...register('displayType')}
                  id="THIRD_PARTY"
                  value="THIRD_PARTY"
                  checked={initialValues?.displayType == 'THIRD_PARTY'}
                />
              ) : (
                <h2>Please choose system first</h2>
              )}
            </div>
            <label className="w-full py-2  text-heading md:w-1/4">
              {t('Active ')}
              <span className="text-red-500">*</span>
            </label>
            <div className="flex w-full md:w-3/4">
              <Radio
                disabled={viewPage}
                className="py-3 w-full font-semibold md:w-1/2"
                label={t('Yes')}
                name="validYn"
                checked={initialValues?.validYn === 'Y'}
                id="y"
                value="Y"
              />
              <Radio
                disabled={viewPage}
                className="py-3 w-full font-semibold md:w-1/2"
                label={t('No')}
                name="validYn"
                checked={initialValues?.validYn === 'N'}
                id="n"
                value="N"
              />
            </div>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          {![
            SYSTEM_BRAND_TYPE.CHOICE,
            SYSTEM_BRAND_TYPE.BULK,
            SYSTEM_BRAND_TYPE.VNPT_EPAY,
            SYSTEM_BRAND_TYPE.XPAY,
          ].includes(systemType) && (
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <div className="flex w-full flex-wrap pb-3">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Brand logo: ')}
                </label>
                <div className="w-full pt-1 md:w-3/4 ">
                  {initialValues?.brandLogoName && (
                    <p>Selected file: {initialValues?.brandLogoName}</p>
                  )}
                </div>
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%] ">
                  {t(errors.brandLogoPath?.message!)}
                </span>
              </div>
              <div className="w-full md:pl-[25%]">
                {initialValues?.brandLogoPath && (
                  <img
                    src={
                      getUrlPublicAsset(initialValues?.brandLogoPath) ??
                      emptyPlaceholder
                    }
                    alt={'Brand logo'}
                    width={300}
                    height={300}
                  />
                )}
              </div>
            </div>
          )}
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <div className="flex w-full flex-wrap pb-3">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand image: ')}
                <span className="text-red-500">*</span>
              </label>
              <div className="w-full pt-1 md:w-3/4 ">
                {initialValues?.brandImageName && (
                  <p>Selected file: {initialValues?.brandImageName}</p>
                )}
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%] ">
                {t(errors.brandImagePath?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-8  md:pl-[25%]">
              {initialValues?.brandImagePath && (
                <img
                  src={
                    getUrlPublicAsset(initialValues?.brandImagePath) ??
                    emptyPlaceholder
                  }
                  alt={'Brand Image'}
                  width={300}
                  height={300}
                />
              )}
            </div>
          </div>
        </div>

        {/* {initialValues?.brandImagePath && (
          <div className="mb-5 flex flex-wrap -mt-36">
            <div className="flex w-full flex-wrap px-4 md:w-1/2" />
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <div className="w-full py-2 md:w-1/4" />
              <div className="w-full pt-1 md:w-3/4">
                <img
                  src={getUrlPublicAsset(initialValues?.brandImagePath) ?? emptyPlaceholder}
                  alt={'Brand Image'}
                  width={300}
                  height={300}
                />
              </div>
            </div>
          </div>
        )} */}
      </div>

      {/*Channel Information*/}
      {initialValues?.system === SYSTEM_BRAND_TYPE.INTERNAL && (
        <>
          <div className=" flex flex-wrap border-b border-dashed border-border-base pb-8" />
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-base uppercase font-semibold ">
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
                className={`${rootClassName} bg-gray-200`}
                type="text"
                id="appId"
                value={initialValues?.appId}
                placeholder={t('Type here')}
                autoComplete="off"
                disabled={viewPage}
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Authentication Key : ')}
              </label>
              <input
                className={`${rootClassName} bg-gray-200`}
                type="text"
                id="authenticationKey"
                value={initialValues?.authenticationKey}
                autoComplete="off"
                disabled={viewPage}
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
                id="serialNumberPrefix"
                value={initialValues?.serialNumberPrefix}
                placeholder={t('Type here')}
                autoComplete="off"
                disabled={viewPage}
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Encryption Key : ')}
              </label>
              <input
                className={`${rootClassName} bg-gray-200`}
                type="text"
                id="encryptionKey"
                value={initialValues?.encryptionKey}
                autoComplete="off"
                disabled={viewPage}
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
                id="serialNumberTotalLength"
                value={initialValues?.serialNumberTotalLength}
                placeholder={t('Type number here')}
                autoComplete="off"
                disabled={viewPage}
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Ip white list')}
              </label>
              <TextArea
                id="ipWhiteList"
                name="ipWhiteList"
                disabled={viewPage}
                value={initialValues?.ipWhiteList}
                variant="outline"
                className="w-full md:w-3/4 pl-0"
              />
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
            <h1 className="text-base uppercase font-semibold ">
              {t('OWNED PRODUCT LIST')}
            </h1>
          </div>
        </div>
      )}
      {initialValues && <GoodsList goods={initialValues?.listGoods} />}

      {/*OWNED PRODUCT LIST*/}
      {initialValues && (
        <div className=" flex flex-wrap border-b border-dashed border-border-base pb-8" />
      )}
      {initialValues && (
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 py-2">
            <h1 className="text-base uppercase font-semibold ">
              {t('OWNED CHAINED STORE LIST')}
            </h1>
          </div>
        </div>
      )}
      {initialValues && <StoreList stores={initialValues?.stores} />}

      <div className="my-4 text-end ">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('Back')}
        </Button>

        {getUserInfo().roleCode !== PERMISSIONS_EV.ROLE_SUPPLIER && (
          <LinkButton
            href={`${Routes.brands.editWithoutLang(
              initialValues?.id as string
            )}`}
            className="bg-black hover:bg-red-800"
          >
            {t('Edit')}
          </LinkButton>
        )}
      </div>
    </div>
  );
}

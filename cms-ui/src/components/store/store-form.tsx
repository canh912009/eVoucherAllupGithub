import { useForm } from 'react-hook-form';
import Image from 'next/image';
import Button from '@/components/ui/button';
import Radio from '@/components/ui/radio/radio';
import Card from '@/components/common/card';
import { useRouter } from 'next/router';
import SelectInput from '@/components/ui/select-input-autocomplete';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import { storeValidationSchema } from '@/components/store/store-validation-schema';
import {
  Brands,
  CommonApproveStatusAction,
  CommonYesNoEnum,
  Store,
  Supplier,
} from '@/types';
import { useCreateStoreMutation, useUpdateStoreMutation } from '@/data/store';
import { getErrorMessage } from '@/utils/form-error';
import { ChangeEvent, useEffect } from 'react';
import useInputTimeout from '@/utils/use-input-timeout';
import { PAGE_SIZE } from '@/utils/constants';
import { useBrandsQuery } from '@/data/brands';
import { useSuppliersQuery } from '@/data/supplier';
import { useUploadImageMutation } from '@/data/upload';
import { getUrlPublicAsset } from '@/data/download';
import { emptyPlaceholder } from '@/utils/placeholders';
import { supplierClient } from '@/data/client/crud-client';
import {toast} from "react-toastify";
import Loader from "@/components/ui/loader/loader";

type FormValues = Partial<Store> & {
  supplier: Supplier;
  brand: Brands;
};

type IProps = {
  initialValues?: Store | null;
};

export default function CreateOrUpdateStoreForm({ initialValues }: IProps) {
  const router = useRouter();
  const { t } = useTranslation();

  const rootClassName =
    'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const {
    register,
    handleSubmit,
    control,
    setValue,
    watch,
    setError,
    formState: { errors },
  } = useForm<FormValues>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
          ...initialValues,
        }
      : {},
    resolver: yupResolver(storeValidationSchema),
  });

  const supplierId = watch('supplierId');
  const supplier = watch('supplier');

  /**
   * Supplier
   */
  const { inputText: supplierName, onInputChange: handleInputChangeSupplier } =
    useInputTimeout();

  const { suppliers, loading: loadingSuppliers } = useSuppliersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      supplierName: supplierName,
      // brandId: brand?.id,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );

  const handleChangeSupplier = (newSupplier: Supplier) => {
    setValue('brand', null as unknown as Brands);
    setValue('supplier', newSupplier);
  };

  /**
   * Brand
   */

  const { inputText: brandName, onInputChange: handleInputChangeBrand } =
    useInputTimeout();

  const { brands, loading: loadingBrands } = useBrandsQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      brandName: brandName,
      supplierId: supplier?.id,
      validYn: CommonYesNoEnum.YES,
    }
  );

  const handleChangeBrand = (option: Brands) => {
    setValue('brand', option);

    if (option?.supplierId) {
      setValue('supplierId', option?.supplierId);
    } else {
      setValue('supplierId', '');
    }
  };

  /**
   * File
   */
  const storeImageName = watch('storeImageName');
  const storeImagePath = watch('storeImagePath');

  const { mutate: uploadImage, isLoading: uploadingImage } = useUploadImageMutation();

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    setValue('storeImagePath', '');
    setValue('storeImageName', '');

    if (e.target.files) {
      const file = e.target.files[0];
      // console.log('file', file);
      if (file) {
        uploadImage(file, {
          onSuccess: (data: any) => {
            // console.log('data', data);
            setValue('storeImagePath', data?.path);
            setValue('storeImageName', file.name);
          },
          onError: (error: any) => {
            toast.error('Error:' + error?.response?.data.message);
          },
        });
      }
    }
  };

  /**
   * Set supplier if choose brand
   */
  useEffect(() => {
    const fetchData = async () => {
      try {
        if (!supplierId) {
          return;
        }

        const response = await supplierClient.get(supplierId);
        // console.log('supplier', response.data);
        setValue('supplier', response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [supplierId]);

  const { mutate: createStore, isLoading: creating } = useCreateStoreMutation();
  const { mutate: updateStore, isLoading: updating } = useUpdateStoreMutation();

  const onSubmit = async (values: FormValues) => {
    // console.log('values', values);
    // return;

    const inputValues = {
      storeName: values.storeName,
      mapCode: values.mapCode,
      storeType: values.storeType,
      supplierId: values.supplier?.id,
      brandId: values.brand?.id,
      region: values.region,
      fullAddress: values.fullAddress,
      telephoneNumber: values.telephoneNumber,
      validYn: values.validYn,
      storeImageName: values.storeImageName,
      storeImagePath: values.storeImagePath,
    };

    // console.log('inputValues', inputValues);
    // return;

    try {
      if (!initialValues) {
        createStore({
          ...inputValues,
        });
      } else {
        updateStore({
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
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Chained Store Information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Store name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="storeName"
                {...register('storeName')}
                placeholder={t('Store name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.storeName?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Map code')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="mapCode"
                {...register('mapCode')}
                placeholder={t('Map code')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.mapCode?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Store type')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="storeType"
                {...register('storeType')}
                placeholder={t('Store type')}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Brand name')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <SelectInput
                  name="brand"
                  options={brands}
                  isLoading={loadingBrands}
                  getOptionLabel={(option: any) =>
                    option.brandName + ' - ' + option.id
                  }
                  getOptionValue={(option: any) => option.id}
                  onInputChange={handleInputChangeBrand}
                  onChange={handleChangeBrand}
                  placeholder={t('common:filter-by-group-placeholder')}
                  control={control}
                  isClearable={true}
                  disabled={!!initialValues}
                />
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.brand?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Supplier name')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <SelectInput
                  name="supplier"
                  options={suppliers}
                  isLoading={loadingSuppliers}
                  getOptionLabel={(option: any) =>
                    option.supplierName + ' - ' + option.id
                  }
                  getOptionValue={(option: any) => option.id}
                  onInputChange={handleInputChangeSupplier}
                  onChange={handleChangeSupplier}
                  placeholder={t('common:filter-by-group-placeholder')}
                  control={control}
                  isClearable={true}
                  disabled={!!initialValues}
                />
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplier?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Region')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="region"
                {...register('region')}
                placeholder={t('Region')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.region?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Address')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="fullAddress"
                {...register('fullAddress')}
                placeholder={t('Address')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.fullAddress?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Telephone')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="telephoneNumber"
                {...register('telephoneNumber')}
                placeholder={t('Telephone')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.telephoneNumber?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Active')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  className="flex w-full font-semibold md:w-1/2"
                  label={t('Yes')}
                  {...register('validYn')}
                  id="validYn_y"
                  value="Y"
                />
                <Radio
                  className="flex w-full font-semibold md:w-1/2"
                  label={t('No')}
                  {...register('validYn')}
                  id="validYn_n"
                  value="N"
                />
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.validYn?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Store Image')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full pt-1 md:w-3/4">
                <input
                  id="file_input"
                  type="file"
                  className={'e_hide-text'}
                  onChange={handleFileChange}
                />
                {uploadingImage && <Loader
                  uploadFile={true}
                  text={t('common:text-loading')} />}
                {storeImageName && <p>Selected file: {storeImageName}</p>}
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.storeImagePath?.message!)}
              </span>
            </div>
          </div>

          {storeImagePath && (
            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2" />
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <div className="w-full py-2 md:w-1/4" />
                <div className="w-full pt-1 md:w-3/4">
                  <img
                    src={getUrlPublicAsset(storeImagePath) ?? emptyPlaceholder}
                    alt={'Store Image'}
                    width={300}
                    height={300}
                  />
                </div>
              </div>
            </div>
          )}
        </Card>
      </div>
      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>
        <Button
          loading={updating || creating}
          className="bg-red-700 hover:bg-red-800"
        >
          {initialValues ? t('Update') : t('Register')}
        </Button>
      </div>
    </form>
  );
}

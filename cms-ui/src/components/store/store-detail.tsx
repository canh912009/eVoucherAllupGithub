import { useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import Radio from '@/components/ui/radio/radio';
import LinkButton from '@/components/ui/link-button';
import Card from '@/components/common/card';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import { Store } from '@/types';
import { Routes } from '@/config/routes';
import { emptyPlaceholder } from '@/utils/placeholders';
import { getUrlPublicAsset, getUrlLocalAsset } from '@/data/download';
import SwitchInput from '@/components/ui/switch-input';
import StoreQRCode from './store-qrcode';
import {PERMISSIONS_EV} from "@/utils/constants";
import {getUserInfo} from "@/utils/auth-utils";

type FormValues = Partial<Store> & {
  storeQR: boolean;
};

type IProps = {
  initialValues?: Store | null;
};

export default function StoreDetail({ initialValues }: IProps) {
  const router = useRouter();

  const { t } = useTranslation();

  const rootClassName =
    'bg-gray-100 ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const {
    control,
    watch,
    formState: { errors },
  } = useForm<FormValues>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
          ...initialValues,
          storeQR: false,
        }
      : {
          storeQR: false,
        },
  });

  const storeQR = watch('storeQR');

  return (
    <div>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('View Chained Store Information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Store name')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="storeName"
                value={initialValues?.storeName}
                placeholder={t('Store name')}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Map code')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="mapCode"
                value={initialValues?.mapCode}
                placeholder={t('Map code')}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Store type')}
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="storeType"
                value={initialValues?.storeType}
                placeholder={t('Store type')}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Brand name')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="brand"
                value={initialValues?.brand?.brandName}
                placeholder={t('Brand')}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Supplier name')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="supplier"
                value={initialValues?.supplier?.supplierName}
                placeholder={t('Supplier')}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Region')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="region"
                value={initialValues?.region}
                placeholder={t('Region')}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Address')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="fullAddress"
                value={initialValues?.fullAddress}
                placeholder={t('Address')}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Telephone')} <span className="text-red-500">*</span>
              </label>
              <input
                disabled={true}
                className={rootClassName}
                type="text"
                id="telephoneNumber"
                value={initialValues?.telephoneNumber}
                placeholder={t('Telephone')}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Active')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  disabled={true}
                  className="flex w-full font-semibold md:w-1/2"
                  label={t('Yes')}
                  name="validYn"
                  checked={initialValues?.validYn === 'Y'}
                  id="validYn_y"
                  value="Y"
                />
                <Radio
                  disabled={true}
                  className="flex w-full font-semibold md:w-1/2"
                  label={t('No')}
                  name="validYn"
                  checked={initialValues?.validYn === 'N'}
                  id="validYn_n"
                  value="N"
                />
              </div>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Store QR')}
              </label>
              <div className="w-full pt-2 md:w-3/4">
                <SwitchInput name="storeQR" control={control} />
              </div>
              {storeQR && (
                <StoreQRCode
                  qrText={initialValues?.id!}
                  storeName={initialValues?.storeName!}
                  storeImgUrl={
                    getUrlPublicAsset(initialValues?.storeImagePath!)!
                  }
                  storeImgUrlLocal={
                    getUrlLocalAsset(initialValues?.storeImagePath!)!
                  }
                />
              )}
            </div>

            {initialValues?.storeImagePath && (
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                  {t('Store Image')} <span className="text-red-500">*</span>
                </label>
                <div className="w-full pt-12 md:w-3/4">
                  <img
                    src={
                      getUrlPublicAsset(initialValues?.storeImagePath) ??
                      emptyPlaceholder
                    }
                    alt={'Store Image'}
                    width={300}
                    height={300}
                  />
                </div>
              </div>
            )}
          </div>
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
        { getUserInfo().roleCode !== PERMISSIONS_EV.ROLE_SUPPLIER && initialValues ? (
          // <Button onClick={() => `${router.asPath}/${initialValues?.id}`} className="bg-red-700 hover:bg-red-800">{t('Edit')}</Button>
          <LinkButton
            href={`${Routes.stores.editWithoutLang(initialValues?.id)}`}
            className="bg-red-700 hover:bg-red-800"
          >
            {t('Edit')}
          </LinkButton>
        ) : (
          ''
        )}
      </div>
    </div>
  );
}

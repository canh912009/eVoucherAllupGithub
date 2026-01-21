import Button from '@/components/ui/button';
import {useModalAction} from '@/components/ui/modal/modal.context';
import Radio from '@/components/ui/radio/radio';
import {
  useCreateCategoryMutation,
  useUpdateCategoryMutation,
} from '@/data/categories';
import {getUrlPublicAsset} from '@/data/download';
import {Category, VNPTEPayProvider} from '@/types';
import {getErrorMessage} from '@/utils/form-error';
import {emptyPlaceholder} from '@/utils/placeholders';
import {yupResolver} from '@hookform/resolvers/yup';
import {useRouter} from 'next/router';
import React, {ChangeEvent} from 'react';
import {useForm} from 'react-hook-form';
import {useTranslation} from 'react-i18next';
import Card from '../common/card';
import {vnptEpayProviderValidationSchema} from "@/components/vnpt-epay/vnpt-epay-provider-validation-schema";
import Checkbox from "@/components/ui/checkbox/checkbox";
import {formatNumber} from "@/utils/common-utils";
import {useUpdateProviderMutation} from "@/data/vnpt-epay";
import {VnptFacesCard, VnptGoodsCard} from "@/components/ui/vnptCard";
import {useCodeGroupQuery} from "@/data/code-group";
import {CODE_GROUP} from "@/utils/constants";
import {useUpdateProviderXpayMutation} from "@/data/xpay";

type IProps = {
  initialValues?: VNPTEPayProvider | null;
  isXpay?: boolean;
};

export default function CreateOrUpdateProviderForm({
                                                     initialValues, isXpay = false
                                                   }: Readonly<IProps>) {
  const router = useRouter();
  const {t} = useTranslation();

  const {
    register,
    handleSubmit,
    control,
    watch,
    setError,
    setValue,
    formState: {errors},
  } = useForm<Partial<VNPTEPayProvider>>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
        ...initialValues,
      }
      : {},
    resolver: yupResolver(vnptEpayProviderValidationSchema),
  });

  const rootClassName =
    'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const allCardFaces = useCodeGroupQuery(isXpay ? CODE_GROUP.XPAY_FACE_VALUES : CODE_GROUP.VNPT_FACE_VALUES)?.codeGroup?.codes
    .map(code => parseInt(code.codeId, 10))
    .sort((a, b) => b - a);
  const allowedCardFaces = JSON.parse(initialValues?.allowedCardFaces ?? "");

  const {mutate: updateProvider, isLoading: updating} = isXpay
    ? useUpdateProviderXpayMutation()
    : useUpdateProviderMutation();

  const onSubmit = async (values: Partial<VNPTEPayProvider>) => {
    const inputValues = {
      providerCd: values.providerCd,
      providerNm: values.providerNm,
      providerType: values.providerType,
      allowedActions: values.allowedActions,
      validYn: values.validYn,
      allowedCardFaces: values.allowedCardFaces,
    };

    try {
      if (!initialValues) {
        // createProvider({
        //   ...inputValues,
        // });
      } else {
        updateProvider({
          ...inputValues,
          id: initialValues.providerCd,
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
                {t('Provider Information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Provider Code')} <span className="text-red-500">*</span>
              </label>
              <input
                className={initialValues ? `${rootClassName} bg-gray-200` : `${rootClassName}  `}
                type="text"
                id="providerCd"
                {...register('providerCd')}
                placeholder={t('Provider Code')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.providerCd?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Provider Name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                id="providerNm"
                {...register('providerNm')}
                placeholder={t('Provider Name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.providerNm?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Provider Type')} <span className="text-red-500">*</span>
              </label>
              <input
                className={initialValues ? `${rootClassName} bg-gray-200` : `${rootClassName}  `}
                type="text"
                id="providerType"
                {...register('providerType')}
                autoComplete="off"
                disabled={initialValues ? true : false}
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.providerType?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Allowed actions')} <span className="text-red-500">*</span>
              </label>
              <input
                className={initialValues ? `${rootClassName} bg-gray-200` : `${rootClassName}  `}
                type="text"
                id="allowedActions"
                {...register('allowedActions')}
                autoComplete="off"
                disabled={initialValues ? true : false}
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.allowedActions?.message!)}
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
          </div>
          <VnptFacesCard allCardFaces={allCardFaces}  allowedCardFaces={allowedCardFaces}/>
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
        <Button
          className="bg-red-700 hover:bg-red-800"
          loading={updating }
        >
          {initialValues ? t('Update') : t('Register')}
        </Button>
      </div>
    </form>
  );
}

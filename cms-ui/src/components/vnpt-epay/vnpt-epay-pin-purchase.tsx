import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import ValidationError from '@/components/ui/form-validation-error';
import Input from '@/components/ui/input';
import Label from '@/components/ui/label';
import SelectInput from '@/components/ui/select-input-autocomplete';
import { vnptEpayProviderValidationSchema } from '@/components/vnpt-epay/vnpt-epay-provider-validation-schema';
import { useBrandsQuery } from '@/data/brands';
import {
  useProvidersVNPTEpayListQuery,
  useCreateVNPTEPayPinPurchaseMutation,
  useGiftListQuery,
} from '@/data/vnpt-epay';
import { Brands, VNPTEPayGift, VNPTEPayPINPurchase } from '@/types';
import { PAGE_SIZE } from '@/utils/constants';
import { getErrorMessage } from '@/utils/form-error';
import useInputTimeout from '@/utils/use-input-timeout';
import { yupResolver } from '@hookform/resolvers/yup';
import cn from 'classnames';
import { useTranslation } from 'next-i18next';
import { useForm } from 'react-hook-form';
import {useEffect} from "react";
import {customerClient} from "@/data/client/crud-client";

type FormValues = Partial<VNPTEPayPINPurchase> & {
  brand: Brands;
  gift: VNPTEPayGift;
};

type SearchProps = {
  className?: string;
  shadow?: boolean;
  variant?: 'normal' | 'solid' | 'outline';
  inputClassName?: string;
};

const VNPTEPayPINPurchaseComponent: React.FC<SearchProps> = ({
  className,
  variant = 'outline',
  shadow = false,
  inputClassName,
  ...rest
}) => {
  const {
    register,
    handleSubmit,
    setValue,
    setError,
    reset,
    watch,
    control,
    formState: { errors },
  } = useForm<FormValues>({
    defaultValues: {},
    resolver: yupResolver(vnptEpayProviderValidationSchema),
  });

  const { t } = useTranslation();

  const brand = watch('brand');

  /**
   * Brand
   */
  const { inputText: brandName, onInputChange: handleInputChangeBrand } =
    useInputTimeout();
  const { providersList, loading: loadingBrands, paginatorInfo, error } = useProvidersVNPTEpayListQuery(
      { page: 1, pageSize: PAGE_SIZE },
      {
        brandTitle: brandName,
      }
  );
  const handleChangeBrand = (option: Brands) => {
    setValue('brand', option);
    // @ts-ignore
    setValue('gift', null);
  };

  /**
   * Gift
   */

  const { inputText: giftTitle, onInputChange: handleInputChangeGift } =
    useInputTimeout();
  const { giftList, loading: loadingGifts } = useGiftListQuery(
    { page: 1, pageSize: PAGE_SIZE},
    {
      brandId: brand?.id,
      giftTitle: giftTitle,
    },
    (typeof brand !== 'undefined' && brand !== null) //  enabled . conditional call API .
  );
  const handleChangeGift = (option: VNPTEPayGift) => {
    setValue('gift', option);
  };

  const { mutate: createVNPTEPayPinPurchase, isLoading: creating } =
    useCreateVNPTEPayPinPurchaseMutation();

  const onSubmit = async (values: FormValues) => {
    let inputValues = {
      brandId: values.brand?.id,
      giftId: values.gift?.id,
      quantity: values.quantity,
    };

    // console.log('onSubmit inputValues', inputValues);
    // return;

    try {
      createVNPTEPayPinPurchase({ ...inputValues });
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
    <form
      noValidate
      role="search"
      className={cn('relative w-full items-center', className)}
      onSubmit={handleSubmit(onSubmit)}
    >
      <Card className="mb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:!p-4 md:!pt-6">
        <div className="mb-4 flex w-full flex-wrap">
          <div className="w-full px-4 md:w-[20%]">
            <h3 className="pt-6 text-xl font-semibold text-heading">
              {t('VNPT PIN Purchase')}
            </h3>
          </div>
          <div className="w-full px-4 md:w-[20%] ">
            <Label>{t('Brand')}</Label>
            <SelectInput
              name="brand"
              options={providersList}
              isLoading={loadingBrands}
              getOptionLabel={(option: any) =>
                option.brandTitle + ' - ' + option.id
              }
              getOptionValue={(option: any) => option.id}
              onInputChange={handleInputChangeBrand}
              onChange={handleChangeBrand}
              placeholder={t('common:filter-by-group-placeholder')}
              control={control}
              isClearable={true}
            />
            <ValidationError message={t(errors.brand?.message)} />
          </div>
          <div className="w-full px-4 md:w-[20%] ">
            <Label>{t('Gift')}</Label>
            <SelectInput
              name="gift"
              options={giftList}
              disabled={typeof brand === 'undefined' || typeof brand === null}
              isLoading={loadingGifts}
              getOptionLabel={(option: any) =>
                option.giftTitle + ' - ' + option.id
              }
              getOptionValue={(option: any) => option.id}
              onInputChange={handleInputChangeGift}
              onChange={handleChangeGift}
              placeholder={t('common:filter-by-group-placeholder')}
              control={control}
              isClearable={true}
            />
            <ValidationError message={t(errors.gift?.message)} />
          </div>
          <Input
            label={t('Purchase Quantity')}
            {...register('quantity')}
            type="number"
            step="1"
            error={t(errors.quantity?.message!)}
            variant="outline"
            className="w-full px-4 md:w-[20%]"
          />
          <div className="w-full px-4 md:w-[20%]">
            <div className="pt-6">
              <Button
                className="bg-green-700 hover:bg-green-800"
                loading={creating}
              >
                {t('Purchase New PIN')}
              </Button>
            </div>
          </div>
        </div>
      </Card>
    </form>
  );
};

export default VNPTEPayPINPurchaseComponent;

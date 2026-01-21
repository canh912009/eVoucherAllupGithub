
import { useTranslation } from "next-i18next";
import { Category, Good, Store, SupplierContract } from "@/types";
import { Controller, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { goodValidationSchema } from "./good-validation-schema";
import SelectInput from '@/components/ui/select-input-autocomplete';


type IProps = {
  initialValues?: Good | null;
};


type FormValues = Partial<Good> & {
  id: string;
  supplierContract: SupplierContract;
  supplierId: string;
  brandId: string;
  goodsType: string;
  settlementMethodCode: string;
  errorMessage: string;
  image: any;
};

export default function GoodInformationInput({ initialValues }: IProps) {
  const { t } = useTranslation();

  const rootClassName =
  'ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  let classes = {
    // title: 'font-semibold',
    // content: 'font-normal text-[#212121]',
    wrapper:
      'flex flex-wrap pb-2 my-5 border-b border-dashed border-border-base sm:my-2 ',
    side_left: 'w-full px-0 pb-5 sm:w-1/4 sm:py-8 sm:pe-4 md:w-1/4 md:pe-5',
    side_right: 'w-full sm:w-3/4 md:w-3/4',
    row: 'mb-1 flex flex-wrap',
    title: 'w-full md:w-1/4 px-4 py-2 text-heading',
    content: 'w-full md:w-3/4 px-4 py-2',
  };
  const {
    register,
    handleSubmit,
    control,
    setError,
    setValue,
    watch,
    formState: { errors },
  } = useForm<FormValues>({
    resolver: yupResolver(goodValidationSchema),
    ...(initialValues && {
      defaultValues: {
        ...initialValues,
        startDate: initialValues.startDate
          ? new Date(initialValues.startDate!)
          : '',
        endDate: initialValues.endDate
          ? new Date(initialValues.endDate!)
          : '',
        periodExpireDate: initialValues.periodExpireDate
          ? new Date(initialValues.periodExpireDate!)
          : '',
      } as any,
    }),
  });
  return (
    <>
      {/* Product Name + Supplier Name */}
      <div className="mb-5 flex flex-wrap">
            {/* Product Name */}
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Product name ')}<span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={initialValues ? true : false}
                id="goodsName"
                {...register('goodsName')}
                placeholder={initialValues?.goodsName}
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.goodsName?.message!)}
              </span>
            </div>

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Supplier Name ')}<span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <SelectInput
                  name="supplier"
                  options={suppliers}
                  isLoading={loading}
                  disabled={initialValues?.supplierContractId ? true : false}
                  getOptionLabel={(option: any) =>
                    option.supplierName + ' - ' + option.id
                  }
                  getOptionValue={(option: any) => option.id}
                  onInputChange={handleInputChangeSupplier}
                  onChange={handleChangeSupplier}
                  placeholder={t('common:filter-by-group-placeholder')}
                  control={control}
                  isClearable={true}
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {t(errors.supplier?.message!)}
                </span>
              </div>
            </div>
          </div>
    </>
  );
}
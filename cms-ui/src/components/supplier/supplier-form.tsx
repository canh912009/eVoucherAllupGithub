import { useForm } from 'react-hook-form';
import { useRouter } from 'next/router';
import Badge from '@/components/ui/badge/badge';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import { supplierValidationSchema } from '@/components/supplier/supplier-validation-schema';
import {Code, CommonApproveStatusAction, Customer, Supplier} from '@/types';
import {
  useCreateSupplierMutation,
  useUpdateSupplierMutation,
} from '@/data/supplier';
import { getErrorMessage } from '@/utils/form-error';
import {useEffect, useState} from 'react';
import {tr} from "date-fns/locale";
import {useCodeGroupQuery} from "@/data/code-group";
import {CODE_GROUP, PERMISSIONS_EV} from "@/utils/constants";
import {useCodeQuery} from "@/data/code";
import SelectInput from "@/components/ui/select-input";
import ValidationError from "@/components/ui/form-validation-error";
import Radio from "@/components/ui/radio/radio";
import {Routes} from "@/config/routes";
import ButtonsCreateEditPage from "@/components/common/button-create-edit-page";

type IProps = {
  initialValues?: Supplier | null;
  viewPage?: boolean;
};
type FormValues = Partial<Supplier> & {
  code_settlement: Code;
};

export default function CreateOrUpdateSupplierForm({ initialValues, viewPage = false }: IProps) {
  const router = useRouter();
  const { t } = useTranslation();
  const { codeGroup, loading: codeLoading } = useCodeGroupQuery(
    CODE_GROUP.SETTLEMENT_METHOD_CD
  );

  /**
   * Event onClick button
   */
  const [actionType, setActionType] = useState(1); //1: REQ, 0: Null (Draft)
  const handleDraftClick = () => {
    setActionType(0);
  };
  const handleRegisterClick = () => {
    setActionType(1);
  };
  const {
    register,
    handleSubmit,
    setValue,
    control,
    watch,
    setError,
    formState: { errors },
  } = useForm<FormValues>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
        ...initialValues,
        code_settlement: codeGroup?.codes.length
          ? codeGroup?.codes?.find(
            (code) => initialValues?.settlementMethodCode === code.codeId
          )
          : '',
      }
      : {},
    resolver: yupResolver(supplierValidationSchema),
  });

  const handleChangeMethodCode = (option: any) => {
    setValue('code_settlement', option);
  };
  // (Page Update) find method
  let methodCodeInit: any = null;
  if (initialValues?.settlementMethodCode) {
    const queryCodeResult = useCodeQuery(
      CODE_GROUP.SETTLEMENT_METHOD_CD,
      initialValues.settlementMethodCode as string
    );
    methodCodeInit = queryCodeResult.code;
  }
  // Set the initial value of 'method_code'
  useEffect(() => {
    if (methodCodeInit) {
      setValue('code_settlement', methodCodeInit);
    }
  }, [methodCodeInit, setValue]);

  const rootClassName = viewPage
    ? 'bg-gray-300 ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent'
    : 'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent'
  ;
  const { mutate: createSupplier, isLoading: creating } =
    useCreateSupplierMutation();
  const { mutate: updateSupplier, isLoading: updating } =
    useUpdateSupplierMutation();

  const onSubmit = async (values: FormValues) => {
    const inputValues = {
      approveStatusCode: values.approveStatusCode,
      taxcode: values.taxcode,
      id: values.id,
      supplierName: values.supplierName,
      bankName: values.bankName,
      accountNumber: values.accountNumber,
      accountName: values.accountName,
      supplyDiscountRate: values.supplyDiscountRate,
      supplyCommissionRate: values.supplyCommissionRate,
      vatIncludeYn: values.vatIncludeYn,
      settlementMethodCode: values.code_settlement?.codeId,
      managerName: values.managerName,
      managerEmail: values.managerEmail,
      managerMobileNumber: values.managerMobileNumber,
      primaryContactName: values.primaryContactName,
      primaryContactEmail: values.primaryContactEmail,
      primaryContactMobile: values.primaryContactMobile,
    };

    // console.log("inputValues", inputValues)
    // return

    try {
      switch (actionType) {
        case 0: //DraftClick
          initialValues
            ? updateSupplier({
                ...inputValues,
                id: initialValues.id, })
            : createSupplier({
                ...inputValues, });
          break;
        case 1: //RegisterClick
          initialValues
            ? updateSupplier({
                ...inputValues,
                id: initialValues.id,
                approveStatusCode: CommonApproveStatusAction.REQ, })
            : createSupplier({
                ...inputValues,
                approveStatusCode: CommonApproveStatusAction.REQ,});
          break;
        default:
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
  // @ts-ignore
  const handleKeyPress = (event) => {
    // Prevent form submission on Enter key press (key code 13)
    if (event.key === 'Enter') {
      event.preventDefault();
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="w-full sm:w-full md:w-full">
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 py-2">
            <h1 className="text-base uppercase font-semibold ">
              {t('Supplier Information')}
            </h1>
          </div>
        </div>

        {initialValues && (
          <div className="flex flex-wrap">
            <div className="mb-5 px-4 sm:w-full md:w-1/2">
              {t('Status ')}
              <>
                {initialValues?.approveStatusCode === 'REQ' && (
                  <Badge text="Requesting" color="bg-yellow-600" />
                )}
                {initialValues?.approveStatusCode === 'APPRV' && (
                  <Badge text="Approved" color="bg-accent" />
                )}
                {initialValues?.approveStatusCode === 'REJCT' && (
                  <Badge text="Rejected" color="bg-red-800" />
                )}
              </>
            </div>
          </div>
        )}

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Supplier Name: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="supplierName"
              {...register('supplierName')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplierName?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Bank Name: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="bankName"
              {...register('bankName')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.bankName?.message!)}
            </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Tax code: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={`${rootClassName} `}
              type="text"
              onKeyPress={handleKeyPress}
              id="taxcode"
              {...register('taxcode')}
              placeholder={t('Taxcode')}
              autoComplete="off"
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.taxcode?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Bank account name: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="accountName"
              {...register('accountName')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.accountName?.message!)}
            </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Supplier ID: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={initialValues ? `${rootClassName} bg-gray-300` : rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="id"
              disabled={!!initialValues}
              {...register('id')}
              placeholder={t('Type here')}
              autoComplete="off"
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.id?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Bank account number: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="accountNumber"
              {...register('accountNumber')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.accountNumber?.message!)}
            </span>
          </div>
          {/*<div className="flex w-full flex-wrap px-4 md:w-1/2">*/}
          {/*  <label className="w-full py-2 font-semibold text-heading md:w-1/4">*/}
          {/*    {t('Supply comission rate')}*/}
          {/*  </label>*/}
          {/*  <input*/}
          {/*    className={rootClassName}*/}
          {/*    type="number"*/}
          {/*    step="any"*/}
          {/*    id="supplyCommissionRate"*/}
          {/*    {...register('supplyCommissionRate')}*/}
          {/*    placeholder={t('Supply comission rate')}*/}
          {/*    autoComplete="off"*/}
          {/*  />*/}
          {/*  <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">*/}
          {/*      {t(errors.supplyCommissionRate?.message!)}*/}
          {/*    </span>*/}
          {/*</div>*/}
        </div>

        <div className=" flex flex-wrap border-b border-dashed border-border-base pb-8" />
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 py-2">
            <h1 className="text-base uppercase font-semibold ">
              {t('Contract Information')}
            </h1>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Settlement method : ')}<span className="text-red-500">*</span>
            </label>
            <div className="w-full md:w-3/4 ">
              <SelectInput
                name="code_settlement"
                control={control}
                getOptionLabel={(option: any) => option?.codeName}
                getOptionValue={(option: any) => option?.codeId}
                onChange={handleChangeMethodCode}
                options={codeGroup?.codes ?? []}
                isLoading={codeLoading}
                disabled={viewPage}
              />
              <ValidationError message={t(errors.code_settlement?.message)} />
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Manager name : ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="managerName"
              {...register('managerName')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.managerName?.message!)}
            </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Discount rate: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="number"
              step="0.01"
              onKeyPress={handleKeyPress}
              id="supplyDiscountRate"
              {...register('supplyDiscountRate')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplyDiscountRate?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Manager email: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="managerEmail"
              {...register('managerEmail')}
              placeholder={t('Manager email')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.managerEmail?.message!)}
              </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Commission rate: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="number"
              step="0.01"
              onKeyPress={handleKeyPress}
              id="supplyCommissionRate"
              {...register('supplyCommissionRate')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplyCommissionRate?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Manager phone number: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="managerMobileNumber"
              {...register('managerMobileNumber')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.managerMobileNumber?.message!)}
            </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full text-heading md:w-1/4">
              {t('Including VAT: ')}<span className="text-red-500">*</span>
            </label>
            <div className="flex w-full md:w-3/4">
              <Radio
                disabled={viewPage}
                className="flex w-full font-semibold md:w-1/2"
                label={t('Yes')}
                {...register('vatIncludeYn')}
                id="vat_y"
                value="Y"
              />
              <Radio
                disabled={viewPage}
                className="flex w-full font-semibold md:w-1/2"
                label={t('No')}
                {...register('vatIncludeYn')}
                id="vat_n"
                value="N"
              />
            </div>
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.vatIncludeYn?.message!)}
            </span>
          </div>
        </div>

        <div className="flex flex-wrap border-b border-dashed border-border-base pb-8" />
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 py-2">
            <h1 className="text-base uppercase font-semibold ">
              {t('Primary Contract Information')}
            </h1>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Primary contact name: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="primaryContactName"
              {...register('primaryContactName')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.primaryContactName?.message!)}
              </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Primary contact email: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="primaryContactEmail"
              {...register('primaryContactEmail')}
              placeholder={t('Primary contact email')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.primaryContactEmail?.message!)}
              </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Primary Contact Phone number: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              onKeyPress={handleKeyPress}
              id="primaryContactMobile"
              {...register('primaryContactMobile')}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.primaryContactMobile?.message!)}
              </span>
          </div>
        </div>
      </div>

      <ButtonsCreateEditPage linkBackButton={Routes?.suppliers.list} initialValues={initialValues}
                             updating={updating} creating={creating}
                             handleDraftClick={handleDraftClick} handleRegisterClick={handleRegisterClick} />
    </form>
  );
}

import { useForm } from 'react-hook-form';
import SelectInput from '@/components/ui/select-input';
import ValidationError from '@/components/ui/form-validation-error';
import Router, { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import { customerValidationSchema } from '@/components/customer/customer-validation-schema';
import { Customer, Code } from '@/types';
import {
  useCreateCustomerMutation,
  useUpdateCustomerMutation,
} from '@/data/customer';
import { useCodeGroupQuery } from '@/data/code-group';
import { getErrorMessage } from '@/utils/form-error';
import {CODE_GROUP, PERMISSIONS_EV} from '@/utils/constants';
import ApproveStatusCodeBadge from '../common/approve-status-code-badge';
import React, {useEffect} from "react";
import {useCodeQuery} from "@/data/code";
import Radio from "@/components/ui/radio/radio";
import {Routes} from "@/config/routes";
import {getAuthCredentials} from "@/utils/auth-utils";
import ButtonsDetailPage from "@/components/common/button-detail-page";
import Button from "@/components/ui/button";

type FormValues = Partial<Customer> & {
  code_settlement: Code;
};

type IProps = {
  initialValues?: Customer | null;
  viewPage?: boolean;
};

export default function CustomerFormDetail({ initialValues, viewPage = true }: IProps) {
  const rootClassName = viewPage
    ? 'bg-gray-300 ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent'
    : 'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent'
    ;

  const router = useRouter();
  const { t } = useTranslation();
  const { codeGroup, loading: codeLoading } = useCodeGroupQuery(
    CODE_GROUP.SETTLEMENT_METHOD_CD
  );

  const {
    register,
    handleSubmit,
    control,
    setValue,
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
    resolver: yupResolver(customerValidationSchema),
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

  const { mutate: createCustomer, isLoading: creating } =
    useCreateCustomerMutation();
  const { mutate: updateCustomer, isLoading: updating } =
    useUpdateCustomerMutation();

  const onSubmit = async (values: FormValues) => {
    const inputValues = {
      taxcode: values.taxcode,
      customerName: values.customerName,
      bankName: values.bankName,
      accountName: values.accountName,
      accountNumber: values.accountNumber,
      sellDiscountRate: values.sellDiscountRate,
      sellCommissionRate: values.sellCommissionRate,
      vatIncludeYn: values.vatIncludeYn,
      settlementMethodCode: values.code_settlement?.codeId,
      sendCost: values.sendCost,
      managerName: values.managerName,
      managerEmail: values.managerEmail,
      managerMobileNo: values.managerMobileNo,
      primaryContactName: values.primaryContactName,
      primaryContactEmail: values.primaryContactEmail,
      primaryContactMobileNo: values.primaryContactMobileNo,
    };
    // try {
    //   if (!initialValues) {
    //     createCustomer({
    //       ...inputValues,
    //     });
    //   } else {
    //     viewPage ? await router.push(`${initialValues.id}/edit`) : updateCustomer({
    //       ...inputValues,
    //       id: initialValues.id,
    //     });
    //   }
    // } catch (error) {
    //   const serverErrors = getErrorMessage(error);
    //   Object.keys(serverErrors?.validation).forEach((field: any) => {
    //     setError(field.split('.')[1], {
    //       type: 'manual',
    //       message: serverErrors?.validation[field][0],
    //     });
    //   });
    // }
  };

  const requestingStatus = !(initialValues?.approveStatusCode === 'APPRV' || initialValues?.approveStatusCode === 'REJCT')
  const { token, permissions } = getAuthCredentials();

  // @ts-ignore
  return (
    <div >
      <div className="w-full sm:w-full md:w-full ">
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 py-2">
            <h1 className="text-base uppercase font-semibold ">
              {t('Customer Information')}
            </h1>
          </div>
        </div>

        {initialValues && (
          <div className="flex flex-wrap">
            <div className="mb-5 px-4 sm:w-full md:w-1/2">
              {t('Status')}:{' '}
              <>
                <ApproveStatusCodeBadge
                  approveStatusCode={initialValues?.approveStatusCode}
                />
              </>
            </div>
          </div>
        )}

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Customer name: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              id="customerName"
              value={initialValues?.customerName}
              placeholder={t('Type here')}
              disabled={viewPage}
              autoComplete="off"
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.customerName?.message!)}
              </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Bank name: ')}<span className="text-[#FF0000]">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              id="bankName"
              value={initialValues?.bankName}
              placeholder={t('Type here')}
              disabled={viewPage}
              autoComplete="off"
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.bankName?.message!)}
              </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Taxcode: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              disabled={initialValues ? true : false}
              id="taxcode"
              value={initialValues?.taxcode}
              placeholder={t('Type here')}
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
              id="accountName"
              value={initialValues?.accountName}
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
              {t('Customer ID: ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              disabled={!!initialValues}
              id="id"
              value={initialValues?.id}
              autoComplete="off"
            />
          </div>
        </div>
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
              {t('Type : ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              id="customerType"
              value={initialValues?.customerTypeCode}
              autoComplete="off"
              disabled={viewPage}
            />
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Bank account number : ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              id="accountNumber"
              value={initialValues?.accountNumber}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.accountNumber?.message!)}
            </span>
          </div>
        </div>
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Representative Email : ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              value={initialValues?.representativeMail}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.representativeMail?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Representative Number : ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              value={initialValues?.representativeMobile}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.representativeMobile?.message!)}
            </span>
          </div>
        </div>

        <div className=" flex flex-wrap border-b border-dashed border-border-base pb-8" />

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 py-2">
            <h1 className="text-base uppercase font-semibold ">
              {t('CUSTOMER MANAGER INFORMATION')}
            </h1>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Manager name : ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              id="managerName"
              value={initialValues?.managerName}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.managerName?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Manager email : ')}<span className="text-red-500">*</span>
            </label>
            <input
              className={rootClassName}
              type="text"
              id="managerEmail"
              value={initialValues?.managerEmail}
              placeholder={t('Type here')}
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
              {t('Manager number')}
            </label>
            <input
              className={rootClassName}
              type="text"
              id="managerMobileNo"
              value={initialValues?.managerMobileNo}
              placeholder={t('Type here')}
              autoComplete="off"
              disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.managerMobileNo?.message!)}
            </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap border-b border-dashed border-border-base pb-8" />
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4">
            <h1 className="text-base uppercase font-semibold ">
              {t('AQUA MANAGER INFORMATION')}
            </h1>
          </div>
        </div>
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Aqua Manager Id : ')}<span className="text-red-500">*</span>
            </label>
            <div className="flex w-full md:w-3/4">
              <input
                disabled
                className={`${rootClassName} bg-gray-200 `}
                type="text"
                value={initialValues?.admin?.id}
              />
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Aqua Manager Number : ')}<span className="text-red-500">*</span>
            </label>
            <input
              disabled
              className={`${rootClassName} bg-gray-200`}
              type="text"
              value={initialValues?.admin?.mobileNumber}
            />
          </div>
        </div>
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Aqua Manager Name : ')}<span className="text-red-500">*</span>
            </label>
            <input
              disabled
              className={`${rootClassName} bg-gray-200`}
              type="text"
              value={initialValues?.admin?.adminName}
            />
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Aqua Manager Email : ')}<span className="text-red-500">*</span>
            </label>
            <input
              disabled
              className={`${rootClassName} bg-gray-200`}
              type="text"
              value={initialValues?.admin?.email}
            />
          </div>
        </div>
      </div>
      <ButtonsDetailPage
        backLink={Routes?.customers.list}
        editLink={Routes.customers.editWithoutLang(initialValues?.id as string)}
        initialValues={initialValues}
        rejectModelView={'DISAPPROVE_CUSTOMER'}
        approveModelView={'APPROVE_CUSTOMER'}
      />
    </div>
  );
}

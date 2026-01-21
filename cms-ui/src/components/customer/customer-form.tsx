import { useForm } from 'react-hook-form';
import SelectInput from '@/components/ui/select-input';
import ValidationError from '@/components/ui/form-validation-error';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import { customerValidationSchema } from '@/components/customer/customer-validation-schema';
import {Customer, Code, CommonApproveStatusAction} from '@/types';
import {
  useCreateCustomerMutation,
  useUpdateCustomerMutation,
} from '@/data/customer';
import { useCodeGroupQuery } from '@/data/code-group';
import { getErrorMessage } from '@/utils/form-error';
import {CODE_GROUP} from '@/utils/constants';
import ApproveStatusCodeBadge from '../common/approve-status-code-badge';
import React, {useEffect, useState} from "react";
import {useCodeQuery} from "@/data/code";
import Radio from "@/components/ui/radio/radio";
import {Routes} from "@/config/routes";
import ButtonsCreateEditPage from "@/components/common/button-create-edit-page";
import Button from "@/components/ui/button";
import {useModalAction} from "@/components/ui/modal/modal.context";

type FormValues = Partial<Customer> & {
  customerType: Code;
  adminID : string;
  adminName : string;
  adminEmail : string;
  adminNumber : string;
};

type IProps = {
  initialValues?: Customer | null;
  viewPage?: boolean;
};

export default function CreateOrUpdateCustomerForm({ initialValues, viewPage = false }: IProps) {
  const rootClassName = viewPage
    ? 'bg-gray-300 ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent'
    : 'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent'
    ;

  const { t } = useTranslation();
  const { codeGroup, loading: codeLoading } = useCodeGroupQuery(
    CODE_GROUP.SETTLEMENT_METHOD_CD
  );

  const { codeGroup: customerType, loading: customerTypeLoading } = useCodeGroupQuery(
    CODE_GROUP.CUSTOMER_TYPE
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
    control,
    setValue,
    setError,
    formState: { errors },
  } = useForm<FormValues>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
          ...initialValues,
        }
      : {},
    resolver: yupResolver(customerValidationSchema),
  });
  const handleChangeCustomerType = (option: any) => {
    setValue('customerType', option);
  };

  let customerTypeInit: any = null;
  if (initialValues?.customerTypeCode) {
    const customerTypeResult = useCodeQuery(
      CODE_GROUP.CUSTOMER_TYPE,
      initialValues.customerTypeCode as string
    );
    customerTypeInit = customerTypeResult.code;
  }

  useEffect(() => {
    if (customerTypeInit) {
      setValue('customerType', customerTypeInit);
    }
  }, [customerTypeInit, setValue]);

  useEffect(() => {
    setValue('adminID', initialValues?.admin?.id);
    setValue('adminNumber', initialValues?.admin?.mobileNumber);
    setValue('adminName', initialValues?.admin?.adminName);
    setValue('adminEmail',  initialValues?.admin?.email);
  }, [initialValues]);

  const { mutate: createCustomer, isLoading: creating } =
    useCreateCustomerMutation();
  const { mutate: updateCustomer, isLoading: updating } =
    useUpdateCustomerMutation();

  const { openModal, closeModal } = useModalAction();
  function handleSelectShowPopup(modalView: any) {
    if (modalView && modalView === "SELECT_CUSTOMER_AMIN") {
      openModal(modalView, { handleSelectAdmin });
    }
  }
  function handleSelectAdmin(items: any[] ) {
    setValue('adminID', items[0].id);
    setValue('adminNumber', items[0].mobileNumber);
    setValue('adminName', items[0].adminName);
    setValue('adminEmail',  items[0].email);
    closeModal();
  }

  const onSubmit = async (values: FormValues) => {
    // console.log('onSubmit values', values);
    // return;
    const inputValues = {
      taxcode: values.taxcode,
      id: values.id,
      customerName: values.customerName,
      customerTypeCode: values.customerType?.codeId,
      admin : {id : values.adminID} ,
      bankName: values.bankName,
      accountName: values.accountName,
      accountNumber: values.accountNumber,
      representativeMail: values.representativeMail,
      representativeMobile: values.representativeMobile,
      sendCost: values.sendCost,
      managerName: values.managerName,
      managerEmail: values.managerEmail,
      managerMobileNo: values.managerMobileNo,
    };
    try {
      switch (actionType) {
        case 0: //DraftClick
          initialValues
            ? updateCustomer({
                ...inputValues,
                id: initialValues.id, })
            : createCustomer({
                ...inputValues, });
          break;
        case 1: //RegisterClick
          initialValues
            ? updateCustomer({
                ...inputValues,
                id: initialValues.id,
                approveStatusCode: CommonApproveStatusAction.REQ, })
            : createCustomer({
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
                onKeyPress={handleKeyPress}
                id="customerName"
                {...register('customerName')}
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
                onKeyPress={handleKeyPress}
                id="bankName"
                {...register('bankName')}
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
              {t('Tax code: ')}<span className="text-red-500">*</span>
            </label>
            <input
                className={rootClassName}
                type="text"
                onKeyPress={handleKeyPress}
                id="taxcode"
                {...register('taxcode')}
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
              {t('Customer ID: ')}<span className="text-red-500">*</span>
            </label>
            <input
                className={initialValues ? `${rootClassName} bg-gray-200` : rootClassName}
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
        </div>
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Type: ')}<span className="text-red-500">*</span>
            </label>
            <div className="w-full md:w-3/4 ">
              <SelectInput
                  name="customerType"
                  control={control}
                  getOptionLabel={(option: any) => option?.codeName}
                  getOptionValue={(option: any) => option?.codeId}
                  onChange={handleChangeCustomerType}
                  options={customerType?.codes ?? []}
                  isLoading={customerTypeLoading}
                  disabled={viewPage}
              />
              <ValidationError message={t(errors.customerType?.message)}/>
            </div>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Bank account number : ')}<span className="text-red-500">*</span>
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
        </div>
        <div className="mb-5 flex flex-wrap">
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Representative Email : ')}<span className="text-red-500">*</span>
            </label>
            <input
                className={rootClassName}
                type="text"
                onKeyPress={handleKeyPress}
                id="representativeMail"
                {...register('representativeMail')}
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
                onKeyPress={handleKeyPress}
                id="representativeMobile"
                {...register('representativeMobile')}
                placeholder={t('Type here')}
                autoComplete="off"
                disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.representativeMobile?.message!)}
            </span>
          </div>
        </div>

        <div className=" flex flex-wrap border-b border-dashed border-border-base pb-8"/>

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
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Manager email : ')}<span className="text-red-500">*</span>
            </label>
            <input
                className={rootClassName}
                type="text"
                onKeyPress={handleKeyPress}
                id="managerEmail"
                {...register('managerEmail')}
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
                onKeyPress={handleKeyPress}
                id="managerMobileNo"
                {...register('managerMobileNo')}
                placeholder={t('Type here')}
                autoComplete="off"
                disabled={viewPage}
            />
            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.managerMobileNo?.message!)}
            </span>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap border-b border-dashed border-border-base pb-8"/>
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
                  className={`${rootClassName} bg-gray-200 mr-10`}
                  type="text"
                  onKeyPress={handleKeyPress}
                  id="adminID"
                  {...register('adminID')}
              />
              <Button
                  className="bg-red-600 hover:bg-red-700 rounded-xl mx-2"
                  onClick={(event) => {
                    event.preventDefault();
                    return handleSelectShowPopup('SELECT_CUSTOMER_AMIN')
                  }}
              >
                {t('Select')}
              </Button>
            </div>

            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
              {t(errors.adminID?.message!)}
            </span>
          </div>
          <div className="flex w-full flex-wrap px-4 md:w-1/2">
            <label className="w-full py-2 text-heading md:w-1/4">
              {t('Aqua Manager Number : ')}<span className="text-red-500">*</span>
            </label>
            <input
                disabled
                className={`${rootClassName} bg-gray-200`}
                type="text"
                onKeyPress={handleKeyPress}
                id="adminNumber"
                {...register('adminNumber')}
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
                onKeyPress={handleKeyPress}
                id="adminName"
                {...register('adminName')}
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
                onKeyPress={handleKeyPress}
                id="adminEmail"
                {...register('adminEmail')}
            />
          </div>
        </div>
      </div>
      <ButtonsCreateEditPage linkBackButton={Routes?.customers.list} initialValues={initialValues}
                             updating={updating} creating={creating}
                             handleDraftClick={handleDraftClick} handleRegisterClick={handleRegisterClick}/>

    </form>
  );
}

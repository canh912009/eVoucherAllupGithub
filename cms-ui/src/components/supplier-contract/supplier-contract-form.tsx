import { Controller, useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import LinkButton from '@/components/ui/link-button';
import { DatePicker } from '@/components/ui/date-picker';
import ValidationError from '@/components/ui/form-validation-error';
import Radio from '@/components/ui/radio/radio';
import Card from '@/components/common/card';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import { supplierContractValidationSchema } from '@/components/supplier-contract/supplier-contract-validation-schema';
import {
  Code,
  CommonApproveStatusAction,
  Supplier,
  SupplierContract,
} from '@/types';
import {
  useCreateSupplierContractMutation,
  useUpdateSupplierContractMutation,
} from '@/data/supplier-contract';
import { useUploadDocumentMutation } from '@/data/upload';
import { getErrorMessage } from '@/utils/form-error';
import { PAGE_SIZE, CODE_GROUP } from '@/utils/constants';
import { useSuppliersQuery } from '@/data/supplier';
import SelectInput from '@/components/ui/select-input-autocomplete';
import { ChangeEvent, useEffect, useState } from 'react';
import useInputTimeout from '@/utils/use-input-timeout';
import { useCodeGroupQuery } from '@/data/code-group';
import ApproveStatusCodeBadge from '../common/approve-status-code-badge';
import { formatDate } from '@/utils/common-utils';
import { useModalAction } from '@/components/ui/modal/modal.context';
import { Routes } from '@/config/routes';
import {toast} from "react-toastify";

type FormValues = Partial<SupplierContract> & {
  supplier: Supplier;
  code_settlement: Code;
};

type IProps = {
  initialValues?: SupplierContract | null;
};

export default function CreateOrUpdateSupplierContractForm({
  initialValues,
}: IProps) {
  const rootClassName =
    'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const { t } = useTranslation();
  const { openModal } = useModalAction();

  /**
   * Codes, Code Groups
   */
  const { codeGroup, loading: codeLoading } = useCodeGroupQuery(
    CODE_GROUP.SETTLEMENT_METHOD_CD
  );

  /**
   * Supplier
   */
  const { inputText: supplierName, onInputChange: handleInputChangeSupplier } =
    useInputTimeout();

  // Just search suppliers approved
  const { suppliers, loading } = useSuppliersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      supplierName: supplierName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );

  const handleChangeSupplier = (option: any) => {
    setValue('supplier', option);
    setValue('supplierId', option?.id);
  };

  const handleChangeMethodCode = (option: any) => {
    setValue('code_settlement', option);
  };

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
    resolver: yupResolver(supplierContractValidationSchema),
    ...(initialValues && {
      defaultValues: {
        ...initialValues,
        startDate: initialValues.startDate
          ? new Date(initialValues.startDate!)
          : '',
        endDate: initialValues.endDate ? new Date(initialValues.endDate!) : '',
        supplierId: initialValues.supplier?.id,
      } as any,
    }),
  });

  const { mutate: createSupplierContract, isLoading: creating } =
    useCreateSupplierContractMutation();
  const { mutate: updateSupplierContract, isLoading: updating } =
    useUpdateSupplierContractMutation();

  const [contractFilePath, setContractFilePath] = useState(
    initialValues?.contractFilePath
  );
  const { mutate: uploadDocument } = useUploadDocumentMutation();

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    setContractFilePath('');

    if (e.target.files) {
      const file = e.target.files[0];
      // console.log('file', file);
      if (file) {
        uploadDocument(file, {
          onSuccess: (data: any) => {
            // console.log('data', data);
            setContractFilePath(data?.path);
            setValue('contractFileName', file.name);
          },
          onError: (error: any) => {
            toast.error('Error:' + error?.response?.data.message);
          },
        });
      }
    }
  };

  function close(approveModalView: any, id: string) {
    openModal(approveModalView, id);
  }

  const onSubmit = async (values: FormValues) => {
    // console.log('values', values);
    // return;
    const inputValues = {
      contractName: values.contractName,
      supplierId: values.supplierId,
      startDate: values.startDate ? formatDate(values.startDate) : '',
      endDate: values.endDate ? formatDate(values.endDate) : '',
      supplySettlementMethodCode: values.code_settlement?.codeId,
      supplyDiscountRate: values.supplyDiscountRate,
      supplyDiscountAmount: values.supplyDiscountAmount,
      supplyCommissionRate: values.supplyCommissionRate,
      supplyVatIncludeYn: values.supplyVatIncludeYn,
      contractFilePath: contractFilePath,
      contractFileName: values.contractFileName,
    };
    // console.log('inputValues', inputValues);
    // return;
    try {
      if (!initialValues) {
        // console.log('inputValues', inputValues);
        if (actionType === 0) {
          // Save Draff
          createSupplierContract({
            ...inputValues,
          });
        } else if (actionType === 1) {
          // Save Register
          createSupplierContract({
            ...inputValues,
            approveStatusCode: CommonApproveStatusAction.REQ,
          });
        }
      } else {
        if (actionType === 0) {
          // Save (no set approveStatusCode)
          updateSupplierContract({
            ...inputValues,
            id: initialValues.id,
          });
        } else if (actionType === 1) {
          // Save and Register
          updateSupplierContract({
            ...inputValues,
            id: initialValues.id,
            approveStatusCode: CommonApproveStatusAction.REQ,
          });
        }
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

  // This effect will run whenever get 'codeGroup' success
  useEffect(() => {
    // console.log('codeGroup', codeGroup);
    if (
      initialValues?.supplySettlementMethodCode &&
      codeGroup &&
      codeGroup.codes
    ) {
      let codeSettelement = codeGroup.codes.find(
        (code) => code.codeId === initialValues.supplySettlementMethodCode
      );
      if (codeSettelement) {
        setValue('code_settlement', codeSettelement);
      }
    }
  }, [initialValues, setValue, codeGroup]);

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Supplier Contract Information')}
              </h1>
            </div>
          </div>

          {initialValues && (
            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Status')} <span className="text-red-500">*</span>
                </label>
                {
                  <ApproveStatusCodeBadge
                    approveStatusCode={initialValues?.approveStatusCode}
                    className="py-3 font-medium"
                  />
                }
              </div>
            </div>
          )}
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={initialValues ? true : false}
                id="contractName"
                {...register('contractName')}
                placeholder={t('Contract name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.contractName?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Start date')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <Controller
                  control={control}
                  name="startDate"
                  render={({ field: { onChange, onBlur, value } }) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      onChange={onChange}
                      onBlur={onBlur}
                      //@ts-ignore
                      selected={value}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                    />
                  )}
                />
                <ValidationError message={t(errors.startDate?.message!)} />
              </div>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Supplier name')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <SelectInput
                  name="supplier"
                  options={suppliers}
                  isLoading={loading}
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
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplier?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('End date')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full md:w-3/4">
                <Controller
                  control={control}
                  name="endDate"
                  render={({ field: { onChange, onBlur, value } }) => (
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      onChange={onChange}
                      onBlur={onBlur}
                      //@ts-ignore
                      selected={value}
                      selectsEnd
                      startDate={new Date()}
                      className="border border-border-base"
                    />
                  )}
                />
                <ValidationError message={t(errors.endDate?.message!)} />
              </div>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Supplier ID')} <span className="text-red-500">*</span>
              </label>
              <input
                className={`${rootClassName} ${'cursor-not-allowed bg-gray-100'}`}
                disabled={true}
                type="text"
                id="supplierId"
                {...register('supplierId')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplierId?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('VAT (included)')}
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  className="flex w-full md:w-1/2"
                  label={t('Yes')}
                  {...register('supplyVatIncludeYn')}
                  id="vat_y"
                  value="Y"
                />
                <Radio
                  className="flex w-full md:w-1/2"
                  label={t('No')}
                  {...register('supplyVatIncludeYn')}
                  id="vat_n"
                  value="N"
                />
              </div>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Discount rate')}
              </label>
              <input
                className={rootClassName}
                type="number"
                step="0.01"
                id="supplyDiscountRate"
                {...register('supplyDiscountRate')}
                placeholder={t('Discount rate')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplyDiscountRate?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Settlement method')}
              </label>
              <div className="w-full md:w-3/4 ">
                <SelectInput
                  name="code_settlement"
                  control={control}
                  getOptionLabel={(option: any) =>
                    option?.codeId + ' - ' + option?.codeName
                  }
                  getOptionValue={(option: any) => option?.codeId}
                  onChange={handleChangeMethodCode}
                  options={codeGroup?.codes ?? []}
                  isLoading={codeLoading}
                />
                {/* <ValidationError message={t(errors.code_settlement?.message)} /> */}
              </div>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Discount amount')}
              </label>
              <input
                className={rootClassName}
                type="number"
                step="0.01"
                id="supplyDiscountAmount"
                {...register('supplyDiscountAmount')}
                placeholder={t('Discount amount')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplyDiscountAmount?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Commission rate')}
              </label>
              <input
                className={rootClassName}
                type="number"
                step="0.01"
                id="supplyCommissionRate"
                {...register('supplyCommissionRate')}
                placeholder={t('Commission rate')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplyCommissionRate?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 mt-10 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Contract file upload')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract file upload')}
              </label>
              <div className="w-full pt-1 md:w-3/4">
                <input
                  id="file_input"
                  type="file"
                  className={'e_hide-text'}
                  onChange={handleFileChange}
                />
                {/* <p>Selected file: {contractFileName}</p> */}
              </div>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract file name')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={initialValues ? true : false}
                id="contractFileName"
                {...register('contractFileName')}
                placeholder={t('Contract file name')}
                autoComplete="off"
              />
            </div>
          </div>
        </Card>
      </div>
      <div className="mb-4 text-end">
        {/* <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('Close')}
        </Button> */}

        <LinkButton
          variant="outline"
          href={`${Routes?.supplierContracts.list}`}
          className="me-4 hover:bg-red-800"
        >
          {t('Close')}
        </LinkButton>

        {/** Page update */}
        {initialValues && (
          <>
            {!initialValues.approveStatusCode && (
              <>
                <Button
                  variant="outline"
                  className="me-4 hover:bg-red-800"
                  loading={updating}
                  onClick={handleDraftClick}
                >
                  {t('Save as a Draft')}
                </Button>
                <Button
                  className="bg-red-700 hover:bg-red-800"
                  loading={updating}
                  onClick={handleRegisterClick}
                >
                  {t('Register')}
                </Button>
              </>
            )}
            {initialValues.approveStatusCode ===
              CommonApproveStatusAction.REQ && (
              <Button
                className="bg-red-700 hover:bg-red-800"
                loading={updating}
                onClick={handleRegisterClick}
              >
                {t('Save')}
              </Button>
            )}
          </>
        )}

        {/** Page create */}
        {!initialValues && (
          <>
            <Button
              variant="outline"
              className="me-4 hover:bg-red-800"
              loading={creating}
              onClick={handleDraftClick}
            >
              {t('Save as a Draft')}
            </Button>
            <Button
              className="bg-red-700 hover:bg-red-800"
              loading={creating}
              onClick={handleRegisterClick}
            >
              {t('Register')}
            </Button>
          </>
        )}
      </div>
    </form>
  );
}

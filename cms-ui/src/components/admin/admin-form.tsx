import { useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import Card from '@/components/common/card';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import { Admin, CommonApproveStatusAction, CommonYesNoEnum, Corporation, Role, RoleCode } from "@/types";
import { getErrorMessage } from '@/utils/form-error';
import React, { useState, useEffect } from 'react';
import {PAGE_SIZE, PASS_REGEX_STRING, PERMISSIONS_EV, ROLES} from '@/utils/constants';
import { adminValidationSchema } from './admin-validation-schema';
import { useCreateAdminMutation, useUpdateAdminMutation } from '@/data/admin';
import AdminPasswordInput from './admin-password';
import { useSuppliersQuery } from '@/data/supplier';
import { useBrandsQuery } from '@/data/brands';
import { useStoresQuery } from '@/data/store';
import { useCustomersQuery } from '@/data/customer';
import SelectInput from '../ui/select-input-autocomplete';
import useInputTimeout from '@/utils/use-input-timeout';
import {getAuthCredentials} from "@/utils/auth-utils";

type IProps = {
  initialValues?: Admin | null;
};


type FormValues = Partial<Admin> & {
  password: string;
  confirmPassword: string;
  telephone: string;
  corporation: Corporation;
};

const roleSupplier = PERMISSIONS_EV.ROLE_SUPPLIER;
const roleBrand = PERMISSIONS_EV.ROLE_BRAND;
const roleStore = PERMISSIONS_EV.ROLE_STORE;
const roleCustomer = PERMISSIONS_EV.ROLE_CUSTOMER;
const regexPassword: RegExp = PASS_REGEX_STRING;


export default function CreateOrUpdateAdminForm({ initialValues }: IProps) {
  // console.log('initialValues = ', initialValues)
  const router = useRouter();
  const { t } = useTranslation();
  const {  permissions } = getAuthCredentials();
  const listRolesByManager = permissions?.includes(PERMISSIONS_EV.ROLE_ADMIN)
    ? ROLES
    : ROLES.filter(role => role.code !== PERMISSIONS_EV.ROLE_ADMIN) ; // Operator

  let listCorp: object[];
  let corp: Corporation = {
    adminCorporationId: '',
    adminCorporationName: ''
  };

  const rootClassName =
    'ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  // const { roles, loading: roleLoading } = useRolesQuery({ page: 1, pageSize: PAGE_SIZE }, {});

  const { inputText: corporationName, onInputChange: hanleInputChangeCorporation } =
    useInputTimeout();

  const [corporations, setCorporations] = useState<object[]>()

  const [showCorporation, setShowCorporation] = useState<boolean>(false)

  const { suppliers } = useSuppliersQuery({ page: 1, pageSize: PAGE_SIZE },
    {
      supplierName: corporationName,
      approveStatusCode: CommonApproveStatusAction.APPRV
    });

  const { brands } = useBrandsQuery({ page: 1, pageSize: PAGE_SIZE }, {
    brandName: corporationName,
    validYn: CommonYesNoEnum.YES
  });

  const { stores } = useStoresQuery({ page: 1, pageSize: PAGE_SIZE }, {
    storeName: corporationName,
    validYn: CommonYesNoEnum.YES
  });

  const { customers } = useCustomersQuery(
    { page: 1, pageSize: PAGE_SIZE },
    {
      customerName: corporationName,
      approveStatusCode: CommonApproveStatusAction.APPRV,
    }
  );

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
    resolver: yupResolver(adminValidationSchema),
    defaultValues: {
      ...initialValues,
    },
  });

  const roleCode = watch('role')
  // console.log('roleCode', roleCode)

  const handleChangeRoleCode = (option: any) => {
    // console.log(option)
    setCorporations([])
    setValue('role', option)
    setValue('corporation', {
      adminCorporationId: '',
      adminCorporationName: ''
    })
    setValue('adminCorporationId', ' ')
    handleSetRoleCode(option?.code);
  }

  /**
   * change corporation list depend on selected role Code
   * @param roleCode
   */
  const handleSetRoleCode = (roleCode: string) => {
    listCorp = []

    // console.log('rolecode', roleCode)
    setShowCorporation(roleCode === roleSupplier || roleCode === roleBrand || roleCode === roleStore || roleCode === roleCustomer)

    if (roleCode === roleSupplier) {
      suppliers.forEach(element => {
        corp = {
          adminCorporationId: element.id,
          adminCorporationName: element.supplierName,
        };
        listCorp.push(corp)
      });
      // console.log('roleSupplier ... corporations = ', listCorp)
      setCorporations(listCorp)

    } else if (roleCode === roleBrand) {
      brands.forEach(element => {
        corp = {
          adminCorporationId: element.id,
          adminCorporationName: element.brandName,
        };
        listCorp.push(corp)
      })
      // console.log('roleBrand ... listCorp = ', listCorp)
      setCorporations(listCorp)

    } else if (roleCode === roleStore) {
      stores.forEach(element => {
        corp = {
          adminCorporationId: element.id,
          adminCorporationName: element.storeName
        }
        listCorp.push(corp)
      })
      // console.log('roleStore ... listCorp = ', listCorp)
      setCorporations(listCorp)

    } else if (roleCode === roleCustomer) {
      customers.forEach(element => {
        corp = {
          adminCorporationId: element.id,
          adminCorporationName: element.customerName
        }
        listCorp.push(corp)
      })
      // console.log('roleCustomer ... listCorp = ', listCorp)
      setCorporations(listCorp)
    } else {
      setCorporations(listCorp)
    }
  }

  // const [selectedCorporation, setSelectedCorporation] = useState<Corporation>();
  const handleChangeCorporation = (option: any) => {
    // console.log(option)
    // setSelectedCorporation(option)
    setValue('corporation', option)
    setValue('adminCorporationId', option?.adminCorporationId)
  }

  const handleInputPasswordChange = () => {
    setErrorMessagePasswordCharacters('')
  }

  const handleChangeConfirmPassword = () => {
    setErrorMessageConfirmPasswordNotMatched('')
  }

  useEffect(() => {
    if (initialValues?.roleCode) {
      setValue('role', { code: initialValues?.roleCode, name: initialValues?.roleCode });
    }

    if (initialValues?.adminCorporationId) {
      setShowCorporation(true)
      setValue('corporation', {
        adminCorporationId: initialValues?.adminCorporationId,
        adminCorporationName: initialValues?.adminCorporationName
      })
    }
  }, [initialValues, setValue]);

  /**
   * Reload Corporation list select
   */
  useEffect(() => {
    // console.log('CorporationName in userEffect ', corporationName)
    if (roleCode) {
      handleSetRoleCode(roleCode.code)
    }

  }, [corporationName]);

  const [errorMessagePasswordCharacters, setErrorMessagePasswordCharacters] = useState<string>();

  const [errorMessageConfirmPasswordNotMatched, setErrorMessageConfirmPasswordNotMatched] = useState<string>();
  const [errorRoleCode, setErrorRoleCode] = useState<string>();
  const [errorCorporationName, setErrorCorporationName] = useState<string>();

  const { mutate: createAdmin, isLoading: creating } =
    useCreateAdminMutation();
  const { mutate: updateAdmin, isLoading: updating } =
    useUpdateAdminMutation(false);

  const onSubmit = (values: FormValues) => {
    // console.log('[CreateOrUpdateAdminForm] onSubmit, values = ', values)

    if (!initialValues) {
      if (!values?.password || values?.password.length < 8) {
        setErrorMessagePasswordCharacters('Password must not empty and at least 8 characters!')
        return
      }
      if (!values?.password.match(regexPassword)) {
        setErrorMessagePasswordCharacters('Password must have at least 1 Capital character, at least 1 Special characters, at least 1 number characters!')
        return
      }
      if (!values?.confirmPassword) {
        setErrorMessageConfirmPasswordNotMatched('Confirm Password must not empty!')
        return
      }
      if (values?.confirmPassword != values?.password) {
        setErrorMessageConfirmPasswordNotMatched('Confirm Password must matches with Password!')
        return
      }
      if (!values?.role) {
        setErrorRoleCode('Please select account role!')
        return
      }
      // console.log(showCorporation && !values?.corporation?.adminCorporationId)
      if (showCorporation && !values?.corporation?.adminCorporationId) {
        setErrorCorporationName('Please select a Corporation!')
        return
      }
    }

    const inputValues = {
      id: values.id,
      adminName: values.adminName,
      email: values.email,
      password: values.password,
      mobileNumber: values.mobileNumber,
      roleCode: values.role?.code,

      telephone: values.telephone,
      adminCorporationId: values.corporation?.adminCorporationId,
    };

    // console.log('[CreateOrUpdateAdminForm] onSubmit, inputValues =', inputValues)
    // return;
    try {
      if (!initialValues) {
        createAdmin({
          ...inputValues,
        });
      } else {
        updateAdmin({
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
              <h1 className="text-lg text-cyan-600 text-heading font-semibold uppercase">
                {t('Basic Account Information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Admin Name ')}<span className="text-red-500">*</span>
              </label>
              <input
                className={'mb-2 ' + rootClassName}
                type="text"
                // disabled={initialValues ? true : false}
                id="adminName"
                {...register('adminName')}
                placeholder={t('Admin Name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:pl-[25%]">
                {t(errors.adminName?.message!)}
              </span>
            </div>

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Mobile Number ')}<span className="text-red-500">*</span>
              </label>
              <input
                className={'mb-2 ' + rootClassName}
                type="text"
                // disabled={initialValues ? true : false}
                id="mobileNumber"
                {...register('mobileNumber')}
                placeholder={t('Mobile Number')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.mobileNumber?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            {!initialValues && (<>
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Password ')}<span className="text-red-500">*</span>
                </label>
                <div className="w-full md:w-3/4">
                  <AdminPasswordInput
                    label={t('form:input-label-password')}
                    {...register('password')}
                    error={t(errors.password?.message!)}
                    variant="outline"
                    className="mb-4"
                    onInput={handleInputPasswordChange}
                    autoComplete="off"
                  />
                  <span className="w-full text-xs text-red-500 text-start md:w-3/4">
                    {t(errorMessagePasswordCharacters!)}
                  </span>
                </div>
              </div>
            </>)}

            {initialValues && (<>
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Admin ID *')}
                </label>
                <input
                  className={'bg-gray-200 ' + rootClassName}
                  type="text"
                  // disabled={initialValues ? true : false}
                  disabled={true}
                  id="adminName"
                  {...register('id')}
                  autoComplete="off"
                />
                <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                </span>
              </div>
            </>)}

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Telephone (office)')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="telephone"
                {...register('telephone')}
                placeholder={t('Telephone')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
              </span>
            </div>
          </div>

          <div className="mb-4 flex flex-wrap">
            {!initialValues && (<>
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Confirm Password ')}<span className="text-red-500">*</span>
                </label>
                <div className="w-full md:w-3/4">
                  <AdminPasswordInput
                    label={t('form:input-label-password')}
                    {...register('confirmPassword')}
                    error={t(errors.confirmPassword?.message!)}
                    variant="outline"
                    className="mb-4"
                    onInput={handleChangeConfirmPassword}
                    autoComplete='off'
                  />
                  <span className="w-full text-xs text-red-500 text-start md:w-3/4">
                    {t(errorMessageConfirmPasswordNotMatched!)}
                  </span>
                </div>
              </div>
            </>)}

            {initialValues && (<>
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Role Code *')}
                </label>
                <div className="w-full md:w-3/4">
                  <SelectInput
                    name="role"
                    options={listRolesByManager}
                    getOptionLabel={(option: any) =>  option.roleName}
                    getOptionValue={(option: any) => option.code}
                    control={control}
                    disabled={initialValues ? true : false}
                  />
                  <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                    {t(errors.roleCode?.message!)}
                  </span>
                </div>
              </div>
            </>)}

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Email ')}<span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={initialValues ? true : false}
                id="email"
                {...register('email')}
                placeholder={t('Email')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.email?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            {!initialValues && (<>
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Role Code ')}<span className="text-red-500">*</span>
                </label>
                <div className="w-full md:w-3/4">
                  <SelectInput
                    name="role"
                    options={listRolesByManager}
                    getOptionLabel={(option: any) => option.roleName }
                    getOptionValue={(option: any) => option.code}
                    onChange={handleChangeRoleCode}
                    control={control}
                    isClearable={true}
                  />
                  <span className="w-full text-xs text-red-500 text-start md:w-full">
                    {t(errorRoleCode!)}
                  </span>
                </div>
              </div>
            </>)}
          </div>

          {showCorporation && (<>
            <div className="mb-3 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Corporation Name ')}<span className="text-red-500">*</span>
                </label>
                <div className="w-full md:w-3/4">
                  <SelectInput
                    // {...register('corporation')}
                    name="corporation"
                    options={corporations}
                    disabled={initialValues ? true : false}
                    getOptionLabel={(option: any) => option.adminCorporationName + ' - ' + option.adminCorporationId}
                    getOptionValue={(option: any) => option.adminCorporationId}
                    onInputChange={hanleInputChangeCorporation}
                    onChange={handleChangeCorporation}
                    control={control}
                    isClearable={true}
                  />
                  <span className="w-full text-xs text-red-500 text-start">
                    {t(errorCorporationName!)}
                  </span>
                </div>
              </div>
            </div>

            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Corporation Id')}
                </label>
                <input
                  className={'bg-gray-200 ' + rootClassName}
                  disabled={true}
                  {...register('adminCorporationId')}
                  // placeholder={t('adminCorporationId')}
                  autoComplete="off"
                />
              </div>
            </div>
          </>)
          }
        </Card>
      </div>

      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 bg-red-700 hover:bg-red-800"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>
        <Button className='bg-red-700 hover:bg-red-800'
          loading={updating || creating}>
          {initialValues
            ? t('Update')
            : t('Register')}
        </Button>
      </div>
    </form>
  )
}

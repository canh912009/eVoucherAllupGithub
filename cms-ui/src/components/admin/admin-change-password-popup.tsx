
import Card from "../common/card";
import { useTranslation } from 'next-i18next';
import LogoBlack from "../ui/logo-black";
import { User } from '@/types';
import Button from '@/components/ui/button';
import AdminPasswordInput from './admin-password';
import { useForm } from 'react-hook-form';
import React, { useState } from 'react';
import { PASS_REGEX_STRING } from '@/utils/constants';
import { useUpdateAdminMutation } from "@/data/admin";
import { getErrorMessage } from '@/utils/form-error';
import { useModalAction } from "../ui/modal/modal.context";

interface AdminChangePasswordPopupProps {
  // productSlug: string;
  data?: User;
}

type FormValues = {
  newPassword: string;
  confirmPassword: string;
};

const regexPassword: RegExp = PASS_REGEX_STRING;

const AdminChangePasswordPopup: React.FC<AdminChangePasswordPopupProps> = ({ data }) => {
  // console.log(data);

  const { t } = useTranslation();
  const { closeModal } = useModalAction();

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<FormValues>({
  });

  const [errorMessagePasswordCharacters, setErrorMessagePasswordCharacters] = useState<string>();
  const [errorMessageConfirmPasswordNotMatched, setErrorMessageConfirmPasswordNotMatched] = useState<string>();

  const handleInputPasswordChange = () => {
    setErrorMessagePasswordCharacters('')
  }

  const handleChangeConfirmPassword = () => {
    setErrorMessageConfirmPasswordNotMatched('')
  }

  const { mutate: changeAdminPassword, isLoading: updating } =
    useUpdateAdminMutation(true);

  const onSubmit = (values: FormValues) => {
    // console.log('[CreateOrUpdateAdminForm] onSubmit, values = ', values)

    if (!values?.newPassword || values?.newPassword.length < 8) {
      setErrorMessagePasswordCharacters('Password must not empty and at least 8 characters!')
      return
    }
    if (!values?.newPassword.match(regexPassword)) {
      setErrorMessagePasswordCharacters('Password must have at least 1 Capital character, at least 1 Special characters, at least 1 number characters!')
      return
    }
    if (!values?.confirmPassword) {
      setErrorMessageConfirmPasswordNotMatched('Confirm Password must not empty!')
      return
    }
    if (values?.confirmPassword != values?.newPassword) {
      setErrorMessageConfirmPasswordNotMatched('Confirm Password must matches with Password!')
      return
    }

    const inputValues = {
      newPassword: values.newPassword,
    };

    // console.log('inputValues = ', inputValues)
    // return;

    try {
      changeAdminPassword({
        ...inputValues,
        id: 'change-password',
      });
      closeModal();
    } catch (error) {
      const serverErrors = getErrorMessage(error);
      Object.keys(serverErrors?.validation).forEach((field: any) => {
        setError(field.split('.')[1], {
          type: 'manual',
          message: serverErrors?.validation[field][0],
        });
      });
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex flex-wrap">
        <Card className="w-full sm:w-full md:w-full md:rounded-xl">
          <article className="relative z-[51] w-full h-full max-w-6xl bg-light md:rounded-xl xl:min-w-[582px]">
            <LogoBlack className='p-5' />

            <div className="flex px-5 pb-2 w-full">
              <h1 className="text-lg font-semibold text-heading xx-large uppercase text-red-600">
                {t('New Password')}
              </h1>
            </div>

            <div className="flex px-5 pb-8 w-full">
              <span className="flex text-gray-500">
                {t('Set the new password for your account so you can login and access all features.')}
              </span>
            </div>

            <div className="flex px-5">
              <label className="w-full py-2 text-heading text-red-700">
                {t('Enter New Password ')}
              </label>
            </div>

            <div className="flex px-5 w-full">
              <AdminPasswordInput
                label={t('form:input-label-password')}
                {...register('newPassword')}
                error={t(errors.newPassword?.message!)}
                variant="outline"
                className="mb-4 w-full"
                onInput={handleInputPasswordChange}
              />
            </div>
            <div className="flex px-5 pb-5 w-full">
              <span className="w-full text-xs text-red-500 text-start md:w-3/4">
                {t(errorMessagePasswordCharacters!)}
              </span>
            </div>

            <div className="flex px-5">
              <label className="w-full py-2 text-heading md:w-1/4 text-red-700">
                {t('Confirm Password ')}
              </label>
            </div>

            <div className="flex px-5">
              <AdminPasswordInput
                label={t('form:input-label-password')}
                {...register('confirmPassword')}
                error={t(errors.confirmPassword?.message!)}
                variant="outline"
                className="mb-4 w-full"
                onInput={handleChangeConfirmPassword}
              />
            </div>
            <div className="flex px-5 pb-5 w-full">
              <span className="w-full text-xs text-red-500 text-start md:w-3/4">
                {t(errorMessageConfirmPasswordNotMatched!)}
              </span>
            </div>

            <div className="flex px-5 p-3 items-center justify-center">
              <Button
                size="medium"
                className="flex px-5 bg-red-700 me-4 hover:bg-red-800 md:w-2/4"
              >
                {t('UPDATE PASSWORD')}
              </Button>
            </div>
          </article>
        </Card>
      </div>
    </form>
  );
}

export default AdminChangePasswordPopup;
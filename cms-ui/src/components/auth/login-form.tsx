import Button from '@/components/ui/button';
import Input from '@/components/ui/input';
import PasswordInput from '@/components/ui/password-input';
import { useTranslation } from 'next-i18next';
import * as yup from 'yup';
import Link from '@/components/ui/link';
import Form from '@/components/ui/forms/form';
import { Routes } from '@/config/routes';
import { useLogin } from '@/data/user';
import type { LoginInput } from '@/types';
import { useState } from 'react';
import Alert from '@/components/ui/alert';
import Router from 'next/router';
import {
  allowedRolesEV,
  hasAccess,
  setAuthCredentials,
} from '@/utils/auth-utils';

const loginFormSchema = yup.object().shape({
  phoneNumber: yup
    .string()
    // .email('form:error-email-format')
    .required('form:error-phone-required'),
  password: yup.string().required('form:error-password-required'),
});

const LoginForm = () => {
  const { t } = useTranslation();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const { mutate: login, isLoading, error } = useLogin();

  function onSubmit({ phoneNumber, password }: LoginInput) {
    login(
      {
        phoneNumber,
        password,
      },
      {
        onSuccess: (data) => {
          if (data?.errorCode === "0" && data?.data?.token) {
            const permissions: string[] = [data?.data?.roleCode] || [];
            if (hasAccess(allowedRolesEV, permissions)) {
              setAuthCredentials(data?.data?.token, permissions);
              Router.push('');
              return;
            }
            setErrorMessage('form:error-enough-permission');
          } else {
            setErrorMessage('form:error-credential-wrong');
          }
        },
        onError: (error) => {
          // @ts-ignore
          setErrorMessage(error?.response?.data?.message ?? data?.message);
        },
      }
    );
  }

  return (
    <>
      <Form<LoginInput> validationSchema={loginFormSchema} onSubmit={onSubmit}>
        {({ register, formState: { errors } }) => (
          <>
            <Input
              label={t('form:input-label-phone')}
              {...register('phoneNumber')}
              type="text"
              variant="outline"
              className="mb-4"
              error={t(errors?.phoneNumber?.message!)}
            />
            <PasswordInput
              label={t('form:input-label-password')}
              // forgotPassHelpText={t('form:input-forgot-password-label')}
              {...register('password')}
              error={t(errors?.password?.message!)}
              variant="outline"
              className="mb-4 text-red-700"
              forgotPageLink={Routes.forgotPassword}
            />
            <Button className="w-full bg-red-700 hover:bg-red-800" loading={isLoading} disabled={isLoading}>
              {t('form:button-label-login')}
            </Button>

            {/*<div className="relative mt-8 mb-6 flex flex-col items-center justify-center text-sm text-heading sm:mt-11 sm:mb-8">*/}
            {/*  <hr className="w-full" />*/}
            {/*  <span className="absolute -top-2.5 bg-light px-2 -ms-4 start-2/4">*/}
            {/*    {t('common:text-or')}*/}
            {/*  </span>*/}
            {/*</div>*/}

            {/*<div className="text-center text-sm text-body sm:text-base">*/}
            {/*  {t('form:text-no-account')}{' '}*/}
            {/*  <Link*/}
            {/*    href={Routes.register}*/}
            {/*    className="font-semibold text-accent underline transition-colors duration-200 ms-1 hover:text-accent-hover hover:no-underline focus:text-accent-700 focus:no-underline focus:outline-none"*/}
            {/*  >*/}
            {/*    {t('form:link-register-shop-owner')}*/}
            {/*  </Link>*/}
            {/*</div>*/}
          </>
        )}
      </Form>
      {errorMessage ? (
        <Alert
          message={t(errorMessage)}
          variant="error"
          closeable={true}
          className="mt-5"
          onClose={() => setErrorMessage(null)}
        />
      ) : null}
    </>
  );
};

export default LoginForm;

{
  /* {errorMsg ? (
          <Alert
            message={t(errorMsg)}
            variant="error"
            closeable={true}
            className="mt-5"
            onClose={() => setErrorMsg('')}
          />
        ) : null} */
}

import { useTranslation } from 'next-i18next';
import { Admin, Corporation } from '@/types';
import Card from "../common/card";
import Button from '@/components/ui/button';
import LinkButton from '@/components/ui/link-button';
import router from 'next/router';
import { PERMISSIONS_EV } from '@/utils/constants';
import { Routes } from '@/config/routes';
import { useState } from 'react';

type IProps = {
  data?: Admin | null;
};

export default function AdminDetail({ data }: IProps) {
  // console.log(data)
  const { t } = useTranslation();

  const rootClassName =
    'bg-gray-200 ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  return (
    <>
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
                {t('Admin Name *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="adminName"
                placeholder={data?.adminName}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Mobile Number *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="mobileNumber"
                placeholder={data?.mobileNumber}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Admin Id *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="adminName"
                placeholder={data?.id}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Telephone')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="telephone"
                placeholder={data?.telephone}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Role Code *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="roleCode"
                placeholder={data?.roleCode}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Email *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="email"
                placeholder={data?.email}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Corporation Name *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="adminCorporationName"
                placeholder={data?.adminCorporationName ? data?.adminCorporationName : ' ... '}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Last Login *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="roleCode"
                placeholder='Last login'
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Corporation Id *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled
                id="corporationId"
                placeholder={data?.adminCorporationId ? data?.adminCorporationId : ' ... '}
                autoComplete="off"
              />
            </div>
          </div>
        </Card>
      </div>

      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 bg-red-700 hover:bg-red-800"
          type="button"
        >
          {t('Back')}
        </Button>
        <LinkButton size="medium"
          href={`${Routes?.adminUser.editWithoutLang(data?.id as string)}`}
          className="bg-red-700 hover:bg-red-800">
          {t('Update & Edit')}
        </LinkButton>
      </div>
    </>
  );
}


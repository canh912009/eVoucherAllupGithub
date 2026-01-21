import Card from '@/components/common/card';
import { getUrlPublicAsset } from '@/data/download';
import {UrBoxGood, UrBoxGoodType, WataneGood, WataneGoodType, WatanePackageType} from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { useTranslation } from 'next-i18next';
import React, { useEffect, useState } from 'react';
import TextArea from '../ui/text-area';

type IProps = {
  initialValues?: WataneGood | null;
};

export default function WataneGoodDetai({ initialValues }: Readonly<IProps>) {
  // console.log('initialValues', initialValues);

  const { t } = useTranslation();

  const rootClassName =
    'bg-gray-100 ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  return (
    <div>
      <div className="flex border-b border-dashed border-border-base py-4">
        <h2 className="text-2xl font-semibold uppercase text-red-500">
          {initialValues?.name}
        </h2>
      </div>
      <div className="my-5 flex flex-wrap sm:my-6">
        <Card className="w-full sm:w-full md:w-full md:p-4">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold">
                {t('Good Details (Product)')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Good Id / Code')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.code}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Good Name')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.name}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Good Type')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={ WataneGoodType[ initialValues?.type as keyof typeof WataneGoodType ] }
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Price')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={formatNumber(initialValues?.price! as number, false, true)}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('value')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={formatNumber(initialValues?.value! as number, false, true)}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Direct Voucher')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={ initialValues?.directVoucher }
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('validIn')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.validIn}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Package Type')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={WatanePackageType[initialValues?.packageType as keyof typeof WatanePackageType]}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Description')}
              </label>
              <TextArea
                id="description"
                disabled={true}
                name="description"
                placeholder={t('Description')}
                value={initialValues?.description}
                variant="outline"
                className="w-full md:w-3/4 pl-0"
              />
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}

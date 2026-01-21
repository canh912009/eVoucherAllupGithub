import Button from '@/components/ui/button';
import LinkButton from '@/components/ui/link-button';
import Radio from '@/components/ui/radio/radio';
import { Routes } from '@/config/routes';
import { getUrlPublicAsset } from '@/data/download';
import {Category, VNPTEPayProvider} from '@/types';
import { isPermitted } from '@/utils/auth-utils';
import {CODE_GROUP, PERMISSIONS_EV as p} from '@/utils/constants';
import { emptyPlaceholder } from '@/utils/placeholders';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import Card from '../common/card';
import { useModalAction } from '../ui/modal/modal.context';
import Checkbox from "@/components/ui/checkbox/checkbox";
import React from "react";
import {formatNumber} from "@/utils/common-utils";
import {VnptFacesCard} from "@/components/ui/vnptCard";
import {useCodeGroupQuery} from "@/data/code-group";

type IProps = {
  initialValues?: VNPTEPayProvider | null;
  isXPAY?: boolean
};

export default function ProviderDetail({ initialValues, isXPAY = false }: Readonly<IProps>) {
  const { t } = useTranslation();
  const router = useRouter();
  const { openModal } = useModalAction();

  const rootClassName =
    'bg-gray-200 ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  function handleStatus(modalView: any, id: string) {
    openModal(modalView, id);
  }

  const allCardFaces = useCodeGroupQuery(isXPAY ? CODE_GROUP.XPAY_FACE_VALUES : CODE_GROUP.VNPT_FACE_VALUES)?.codeGroup?.codes
    .map(code => parseInt(code.codeId, 10))
    .sort((a, b) => b - a);
  const allowedCardFaces = JSON.parse(initialValues?.allowedCardFaces ?? "");

  return (
    <div>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full  ">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-xl font-semibold uppercase">
                {t('Provider Information ')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Provider Code')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.providerCd}
                autoComplete="off"
                disabled={true}
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Provider Name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.providerNm}
                autoComplete="off"
                disabled={true}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Provider Type')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.providerType}
                autoComplete="off"
                disabled={true}
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Allowed actions')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.allowedActions}
                autoComplete="off"
                disabled={true}
              />
            </div>
          </div>

          <div className="flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Active')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  disabled={true}
                  className="flex w-full font-semibold md:w-1/2"
                  label={t('Yes')}
                  name="validYn"
                  checked={initialValues?.validYn === 'Y'}
                  id="validYn_y"
                  value="Y"
                />
                <Radio
                  disabled={true}
                  className="flex w-full font-semibold md:w-1/2"
                  label={t('No')}
                  name="validYn"
                  checked={initialValues?.validYn === 'N'}
                  id="validYn_n"
                  value="N"
                />
              </div>
            </div>
          </div>
          <VnptFacesCard allCardFaces={allCardFaces}  allowedCardFaces={allowedCardFaces}/>
        </Card>
      </div>

      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('Back')}
        </Button>
        {initialValues && (
          <>
            <LinkButton
              href={`${(isXPAY ? Routes.xpayProvider : Routes.vnptEpayProvider).editWithoutLang(
                initialValues?.providerCd
              )}`}
              className="bg-red-700 hover:bg-red-800"
            >
              {t('Edit')}
            </LinkButton>
          </>
        )}
      </div>
    </div>
  );
}

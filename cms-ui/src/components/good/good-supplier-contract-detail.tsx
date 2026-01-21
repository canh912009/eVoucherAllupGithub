import { useTranslation } from 'next-i18next';
import { Good, SupplierContract } from '@/types';
import Radio from '@/components/ui/radio/radio';
import {formatNumber} from "@/utils/common-utils";

type IProps = {
  supplierContract?: SupplierContract | null;
  goodDetail?: Good | null;
};

export default function SupplierContractDetail({ goodDetail }: IProps) {
  let classes = {
    wrapper:
      'flex flex-wrap pb-8 my-5 border-b border-dashed border-border-base sm:my-8',
    side_left: 'w-full px-0 pb-5 sm:w-1/4 sm:py-8 sm:pe-4 md:w-1/4 md:pe-5',
    side_right: 'w-full sm:w-3/4 md:w-3/4',
    row: 'mb-1 flex flex-wrap',
    title: 'w-full md:w-1/4 px-4 py-2 text-heading',
    content: 'w-full md:w-3/4 px-4 py-2',
  };

  const rootClassName =
    'bg-gray-200 ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';
  const { t } = useTranslation();

  return (
    <>
      {/* <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full"> */}
      {/* <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg uppercase">
                {t('Product Information')}
              </h1>
            </div>
          </div> */}

      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Sales Price *')}
          </label>
          <input
            readOnly
            className={rootClassName}
            disabled={true}
            placeholder={formatNumber(Number(goodDetail?.sellPrice))}
          />
        </div>

        <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('List Price *')}
          </label>
          <input
            readOnly
            className={rootClassName}
            disabled={true}
            placeholder={formatNumber(Number(goodDetail?.listPrice))}
          />
        </div>
      </div>

      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Seltlement Method *')}
          </label>
          <input
            readOnly
            className={rootClassName}
            disabled={true}
            placeholder={goodDetail?.settlementMethodCode == 'PER_EXCHANGE' ? 'Per exchange' : goodDetail?.settlementMethodCode == 'PER_PUBLISH' ? 'Per publish' : 'Per use amount'}
          />
        </div>
        <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Discount Amount *')}
          </label>
          <input
            readOnly
            className={rootClassName}
            disabled={true}
            placeholder={formatNumber(Number(goodDetail?.supplyDiscountAmount))}
          />
        </div>
      </div>

      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Commission Rate *')}
          </label>
          <input
            readOnly
            className={rootClassName}
            disabled={true}
            placeholder={goodDetail?.supplyCommissionRate}
          />
        </div>

        <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Include VAT *')}
          </label>
          <div className="flex w-full md:w-3/4">
            <Radio
              className="flex w-full md:w-1/2"
              readOnly
              label={t('Yes')}
              name='vatIncludeYn'
              id="valid_Yes"
              value="Y"
              checked={goodDetail?.vatIncludeYn === 'Y'}
            />
            <Radio
              className="flex w-full md:w-1/2"
              readOnly
              label={t('No')}
              name='vatIncludeYn'
              id="valid_No"
              value="N"
              checked={goodDetail?.vatIncludeYn === 'N'}
            />
          </div>
        </div>
      </div>

      <div className="mb-5 flex flex-wrap">
        <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('Start Date *')}
          </label>
          <input
            readOnly
            className={rootClassName}
            disabled={true}
            // id="brandName"
            placeholder={goodDetail?.startDate}
          />
        </div>
        <div className="flex w-full flex-wrap px-4 md:w-1/2">
          <label className="w-full py-2 text-heading md:w-1/4">
            {t('End Date *')}
          </label>
          <input
            readOnly
            className={rootClassName}
            disabled={true}
            // id="brandName"
            placeholder={goodDetail?.endDate}
          />
        </div>
      </div>
      {/* </Card>
      </div> */}
    </>
  )
}

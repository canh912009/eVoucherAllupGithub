import Card from '@/components/common/card';
import { getUrlPublicAsset } from '@/data/download';
import { GiftPopGood } from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { useTranslation } from 'next-i18next';
import Radio from '../ui/radio/radio';
import TextArea from '../ui/text-area';

type IProps = {
  initialValues?: GiftPopGood | null;
};

export default function GiftPopGoodDetail({ initialValues }: Readonly<IProps>) {
  // console.log('initialValues', initialValues);

  const { t } = useTranslation();

  const rootClassName =
    'bg-gray-100 ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  return (
    <div>
      <div className="flex border-b border-dashed border-border-base py-4">
        <h2 className="text-2xl font-semibold uppercase uppercase text-heading text-red-500">
          {initialValues?.goodsName + ' (' + initialValues?.goodsId + ')'}
        </h2>
      </div>
      <div className="my-5 flex flex-wrap sm:my-6">
        <Card className="w-full sm:w-full md:w-full md:p-4">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold text-cyan-600 text-heading">
                {t('Good Details (Product)')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Good Code')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="goodsId"
                value={initialValues?.goodsId}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Good')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="goodsName"
                value={initialValues?.goodsName}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand Code')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="brandCode"
                value={initialValues?.brandCode}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand Name')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="brandName"
                value={initialValues?.brandName}
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
                id="goodsType"
                value={initialValues?.goodsType}
              />
              <label className="mt-4 w-full py-2 text-heading md:w-1/4">
                {t('List Price')}
              </label>
              <input
                className={`${rootClassName} mt-4`}
                disabled={true}
                type="text"
                id="listPrice"
                value={formatNumber(initialValues?.listPrice!)}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Product Image (Review)')}
              </label>
              {initialValues?.originImg && (
                <div className="w-full pt-1 md:w-3/4">
                  <img
                    src={getUrlPublicAsset(initialValues?.originImg) ?? ''}
                    alt="Product"
                    width={160}
                    height={160}
                  />
                </div>
              )}
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Expired Date')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="expiryDate"
                value={initialValues?.expiryDate}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Sale Price')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="salePrice"
                value={formatNumber(initialValues?.salePrice!)}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Stock (Quantity)')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                id="stock"
                value={formatNumber(initialValues?.stock!)}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Used')}
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  disabled={true}
                  className="flex w-full md:w-1/2"
                  label={t('Yes')}
                  name="useYN"
                  id="vat_y"
                  checked={initialValues?.useYN === 'Y'}
                  value="Y"
                />
                <Radio
                  disabled={true}
                  className="flex w-full md:w-1/2"
                  label={t('No')}
                  name="useYN"
                  id="vat_n"
                  checked={initialValues?.useYN === 'N'}
                  value="N"
                />
              </div>
            </div>
          </div>
        </Card>

        <Card className="mt-5 w-full sm:w-full md:w-full md:p-4">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold text-cyan-600 text-heading">
                {t('Addition Details')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full w-full flex-wrap px-2">
              <label className="w-full py-2 text-heading md:w-[12%]">
                {t('Good Guide')}
              </label>
              <TextArea
                disabled={true}
                name="commtGuide"
                value={initialValues?.commtGuide}
                variant="outline"
                className="w-full md:w-[88%]"
                rows={8}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full w-full flex-wrap px-2">
              <label className="w-full py-2 text-heading md:w-[12%]">
                {t('Good Description')}
              </label>
              <TextArea
                disabled={true}
                name="commtProduct"
                value={initialValues?.commtProduct}
                variant="outline"
                className="w-full md:w-[88%]"
                rows={8}
              />
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}


import Card from '@/components/common/card';
import ExternalPinUploadDetail from '@/components/good/external-pin-upload-detail';
import Layout from '@/components/layouts/owner';
import Button from "@/components/ui/button";
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import TextArea from "@/components/ui/text-area";
import { useExternalPinUploadDataQuery } from '@/data/external-pin-upload';
import { allowedRolesEV } from '@/utils/auth-utils';
import { parse } from 'json2csv';
import { useTranslation } from 'next-i18next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import { useState } from 'react';

const ExternalPinData = () => {
  const { t } = useTranslation();
  const { query } = useRouter();

  const rootClassName =
    'bg-gray-200 ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  let classes = {
    // title: 'font-semibold',
    // content: 'font-normal text-[#212121]',
    wrapper:
      'flex flex-wrap pb-8 my-5 border-b border-dashed border-border-base sm:my-8',
    side_left: 'w-full px-0 pb-5 sm:w-1/4 sm:py-8 sm:pe-4 md:w-1/4 md:pe-5',
    side_right: 'w-full sm:w-3/4 md:w-3/4',
    row: 'mb-1 flex flex-wrap',
    title: 'w-full md:w-1/4 px-4 py-2 font-semibold text-heading',
    content: 'w-full md:w-3/4 px-4 py-2',
  };

  const [page, setPage] = useState(1);

  const { externalPinUpload, loading: loadingExternalPinData, error: errorExternalPinData } = useExternalPinUploadDataQuery(query.id as string);

  // const { good } = useGoodQuery(query.id as string);

  if (loadingExternalPinData) return <Loader text={t('common:text-loading')} />;
  if (errorExternalPinData) return <ErrorMessage message={errorExternalPinData.message} />;

  function handlePagination(current: number) {
    setPage(current);
  }

  const downloadCsv = async () => {
    try {
      if (!externalPinUpload?.pins || externalPinUpload?.pins.length === 0) {
        throw new Error('rawsCSV is undefined or empty');
      }

      const modifiedRawsCSV = externalPinUpload?.pins.map(raw => ({
        ...raw,
        externalPinNo: raw?.externalPinNo ? `"${raw?.externalPinNo}"` : '',
        password: raw?.password ? `"${raw?.password}"` : '',
      }));

      const csv = parse(modifiedRawsCSV); // convert the JSON to CSV using the json2csv package
      // console.log('csv = ', csv)
      const utf8BOM = '\uFEFF';
      const blob = new Blob([utf8BOM + csv], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);

      const hideLink = document.createElement('a');
      hideLink.href = url;
      hideLink.download = 'ExternalPinData.csv';
      hideLink.style.display = 'none';
      document.body.appendChild(hideLink);
      hideLink.click();

      // delete Blob object và a tag when download done
      URL.revokeObjectURL(url);
      document.body.removeChild(hideLink);
    } catch (error) {
      console.error('Error while downloading CSV:', error);
    }
  }


  return (
    <>
      <div className="flex mb-5 py-2 sm:py-2">
        <h1 className="text-lg font-semibold text-heading uppercase text-red-600">
          {t('Upload External PIN Information ')}
          <span>{t('(')}{externalPinUpload?.uploadName}{t(')')}</span>
        </h1>
      </div>
      <div className="my-4 flex flex-wrap ">
        <Card className="w-full sm:w-full md:w-full">

          <div className="flex mb-5 border-b border-dashed border-border-base py-2 sm:py-2">
            <h1 className="text-lg font-semibold text-heading uppercase">
              {t('Upload External PIN Information ')}
              <span>{t('(')}{externalPinUpload?.uploadName}{t(')')}</span>
            </h1>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Upload Name: ')}
              </label>
              <div className="w-full md:w-3/4">
                <input
                  readOnly
                  className={rootClassName}
                  disabled={true}
                  placeholder={externalPinUpload?.uploadName}
                />
              </div>
            </div>

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Upload File Name: ')}
              </label>
              <div className="w-full md:w-3/4">
                <input
                  readOnly
                  className={rootClassName}
                  disabled={true}
                  placeholder={externalPinUpload?.uploadFileName}
                />
              </div>
            </div>
          </div>

          <div className="mb-10 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Product ID: ')}
              </label>
              <div className="w-full md:w-3/4">
                <input
                  readOnly
                  className={rootClassName}
                  disabled={true}
                  placeholder={externalPinUpload?.goodsId}
                />
              </div>
            </div>

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Upload File Path: ')}
              </label>
              <div className="w-full md:w-3/4">
                <input
                  readOnly
                  className={rootClassName}
                  disabled={true}
                  placeholder={externalPinUpload?.uploadFilePath}
                />
              </div>
            </div>
          </div>

          <div className="mb-8 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Memo Description: ')}
              </label>
              <div className="w-full md:w-3/4">
                <TextArea
                  readOnly
                  disabled
                  name='memo'
                  placeholder={externalPinUpload?.memo}
                  variant="outline"
                />
              </div>
            </div>
          </div>

          <div className="flex w-full flex-wrap px-4 md:w-1/2">
          </div>

          <div className="flex py-2 sm:py-2">
            <h1 className="text-lg font-semibold text-heading uppercase">
              {t('External PIN Data')}
            </h1>
          </div>

          <div className="flex py-2 sm:py-2">
            <h3 className="py-1 text-sm w-full center">
              {t('Total data: ')}{externalPinUpload?.pins.length}
            </h3>

            <div className="flex w-full flex-col items-center md:flex-row-reverse">
              <Button
                size="small"
                onClick={downloadCsv}
                className="w-full md:w-auto md:ms-6 bg-red-500 hover:bg-red-600"
              >
                <span className="text-xs xl:block">
                  {t('Download PIN (CSV)')}
                </span>
              </Button>
            </div>
          </div>

          <ExternalPinUploadDetail
            externalPins={externalPinUpload?.pins}
            onPagination={handlePagination}
            externalPinUpload={externalPinUpload}
          />
        </Card>
      </div>
    </>
  );
};
ExternalPinData.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

ExternalPinData.authenticate = {
  permissions: allowedRolesEV,
};

export default ExternalPinData;
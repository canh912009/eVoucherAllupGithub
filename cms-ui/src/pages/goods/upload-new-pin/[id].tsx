
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import { useRouter } from 'next/router';
import Loader from '@/components/ui/loader/loader';
import ErrorMessage from '@/components/ui/error-message';
import { allowedRolesEV } from '@/utils/auth-utils';
import { useExternalPinUploadsQuery } from '@/data/external-pin-upload';
import ExternalPinUploadList from '@/components/good/external-pin-upload-list';
import { useState } from 'react';
import { useModalAction } from "@/components/ui/modal/modal.context";
import { PAGE_SIZE } from '@/utils/constants';
import { useGoodQuery } from '@/data/good';
import Card from "@/components/common/card";
import Button from '@/components/ui/button';
import { PERMISSIONS_EV as p } from '@/utils/constants';

const GoodsExternalPinUpload = () => {
  const { t } = useTranslation();
  const { query } = useRouter();

  // TODO upload file
  const { openModal, closeModal } = useModalAction();

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

  const { externalPinUploads, loading: loadingExternalPin, paginatorInfo, error: errorExternalPin } = useExternalPinUploadsQuery({ page, pageSize: PAGE_SIZE },
    { goodsId: query.id as string })

  const { good } = useGoodQuery(query.id as string);
  const displayType = good?.brand?.displayType

  if (loadingExternalPin) return <Loader text={t('common:text-loading')} />;
  if (errorExternalPin) return <ErrorMessage message={errorExternalPin.message} />;

  function handlePagination(current: number) {
    setPage(current);
  }

  function handleUpdateNewPIN() {
    openModal('UPLOAD_EXTERNAL_PIN', {good, displayType})
  }

  return (
    <>
      <div className="flex mb-2 py-2 sm:py-2">
        <h1 className="text-lg font-semibold text-heading uppercase text-red-600">
          {t('General External PIN Information')}
        </h1>
      </div>

      <div className="my-4 flex flex-wrap ">
        <Card className="w-full sm:w-full md:w-full">

          <div className="flex mb-5 border-b border-dashed border-border-base py-2 sm:py-2">
            <h1 className="text-lg font-semibold text-heading uppercase">
              {t('General External PIN Information')}
            </h1>
          </div>

          {/* <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap font-semibold px-4 py-2">
              <h1 className="text-lg uppercase">
                {t('General External PIN Information')}
              </h1>
            </div>
          </div> */}

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Product name ')}
              </label>
              <div className="w-full md:w-3/4">
                <input
                  readOnly
                  className={rootClassName}
                  disabled={true}
                  placeholder={good?.goodsName}
                />
              </div>
            </div>

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Supplier Name ')}
              </label>
              <div className="w-full md:w-3/4">
                <input
                  readOnly
                  className={rootClassName}
                  disabled={true}
                  placeholder={good?.supplier?.supplierName}
                />
              </div>
            </div>
          </div>

          <div className="mb-8 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Product ID ')}
              </label>
              <div className="w-full md:w-3/4">
                <input
                  readOnly
                  className={rootClassName}
                  disabled={true}
                  placeholder={good?.id as unknown as string}
                />
              </div>
            </div>

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Total Count ')}
              </label>
              <div className="w-full md:w-3/4">
                <input
                  readOnly
                  className={rootClassName}
                  disabled={true}
                  placeholder={t(paginatorInfo?.totalCount)}
                />
              </div>
            </div>
          </div>

          <div className="mb-6 flex flex-wrap">
          </div>

          <div className="mb-3 flex flex-wrap">
            <div className="flex w-full px-4 md:w-1/2">
              <label className="w-full py-2 text-bold md:w-1/4">
                {/* {t('Total Data:')} {t(paginatorInfo?.perPage > paginatorInfo?.)}  */}
                {/* TODO */}
              </label>
            </div>
            <div className="flex w-full md:w-1/2">
              <label className="w-full py-2 text-heading md:w-[80%]">
              </label>
              {good?.system === 'EXTERNAL' && <Button
                  size="small"
                  className="me-4 bg-red-700 hover:bg-red-800"
                  onClick={(event) => {
                    event.preventDefault();
                    return handleUpdateNewPIN();
                  }}
              >
                {t('Upload new PIN')}
              </Button> }
            </div>
          </div>

          <ExternalPinUploadList
            externalPinUploads={externalPinUploads}
            good={good}
            paginatorInfo={paginatorInfo}
            onPagination={handlePagination} />

        </Card>
      </div>
    </>
  );
};
GoodsExternalPinUpload.Layout = Layout;

export const getServerSideProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

GoodsExternalPinUpload.authenticate = {
  permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
};

export default GoodsExternalPinUpload;
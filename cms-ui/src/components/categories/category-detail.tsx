import Button from '@/components/ui/button';
import LinkButton from '@/components/ui/link-button';
import Radio from '@/components/ui/radio/radio';
import { Routes } from '@/config/routes';
import { getUrlPublicAsset } from '@/data/download';
import { Category } from '@/types';
import { isPermitted } from '@/utils/auth-utils';
import { PERMISSIONS_EV as p } from '@/utils/constants';
import { emptyPlaceholder } from '@/utils/placeholders';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import Card from '../common/card';
import { useModalAction } from '../ui/modal/modal.context';

type IProps = {
  initialValues?: Category | null;
};

type FormValues = Partial<Category> & {};

export default function CategoryDetail({ initialValues }: Readonly<IProps>) {
  const { t } = useTranslation();
  const router = useRouter();
  const { openModal } = useModalAction();

  const rootClassName =
    'bg-gray-200 ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  function handleStatus(modalView: any, id: string) {
    openModal(modalView, id);
  }

  return (
    <div>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-base font-semibold uppercase">
                {t('Category Information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Category Name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.categoryName}
                autoComplete="off"
                disabled={true}
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Category Code')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.categoryCode}
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

            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Category Image')} <span className="text-red-500">*</span>
              </label>
              <span className="pt-3">{initialValues?.imageName}</span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2" />
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              {initialValues?.imagePath && (
                <>
                  <div className="w-full py-2 font-semibold text-heading md:w-1/4"></div>
                  <div className="flex w-full md:w-3/4">
                    <img
                      src={
                        getUrlPublicAsset(initialValues?.imagePath) ??
                        emptyPlaceholder
                      }
                      alt={'Category img'}
                      width={300}
                      height={300}
                    />
                  </div>
                </>
              )}
            </div>
          </div>
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
            {/*{isPermitted([p.ROLE_ADMIN]) && (*/}
            {/*  <Button*/}
            {/*    className="bg-black me-4 hover:bg-gray-600"*/}
            {/*    onClick={() =>*/}
            {/*      handleStatus('DELETE_CATEGORY', initialValues.categoryCode)*/}
            {/*    }*/}
            {/*  >*/}
            {/*    {t('Delete')}*/}
            {/*  </Button>*/}
            {/*)}*/}
            <LinkButton
              href={`${Routes.category.editWithoutLang(
                initialValues?.categoryCode
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

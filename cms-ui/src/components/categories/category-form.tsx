import { categoryValidationSchema } from '@/components/categories/category-validation-schema';
import Button from '@/components/ui/button';
import Loader from '@/components/ui/loader/loader';
import { useModalAction } from '@/components/ui/modal/modal.context';
import Radio from '@/components/ui/radio/radio';
import {
  useCreateCategoryMutation,
  useUpdateCategoryMutation,
} from '@/data/categories';
import { getUrlPublicAsset } from '@/data/download';
import { useUploadImageMutation } from '@/data/upload';
import { Category } from '@/types';
import { getErrorMessage } from '@/utils/form-error';
import { emptyPlaceholder } from '@/utils/placeholders';
import { yupResolver } from '@hookform/resolvers/yup';
import { useRouter } from 'next/router';
import { ChangeEvent } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import Card from '../common/card';

type IProps = {
  initialValues?: Category | null;
};

export default function CreateOrUpdateCategoryForm({
  initialValues,
}: Readonly<IProps>) {
  const router = useRouter();
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    control,
    watch,
    setError,
    setValue,
    formState: { errors },
  } = useForm<Partial<Category>>({
    // @ts-ignore
    defaultValues: initialValues
      ? {
          ...initialValues,
        }
      : {},
    resolver: yupResolver(categoryValidationSchema),
  });

  const rootClassName =
    'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const { openModal } = useModalAction();

  const validYn = watch('validYn');

  function handleChangeActive(modalView: any, newValidYn: any) {
    // console.log(validYn, newValidYn);
    if (initialValues && validYn !== newValidYn) {
      openModal(modalView, { triggerActiveYn, validYn });
    }
  }

  function triggerActiveYn(isActive: boolean) {
    // console.log('triggerActiveYn', isActive);
    setValue('validYn', isActive ? 'Y' : 'N');
  }

  const { mutate: createCategory, isLoading: creating } =
    useCreateCategoryMutation();
  const { mutate: updateCategory, isLoading: updating } =
    useUpdateCategoryMutation();

  /**
   * File
   */
  const imageName = watch('imageName');
  const imagePath = watch('imagePath');

  const { mutate: uploadImage, isLoading: uploadingImage } =
    useUploadImageMutation();

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    setValue('imageName', '');
    setValue('imagePath', '');

    if (e.target.files) {
      const file = e.target.files[0];
      // console.log('file', file);
      if (file) {
        uploadImage(file, {
          onSuccess: (data: any) => {
            // console.log('data', data);
            setValue('imageName', file.name);
            setValue('imagePath', data?.path);
          },
          onError: (error: any) => {
            toast.error('Error:' + error?.response?.data.message);
          },
        });
      }
    }
  };

  const onSubmit = async (values: Partial<Category>) => {
    // console.log('values', values);
    // return;

    const inputValues = {
      categoryCode: values.categoryCode,
      categoryName: values.categoryName,
      validYn: values.validYn,
      imageName: values.imageName,
      imagePath: values.imagePath,
    };

    try {
      if (!initialValues) {
        createCategory({
          ...inputValues,
        });
      } else {
        updateCategory({
          ...inputValues,
          id: initialValues.categoryCode,
        });
      }
    } catch (error) {
      const serverErrors = getErrorMessage(error);
      Object.keys(serverErrors?.validation).forEach((field: any) => {
        setError(field.split('.')[1], {
          type: 'manual',
          message: serverErrors?.validation[field][0],
        });
      });
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
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
                id="categoryName"
                {...register('categoryName')}
                placeholder={t('Category Name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.categoryName?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Category Code')} <span className="text-red-500">*</span>
              </label>
              <input
                className={initialValues ? `${rootClassName} bg-gray-200` :`${rootClassName}  `}
                type="text"
                id="categoryCode"
                {...register('categoryCode')}
                placeholder={t('Category Code')}
                autoComplete="off"
                disabled={initialValues ? true : false}
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.categoryCode?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Active')} <span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  className="flex w-full font-semibold md:w-1/2"
                  label={t('Yes')}
                  {...register('validYn')}
                  id="validYn_y"
                  onClick={() => handleChangeActive('WARNING_ACTIVE_CATEGORY', 'Y')}
                  value="Y"
                />
                <Radio
                  className="flex w-full font-semibold md:w-1/2"
                  label={t('No')}
                  {...register('validYn')}
                  id="validYn_n"
                  onClick={() => handleChangeActive('WARNING_ACTIVE_CATEGORY', 'N')}
                  value="N"
                />
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.validYn?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Category Image')} <span className="text-red-500">*</span>
              </label>
              <div className="w-full pt-1 md:w-3/4">
                <input
                  id="file_input"
                  type="file"
                  className={'e_hide-text'}
                  onChange={handleFileChange}
                />
                {uploadingImage && (
                  <Loader uploadFile={true} text={t('common:text-loading')} />
                )}
                {imageName && <p>Selected file: {imageName}</p>}
              </div>
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.imagePath?.message!)}
              </span>
            </div>
          </div>

          {imagePath && (
            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2" />
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <div className="w-full py-2 md:w-1/4" />
                <div className="w-full pt-1 md:w-3/4">
                  <img
                    src={getUrlPublicAsset(imagePath) ?? emptyPlaceholder}
                    alt={'Category Img'}
                    width={300}
                    height={300}
                  />
                </div>
              </div>
            </div>
          )}
        </Card>
      </div>
      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>
        <Button
          className="bg-red-700 hover:bg-red-800"
          loading={updating || creating}
        >
          {initialValues ? t('Update') : t('Register')}
        </Button>
      </div>
    </form>
  );
}

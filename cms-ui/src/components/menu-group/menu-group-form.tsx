import { useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import SwitchInput from '@/components/ui/switch-input';
import Card from '@/components/common/card';
import { useRouter } from 'next/router';
import Badge from '@/components/ui/badge/badge';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import {MenuGroup} from '@/types';
import { getErrorMessage } from '@/utils/form-error';
import {menuGroupValidationSchema} from "@/components/menu-group/menu-group-validation-schema";
import {useCreateMenuGroupMutation, useUpdateMenuGroupMutation} from "@/data/menu-group";

type IProps = {
  initialValues?: MenuGroup | null;
};

export default function CreateOrUpdateMenuGroupForm({ initialValues }: IProps) {
  const router = useRouter();
  const { t } = useTranslation();
  const {
    register,
    handleSubmit,
    control,
    watch,
    setError,
    formState: { errors },
  } = useForm<Partial<MenuGroup>>({
    defaultValues: initialValues
      ? {
        ...initialValues,
      }
      : {},
    resolver: yupResolver(menuGroupValidationSchema),
  });

  const rootClassName =
    'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const { mutate: createMenuGroup, isLoading: creating } =
    useCreateMenuGroupMutation();
  const { mutate: updateMenuGroup, isLoading: updating } =
    useUpdateMenuGroupMutation();

  const onSubmit = async (values: Partial<MenuGroup>) => {
    const inputValues = {
      menuGroupName: values.menuGroupName,
      sortOrder: values.sortOrder,
    };
    try {
      if (!initialValues) {
        // @ts-ignore
        createMenuGroup({
          ...inputValues,
        });
      } else {
        updateMenuGroup({
          ...inputValues,
          id: initialValues.id,
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
              <h1 className="text-lg font-semibold text-cyan-600 text-heading">
                {t('Information')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('MenuGroup name *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="menuGroupName"
                {...register('menuGroupName')}
                placeholder={t('MenuGroup name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.menuGroupName?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Sort Order *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="sortOrder"
                {...register('sortOrder')}
                placeholder={t('Sort Order')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sortOrder?.message!)}
              </span>
            </div>
          </div>
        </Card>
      </div>
      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>
        <Button className='bg-red-700 hover:bg-red-800' loading={updating || creating}>
          {initialValues ? t('Save') : t('Register')}
        </Button>
      </div>
    </form>
  );
}

import { useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import Card from '@/components/common/card';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import {Menu, MenuGroup} from '@/types';
import { getErrorMessage } from '@/utils/form-error';
import {menuValidationSchema} from "@/components/menu/menu-validation-schema";
import {useCreateMenuMutation, useUpdateMenuMutation} from "@/data/menu";
import {useMenuGroupsQuery} from "@/data/menu-group";
import React, {useState} from "react";
import {PAGE_SIZE, PAGE_SIZE_MAX} from "@/utils/constants";

type IProps = {
  initialValues?: Menu | null;
};

export default function CreateOrUpdateMenuForm({ initialValues }: IProps) {
  const router = useRouter();
  const { t } = useTranslation();
  const [page, setPage] = useState(1);
  const { menuGroups, loading } = useMenuGroupsQuery({ page, pageSize: PAGE_SIZE_MAX },);
  menuGroups.sort((a, b) => a.sortOrder - b.sortOrder)
  const {
    register,
    handleSubmit,
    control,
    watch,
    setError,
    formState: { errors },
  } = useForm<Partial<Menu>>({
    defaultValues: initialValues
      ? {
        ...initialValues,
      }
      : {},
    resolver: yupResolver(menuValidationSchema),
  });

  const rootClassName =
    'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const { mutate: createMenu, isLoading: creating } =
    useCreateMenuMutation();
  const { mutate: updateMenu, isLoading: updating } =
    useUpdateMenuMutation();

  const onSubmit = async (values: Partial<Menu>) => {
    const inputValues = {
      menuGroupId: values.menuGroupId ? values.menuGroupId : menuGroups[0]?.id,
      menuName: values.menuName,
      sortOrder: values.sortOrder,
      menuUrl: values.menuUrl,
    };
    try {
      if (!initialValues) {
        // @ts-ignore
        createMenu({
          ...inputValues,
        });
      } else {
        // @ts-ignore
        updateMenu({
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

            {initialValues && <div className="flex w-full flex-wrap px-4 py-2">
                <label className="w-full py-2 font-semibold text-heading">
                  {t('MenuGroup name ')} : {menuGroups.find((menuGroup) => menuGroup.id === initialValues?.menuGroupId)?.menuGroupName}
                </label>
            </div>}
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('MenuGroup Id *')}
              </label>

              {initialValues
                ? <input
                  className={rootClassName}
                  type="text"
                  disabled={true}
                  id="menuGroupId"
                  {...register('menuGroupId')}
                  placeholder={t('MenuGroup Id')}
                  autoComplete="off"
                />
                : <select
                  className={rootClassName}
                  id="menuGroupId"
                  {...register('menuGroupId')}
                >
                  {menuGroups.map((menuGroup) => (
                    <option key={menuGroup.id} value={menuGroup.id}>
                      {menuGroup.id} : {menuGroup.menuGroupName}
                    </option>
                  ))}</select>
              }

              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.menuGroupId?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Menu name *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="menuName"
                {...register('menuName')}
                placeholder={t('Menu name')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.menuName?.message!)}
              </span>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('Sort Order *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="sortOrder"
                {...register('sortOrder')}
                placeholder={t('sortOrder')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sortOrder?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('menuUrl *')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="menuUrl"
                {...register('menuUrl')}
                placeholder={t('menuUrl')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.menuUrl?.message!)}
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

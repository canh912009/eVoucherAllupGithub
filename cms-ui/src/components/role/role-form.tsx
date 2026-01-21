import { useForm } from 'react-hook-form';
import Button from '@/components/ui/button';
import Card from '@/components/common/card';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import { yupResolver } from '@hookform/resolvers/yup';
import {Menu, Role} from '@/types';
import { getErrorMessage } from '@/utils/form-error';
import {roleValidationSchema} from "@/components/role/role-validation-schema";
import {useCreateRoleMutation, useRoleQuery, useUpdateRoleMutation} from "@/data/role";
import {useMenusQuery} from "@/data/menu";
import {PAGE_SIZE, PAGE_SIZE_MAX} from "@/utils/constants";
import React, {useState} from "react";
import {useMenuGroupsQuery} from "@/data/menu-group";

type IProps = {
  initialValues?: Role | null;
};

export default function CreateOrUpdateRoleForm({ initialValues }: IProps) {
  const router = useRouter();
  const { t } = useTranslation();
  const {
    register,
    handleSubmit,
    control,
    watch,
    setError,
    formState: { errors },
  } = useForm<Partial<Role>>({
    defaultValues: initialValues
      ? {
        ...initialValues,
      }
      : {},
    resolver: yupResolver(roleValidationSchema),
  });

  const [page, setPage] = useState(1);
  const { menus, } = useMenusQuery({ page, pageSize: PAGE_SIZE_MAX },);
  const menusByGroup: Menu[][] = menus.reduce((acc, item) => {
    const groupArray = acc.find((group) => group[0]?.menuGroupId === item.menuGroupId);
    if (groupArray) {
      groupArray.push(item);
    } else {
      acc.push([item]);
    }
    return acc;
  }, []);

  // initialValues for menus add??
  const { query } = useRouter();
  const { role } = useRoleQuery(query?.id as string);
  function listMenusAdded(role: Role): number[] {
    let menusAdded: number[] = []
    role?.menuGroups?.map(group => {
      group.menus.map(menu => {
        menusAdded.push(menu.id);
      })
    })
    return menusAdded
  }
  const menusAdded = listMenusAdded(role)
  const { menuGroups, loading } = useMenuGroupsQuery({ page, pageSize: PAGE_SIZE_MAX },);

  const rootClassName =
    'ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const { mutate: createRole, isLoading: creating } =
    useCreateRoleMutation();
  const { mutate: updateRole, isLoading: updating } =
    useUpdateRoleMutation();

  const onSubmit = async (values: Partial<Role>) => {
    const inputValues = {
      roleCode: values.roleCode,
      roleName: values.roleName,
      sortOrder: values.sortOrder,
      menus: values?.menus ? values?.menus?.map((item) => {
        return { id: parseInt(item) };
      }) : []
    };
    try {
      if (!initialValues) {
        // @ts-ignore
        createRole({
          ...inputValues,
        });
      } else {
        // @ts-ignore
        updateRole({
          ...inputValues,
          roleCode: initialValues.roleCode,
        });
      }
      // console.log("inputValues role", inputValues)
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
                {t('table:table-role-code')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="roleCode"
                disabled={initialValues ? true : false}
                {...register('roleCode')}
                placeholder={t('roleCode')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.roleCode?.message!)}
              </span>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('table:table-role-name')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="roleName"
                {...register('roleName')}
                placeholder={t('roleName')}
                autoComplete="off"
              />
              <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.roleName?.message!)}
              </span>
            </div>
          </div>
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 font-semibold text-heading md:w-1/4">
                {t('table:sortOrder')}
              </label>
              <input
                className={rootClassName}
                type="text"
                id="sortOrder"
                {...register('sortOrder')}
                placeholder={t('sortOrder')}
                autoComplete="off"
              />
              <span className ="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sortOrder?.message!)}
              </span>
            </div>
          </div>

          {/*add menus*/}
          <label className="w-full px-4 font-semibold text-heading  ">
            {t('Add menus')} :
          </label>
          {menusByGroup.map(groupMenu => (
            <div className="flex w-full flex-wrap px-6 py-2 mt-6 bg-red-100 rounded-xl">
              <div className="w-full ">
                <label className="w-auto font-semibold text-heading  ">
                  {t('MenuGroup ID ')} : {groupMenu[0].menuGroupId}
                  _ {menuGroups.find((menuGroup) => menuGroup.id === groupMenu[0].menuGroupId)?.menuGroupName}
                </label>
              </div>

              {groupMenu.map((menu) => (
                <div key={menu.id} className="w-full py-2 font-semibold text-heading md:w-1/3">
                  <label>
                    <input
                      key={menu.id}
                      type="checkbox"
                      className="form-checkbox mr-2 checked:bg-red-800  "
                      value={menu.id}
                      defaultChecked={initialValues ? menusAdded.includes(menu.id) : false}
                      {...register('menus')}
                    />
                    {menu.menuName}
                  </label>
                </div>
              ))}
            </div>
          ))}
          <span className ="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.menus?.message!)}
          </span>
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

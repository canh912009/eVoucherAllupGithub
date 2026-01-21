import { AdminQueryOptions } from "@/types";
import cn from 'classnames';
import Card from '../common/card';
import { useForm } from "react-hook-form";
import { useTranslation } from 'next-i18next';
import Button from '@/components/ui/button';
import LinkButton from '@/components/ui/link-button';
import { Routes } from '@/config/routes';
import Select from '@/components/ui/select/select';
import {ROLES} from "@/utils/constants";

const classes = {
  root: 'ps-5 pe-4 h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0',
  normal:
    'bg-gray-100 border border-border-base focus:shadow focus:bg-light focus:border-accent',
  solid:
    'bg-gray-100 border border-border-100 focus:bg-light focus:border-accent',
  outline: 'border border-border-base focus:border-accent',
  shadow: 'focus:shadow',
};

type SearchProps = {
  className?: string;
  shadow?: boolean;
  variant?: 'normal' | 'solid' | 'outline';
  inputClassName?: string;
  popupType?: boolean;
  onSearch: (data: AdminQueryOptions) => void;
};


const AdminSearch: React.FC<SearchProps> = ({
  className,
  onSearch,
  variant = 'outline',
  shadow = false,
  popupType = false,
  inputClassName,
  ...rest
}) => {

  const {
    register,
    handleSubmit,
    setValue,
    control,
    formState: { errors },
  } = useForm<AdminQueryOptions>({
    defaultValues: {
      adminId: '',
      adminName: '',
      email: '',
      mobilePhone: '',
      corporationName: '',
      roleCode: '',
    },
  });

  const { t } = useTranslation();

  function onchangeRoleCode(newValue: any) {
    // console.log(newValue)
    return setValue('roleCode', newValue?.code);
  }

  const rootClassName = cn(
    classes.root,
    {
      [classes.normal]: variant === 'normal',
      [classes.solid]: variant === 'solid',
      [classes.outline]: variant === 'outline',
    },
    {
      [classes.shadow]: shadow,
    },
    inputClassName
  );

  return (
    <>
      <form
        noValidate
        role="search"
        className={cn('relative w-full items-center', className)}
        onSubmit={handleSubmit(onSearch)}
      >
        <Card className="mb-4 md:pb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:p-2">
          <div className="flex w-full flex-wrap md:w-[90%]">

            <div className="mb-4 flex w-full w-full flex-wrap">
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Admin Id')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="adminId" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="adminId"
                    {...register('adminId')}
                    className={rootClassName}
                    placeholder={t('form:input-placeholder-search')}
                    aria-label="Search"
                    autoComplete="off"
                    {...rest}
                  />
                </div>
              </div>
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Admin Name')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="adminName" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="adminName"
                    {...register('adminName')}
                    className={rootClassName}
                    placeholder={t('form:input-placeholder-search')}
                    aria-label="Search"
                    autoComplete="off"
                    {...rest}
                  />
                </div>
              </div>
            </div>
            {!popupType &&
                <div className="mb-4 flex w-full w-full flex-wrap">
                    <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                        <div className="mb-4 md:mb-0 md:w-1/4">
                            <h1 className="font-semibold text-heading">
                              {t('Email')}
                            </h1>
                        </div>
                        <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                            <label htmlFor="email" className="sr-only">
                              {t('form:input-label-search')}
                            </label>
                            <input
                                type="text"
                                id="email"
                                {...register('email')}
                                className={rootClassName}
                                placeholder={t('form:input-placeholder-search')}
                                aria-label="Search"
                                autoComplete="off"
                                {...rest}
                            />
                        </div>
                    </div>
                    <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                        <div className="mb-4 md:mb-0 md:w-1/4">
                            <h1 className="font-semibold text-heading">
                              {t('Mobile No')}
                            </h1>
                        </div>
                        <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                            <label htmlFor="mobilePhone" className="sr-only">
                              {t('form:input-label-search')}
                            </label>
                            <input
                                type="text"
                                id="mobilePhone"
                                {...register('mobilePhone')}
                                className={rootClassName}
                                placeholder={t('form:input-placeholder-search')}
                                aria-label="Search"
                                autoComplete="off"
                                {...rest}
                            />
                        </div>
                    </div>
                </div>
            }
            {!popupType &&
                <div className="mb-4 flex w-full w-full flex-wrap">
                    <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                        <div className="mb-4 md:mb-0 md:w-1/4">
                            <h1 className="font-semibold text-heading">
                              {t('Corporation Name')}
                            </h1>
                        </div>
                        <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                            <label htmlFor="corporationName" className="sr-only">
                              {t('form:input-label-search')}
                            </label>
                            <input
                                type="text"
                                id="corporationName"
                                {...register('corporationName')}
                                className={rootClassName}
                                placeholder={t('form:input-placeholder-search')}
                                aria-label="Search"
                                autoComplete="off"
                                {...rest}
                            />
                        </div>
                    </div>


                  {/* RoleCode should be a select dropdown */}
                    <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                        <div className="mb-4 md:mb-0 md:w-1/4">
                            <h1 className="font-semibold text-heading">
                              {t('Role Code')}
                            </h1>
                        </div>
                        <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                            <label htmlFor="roleCode" className="sr-only">
                              {t('form:input-label-search')}
                            </label>
                            <div className="w-full md:w-4/4">
                                <Select
                                    id="roleCode"
                                    name="roleCode"
                                    options={ROLES}
                                    getOptionLabel={(option: any) => option.roleName}
                                    getOptionValue={(option: any) => option.code}
                                    placeholder={t('Type your query and press enter')}
                                    onChange={onchangeRoleCode}
                                    isClearable
                                />
                            </div>
                          {/* <input
                type="text"
                id="roleCode"
                {...register('roleCode')}
                className={rootClassName}
                placeholder={t('form:input-placeholder-search')}
                aria-label="Search"
                autoComplete="off"
                {...rest}
              /> */}
                        </div>
                    </div>
                </div>
            }
          </div>
          <div className="flex w-full flex-wrap md:w-[10%]">
            <Button
              className="w-full bg-red-700 hover:bg-red-800"
              aria-label="Search"
            >
              {t('Search')}
            </Button>
          </div>
        </Card>
      </form>

      {!popupType &&
          <div className="mb-4 flex w-full flex-wrap">
              <div className="flex w-full flex-col items-center px-4 md:flex-row-reverse">
                  <LinkButton
                      size="small"
                      href={`${Routes?.adminUser.create}`}
                      className="w-full bg-black hover:bg-slate-700 md:w-auto md:ms-6"
                  >
            <span className="text-xs xl:block">
              {t('Create new')}
            </span>
                  </LinkButton>
              </div>
          </div>
      }

    </>
  )
}

export default AdminSearch;

import { BrandsQueryOptions } from "@/types";
import cn from 'classnames';
import Card from '../common/card';
import { useForm } from "react-hook-form";
import { useTranslation } from 'next-i18next';
import { SearchIcon } from '@/components/icons/search-icon';
import LinkButton from '@/components/ui/link-button';
import { Routes } from '@/config/routes';
import Button from "@/components/ui/button";
import Select from "@/components/ui/select/select";
import {APPROVE_STATUS_CODES} from "@/components/common/approve-status-code-badge";
import {ACTIVE_STATUS_CODES} from "@/components/common/status-code-badge";
import {getUserInfo} from "@/utils/auth-utils";
import {PERMISSIONS_EV, PERMISSIONS_EV as p} from "@/utils/constants";
import React from "react";

const classes = {
  root: ' h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0',
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
  popupType?: boolean;
  variant?: 'normal' | 'solid' | 'outline';
  inputClassName?: string;
  onSearch: (data: BrandsQueryOptions) => void;
};

const BrandSearch: React.FC<SearchProps> = ({
  className,
  onSearch,
  variant = 'outline',
  shadow = false,
  inputClassName,
  popupType = false,
  ...rest
}) => {

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<BrandsQueryOptions>({
    defaultValues: {
      brandName: '',
      brandId: '',
      supplierName: '',
      validYn: '', //Active
    },
  });

  const { t } = useTranslation();
  const infoUser = getUserInfo()
  const supplierRole = (infoUser?.roleCode === PERMISSIONS_EV.ROLE_SUPPLIER)

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

  function onChangeBrandActive(newValue: any) {
    return setValue('validYn', newValue.code);
  }

  return (
    <>
      <form
        noValidate
        role="search"
        className={cn('relative w-full items-center', className)}
        onSubmit={handleSubmit(onSearch)}
      >
        <Card className="mb-8 flex flex-wrap p-3 md:p-2 bg-[#EEEEEE]">
          <div className="mb-4 flex md:w-11/12 flex-wrap">
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Brand name')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="brandName" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="brandName"
                  {...register('brandName')}
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
                  {t('Brand ID')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="id" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="brandId"
                  {...register('brandId')}
                  className={rootClassName}
                  placeholder={t('form:input-placeholder-search')}
                  aria-label="Search"
                  autoComplete="off"
                  {...rest}
                />
              </div>
            </div>
            { !popupType && <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Supplier name')}
                </h1>
              </div>
              {supplierRole
                ? <input
                    className={`${rootClassName}  md:w-3/4 cursor-not-allowed bg-gray-100 `}
                    disabled={true}
                    type="text"
                    id="supplierName"
                    {...register('supplierName')}
                    value={infoUser?.adminCorporationName}
                    autoComplete="off"
                  />
                : <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="supplierName" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="supplierName"
                    {...register('supplierName')}
                    className={rootClassName}
                    placeholder={t('form:input-placeholder-search')}
                    aria-label="Search"
                    autoComplete="off"
                    {...rest}
                 />
                </div> }
            </div> }
            { !popupType && <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Active')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="status" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <div className="w-full">
                  <Select
                    id="validYn"
                    name="validYn"
                    options={ACTIVE_STATUS_CODES}
                    getOptionLabel={(option: any) => option.text}
                    getOptionValue={(option: any) => option.code}
                    placeholder={t('All')}
                    onChange={onChangeBrandActive}
                  />
                </div>
              </div>
            </div> }
          </div>

          <div className="mb-4 flex md:w-1/12 flex-wrap mr-0">
            <Button
              className="w-full bg-red-700 hover:bg-red-800"
              aria-label="Search"
            >
              {t('Search')}
            </Button>
          </div>
        </Card>
      </form>
      { getUserInfo().roleCode !== p.ROLE_SUPPLIER && !popupType &&
          <div className="mb-4 flex w-full w-full flex-wrap">
            <div className="flex w-full flex-col items-center px-4 md:flex-row-reverse">
              <LinkButton
                  size="small"
                  href={`${Routes?.brands.create}`}
                  className="w-full md:w-auto md:ms-6 bg-black hover:bg-slate-700"
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

export default BrandSearch;

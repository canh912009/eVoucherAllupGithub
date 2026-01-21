import {CommonYesNoEnum, GoodQueryOptions} from '@/types';
import { useTranslation } from 'next-i18next';
import { useForm } from 'react-hook-form';
import cn from 'classnames';
import Card from '../common/card';
import { SearchIcon } from '../icons/search-icon';
import LinkButton from '../ui/link-button';
import { Routes } from '@/config/routes';
import Button from '@/components/ui/button';
import {PERMISSIONS_EV, PERMISSIONS_EV as p, SYSTEM_BRAND_TYPE} from "@/utils/constants";
import Select from "@/components/ui/select/select";
import {ACTIVE_STATUS_CODES, SYSTEM_GROUP_IN_EX, SYSTEM_GROUP_ALL} from "@/components/common/status-code-badge";
import {getUserInfo} from "@/utils/auth-utils";
import React from "react";

const classes = {
  root: 'ps-4 pe-4 h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0',
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
  onSearch: (data: GoodQueryOptions) => void;
  isPopupChoiceType?: boolean
  isPopupBulkType?: boolean
};

const GoodSearch: React.FC<SearchProps> = ({
  className,
  onSearch,
  variant = 'outline',
  shadow = false,
  inputClassName,
  isPopupChoiceType = false,
  isPopupBulkType = false,
  ...rest
}) => {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<GoodQueryOptions>({
    defaultValues: {
      goodsId: '',
      goodsName: '',
      supplierName: '',
      brandName: '',
      validYn: isPopupChoiceType ? CommonYesNoEnum.YES : CommonYesNoEnum.NULL,
      system: isPopupChoiceType ? SYSTEM_BRAND_TYPE.INTERNAL : CommonYesNoEnum.NULL,
      isExpired: isPopupChoiceType ? CommonYesNoEnum.NO : CommonYesNoEnum.NULL,
      keyWord: '',
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
  function onChangeGoodActive(newValue: any) {
    return setValue('validYn', newValue.code);
  }
  function onChangeSystem(newValue: any) {
    return setValue('system', newValue.code);
  }

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
                    {t('Product Id')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="id" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="id"
                    {...register('goodsId')}
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
                    {t('Product Name')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="goodsName" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="goodsName"
                    {...register('goodsName')}
                    className={rootClassName}
                    placeholder={t('form:input-placeholder-search')}
                    aria-label="Search"
                    autoComplete="off"
                    {...rest}
                  />
                </div>
              </div>
            </div>

            {!isPopupBulkType &&
                <div className="mb-4 flex w-full w-full flex-wrap">
                  <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                    <div className="mb-4 md:mb-0 md:w-1/4">
                      <h1 className="font-semibold text-heading">
                        {t('Supplier Name')}
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
                  </div>

                  <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                    <div className="mb-4 md:mb-0 md:w-1/4">
                      <h1 className="font-semibold text-heading">
                        {t('Brand Name')}
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
                </div>
            }

            <div className="mb-4 flex w-full w-full flex-wrap">
              {(!isPopupChoiceType && !isPopupBulkType) && <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Active')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="validYn" className="sr-only">
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
                        onChange={onChangeGoodActive}
                    />
                  </div>
                </div>
              </div> }
              {( !isPopupBulkType && !supplierRole) && <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('System')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="validYn" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <div className="w-full">
                    <Select
                        id="system"
                        name="system"
                        options={isPopupChoiceType ? SYSTEM_GROUP_IN_EX : SYSTEM_GROUP_ALL}
                        getOptionLabel={(option: any) => option.text}
                        getOptionValue={(option: any) => option.code}
                        placeholder={isPopupChoiceType ? SYSTEM_BRAND_TYPE.INTERNAL : "All"}
                        onChange={onChangeSystem}
                    />
                  </div>
                </div>
              </div> }
            </div>
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

      { getUserInfo().roleCode !== p.ROLE_SUPPLIER && (!isPopupChoiceType && !isPopupBulkType) && <div className="mb-4 flex w-full flex-wrap">
        <div className="flex w-full flex-col items-center px-4 md:flex-row-reverse">
          <LinkButton
              size="small"
              href={`${Routes?.goods.create}`}
              className="w-full bg-black hover:bg-slate-700 md:w-auto md:ms-6"
          >
            <span className="text-xs xl:block">
              {t('Create new')}
            </span>
          </LinkButton>
        </div>
      </div>}
    </>
  )
}

export default GoodSearch;

import { RoleQueryOptions as SearchValue} from "@/types";
import cn from "classnames";
import Card from "@/components/common/card";
import {SearchIcon} from "@/components/icons/search-icon";
import LinkButton from "@/components/ui/link-button";
import {Routes} from "@/config/routes";
import React from "react";
import {useForm} from "react-hook-form";
import {useTranslation} from "next-i18next";

const classes = {
  root: 'ps-10 pe-4 h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0',
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
  onSearch: (data: SearchValue) => void;
};

const Search: React.FC<SearchProps> = ({
  className,
  onSearch,
  variant = 'outline',
  shadow = false,
  inputClassName,
   ...rest
}) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      roleCode: '',
      roleName: '',
      keyWord: '',
    },
  });

  const { t } = useTranslation();

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
    <form
      noValidate
      role="search"
      className={cn('relative w-full items-center', className)}
      onSubmit={handleSubmit(onSearch)}
    >
      <Card className="mb-8 flex flex-col items-center ">
        <div className="mb-4 flex w-full w-full flex-wrap ">
          <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
            <div className="mb-4 md:mb-0 md:w-1/4">
              <h1 className="font-semibold text-heading">{t('table:table-item-id')}</h1>
            </div>
            <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
              <label htmlFor="roleCode" className="sr-only">
                {t('form:input-label-search')}
              </label>
              <input
                type="text"
                id="roleCode"
                {...register('roleCode')}
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
                {t('table:table-role-name')}
              </h1>
            </div>
            <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
              <label htmlFor="roleName" className="sr-only">
                {t('form:input-label-search')}
              </label>
              <input
                type="text"
                id="roleName"
                {...register('roleName')}
                className={rootClassName}
                placeholder={t('form:input-placeholder-search')}
                aria-label="Search"
                autoComplete="off"
                {...rest}
              />
            </div>
          </div>
        </div>

        <div className="mb-4 flex w-full w-full flex-wrap">
          <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
            <div className="mb-4 md:mb-0 md:w-1/4">
              <h1 className="font-semibold text-heading">{t('form:keyword')}</h1>
            </div>
            <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
              <label htmlFor="keyWord" className="sr-only">
                {t('form:input-label-search')}
              </label>
              <input
                type="text"
                id="keyWord"
                {...register('keyWord')}
                className={rootClassName}
                placeholder={t('form:input-placeholder-search')}
                aria-label="Search"
                autoComplete="off"
                {...rest}
              />
            </div>
          </div>
        </div>

        {/*Button*/}
        <div className="mb-4 flex w-full w-full flex-wrap">
          <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
            <button className="px-4 py-2 font-semibold">
              <SearchIcon className="h-5 w-5" />
            </button>
            <div>
              <LinkButton
                href={`${Routes.roles.create}`}
                className="h-12 w-full bg-red-700 hover:bg-red-800 md:w-auto md:ms-6"
              >
                <span className="xl:block">+ {t('form:button-label-add')}</span>
              </LinkButton>
            </div>
          </div>
        </div>
      </Card>
    </form>
  );
}

export default Search;



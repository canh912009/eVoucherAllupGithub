import { CategoryQueryOptions } from '@/types';
import { useForm } from 'react-hook-form';
import cn from 'classnames';
import { useTranslation } from 'react-i18next';
import LinkButton from '@/components/ui/link-button';
import Card from '../common/card';
import { Routes } from '@/config/routes';
import Button from "@/components/ui/button";

const classes = {
  root: 'ps-4 pe-4 h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0',
  normal:
    'bg-gray-100 border border-border-base focus:shadow focus:bg-light focus:border-accent',
  solid:
    'bg-gray-100 border border-border-100 focus:bg-light focus:border-accent',
  outline: 'border border-border-base focus:border-accent',
  shadow: 'focus:shadow',
};;

type SearchProps = {
  className?: string;
  shadow?: boolean;
  bulkPopup?: boolean;
  variant?: 'normal' | 'solid' | 'outline';
  inputClassName?: string;
  onSearch: (data: CategoryQueryOptions) => void;
};

const ManagerSearch: React.FC<SearchProps> = ({
  className,
  onSearch,
  variant = 'outline',
  shadow = false,
  bulkPopup = false,
  inputClassName,
  ...rest
}) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CategoryQueryOptions>({
    defaultValues: {
      categoryCode: '',
      categoryName: '',
      validYn: '',
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
    <>
      <form
        noValidate
        role="search"
        className={cn('relative w-full items-center', className)}
        onSubmit={handleSubmit(onSearch)}
      >
        <Card className="mb-8 flex flex-wrap p-3 md:p-2 bg-[#EEEEEE]">
          <div className="mb-2 flex  md:w-11/12 flex-wrap">
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
              <div className=" md:mb-0 md:w-1/4">
                <h1 className="text-base font-semibold text-heading">
                  {t('Category Code')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="categoryCode" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="categoryCode"
                  {...register('categoryCode')}
                  className={rootClassName}
                  placeholder={t('Input Category Code')}
                  aria-label="Search"
                  autoComplete="off"
                  {...rest}
                />
              </div>
            </div>

            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
              <div className=" md:mb-0 md:w-1/4">
                <h1 className="text-base font-semibold text-heading">
                  {t('Category Name')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="categoryName" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="categoryName"
                  {...register('categoryName')}
                  className={rootClassName}
                  placeholder={t('Input Category Name')}
                  aria-label="Search"
                  autoComplete="off"
                  {...rest}
                />
              </div>
            </div>
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
      { !bulkPopup && <div className="mb-4 flex w-full flex-wrap">
        <div className="flex w-full flex-col items-center px-4 md:flex-row-reverse">
          <LinkButton
            size="small"
            href={`${Routes?.category.create}`}
            className="w-full bg-black hover:bg-slate-700 md:w-auto md:ms-6"
          >
            <span className="text-xs xl:block">
              {t('Create new')}
            </span>
          </LinkButton>
        </div>
      </div> }
    </>
  )
}

export default ManagerSearch;

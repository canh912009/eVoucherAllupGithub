import cn from 'classnames';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'next-i18next';
import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import { StoreQueryOptions as SearchValue } from '@/types';

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
  onSearch: (data: SearchValue) => void;
};

const ExceptedStoresSearch: React.FC<SearchProps> = ({
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
      storeId: '',
      storeName: '',
      region: '',
      supplierName: '',
      brandName: '',
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
        <Card className="mb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:p-2 md:pb-4">
          <div className="flex w-full flex-wrap md:w-[90%]">
            <div className="mb-4 flex w-full flex-wrap">
              <div className="flex w-full flex-col items-center px-4 md:w-1/3 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Store name')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="storeName" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="storeName"
                    {...register('storeName')}
                    className={rootClassName}
                    placeholder={t('form:input-placeholder-search')}
                    aria-label="Search"
                    autoComplete="off"
                    {...rest}
                  />
                </div>
              </div>
              <div className="flex w-full flex-col items-center px-4 md:w-1/3 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Store Id')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="storeId" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="storeId"
                    {...register('storeId')}
                    className={rootClassName}
                    placeholder={t('form:input-placeholder-search')}
                    aria-label="Search"
                    autoComplete="off"
                    {...rest}
                  />
                </div>
              </div>
              <div className="flex w-full flex-col items-center px-4 md:w-1/3 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">{t('Region')}</h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="region" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="region"
                    {...register('region')}
                    className={rootClassName}
                    placeholder={t('form:input-placeholder-search')}
                    aria-label="Search"
                    autoComplete="off"
                    {...rest}
                  />
                </div>
              </div>
            </div>

            <div className="mb-4 flex w-full flex-wrap">
              <div className="flex w-full flex-col items-center px-4 md:w-1/3 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Supplier name')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
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
                </div>
              </div>
              <div className="flex w-full flex-col items-center px-4 md:w-1/3 md:flex-row">
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
              <div className="flex w-full flex-col items-center px-4 md:w-1/3 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">{t('Active')}</h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="validYn" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <select
                    id="validYn"
                    {...register('validYn')}
                    className={rootClassName}
                  >
                    <option value="">All</option>
                    <option value="Y">Yes</option>
                    <option value="N">No</option>
                  </select>
                </div>
              </div>
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
    </>
  );
};

export default ExceptedStoresSearch;

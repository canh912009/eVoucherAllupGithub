import Select from '@/components/ui/select/select';
import cn from 'classnames';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'next-i18next';
import Card from '@/components/common/card';
import LinkButton from '@/components/ui/link-button';
import { CustomerQueryOptions as SearchValue } from '@/types';
import { Routes } from '@/config/routes';
import Button from "@/components/ui/button";
import { useCodeGroupQuery } from '@/data/code-group';
import { CODE_GROUP } from '@/utils/constants';

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
    setValue,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      customerId: '',
      customerName: '',
      taxcode: '',
      approveStatusCode: '',
      customerTypeCode: '',
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

  const { codeGroup: approveStatusCode, loading: approveStatusCodeLoading } = useCodeGroupQuery(
    CODE_GROUP.APPROVE_STATUS_CD_1
  );

  const { codeGroup: customerType, loading: customerTypeLoading } = useCodeGroupQuery(
    CODE_GROUP.CUSTOMER_TYPE
  );

  const handleChangeApproveStatusCode = (option: any) => {
    setValue('approveStatusCode', option?.codeId);
  };

  const handleChangeCustomerType = (option: any) => {
    setValue('customerTypeCode', option?.codeId);
  };

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
                  {t('Customer name')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="customerName" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="customerName"
                  {...register('customerName')}
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
                <h1 className="font-semibold text-heading">{t('Customer ID')}</h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="customerId" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="customerId"
                  {...register('customerId')}
                  className={rootClassName}
                  placeholder={t('form:input-placeholder-search')}
                  aria-label="Search"
                  autoComplete="off"
                  {...rest}
                />
              </div>
            </div>
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Approve status')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="statusCode" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <div className="w-full">
                  <Select
                    id="statusCode"
                    isClearable={true}
                    getOptionLabel={(option: any) => option?.codeName}
                    getOptionValue={(option: any) => option?.codeId}
                    onChange={handleChangeApproveStatusCode}
                    options={approveStatusCode?.codes ?? []}
                    isLoading={approveStatusCodeLoading}
                  />
                </div>
              </div>
            </div>
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">{t('Tax code')}</h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="taxcode" className="sr-only">
                  {t('Tax code')}
                </label>
                <input
                  type="text"
                  id="taxcode"
                  {...register('taxcode')}
                  className={rootClassName}
                  placeholder={t('form:input-placeholder-search')}
                  aria-label="Search"
                  autoComplete="off"
                  {...rest}
                />
              </div>
            </div>
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row mt-2">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">
                  {t('Customer type')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="customerType" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <div className="w-full">
                  <Select
                    id="customerType"
                    isClearable={true}
                    getOptionLabel={(option: any) => option?.codeName}
                    getOptionValue={(option: any) => option?.codeId}
                    onChange={handleChangeCustomerType}
                    options={customerType?.codes ?? []}
                    isLoading={customerTypeLoading}
                  />
                </div>
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
      <div className="mb-4 flex w-full w-full flex-wrap">
        <div className="flex w-full flex-col items-center px-4 md:flex-row-reverse">
          <LinkButton
            size="small"
            href={`${Routes?.customers.create}`}
            className="w-full md:w-auto md:ms-6 bg-black hover:bg-slate-700"
          >
            <span className="text-xs xl:block">
              {t('Create new')}
            </span>
          </LinkButton>
        </div>
      </div>
    </>

  );
};

export default Search;

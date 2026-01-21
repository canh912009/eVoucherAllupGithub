import cn from 'classnames';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'next-i18next';
import Card from '@/components/common/card';
import LinkButton from '@/components/ui/link-button';
import { CustomerContractQueryOptions as SearchValue } from '@/types';
import { Routes } from '@/config/routes';
import Select from '@/components/ui/select/select';
import Button from '@/components/ui/button';
import { APPROVE_STATUS_CODES } from '@/components/common/approve-status-code-badge';

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
    setValue,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      contractId: '',
      contractName: '',
      customerId: '',
      customerName: '',
      approveStatusCode: '',
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

  function onChangeCustomerContractStatus(newValue: any) {
    return setValue('approveStatusCode', newValue.code);
  }

  return (
    <>
      <form
        noValidate
        role="search"
        className={cn('relative w-full items-center', className)}
        onSubmit={handleSubmit(onSearch)}
      >
        <Card className="mb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:p-2">
          <div className="flex w-full flex-wrap md:w-[90%]">
            <div className="mb-4 flex w-full flex-wrap">
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:w-1/4">
                  <h1 className="text-sm font-semibold text-heading">
                    {t('Customer contract name')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="contractName" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="contractName"
                    {...register('contractName')}
                    className={rootClassName}
                    placeholder={t('form:input-placeholder-search')}
                    aria-label="Search"
                    autoComplete="off"
                    {...rest}
                  />
                </div>
              </div>
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:w-1/4">
                  <h1 className="text-sm font-semibold text-heading">
                    {t('Customer contract ID')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="contractId" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="contractId"
                    {...register('contractId')}
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
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:w-1/4">
                  <h1 className="text-sm font-semibold text-heading">
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
                <div className="mb-4 md:w-1/4">
                  <h1 className="text-sm font-semibold text-heading">
                    {t('Customer ID')}
                  </h1>
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
            </div>

            <div className="mb-4 md:mb-0 flex w-full flex-wrap">
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:w-1/4">
                  <h1 className="text-sm font-semibold text-heading">
                    {t('Approve status')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="approveStatusCode" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <div className="w-full">
                    <Select
                      id="approveStatusCode"
                      name="approveStatusCode"
                      options={APPROVE_STATUS_CODES}
                      getOptionLabel={(option: any) => option.text}
                      getOptionValue={(option: any) => option.code}
                      placeholder={t('form:Status')}
                      onChange={onChangeCustomerContractStatus}
                    />
                  </div>
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
      <div className="mb-4 flex w-full flex-wrap">
        <div className="flex w-full flex-col items-center px-4 md:flex-row-reverse">
          <LinkButton
            size="small"
            href={`${Routes?.customerContracts.create}`}
            className="w-full bg-black hover:bg-slate-700 md:w-auto md:ms-6"
          >
            <span className="text-xs xl:block">
              {t('New customer contract')}
            </span>
          </LinkButton>
        </div>
      </div>
    </>
  );
};

export default Search;

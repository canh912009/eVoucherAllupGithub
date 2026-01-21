import cn from 'classnames';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'next-i18next';
import Card from '@/components/common/card';
import Select from '@/components/ui/select/select';
import Button from '@/components/ui/button';
import LinkButton from '@/components/ui/link-button';
import { CampaignQueryOptions as SearchValue } from '@/types';
import { Routes } from '@/config/routes';
import { STATUS_CODES } from '@/components/common/status-code-badge';

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
    reset,
    setValue,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      campaignId: '',
      campaignName: '',
      customerName: '',
      statusCode: '',
    },
  });

  const { t } = useTranslation();

  function onChangeCampaignStatus(newValue: any) {
    return setValue('statusCode', newValue.code);
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

  function clear() {
    reset();
    onSearch({
      campaignId: '',
      campaignName: '',
      customerName: '',
      statusCode: '',
    });
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
            <div className="mb-4 flex w-full flex-wrap">
              <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                <div className="mb-4 md:mb-0 md:w-1/4">
                  <h1 className="font-semibold text-heading">
                    {t('Campaign name')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="campaignName" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="campaignName"
                    {...register('campaignName')}
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
                    {t('Campaign ID')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="campaignId" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <input
                    type="text"
                    id="campaignId"
                    {...register('campaignId')}
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
                  <h1 className="font-semibold text-heading">
                    {t('Status')}
                  </h1>
                </div>
                <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                  <label htmlFor="campaignStatus" className="sr-only">
                    {t('form:input-label-search')}
                  </label>
                  <div className="w-full">
                    <Select
                      id="campaignStatus"
                      name="campaignStatus"
                      options={STATUS_CODES}
                      getOptionLabel={(option: any) => option.text}
                      getOptionValue={(option: any) => option.code}
                      placeholder={t('form:select-placeholder-search')}
                      onChange={onChangeCampaignStatus}
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
      {/*<div className="mb-4 flex w-full flex-wrap">
        <div className="flex w-full flex-col items-center px-4 md:flex-row-reverse">
          <LinkButton
            size="small"
            href={`${Routes?.campaigns.create}`}
            className="w-full bg-black hover:bg-slate-700 md:w-auto md:ms-6"
          >
            <span className="text-xs xl:block">
              {t('Create new')}
            </span>
          </LinkButton>
        </div>
      </div>*/}
    </>
  );
};

export default Search;

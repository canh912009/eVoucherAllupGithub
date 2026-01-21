import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import Input from '@/components/ui/input';
import { VNPTEPayProvidersQueryOptions as SearchValue } from '@/types';
import cn from 'classnames';
import { useTranslation } from 'next-i18next';
import { useForm } from 'react-hook-form';
import LinkButton from "@/components/ui/link-button";
import {Routes} from "@/config/routes";

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
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      providerCode: '',
      providerName: '',
    },
  });

  const { t } = useTranslation();

  function clear() {
    console.log('clear');
    reset();
    onSearch({ providerCode: '', providerName: '',   validYn: '' });
  }

  const classes = {
    root: 'ps-4 pe-4 h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0',
    normal:
      'bg-gray-100 border border-border-base focus:shadow focus:bg-light focus:border-accent',
    solid:
      'bg-gray-100 border border-border-100 focus:bg-light focus:border-accent',
    outline: 'border border-border-base focus:border-accent',
    shadow: 'focus:shadow',
  };;


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
                  {t('Provider Code')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="providerCode" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="providerCode"
                  {...register('providerCode')}
                  className={rootClassName}
                  placeholder={t('Input Provider Code')}
                  aria-label="Search"
                  autoComplete="off"
                  {...rest}
                />
              </div>
            </div>

            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
              <div className=" md:mb-0 md:w-1/4">
                <h1 className="text-base font-semibold text-heading">
                  {t('Provider Name')}
                </h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="providerName" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="providerName"
                  {...register('providerName')}
                  className={rootClassName}
                  placeholder={t('Input Provider Name')}
                  aria-label="Search"
                  autoComplete="off"
                  {...rest}
                />
              </div>
            </div>
          </div>

          <div className="mb-4 flex md:w-1/12 flex-wrap mr-0">
            <Button
              className="w-full bg-red-500 hover:bg-red-600  rounded-xl"
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

export default Search;

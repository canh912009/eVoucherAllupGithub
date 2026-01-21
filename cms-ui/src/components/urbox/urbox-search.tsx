import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import { UrBoxBrandQueryOptions as SearchValue } from '@/types';
import cn from 'classnames';
import { useTranslation } from 'next-i18next';
import { useForm } from 'react-hook-form';

const classes = {
  root: 'h-12 flex items-center w-full rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0',
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
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      brandName: '',
      categoryTitle: '',
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

  function clear() {
    console.log('clear');
    reset();
    onSearch({ brandName: '', categoryTitle: '' });
  }

  return (
    <form
      noValidate
      role="search"
      className={cn('relative w-full items-center', className)}
      onSubmit={handleSubmit(onSearch)}
    >
      <Card className="mb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:p-2 md:pb-4">
        <div className="flex w-full flex-wrap md:w-[90%]">
          <div className="mb-4 flex w-full flex-wrap">
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
            <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
              <div className="mb-4 md:mb-0 md:w-1/4">
                <h1 className="font-semibold text-heading">{t('Category')}</h1>
              </div>
              <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                <label htmlFor="categoryTitle" className="sr-only">
                  {t('form:input-label-search')}
                </label>
                <input
                  type="text"
                  id="categoryTitle"
                  {...register('categoryTitle')}
                  className={rootClassName}
                  placeholder={t('form:input-placeholder-search')}
                  aria-label="Search"
                  autoComplete="off"
                  {...rest}
                />
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
  );
};

export default Search;

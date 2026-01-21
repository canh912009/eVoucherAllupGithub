import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import Input from '@/components/ui/input';
import { VNPTEPayProvidersQueryOptions as SearchValue } from '@/types';
import cn from 'classnames';
import { useTranslation } from 'next-i18next';
import { useForm } from 'react-hook-form';

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
      brandId: '',
      brandTitle: '',
    },
  });

  const { t } = useTranslation();

  function clear() {
    console.log('clear');
    reset();
    onSearch({ brandId: '', brandTitle: '' });
  }

  return (
    <form
      noValidate
      role="search"
      className={cn('relative w-full items-center', className)}
      onSubmit={handleSubmit(onSearch)}
    >
      <Card className="mb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:!p-4 md:!pt-6">
        <div className="mb-4 flex w-full flex-wrap">
          <div className="w-full px-4 md:w-[20%]">
            <h3 className="pt-6 text-xl font-semibold text-heading">
              {t('PIN Stock List')}
            </h3>
          </div>
          <Input
            label={t('Brand Title')}
            {...register('brandTitle')}
            placeholder="Add text..."
            type="text"
            variant="outline"
            className="w-full px-4 md:w-1/4"
            error={t(errors.brandTitle?.message!)}
          />
          <Input
            label={t('Brand ID')}
            {...register('brandId')}
            placeholder="Add text..."
            type="text"
            variant="outline"
            className="w-full px-4 md:w-1/4"
            error={t(errors.brandId?.message!)}
          />
          <div className="w-full px-4 md:w-[30%]">
            <div className="pt-6">
              <Button
                className="bg-green-500 hover:bg-green-700"
                aria-label="Search"
              >
                {t('Filter')}
              </Button>
              <Button
                className="ml-2 bg-gray-500 hover:bg-gray-700"
                aria-label="Search"
                onClick={clear}
              >
                {t('Clear filter')}
              </Button>
            </div>
          </div>
        </div>
      </Card>
    </form>
  );
};

export default Search;

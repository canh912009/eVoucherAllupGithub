import Button from '@/components/ui/button';
import ErrorMessage from '@/components/ui/error-message';
import Loader from '@/components/ui/loader/loader';
import { useGoodsQuery } from '@/data/good';
import {
  CommonYesNoEnum,
  Good,
  GoodQueryOptions as SearchValue,
} from '@/types';
import { PAGE_SIZE, SYSTEM_BRAND_TYPE } from '@/utils/constants';
import cn from 'classnames';
import { useTranslation } from 'next-i18next';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useModalState } from '../ui/modal/modal.context';
import CampaignGoodsList from './campaign-product-list';
import Select from '@/components/ui/select/select';
import {
  SYSTEM_GROUP_ALL,
  SYSTEM_GROUP_CHOICE_BULK,
  SYSTEM_GROUP_IN_EX
} from '@/components/common/status-code-badge';

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
  selectedProducts?: Good[];
};

const CampaignSearchProductList: React.FC<SearchProps> = ({
  className,
  variant = 'outline',
  shadow = false,
  inputClassName,
  selectedProducts,
  ...rest
}) => {
  const { t } = useTranslation();
  const { data } = useModalState();
  // console.log('data', data);

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

  let systemSearch = '';
  if (typeof selectedProducts !== 'undefined' && selectedProducts.length > 0) {
    systemSearch =
      selectedProducts[0].system === SYSTEM_BRAND_TYPE.CHOICE || selectedProducts[0].system === SYSTEM_BRAND_TYPE.BULK
        ? SYSTEM_GROUP_CHOICE_BULK.map(item => item.text).join(',')
        : SYSTEM_GROUP_IN_EX.map(item => item.text).join(',');
  }

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      goodsId: '',
      goodsName: '',
      validYn: CommonYesNoEnum.YES,
      isExpired: CommonYesNoEnum.NO,
      systems: systemSearch, // note : systems not system
    },
  });

  const [selectedItems, setSelectedItems] = useState<Good[]>([]);
  const [searchOptions, setSearchOptions] = useState<Partial<SearchValue>>({
    validYn: CommonYesNoEnum.YES,
    isExpired: CommonYesNoEnum.NO,
    systems: systemSearch, // note : systems not system
  });
  const [page, setPage] = useState(1);

  const { goods, loading, paginatorInfo, error } = useGoodsQuery(
    { page, pageSize: PAGE_SIZE },
    searchOptions
  );

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  function handleSearch(data: Partial<SearchValue>) {
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  function handleSaveProducts() {
    // console.log('selectedItems', selectedItems);
    return data?.handleSaveItems(selectedItems);
  }
  function onChangeSystem(newValue: any) {
    // setSelectedItems([])
    return setValue('system', newValue.code);
  }

  return (
    <div className="m-auto w-full max-w-full rounded-md bg-light p-8 pb-6 sm:min-w-[80rem] md:rounded-xl">
      <div className="h-full w-full text-center">
        <div className="mb-4 flex h-full flex-col justify-between rounded-2xl bg-gray-200 p-4 ">
          <form
            noValidate
            role="search"
            className={cn('relative w-full items-center', className)}
            onSubmit={handleSubmit(handleSearch)}
          >
            <div className="flex flex-wrap pb-4">
              <div className="flex w-full flex-wrap md:w-[90%]">
                <div className="mb-4 flex w-full flex-wrap">
                  <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                    <div className="mb-4 md:mb-0 md:w-1/4">
                      <h1 className="font-semibold text-heading">
                        {t('Product name')}
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
                  <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                    <div className="mb-4 md:mb-0 md:w-1/4">
                      <h1 className="font-semibold text-heading">
                        {t('Product ID')}
                      </h1>
                    </div>
                    <div className="flex w-full flex-col items-center ms-auto md:w-3/4">
                      <label htmlFor="goodsId" className="sr-only">
                        {t('form:input-label-search')}
                      </label>
                      <input
                        type="text"
                        id="goodsId"
                        {...register('goodsId')}
                        className={rootClassName}
                        placeholder={t('form:input-placeholder-search')}
                        aria-label="Search"
                        autoComplete="off"
                        {...rest}
                      />
                    </div>
                  </div>
                </div>
                <div className=" flex w-full w-full flex-wrap">
                  <div className="flex w-full flex-col items-center px-4 md:w-1/2 md:flex-row">
                    <div className="mb-4 md:mb-0 md:w-1/4">
                      <h1 className="font-semibold text-heading">
                        {t('System')}
                      </h1>
                    </div>
                    <div className="flex w-full flex-col items-start ms-auto md:w-3/4">
                      <label htmlFor="validYn" className="sr-only">
                        {t('form:input-label-search')}
                      </label>
                      <div className="w-full">
                        <Select
                            id="system"
                            name="system"
                            options={ selectedProducts && selectedProducts?.length > 0
                                ? selectedProducts[0].system === SYSTEM_BRAND_TYPE.CHOICE || selectedProducts[0].system === SYSTEM_BRAND_TYPE.BULK
                                    ? SYSTEM_GROUP_CHOICE_BULK
                                    : SYSTEM_GROUP_IN_EX
                                : SYSTEM_GROUP_ALL }
                            getOptionLabel={(option: any) => option.text}
                            getOptionValue={(option: any) => option.code}
                            placeholder={'All'}
                            onChange={onChangeSystem}
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
            </div>
          </form>
        </div>
        <CampaignGoodsList
          data={goods}
          paginatorInfo={paginatorInfo}
          onPagination={handlePagination}
          selectedItems={selectedItems}
          setSelectedItems={setSelectedItems}
        />
        <div className="mt-2 flex flex-row-reverse flex-wrap">
          <Button
            size="medium"
            className="bg-red-600 hover:bg-red-700"
            onClick={handleSaveProducts}
          >
            {t('Save')}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default CampaignSearchProductList;

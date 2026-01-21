import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import { DatePicker } from '@/components/ui/date-picker';
import Input from '@/components/ui/input';
import Label from '@/components/ui/label';
import Select from '@/components/ui/select/select';
import { useCodeGroupQuery } from '@/data/code-group';
import { CsExtendQueryOptions as SearchValue, SortOrder} from '@/types';
import { CODE_GROUP } from '@/utils/constants';
import cn from 'classnames';
import { format } from 'date-fns';
import { useTranslation } from 'next-i18next';
import { useRef, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { csClient } from '@/data/client/crud-client';
import { API_ENDPOINTS } from '@/data/client/api-endpoints';
import { parse } from 'json2csv';

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
    control,
    getValues,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      publishId: '',
      publishName: '',
      customerId: '',
      customerName: '',
      targetName: '',
      targetNumber: '',
      ev: '',
      requestStatus: '',
    },
  });

  const { t } = useTranslation();
  const csExtendStatusCode = [
    { codeId : "EXTEND", codeName : "EXTEND ", },
    // { codeId : "RESEND", codeName : "RESEND", },
    // { codeId : "DISABLE", codeName : "DISABLE", },    // sau có thể thêm RESEND/ DISABLE, etc
  ]
  const selectInputRef = useRef<any>(null);
  const handleChangeRequestType = (option: any) => {
    setValue('requestStatus', option?.codeId);
  };

  function clear() {
    selectInputRef.current?.clearValue();
    reset();
    onSearch({
      publishId: '',
      publishName: '',
      customerId: '',
      customerName: '',
      targetName: '',
      targetNumber: '',
      ev: '',
      requestStatus: '',
    });
  }

  return (
    <form
      noValidate
      role="search"
      className={cn('relative w-full items-center', className)}
      onSubmit={handleSubmit(onSearch)}
    >
      <Card className="mb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:!p-4 md:!pt-6">
        <div className="mb-2 flex w-full flex-wrap">
          <div className="flex w-full flex-wrap p-1 md:w-1/2">
            <div className="flex w-full flex-wrap rounded border border-gray-400">
              <Input
                label={t('Delivery ID')}
                {...register('publishId')}
                placeholder="Add id number..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.publishId?.message!)}
              />
              <Input
                label={t('Delivery Name')}
                {...register('publishName')}
                placeholder="Add text..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.publishName?.message!)}
              />
              <Input
                label={t('Customer ID')}
                {...register('customerId')}
                placeholder="Add id number..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.customerId?.message!)}
              />
              <Input
                label={t('Customer Name')}
                {...register('customerName')}
                placeholder="Add text..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.customerName?.message!)}
              />
            </div>
          </div>
          <div className="flex w-full flex-wrap p-1 md:w-1/2">
            <div className="flex w-full flex-wrap rounded border border-gray-400">
              <Input
                label={t('Target Name')}
                {...register('targetName')}
                placeholder='Add target name (multi ",")...'
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.targetName?.message!)}
              />
              <Input
                label={t('Target Number')}
                {...register('targetNumber')}
                placeholder='Add target number (multi ",")...'
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.targetNumber?.message!)}
              />
              <Input
                label={t('Voucher UUID')}
                {...register('ev')}
                placeholder="Add text..."
                type="text"
                variant="outline"
                className="w-full px-2 py-2 md:w-1/2"
                error={t(errors.ev?.message!)}
              />
              <div className="w-full px-2 py-2 md:w-1/2">
                <Label>{t('REQUEST TYPE')}</Label>
                <Select
                  ref={selectInputRef}
                  isClearable={true}
                  getOptionLabel={(option: any) => option?.codeName}
                  getOptionValue={(option: any) => option?.codeId}
                  onChange={handleChangeRequestType}
                  options={csExtendStatusCode}
                />
              </div>
            </div>
          </div>
        </div>

        <div className="flex w-full justify-end">
          <div className="pt-2">
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
      </Card>
    </form>
  );
};

export default Search;

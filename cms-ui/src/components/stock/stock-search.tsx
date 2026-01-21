import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import { StockQueryOptions as SearchValue} from '@/types';
import cn from 'classnames';
import { useTranslation } from 'next-i18next';
import {Controller, useForm} from 'react-hook-form';
import {DatePicker} from "@/components/ui/date-picker";
import {format} from "date-fns";
import React from "react";

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
  onSearchCSV: (data: SearchValue) => void;
  onDownCSV: (data: SearchValue) => void;
};

const Search: React.FC<SearchProps> = ({
                                         className,
                                         onSearchCSV,
                                         onDownCSV,
                                         variant = 'outline',
                                         shadow = false,
                                         inputClassName,
                                         ...rest
                                       }) => {

  const { t } = useTranslation();
  const currentDate = format(new Date(), 'yyyy-MM-dd');
  const {
    register,
    handleSubmit,
    reset,
    control,
    formState: { errors },
  } = useForm<SearchValue>({
    defaultValues: {
      insertedAtFrom: currentDate
    },
  });

  return (
    <form
      noValidate
      role="search"
      className={cn('relative w-full items-center', className)}
      onSubmit={handleSubmit(onDownCSV)}
    >
      <div className="mb-4 flex items-center  ">
        {/* Empty left half */}
        <div className="w-3/4"></div>

        {/* Right half with content in a single row */}
        <div className="flex w-1/4 flex-row items-center space-x-2 ">
          <Controller
            control={control}
            name="insertedAtFrom"
            render={({field: {onChange, onBlur, value}}) => (
              <div className="flex items-center gap-2  p-2 bg-gray-200 rounded-md">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                  <line x1="16" y1="2" x2="16" y2="6"></line>
                  <line x1="8" y1="2" x2="8" y2="6"></line>
                  <line x1="3" y1="10" x2="21" y2="10"></line>
                </svg>
                <DatePicker
                  dateFormat="yyyy-MM-dd"
                  onChange={(date) => {
                    if (date) {
                      const selectedDate = new Date(date);
                      const formattedDate = format(selectedDate, 'yyyy-MM-dd');
                      onChange(formattedDate);

                      onSearchCSV({
                        insertedAtFrom: formattedDate
                      });
                    } else {
                      onChange(null);
                    }
                  }}
                  onBlur={onBlur}
                  selected={value ? new Date(value) : null}
                  selectsStart
                  startDate={new Date()}
                  className="border border-border-base text-lg font-bold "
                  placeholderText={currentDate}
                />
              </div>

            )}
          />
          <Button
            className="bg-red-700 hover:bg-red-800"
            aria-label="downloadCSV"
          >
            {t('Download CSV')}
          </Button>
        </div>
      </div>
    </form>
  );
};

export default Search;
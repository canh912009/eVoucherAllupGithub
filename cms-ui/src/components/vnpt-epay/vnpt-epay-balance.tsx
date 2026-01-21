import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import Input from '@/components/ui/input';
import { vnptEpayBalanceClient } from '@/data/client/crud-client';
import cn from 'classnames';
import { useTranslation } from 'next-i18next';
import { useForm } from 'react-hook-form';
import { VNPTEPayBalance } from '@/types';
import { formatCurrencyVND } from '@/utils/common-utils';
import {useBalanceQuery, usePINListQuery} from "@/data/vnpt-epay";
import {PAGE_SIZE} from "@/utils/constants";
import {useEffect} from "react";

type SearchProps = {
  className?: string;
  shadow?: boolean;
  variant?: 'normal' | 'solid' | 'outline';
  inputClassName?: string;
  // onSearch: (data: {}) => void;
};

const VNPTEPayBalanceComponent: React.FC<SearchProps> = ({
  className,
  // onSearch,
  variant = 'outline',
  shadow = false,
  inputClassName,
  ...rest
}) => {
  const {
    register,
    handleSubmit,
    setValue,
    reset,
    formState: { errors },
  } = useForm<{ balance: string }>({
    defaultValues: {},
  });

  const { t } = useTranslation();

  // const fetchBalanceData = async () => {
  //   try {
  //     const response = await vnptEpayBalanceClient.get('');
  //     // console.log('balance', response.data);
  //     const data: VNPTEPayBalance = response.data;
  //     setValue('balance', formatCurrencyVND(data.balance));
  //   } catch (error) {
  //     console.error('Error fetching data:', error);
  //   }
  // };
  const { balance } = useBalanceQuery();
  const onSubmit = async (values: {}) => {
    // await fetchBalanceData()
    setValue('balance', formatCurrencyVND(balance as number) );
  };

  useEffect(() => {
    setValue('balance', formatCurrencyVND(balance as number) );
  }, [balance]);

  return (
    <form
      noValidate
      role="search"
      className={cn('relative w-full items-center', className)}
      onSubmit={handleSubmit(onSubmit)}
    >
      <Card className="mb-4 flex flex-wrap bg-[#EEEEEE] p-3 md:!p-4 md:!pt-6">
        <div className="mb-4 flex w-full flex-wrap">
          <div className="w-full px-4 md:w-[20%]">
            <h3 className="pt-6 text-xl font-semibold text-heading">
              {t('Balance')}
            </h3>
          </div>
          <Input
            label={t('Balance')}
            {...register('balance')}
            type="text"
            variant="outline"
            disabled={true}
            className="w-full px-4 md:w-1/4"
          />
          <div className="w-full px-4 md:w-1/4">
            <div className="pt-6">
              <Button
                className="bg-orange-600 hover:bg-orange-700"
                aria-label="Search"
              >
                {t('Get Info Balance')}
              </Button>
            </div>
          </div>
        </div>
      </Card>
    </form>
  );
};

export default VNPTEPayBalanceComponent;

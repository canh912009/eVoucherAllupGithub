import { useTranslation } from 'next-i18next';
import { Supplier } from '@/types';
import Link from '@/components/ui/link';
import { IosArrowLeft } from '@/components/icons/ios-arrow-left';
import { Routes } from '@/config/routes';
import Badge from '@/components/ui/badge/badge';
import SwitchInput from '@/components/ui/switch-input';
import Control from 'react-select/dist/declarations/src/components/Control';

type IProps = {
  data?: Supplier | null;
};

export default function SupplierDetail({ data }: IProps) {
  const { t } = useTranslation();
  
  control: () => undefined;

  let classes = {
    title: 'font-semibold',
    content: 'font-normal text-[#212121]',
  };

  return (
    <div className="rounded bg-white px-8 py-10 shadow">
      <div className="mb-5">
        <Link
          href={`${Routes?.suppliers.list}`}
          className="flex items-center font-bold text-accent no-underline transition-colors duration-200 ms-1 hover:text-accent-hover hover:underline focus:text-accent-700 focus:no-underline focus:outline-none"
        >
          <IosArrowLeft height={12} width={15} className="mr-2.5" />
          {t('common:text-back-to-home')}
        </Link>
      </div>

      <h3 className="mb-6 text-[22px] font-bold">
        {t('Taxcode')}: {data?.taxcode}
      </h3>
      <h3 className="mb-6 text-[22px] font-bold">
        {t('Supplier name')}: {data?.supplierName}
      </h3>
      <h3 className="mb-6 text-[16px] font-bold">
        {t('Status')}:{' '}
        <>
          {data?.approveStatusCode === 'REQ' && (
            <Badge text="Requesting" color="bg-yellow-600" />
          )}
          {data?.approveStatusCode === 'APPRV' && (
            <Badge text="Approved" color="bg-accent" />
          )}
          {data?.approveStatusCode === 'REJCT' && (
            <Badge text="Rejected" color="bg-red-800" />
          )}
        </>
      </h3>

      <ul className={`space-y-3.5 ${classes?.content}`}>
        <li>
          <strong className={classes?.title}>{t('Bank name')}: </strong>
          {data?.bankName}
        </li>
        <li>
          <strong className={classes?.title}>{t('Account name')}: </strong>
          {data?.accountNumber}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('Supply discount rate')}:{' '}
          </strong>
          {data?.supplyDiscountRate}
        </li>
        <li>
          <strong className={classes?.title}>{t('Supply comission rate')}: </strong>
          {data?.supplyCommissionRate}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('VAT')}:{' '}
          </strong>
          {data?.vatIncludeYn === 'Y' ? 'Yes' : 'No'}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('Manager name')}:{' '}
          </strong>
          {data?.managerName}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('Manager email')}:{' '}
          </strong>
          {data?.managerEmail}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('Manager mobile number')}:{' '}
          </strong>
          {data?.managerMobileNumber}
        </li>
        <li>
          <strong className={classes?.title}>{t('Primary contact name')}: </strong>
          {data?.primaryContactName}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('Primary contact email')}:{' '}
          </strong>
          {data?.primaryContactEmail}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('Primary contact mobile')}:{' '}
          </strong>
          {data?.primaryContactMobile}
        </li>
      </ul>
    </div>
  );
}

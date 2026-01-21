import { useTranslation } from 'next-i18next';
import { Customer } from '@/types';
import Link from '@/components/ui/link';
import { IosArrowLeft } from '@/components/icons/ios-arrow-left';
import { Routes } from '@/config/routes';
import Description from '@/components/ui/description';
import Card from '@/components/common/card';
import SwitchDisplay from '@/components/ui/switch-display';
import ApproveStatusCodeBadge from '../common/approve-status-code-badge';

type IProps = {
  data?: Customer | null;
};

export default function CustomerDetail({ data }: IProps) {
  const { t } = useTranslation();

  let classes = {
    wrapper:
      'flex flex-wrap pb-8 my-5 border-b border-dashed border-border-base sm:my-8',
    side_left: 'w-full px-0 pb-5 sm:w-1/4 sm:py-8 sm:pe-4 md:w-1/4 md:pe-5',
    side_right: 'w-full sm:w-3/4 md:w-3/4',
    row: 'mb-1 flex flex-wrap',
    title: 'w-full md:w-1/4 px-4 py-2 font-semibold text-heading',
    content: 'w-full md:w-3/4 px-4 py-2',
  };

  return (
    <>
      <div className={classes?.wrapper}>
        <div className="mb-5">
          <Link
            href={`${Routes?.customers.list}`}
            className="flex items-center font-bold text-accent no-underline transition-colors duration-200 ms-1 hover:text-accent-hover hover:underline focus:text-accent-700 focus:no-underline focus:outline-none"
          >
            <IosArrowLeft height={12} width={15} className="mr-2.5" />
            {t('common:text-back-to-home')}
          </Link>
        </div>
      </div>

      <div className={classes?.wrapper}>
        <Description
          title={t('Customer Informations')}
          details={t('Customer Informations')}
          className={classes?.side_left}
        />
        <Card className={classes?.side_right}>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Status')}</label>
            <>
              {
                <ApproveStatusCodeBadge
                  approveStatusCode={data?.approveStatusCode}
                  className="py-3 font-medium"
                />
              }
            </>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Customer name')}</label>
            <span className={classes?.content}>{data?.customerName}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Tax code')}</label>
            <span className={classes?.content}>{data?.taxcode}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Bank name')}</label>
            <span className={classes?.content}>{data?.bankName}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Account number')}</label>
            <span className={classes?.content}>{data?.accountNumber}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Account name')}</label>
            <span className={classes?.content}>{data?.accountName}</span>
          </div>
        </Card>
      </div>

      <div className={classes?.wrapper}>
        <Description
          title={t('Sell Informations')}
          details={t('Sell Informations')}
          className={classes?.side_left}
        />
        <Card className={classes?.side_right}>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('sellDiscountRate')}</label>
            <span className={classes?.content}>{data?.sellDiscountRate}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('sellCommissionRate')}</label>
            <span className={classes?.content}>{data?.sellCommissionRate}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('vatIncludeYn')}</label>
            <span className={classes?.content}>
              <SwitchDisplay checked={data?.vatIncludeYn === 'Y'} />
            </span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>
              {t('settlementMethodCode')}
            </label>
            <span className={classes?.content}>
              {data?.settlementMethodCode}
            </span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('sendCost')}</label>
            <span className={classes?.content}>{data?.sendCost}</span>
          </div>
        </Card>
      </div>

      <div className={classes?.wrapper}>
        <Description
          title={t('Manager Informations')}
          details={t('Manager Informations')}
          className={classes?.side_left}
        />
        <Card className={classes?.side_right}>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Manager name')}</label>
            <span className={classes?.content}>{data?.managerName}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Manager email')}</label>
            <span className={classes?.content}>{data?.managerEmail}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>{t('Manager mobile')}</label>
            <span className={classes?.content}>{data?.managerMobileNo}</span>
          </div>
        </Card>
      </div>

      <div className={classes?.wrapper}>
        <Description
          title={t('Primary Contact Informations')}
          details={t('Primary Contact Informations')}
          className={classes?.side_left}
        />
        <Card className={classes?.side_right}>
          <div className={classes?.row}>
            <label className={classes?.title}>
              {t('Primary contact name')}
            </label>
            <span className={classes?.content}>{data?.primaryContactName}</span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>
              {t('Primary contact email')}
            </label>
            <span className={classes?.content}>
              {data?.primaryContactEmail}
            </span>
          </div>
          <div className={classes?.row}>
            <label className={classes?.title}>
              {t('Primary contact mobile')}
            </label>
            <span className={classes?.content}>
              {data?.primaryContactMobileNo}
            </span>
          </div>
        </Card>
      </div>
    </>
  );
}

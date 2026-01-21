import { Brands } from "@/types";
import { useTranslation } from 'next-i18next';
import Image from 'next/image';
import Link from "../ui/link";
import { Routes } from "@/config/routes";
import { IosArrowLeft } from "../icons/ios-arrow-left";
import Badge from "../ui/badge/badge";
import Layout from '@/components/layouts/owner';
import { getUrlPublicAsset } from '@/data/download';
import { emptyPlaceholder } from '@/utils/placeholders';

type IProps = {
  data?: Brands | null;
};

export default function BrandDetail({ data }: IProps) {
  const { t } = useTranslation();
  let classes = {
    title: 'font-semibold',
    content: 'font-normal text-[#212121]',
  };

  return (
    <div className="rounded bg-white px-8 py-10 shadow">
      <div className="mb-5">
        <Link
          href={`${Routes?.brands.list}`}
          className="flex items-center font-bold text-accent no-underline transition-colors duration-200 ms-1 hover:text-accent-hover hover:underline focus:text-accent-700 focus:no-underline focus:outline-none"
        >
          <IosArrowLeft height={12} width={15} className="mr-2.5" />
          {t('common:text-back-to-home')}
        </Link>
      </div>

      <h3 className="mb-6 text-[22px] font-bold">
        {t('Brand Name')}: {data?.brandName}
      </h3>
      <h3 className="mb-6 text-[16px] font-bold">
        {t('Brand Informtion')}:{' '}
        <>
          {true && (
            <Badge text="Approved" color="bg-accent" />
          )}
        </>
      </h3>

      <ul className={`space-y-3.5 ${classes?.content}`}>
        <li>
          <strong className={classes?.title}>{t('Registration Datetime')}: </strong>
          {data?.regDt}
        </li>
        <li>
          <strong className={classes?.title}>{t('Udpate Datetime')}: </strong>
          {data?.updtDt}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('Brand Description')}:{' '}
          </strong>
          {data?.description}
        </li>
        <li>
          <strong className={classes?.title}>
            {t('Brand image')}:{' '}
          </strong>
          {
            data?.brandImagePath &&
            <img
              src={getUrlPublicAsset(data?.brandImagePath || '') ?? emptyPlaceholder}
              alt={'Brand Logo'}
              width={300}
              height={300}
            />
          }
        </li>
      </ul>
    </div>
  );
}

BrandDetail.layout = Layout;

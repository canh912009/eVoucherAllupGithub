import { useTranslation } from 'next-i18next';
import {Menu } from '@/types';
import Link from '@/components/ui/link';
import { IosArrowLeft } from '@/components/icons/ios-arrow-left';
import { Routes } from '@/config/routes';
import {useRouter} from "next/router";

type IProps = {
  data?: Menu | null;
};

export default function MenuDetail({ data }: IProps) {
  const { t } = useTranslation();
  const router = useRouter()
  control: () => undefined;

  let classes = {
    title: 'font-semibold',
    content: 'font-normal text-[#212121]',
  };

  return (
    <div className="rounded bg-white px-8 py-10 shadow">
      <div className="mb-5">
        <Link
          href="#" onClick={router.back}
          className="flex items-center font-bold text-accent no-underline transition-colors duration-200 ms-1 hover:text-accent-hover hover:underline focus:text-accent-700 focus:no-underline focus:outline-none"
        >
          <IosArrowLeft height={12} width={15} className="mr-2.5" />
          {t('common:text-back-to-home')}
        </Link>
      </div>

      <h3 className="mb-6 text-[22px] font-bold">
        {t('Menu name')}: {data?.menuName}
      </h3>

      <ul className={`space-y-3.5 ${classes?.content}`}>
        <li>
          <strong className={classes?.title}>{t('menu id')}: </strong>
          {data?.id}
        </li>
        <li>
          <strong className={classes?.title}>{t('menuGroup Id')}: </strong>
          {data?.menuGroupId}
        </li>
        <li>
          <strong className={classes?.title}>{t('Sort Order')}: </strong>
          {data?.sortOrder}
        </li>
        <li>
          <strong className={classes?.title}>{t('menu Url')}: </strong>
          {data?.menuUrl}
        </li>
      </ul>

    </div>
  );
}

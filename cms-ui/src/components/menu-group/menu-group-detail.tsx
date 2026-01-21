import { useTranslation } from 'next-i18next';
import {MenuGroup, MenuQueryOptions as SearchValue} from '@/types';
import Link from '@/components/ui/link';
import { IosArrowLeft } from '@/components/icons/ios-arrow-left';
import { Routes } from '@/config/routes';
import MenuList from "@/components/menu/menu-list";

type IProps = {
  data?: MenuGroup | null;
};

export default function MenuGroupDetail({ data }: IProps) {
  const { t } = useTranslation();
  control: () => undefined;

  let classes = {
    title: 'font-semibold',
    content: 'font-normal text-[#212121]',
  };

  return (
    <>
      <div className="rounded bg-white px-8 py-10 shadow">
        <div className="mb-5">
          <Link
            href={`${Routes?.menuGroups.list}`}
            className="flex items-center font-bold text-accent no-underline transition-colors duration-200 ms-1 hover:text-accent-hover hover:underline focus:text-accent-700 focus:no-underline focus:outline-none"
          >
            <IosArrowLeft height={12} width={15} className="mr-2.5" />
            {t('common:text-back-to-home')}
          </Link>
        </div>

        <h3 className="mb-6 text-[22px] font-bold">
          {t('MenuGroup name')}: {data?.menuGroupName}
        </h3>

        <ul className={`space-y-3.5 ${classes?.content}`}>
          <li>
            <strong className={classes?.title}>{t('MenuGroup Id')}: </strong>
            {data?.id}
          </li>
          <li>
            <strong className={classes?.title}>{t('Sort Order')}: </strong>
            {data?.sortOrder}
          </li>
        </ul>
      </div>

      <div className="mt-6">
        <MenuList menus={data?.menus} />
      </div>
    </>
  );
}

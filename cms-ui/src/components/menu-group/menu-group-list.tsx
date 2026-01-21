import React from 'react';
import { MappedPaginatorInfoEV, Supplier } from '@/types';
import {MenuGroup} from "@/types";
import {useTranslation} from "next-i18next";
import {useRouter} from "next/router";
import {Table} from "@/components/ui/table";
import Pagination from "@/components/ui/pagination";
import {PAGE_SIZE} from "@/utils/constants";
import ActionButtons from "@/components/common/action-buttons";
import {Routes} from "@/config/routes";

type IProps = {
  menuGroups: MenuGroup[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

const MenuGroupList = ({ menuGroups, paginatorInfo, onPagination }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();
  const columns = [
    {
      title: t('table:table-item-id'),
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      width: 40,
    },
    {
      title: t('table:table-menu-groups-name'),
      dataIndex: 'menuGroupName',
      key: 'menuGroupName',
      width: 120,
      align: 'left',
      ellipsis: true,
      render: (menuGroupName: string) => (
        <span className="truncate whitespace-nowrap">
          {menuGroupName}
        </span>
      ),
    },
    {
      title: t('table:sortOrder'),
      dataIndex: 'sortOrder',
      key: 'sortOrder',
      width: 40,
      align: 'center',
      ellipsis: true,
      render: (sortOrder: string) => (
        <span className="truncate whitespace-nowrap">
          {sortOrder}
        </span>
      ),
    },
    {
      title: 'validYn',
      dataIndex: 'validYn',
      key: 'validYn',
      width: 40,
      align: 'center',
      ellipsis: true,
      // render: (validYn: string) => (
      //   <span className="truncate whitespace-nowrap">
      //     {validYn}
      //   </span>
      // ),
    },
    {
      title: t('table:table-item-actions'),
      key: 'actions',
      align: 'center',
      width: 100,
      render: (data: MenuGroup) => {
        return (
          <ActionButtons
            id={data?.id}
            editUrl={`${Routes.menuGroups.editWithoutLang(data?.id)}`}
            detailsUrl={Routes?.menuGroups?.details(data?.id)}
            deleteModalView="DELETE_MENU_GROUP"
            customLocale={router?.locale}
          />
        );
      },
    },
  ]

  return (
    <>
      <div className="mb-6 overflow-hidden rounded shadow">
        <Table
          //@ts-ignore
          columns={columns}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">
                {t('table:empty-table-data')}
              </div>
            </div>
          )}
          data={menuGroups.sort((a, b) => a.sortOrder - b.sortOrder)}
          rowKey="id"
          scroll={{ x: 1000 }}
        />
      </div>

      {!!paginatorInfo?.totalCount && (
        <div className="flex items-center justify-end">
          <Pagination
            total={paginatorInfo.totalCount}
            current={paginatorInfo.currentPage}
            pageSize={PAGE_SIZE}
            onChange={onPagination}
          />
        </div>
      )}
    </>
  );
};

export default MenuGroupList;

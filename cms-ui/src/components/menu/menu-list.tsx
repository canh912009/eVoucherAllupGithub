import React, {useState} from 'react';
import {MappedPaginatorInfoEV, Menu} from '@/types';
import {useTranslation} from "next-i18next";
import {useRouter} from "next/router";
import {Table} from "@/components/ui/table";
import Pagination from "@/components/ui/pagination";
import {PAGE_SIZE, PAGE_SIZE_MAX} from "@/utils/constants";
import ActionButtons from "@/components/common/action-buttons";
import {Routes} from "@/config/routes";
import {useMenuGroupsQuery} from "@/data/menu-group";

type IProps = {
  menus: Menu[] | undefined;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination?: (current: number) => void;
};

const MenuList = ({ menus, paginatorInfo, onPagination }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [page, setPage] = useState(1);
  const { menuGroups, loading } = useMenuGroupsQuery({ page, pageSize: PAGE_SIZE_MAX },);
  const columns = [
    {
      title: t('table:table-item-id'),
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      width: 40,
    },
    {
      title: t('table:table-item-group-id'),
      dataIndex: 'menuGroupId',
      key: 'menuGroupId',
      align: 'left',
      width: 80,
      render: (menuGroupId: string) => (
        <span className="truncate whitespace-nowrap">
          {menuGroups.find((menuGroup) => menuGroup.id === menuGroupId)?.menuGroupName}
        </span>
      ),
    },
    {
      title: t('table:table-menu-name'),
      dataIndex: 'menuName',
      key: 'menuName',
      width: 80,
      align: 'left',
      ellipsis: true,
      render: (MenuName: string) => (
        <span className="truncate whitespace-nowrap">
          {MenuName}
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
      title: 'URL',
      dataIndex: 'menuUrl',
      key: 'menuUrl',
      width: 60,
      align: 'left',
      ellipsis: true,
      render: (sortOrder: string) => (
        <span className="truncate whitespace-nowrap">
          {sortOrder}
        </span>
      ),
    },
    {
      title: t('table:table-item-actions'),
      key: 'actions',
      align: 'center',
      width: 100,
      render: (data: Menu) => {
        return (
          <ActionButtons
            id={data?.id}
            editUrl={`${Routes.menus.editWithoutLang(data?.id)}`}
            detailsUrl={Routes?.menus?.details(data?.id)}
            deleteModalView="DELETE_MENU"
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
          data={menus.sort((a, b) => {
            if (a.menuGroupId === b.menuGroupId) {
              return a.sortOrder - b.sortOrder;
            }
            return a.menuGroupId - b.menuGroupId;
          })}
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

export default MenuList;

import React from 'react';
import {MappedPaginatorInfoEV, Role} from '@/types';
import {useTranslation} from "next-i18next";
import {useRouter} from "next/router";
import {Table} from "@/components/ui/table";
import Pagination from "@/components/ui/pagination";
import {PAGE_SIZE} from "@/utils/constants";
import ActionButtons from "@/components/common/action-buttons";
import {Routes} from "@/config/routes";

type IProps = {
  roles: Role[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

const RoleList = ({ roles, paginatorInfo, onPagination }: IProps) => {
  const { t } = useTranslation();
  const router = useRouter();
  const columns = [
    {
      title: t('table:table-role-code'),
      dataIndex: 'roleCode',
      key: 'roleCode',
      align: 'left',
      width: 40,
      render: (roleCode: string) => (
        <span className="truncate whitespace-nowrap">
          {roleCode}
        </span>
      ),
    },
    {
      title: t('table:table-role-name'),
      dataIndex: 'roleName',
      key: 'roleName',
      width: 80,
      align: 'left',
      ellipsis: true,
      render: (roleName: string) => (
        <span className="truncate whitespace-nowrap">
          {roleName}
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
      title: t('table:table-item-actions'),
      key: 'actions',
      align: 'center',
      width: 100,
      render: (data: Role) => {
        return (
          <ActionButtons
            id={data?.roleCode}
            editUrl={`${Routes.roles.editWithoutLang(data?.roleCode)}`}
            detailsUrl={Routes?.roles?.details(data?.roleCode)}
            deleteModalView="DELETE_ROLE"
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
          data={roles?.sort((a, b) => a.sortOrder - b.sortOrder)}
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

export default RoleList;

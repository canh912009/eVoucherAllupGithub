import Pagination from '@/components/ui/pagination';
import { Table } from '@/components/ui/table';
import {Admin, MappedPaginatorInfoEV, UrBoxBrand} from "@/types";
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { PAGE_SIZE } from '@/utils/constants';
import ActionButtons from '../common/action-buttons';
import { Routes } from '@/config/routes';
import Link from "@/components/ui/link";
import {useState} from "react";
import {useModalState} from "@/components/ui/modal/modal.context";
import Radio from "@/components/ui/radio/radio";

type IProps = {
  admins: Admin[] | undefined;
  paginatorInfo: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
  popupType : boolean;
  selectedItems?: Admin[];
  setSelectedItems?: React.Dispatch<React.SetStateAction<Admin[]>>;
};


const AdminsList = ({ admins, paginatorInfo, onPagination, popupType = false, selectedItems, setSelectedItems }: IProps) => {
  const { t } = useTranslation();

  const handleCheckboxChange = (admin: Admin) => {
    setSelectedItems?.([admin]);
  };
  const columns = [
    popupType && {
      title: '',
      width: 30 ,
      align: 'center',
      key: 'index',
      ellipsis: true,
      render: (admin: Admin) =>
        <Radio
          name={`adminCode_${admin.id}`}
          id={`adminCode_${admin.id}`}
          label={t('')}
          checked={selectedItems?.some((item) => item.id === admin.id)}
          onChange={() => handleCheckboxChange(admin)}
        />
    },
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 40,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      // title: t('table:table-item-brand-id'),
      title: 'ADMIN ID',
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      width: 100,
      render: (id: string) => (
        <span className="truncate whitespace-nowrap">
          {id}
        </span>
      ),
    },
    {
      // title: t('table:table-item-brand-id'),
      title: 'ADMIN NAME',
      dataIndex: 'adminName',
      key: 'adminName',
      align: 'center',
      width: 100,
      render: (adminName: string, object: any) => (
        popupType
          ? <span className="truncate whitespace-nowrap">{adminName}</span>
          : <Link
              href={Routes?.adminUser?.details(object?.id)}
              className="text-[#5E5ADB]"
            // className="e_not_link text-#5E5ADB"
            >
              <span className="truncate whitespace-nowrap">{adminName}</span>
            </Link>
      ),
    },
    {
      // title: t('table:table-item-brand-id'),
      title: 'EMAIL',
      dataIndex: 'email',
      key: 'email',
      align: 'center',
      width: 100,
      render: (email: string) => (
        <span className="truncate whitespace-nowrap">
          {email}
        </span>
      ),
    },
    {
      // title: t('table:table-item-brand-id'),
      title: 'MOBILE NUMBER',
      dataIndex: 'mobileNumber',
      key: 'mobileNumber',
      align: 'center',
      width: 100,
      render: (mobileNumber: string) => (
        <span className="truncate whitespace-nowrap">
          {mobileNumber}
        </span>
      ),
    },
    {
      // title: t('table:table-item-brand-id'),
      title: 'CORPORATION NAME',
      dataIndex: 'adminCorporationName',
      key: 'adminCorporationName',
      align: 'center',
      width: 100,
      render: (adminCorporationName: string) => (
        <span className="truncate whitespace-nowrap">
          {adminCorporationName}
        </span>
      ),
    },
    {
      // title: t('table:table-item-brand-id'),
      title: 'ROLE CODE',
      dataIndex: 'roleCode',
      key: 'roleCode',
      align: 'center',
      width: 100,
      render: (roleCode: string) => (
        <span className="truncate whitespace-nowrap">
          {roleCode}
        </span>
      ),
    },
  ];

  return (
    <>
      <div className="mt-4 overflow-hidden rounded shadow">
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
          data={admins}
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

export default AdminsList;

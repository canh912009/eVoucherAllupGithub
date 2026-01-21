import ErrorMessage from "@/components/ui/error-message";
import Loader from "@/components/ui/loader/loader";
import {adminOnly, allowedRolesEV} from "@/utils/auth-utils";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import error from "next/error";
import { useRouter } from "next/router";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import Layout from '@/components/layouts/owner';
import { useAdminsQuery } from "@/data/admin";
import {Admin, AdminQueryOptions, UrBoxBrand} from "@/types";
import {PAGE_SIZE, PERMISSIONS_EV} from "@/utils/constants";
import AdminsList from "@/components/admin/admin-list";
import AdminSearch from "@/components/admin/admin-search";
import LinkButton from "@/components/ui/link-button";
import { Routes } from "@/config/routes";
import Button from "@/components/ui/button";
import {useModalState} from "@/components/ui/modal/modal.context";
import Card from "@/components/common/card";

type IProps = {
  popupType?: boolean;
};

export default function Admins({ popupType = false, }: Readonly<IProps>) {
  const { t } = useTranslation();
  const { data } = useModalState();

  const [selectedItems, setSelectedItems] = useState<Admin[]>([]);
  const [searchOptions, setSearchOptions] = useState<Partial<AdminQueryOptions>>({});
  const [page, setPage] = useState(1);
  const { admins, loading, paginatorInfo, error } = useAdminsQuery(
    { page, pageSize: PAGE_SIZE },
    popupType ? {...searchOptions, roleCodes: `${PERMISSIONS_EV.ROLE_ADMIN},${PERMISSIONS_EV.ROLE_OPERATOR}` } : searchOptions
  );

  if (loading) return <Loader text={t('common:text-loading')} />;
  if (error) return <ErrorMessage message={error.message} />;

  function handleSearch(data: Partial<AdminQueryOptions>) {
    setSearchOptions(data);
    setPage(1);
  }

  function handlePagination(current: number) {
    setPage(current);
  }

  function handleSelectAdmin() {
    return data?.handleSelectAdmin(selectedItems);
  }

  return (
    <div className="bg-white p-2 rounded-md">
      <AdminSearch onSearch={handleSearch} popupType={popupType}/>

      <AdminsList
        admins={admins}
        paginatorInfo={paginatorInfo}
        onPagination={handlePagination}
        popupType={popupType}
        selectedItems={selectedItems}
        setSelectedItems={setSelectedItems}
      />
      {popupType && selectedItems.length > 0 && (
        <div className="mt-4 text-end ">
          <Button
            size="medium"
            className="bg-red-600 hover:bg-red-700"
            onClick={handleSelectAdmin}
          >
            {'Save'}
          </Button>
        </div>
      )}
    </div>
  );
}
Admins.authenticate = {
  permissions: adminOnly,
};
Admins.Layout = Layout;

export const getStaticProps = async ({ locale }: any) => ({
  props: {
    ...(await serverSideTranslations(locale, ['form', 'common', 'table'])),
  },
});

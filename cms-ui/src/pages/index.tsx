import dynamic from 'next/dynamic';
import type { GetServerSideProps } from 'next';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import {
  allowedRolesEV,
  adminOnly,
  getAuthCredentials,
  hasAccess,
  isAuthenticated, customerOnly, supplierOnly, brandOnly, storeOnly,
} from '@/utils/auth-utils';
import AppLayout from '@/components/layouts/app';
import { Routes } from '@/config/routes';
import { Config } from '@/config';

const AdminDashboard = dynamic(() => import('@/components/dashboard/admin'));
const CustomerDashboard = dynamic(() => import('@/components/dashboard/customer'));
const SupplierDashboard = dynamic(() => import('@/components/dashboard/supplier-dashboard'));
const BrandDashboard = dynamic(() => import('@/components/dashboard/brand-dashboard'));
const StoreDashboard = dynamic(() => import('@/components/dashboard/store'));
const OwnerDashboard = dynamic(() => import('@/components/dashboard/owner'));


export default function Dashboard({
  userPermissions,
}: {
  userPermissions: string[];
}) {
  if (hasAccess(adminOnly, userPermissions)) {
    return <AdminDashboard />;
  }
  if (hasAccess(customerOnly, userPermissions)) {
    return <CustomerDashboard />;
  }
  if (hasAccess(supplierOnly, userPermissions)) {
    return <SupplierDashboard />;
  }
  if (hasAccess(brandOnly, userPermissions)) {
    return <BrandDashboard />;
  }
  if (hasAccess(storeOnly, userPermissions)) {
    return <StoreDashboard />;
  }
}

Dashboard.Layout = AppLayout;

export const getServerSideProps: GetServerSideProps = async (ctx) => {
  const { locale } = ctx;
  // TODO: Improve it
  const generateRedirectUrl =
    locale !== Config.defaultLanguage
      ? `/${locale}${Routes.login}`
      : Routes.login;
  const { token, permissions } = getAuthCredentials(ctx);

  if (
    !isAuthenticated({ token, permissions }) ||
    !hasAccess(allowedRolesEV, permissions)
  ) {
    return {
      redirect: {
        destination: generateRedirectUrl,
        permanent: false,
      },
    };
  }
  if (locale) {
    return {
      props: {
        ...(await serverSideTranslations(locale, [
          'common',
          'table',
          'widgets',
        ])),
        userPermissions: permissions,
      },
    };
  }
  return {
    props: {
      userPermissions: permissions,
    },
  };
};

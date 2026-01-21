import { adminAndOwnerOnly, adminOwnerAndStaffOnly } from '@/utils/auth-utils';
import { Routes } from '@/config/routes';

export const siteSettings = {
  name: 'Evoucher',
  description: '',
  logo: {
    url: '/logo.svg',
    alt: 'Evoucher',
    href: '/',
    width: 168,
    height: 60,
  },
  logoAuth: {
    url: '/logoAuth.svg',
    alt: 'EvoucherAuth',
    href: '/',
    width: 168,
    height: 60,
  },
  defaultLanguage: 'vi',
  // author: {
  //   name: 'RedQ, Inc.',
  //   websiteUrl: 'https://redq.io',
  //   address: '',
  // },
  headerLinks: [],
  authorizedLinks: [
    {
      href: Routes.logout,
      labelTransKey: 'authorized-nav-item-logout',
    },
  ],
  currencyCode: 'USD',
  sidebarLinks: {
    admin: [
      {
        href: Routes.dashboard,
        label: 'sidebar-nav-item-dashboard',
        icon: 'DashboardIcon',
      },
      {
        href: Routes.shop.list,
        label: 'sidebar-nav-item-shops',
        icon: 'ShopIcon',
      },
      {
        href: Routes.product.list,
        label: 'sidebar-nav-item-products',
        icon: 'ProductsIcon',
      },
      {
        href: Routes.storeNotice.list,
        label: 'sidebar-nav-item-store-notice',
        icon: 'StoreNoticeIcon',
      },
    ],
  },
  product: {
    placeholder: '/product-placeholder.svg',
  },
  avatar: {
    placeholder: '/avatar-placeholder.svg',
  },
};

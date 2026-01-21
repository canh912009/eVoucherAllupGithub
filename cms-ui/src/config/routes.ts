export const Routes = {
  dashboard: '/',
  overall: '/overall',
  overallCustomer: '/customers/overall',
  overallSuppliers: '/suppliers/overall',
  overallBrands: '/brands/overall',
  overallStores: '/stores/overall',
  login: '/login',
  logout: '/logout',
  register: '/register',
  forgotPassword: '/forgot-password',
  resetPassword: '/reset-password',
  adminMyShops: '/my-shops',
  verifyEmail: '/verify-email',
  shop: {
    ...routesFactory('/shops'),
  },
  product: {
    ...routesFactory('/products'),
  },
  storeNotice: {
    ...routesFactory('/store-notices'),
  },
  suppliers: {
    ...routesFactory('/suppliers')
  },
  menuGroups: {
    ...routesFactory('/menu-groups')
  },
  menus: {
    ...routesFactory('/menus')
  },
  roles: {
    ...routesFactory('/roles')
  },
  campaigns: {
    ...routesFactory('/campaigns')
  },
  supplierContracts: {
    ...routesFactory('/supplier-contracts')
  },
  customerContracts: {
    ...routesFactory('/customer-contracts')
  },
  brands: {
    ...routesFactory('/brands')
  },
  category: {
    ...routesFactory('/categories')
  },
  stock: {
    ...routesFactory('/stock')
  },
  goods: {
    ...routesFactory('/goods')
  },
  codes: {
    ...routesFactory('/codes')
  },
  codeGroups: {
    ...routesFactory('/codeGroups')
  },
  customers: {
    ...routesFactory('/customers')
  },
  stores: {
    ...routesFactory('/stores')
  },
  delivery: {
    ...routesFactory('/delivery')
  },
  adminUser: {
    ...routesFactory('/admins')
  },
  externalPinUpload: {
    ...routesFactory('/external-pin-upload')
  },
  cs: {
    ...routesFactory('/cs')
  },
  csApproval: {
    ...routesFactory('/cs-approval')
  },
  vnptEpayProvider: {
    ...routesFactory('/vnpt-epay-providers')
  },
  xpayProvider: {
    ...routesFactory('/xpay-providers')
  },
  giftPopBrand: {
    ...routesFactory('/giftpop-brand')
  },
  urBoxBrand: {
    ...routesFactory('/urbox-brand')
  },
  wataneProducts: {
    ...routesFactory('/watane-products')
  }
};

function routesFactory(endpoint: string) {
  return {
    list: `${endpoint}`,
    create: `${endpoint}/create`,
    editWithoutLang: (slug: string, shop?: string) => {
      return shop
        ? `/${shop}${endpoint}/${slug}/edit`
        : `${endpoint}/${slug}/edit`;
    },
    cloneWithoutLang: (slug: string, shop?: string) => {
      return shop
        ? `/${shop}${endpoint}/${slug}/clone`
        : `${endpoint}/${slug}/clone`;
    },
    edit: (slug: string, language: string, shop?: string) => {
      return shop
        ? `/${language}/${shop}${endpoint}/${slug}/edit`
        : `${language}${endpoint}/${slug}/edit`;
    },
    translate: (slug: string, language: string, shop?: string) => {
      return shop
        ? `/${language}/${shop}${endpoint}/${slug}/translate`
        : `${language}${endpoint}/${slug}/translate`;
    },
    details: (slug: string) => `${endpoint}/${slug}`,

  };
}

import { PERMISSIONS_EV as p } from '@/utils/constants';
import {Routes} from "@/config/routes";

export const menuTree = [
  {
    id: 1,
    menuGroupName: "Company Management",
    sortOrder: 1,
    validYn: "Y",
    menus: [
      {
        id: 1,
        menuGroupId: 1,
        menuName: "Supplier Management",
        sortOrder: 1,
        menuUrl: Routes.suppliers.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      },
      {
        id: 2,
        menuGroupId: 1,
        menuName: "Customer Management",
        sortOrder: 2,
        menuUrl: Routes.customers.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER],
        validYn: "Y"
      }
    ]
  },
  {
    id: 2,
    menuGroupName: "Product Management",
    sortOrder: 2,
    validYn: "Y",
    menus: [
      {
        id: 3,
        menuGroupId: 2,
        menuName: "Brand Management",
        sortOrder: 1,
        menuUrl: Routes.brands.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER],
        validYn: "Y"
      },
      {
        id: 4,
        menuGroupId: 2,
        menuName: "Product Management",
        sortOrder: 2,
        menuUrl: Routes.goods.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER, p.ROLE_BRAND],
        validYn: "Y"
      },
      {
        id: 5,
        menuGroupId: 2,
        menuName: "Store Management",
        sortOrder: 3,
        menuUrl: Routes.stores.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_SUPPLIER, p.ROLE_BRAND],
        validYn: "Y"
      },
      {
        id: 6,
        menuGroupId: 2,
        menuName: "Category Management",
        sortOrder: 4,
        menuUrl: Routes.category.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      },
      {
        id: 6,
        menuGroupId: 2,
        menuName: "Stock Management",
        sortOrder: 4,
        menuUrl: Routes.stock.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      }
    ]
  },
  {
    id: 3,
    menuGroupName: "Campaign Management",
    sortOrder: 3,
    validYn: "Y",
    menus: [
      {
        id: 7,
        menuGroupId: 3,
        menuName: "Campaign Management",
        sortOrder: 1,
        menuUrl: Routes.campaigns.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER],
        validYn: "Y"
      },
      {
        id: 8,
        menuGroupId: 3,
        menuName: "Publish Management",
        sortOrder: 2,
        menuUrl: Routes.delivery.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER],
        validYn: "Y"
      }
    ]
  },
  {
    id: 4,
    menuGroupName: "CS Management",
    sortOrder: 4,
    validYn: "Y",
    menus: [
      {
        id: 9,
        menuGroupId: 4,
        menuName: "CS Management",
        sortOrder: 1,
        menuUrl: Routes.cs.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      },
      {
        id: 10,
        menuGroupId: 4,
        menuName: "Admin Approval",
        sortOrder: 2,
        menuUrl: Routes.csApproval.list ,
        permissions: [p.ROLE_ADMIN],
        validYn: "Y"
      }
    ]
  },
  {
    id: 5,
    menuGroupName: "Settlement Management",
    sortOrder: 5,
    validYn: "Y",
    menus: [
      {
        id: 11,
        menuGroupId: 5,
        menuName: "Raw Settlement",
        sortOrder: 1,
        menuUrl: "/raw-settlement",
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER, p.ROLE_SUPPLIER, p.ROLE_BRAND],
        validYn: "Y"
      }
    ]
  },
  {
    id: 6,
    menuGroupName: "Administration",
    sortOrder: 6,
    validYn: "Y",
    menus: [
      {
        id: 12,
        menuGroupId: 6,
        menuName: "Admin User Management",
        sortOrder: 1,
        menuUrl: Routes.adminUser.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      }
    ]
  },
  {
    id: 7,
    menuGroupName: "Contract Management",
    sortOrder: 7,
    validYn: "Y",
    menus: [
      {
        id: 13,
        menuGroupId: 7,
        menuName: "Customer Contract",
        sortOrder: 1,
        menuUrl: Routes.customerContracts.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER],
        validYn: "Y"
      },
      {
        id: 14,
        menuGroupId: 7,
        menuName: "Supplier Contract",
        sortOrder: 2,
        menuUrl: Routes.supplierContracts.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      }
    ]
  },
  {
    id: 8,
    menuGroupName: "Statistic Management",
    sortOrder: 8,
    validYn: "Y",
    menus: [
      {
        id: 15,
        menuGroupId: 8,
        menuName: "Overall",
        sortOrder: 1,
        menuUrl: "/",
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR, p.ROLE_CUSTOMER, p.ROLE_BRAND, p.ROLE_STORE],
        validYn: "Y"
      },
      {
        id: 16,
        menuGroupId: 8,
        menuName: "Supplier Statistics",
        sortOrder: 2,
        menuUrl: "/supplier-statistics",
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      },
      {
        id: 17,
        menuGroupId: 8,
        menuName: "Customer Statistics",
        sortOrder: 3,
        menuUrl: "/customer-statistics",
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      }
    ]
  },
  {
    id: 9,
    menuGroupName: "External PIN Services",
    sortOrder: 9,
    validYn: "Y",
    menus: [
      {
        id: 18,
        menuGroupId: 9,
        menuName: "Giftpop Brand",
        sortOrder: 1,
        menuUrl: Routes.giftPopBrand.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      },
      {
        id: 19,
        menuGroupId: 9,
        menuName: "UrBox Brand",
        sortOrder: 2,
        menuUrl: Routes.urBoxBrand.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      },
      {
        id: 20,
        menuGroupId: 9,
        menuName: "VNPT EPay Providers",
        sortOrder: 3,
        menuUrl: Routes.vnptEpayProvider.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      },
      {
        id: 20,
        menuGroupId: 9,
        menuName: "XPay Providers",
        sortOrder: 3,
        menuUrl: Routes.xpayProvider.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      },
      {
        id: 20,
        menuGroupId: 9,
        menuName: "Watane Products",
        sortOrder: 3,
        menuUrl: Routes.wataneProducts.list ,
        permissions: [p.ROLE_ADMIN, p.ROLE_OPERATOR],
        validYn: "Y"
      }
    ]
  }
]

import {
  Supplier,
  MenuGroup,
  SupplierContract,
  Brands,
  CodeGroup,
  Code,
  CustomerContract,
  Customer,
  Good,
  Campaign,
  Store,
  Menu,
  Role,
  RawSettlement,
  BaseResponse,
  RawSettlementQueryOptions,
  Admin,
  Delivery,
  MessageTemplate,
  SupplierChartItem,
  DashboardTimeQueryOptions,
  CustomerChartItem,
  CustomerTableItem,
  CustomerCampaignItem, ItemTableItem, BrandTableItem,
  ExternalPinUpload, StoreTableItem, GoodChartItem,
  CampaignTableItem,
  DeliveryTableItem,
  Cs,
  CsStatusQueryOptions,
  VNPTEPayProvider,
  VNPTEPayGift,
  VNPTEPayPIN,
  VNPTEPayBalance,
  GiftPopBrand,
  GiftPopGood,
  PaginatorInfoEV,
  UrBoxBrand,
  UrBoxGood, Category, CsApproveExtendQueryOptions, WataneGood, Stock,
} from "@/types";
import { API_ENDPOINTS } from "./api-endpoints";
import { crudFactory } from "./curd-factory-ev";
import { HttpClient } from './http-client-ev';

export const supplierClient = {
  ...crudFactory<Supplier, any, Partial<Supplier>>(API_ENDPOINTS.SUPPLIER),
};

export const menuGroupsClient = {
  ...crudFactory<MenuGroup, any, Partial<MenuGroup>>(API_ENDPOINTS.MENU_GROUP),
};

export const rolesClient = {
  ...crudFactory<Role, any, Partial<Role>>(API_ENDPOINTS.ROLE),
};

export const menusClient = {
  ...crudFactory<Menu, any, Partial<Menu>>(API_ENDPOINTS.MENU),
};

export const supplierContractClient = {
  ...crudFactory<SupplierContract, any, Partial<SupplierContract>>(
    API_ENDPOINTS.SUPPLIER_CONTRACT
  ),
};

export const brandsClient = {
  ...crudFactory<Brands, any, Partial<Brands>>(API_ENDPOINTS.BRANDS),
};

export const codeGroupClient = {
  ...crudFactory<CodeGroup, any, Partial<CodeGroup>>(API_ENDPOINTS.CODE_GROUP),
};

export const codeClient = {
  ...crudFactory<Code, any, Partial<Code>>(API_ENDPOINTS.CODE),
};

export const customerContractClient = {
  ...crudFactory<CustomerContract, any, Partial<CustomerContract>>(
    API_ENDPOINTS.CUSTOMER_CONTRACT
  ),
};

export const customerClient = {
  ...crudFactory<Customer, any, Partial<Customer>>(API_ENDPOINTS.CUSTOMER),
};

export const rawSettlement = {
  ...crudFactory<RawSettlement, any, Partial<RawSettlement>>(API_ENDPOINTS.RAW_SETTLEMENT_ALL),
  all: ({ ...params }: Partial<RawSettlementQueryOptions> ) => {
    return HttpClient.get<BaseResponse<RawSettlement[]>>(API_ENDPOINTS.RAW_SETTLEMENT_ALL, {
      ...params,
    });
  },
};

export const supplierChartClient = {
  ...crudFactory<SupplierChartItem, any, Partial<SupplierChartItem>>(API_ENDPOINTS.DASHBOARD_SUPPLIER_CHART_ADMIN),
  all: ({ ...params }: DashboardTimeQueryOptions) => {
    return HttpClient.get<BaseResponse<SupplierChartItem[]>>(API_ENDPOINTS.DASHBOARD_SUPPLIER_CHART_ADMIN, {
      ...params,
    });
  },
};

export const customerChartClient = {
  ...crudFactory<CustomerChartItem, any, Partial<CustomerChartItem>>(API_ENDPOINTS.DASHBOARD_CUSTOMER_CHART_ADMIN),
  all: ({ ...params }: DashboardTimeQueryOptions) => {
    return HttpClient.get<BaseResponse<CustomerChartItem[]>>(API_ENDPOINTS.DASHBOARD_CUSTOMER_CHART_ADMIN, {
      ...params,
    });
  },
};

export const goodChartClient = {
  ...crudFactory<GoodChartItem, any, Partial<GoodChartItem>>(API_ENDPOINTS.DASHBOARD_GOOD_CHART_ADMIN),
  all: ({ ...params }: DashboardTimeQueryOptions) => {
    return HttpClient.get<BaseResponse<GoodChartItem[]>>(API_ENDPOINTS.DASHBOARD_GOOD_CHART_ADMIN, {
      ...params,
    });
  },
};
export const customerTableClient = {
  ...crudFactory<CustomerTableItem, any, Partial<CustomerTableItem>>(API_ENDPOINTS.DASHBOARD_CUSTOMER_TABLE_ADMIN),
};

export const itemTableClient = {
  ...crudFactory<ItemTableItem, any, Partial<ItemTableItem>>(API_ENDPOINTS.DASHBOARD_ITEM_TABLE),
};

export const brandTableClient = {
  ...crudFactory<BrandTableItem, any, Partial<BrandTableItem>>(API_ENDPOINTS.DASHBOARD_BRAND_TABLE),
};

export const storeTableClient = {
  ...crudFactory<StoreTableItem, any, Partial<StoreTableItem>>(API_ENDPOINTS.DASHBOARD_STORE_TABLE),
};

export const campaignTableClient = {
  ...crudFactory<CampaignTableItem, any, Partial<CampaignTableItem>>(API_ENDPOINTS.DASHBOARD_CAMPAIGN_TABLE),
};

export const deliveryTableClient = {
  ...crudFactory<DeliveryTableItem, any, Partial<DeliveryTableItem>>(API_ENDPOINTS.DASHBOARD_DELIVERY_TABLE),
};

export const goodClient = {
  ...crudFactory<Good, any, Partial<Good>>(API_ENDPOINTS.GOOD),
};

export const campaignClient = {
  ...crudFactory<Campaign, any, Partial<Campaign>>(API_ENDPOINTS.CAMPAIGN),
};

export const customerCampaignChartClient = {
  ...crudFactory<CustomerCampaignItem, any, Partial<CustomerCampaignItem>>(API_ENDPOINTS.DASHBOARD_CUSTOMER_CAMPAIGN_ADMIN),
  all: ({ ...params }: DashboardTimeQueryOptions) => {
    return HttpClient.get<BaseResponse<CustomerCampaignItem[]>>(API_ENDPOINTS.DASHBOARD_CUSTOMER_CAMPAIGN_ADMIN, {
      ...params,
    });
  },
};

export const storeClient = {
  ...crudFactory<Store, any, Partial<Store>>(API_ENDPOINTS.STORE),
};

export const adminClient = {
  ...crudFactory<Admin, any, Partial<Admin>>(API_ENDPOINTS.ME),
};

export const deliveryClient = {
  ...crudFactory<Delivery, any, Partial<Delivery>>(API_ENDPOINTS.DELIVERY),
};

export const messageTemplateClient = {
  ...crudFactory<MessageTemplate, any, Partial<MessageTemplate>>(API_ENDPOINTS.MESSAGE_TEMPLATE),
};

export const externalPinUpdateClient = {
  ...crudFactory<ExternalPinUpload, any, Partial<ExternalPinUpload>>(API_ENDPOINTS.EXTERNAL_PIN_UPLOAD),
};

export const csClient = {
  ...crudFactory<Cs, any, Partial<Cs>>(API_ENDPOINTS.CS_PIN_DETAIL),
};

export const csExtendClient = {
  ...crudFactory<Cs, any, Partial<Cs>>(API_ENDPOINTS.CS_EXTEND_DETAIL),
};

export const csDisableClient = {
  ...crudFactory<CsStatusQueryOptions, any, Partial<CsStatusQueryOptions>>(API_ENDPOINTS.CS_DISABLE_VOUCHER),
};

export const csResendClient = {
  ...crudFactory<CsStatusQueryOptions, any, Partial<CsStatusQueryOptions>>(API_ENDPOINTS.CS_RESEND_VOUCHER),
};

export const csApproveExtendClient = {
  ...crudFactory<CsApproveExtendQueryOptions, any, Partial<CsApproveExtendQueryOptions>>(API_ENDPOINTS.CS_EXTEND_APPROVE),
};

export const vnptEpayProvidersClient = {
  ...crudFactory<VNPTEPayProvider, any, Partial<VNPTEPayProvider>>(API_ENDPOINTS.VNPT_EPAY_PROVIDERS_SEACH),
}

export const vnptEpayProviderClient = {
  ...crudFactory<VNPTEPayProvider, any, Partial<VNPTEPayProvider>>(API_ENDPOINTS.VNPT_EPAY_PROVIDER),
};

export const xpayProviderClient = {
  ...crudFactory<VNPTEPayProvider, any, Partial<VNPTEPayProvider>>(API_ENDPOINTS.XPAY_PROVIDER),
};

export const vnptEpayGiftClient = {
  ...crudFactory<VNPTEPayGift, any, Partial<VNPTEPayGift>>(API_ENDPOINTS.VNPT_EPAY_GIFT_SEACH),
}

export const vnptEpayPINClient = {
  ...crudFactory<VNPTEPayPIN, any, Partial<VNPTEPayPIN>>(API_ENDPOINTS.VNPT_EPAY_PURCHASE),
}

export const vnptEpayBalanceClient = {
  ...crudFactory<VNPTEPayBalance, any, Partial<VNPTEPayBalance>>(API_ENDPOINTS.VNPT_EPAY_BALANCE),
}

export const xpayBalanceClient = {
  ...crudFactory<VNPTEPayBalance, any, Partial<VNPTEPayBalance>>(API_ENDPOINTS.XPAY_BALANCE),
}

export const giftPopBrandClient = {
  ...crudFactory<GiftPopBrand, any, Partial<GiftPopBrand>>(API_ENDPOINTS.GIFTPOP_BRAND_SEACH),
}

export const giftPopGoodClient = {
  ...crudFactory<GiftPopGood, any, Partial<GiftPopGood>>(API_ENDPOINTS.GIFTPOP_BRAND_GOOD),
  getGoodList: (brandCode: string) => {
    return HttpClient.get<PaginatorInfoEV<GiftPopGood>>(`${API_ENDPOINTS.GIFTPOP_BRAND_GOOD.replace('{{brandCode}}', brandCode)}`, {});
  },
}

export const urBoxBrandClient = {
  ...crudFactory<UrBoxBrand, any, Partial<UrBoxBrand>>(API_ENDPOINTS.URBOX_BRAND_SEACH),
}

export const stockClient = {
  ...crudFactory<Stock, any, Partial<Stock>>(API_ENDPOINTS.STOCK_LIST),
}

export const urBoxGoodClient = {
  ...crudFactory<UrBoxGood, any, Partial<UrBoxGood>>(API_ENDPOINTS.URBOX_BRAND_GOOD),
  getGoodList: (brandCode: string) => {
    return HttpClient.get<PaginatorInfoEV<UrBoxGood>>(`${API_ENDPOINTS.URBOX_BRAND_GOOD.replace('{{brandCode}}', brandCode)}`, {});
  },
}

export const wataneGoodClient = {
  ...crudFactory<WataneGood, any, Partial<WataneGood>>(API_ENDPOINTS.WATANE_GOOD_ALL),
  getGoodList: () => {
    return HttpClient.get<PaginatorInfoEV<WataneGood>>(`${API_ENDPOINTS.WATANE_GOOD_ALL}`, {});
  },
  getGoodDetails: (code: string) => {
    return HttpClient.get<PaginatorInfoEV<WataneGood>>(`${API_ENDPOINTS.WATANE_GOOD_DETAILS}/${code}`, {});
  },
}

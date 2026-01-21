import BrandDeleteView from '@/components/brands/brands-delete-view';
import CampaignDeleteView from '@/components/campaign/campaign-delete-view';
import CategoryDeleteView from '@/components/categories/category-delete-view';
import CategoryWarningActiveView from '@/components/categories/category-warning-active-view';
import CustomerContractDeleteView from '@/components/customer-contract/customer-contract-delete-view';
import CustomerDeleteView from '@/components/customer/customer-delete-view';
import DeliveryDeleteView from '@/components/delivery/delivery-delete-view';
import GoodDeleteView from '@/components/good/good-delete-view';
import MenuGroupDeleteView from '@/components/menu-group/menu-group-delete-view';
import MenuDeleteView from '@/components/menu/menu-delete-view';
import RoleDeleteView from '@/components/role/role-delete-view';
import StoreDeleteView from '@/components/store/store-delete-view';
import SupplierContractDeleteView from '@/components/supplier-contract/supplier-contract-delete-view';
import SupplierDeleteView from '@/components/supplier/supplier-delete-view';
import Modal from '@/components/ui/modal/modal';
import GiftPopBrandPage from '@/pages/giftpop-brand';
import GiftPopGoodPage from '@/pages/giftpop-brand/[id]';
import UrBoxBrandPage from '@/pages/urbox-brand';
import UrBoxGoodPage from '@/pages/urbox-brand/[id]';
import dynamic from 'next/dynamic';
import { MODAL_VIEWS, useModalAction, useModalState } from './modal.context';
import BulkWarningDeletePopup from "@/components/categories/bulk-warning-delete";
import Admins from "@/pages/admins";
import WataneGoodsPage from "@/pages/watane-products";

const SupplierStatusApproveView = dynamic(
  () => import('@/components/supplier/supplier-status-approve-view')
);
const SupplierStatusDisapproveView = dynamic(
  () => import('@/components/supplier/supplier-status-disapprove-view')
);
const SupplierContractStatusApproveView = dynamic(
  () =>
    import(
      '@/components/supplier-contract/supplier-contract-status-approve-view'
    )
);
const SupplierContractStatusDisapproveView = dynamic(
  () =>
    import(
      '@/components/supplier-contract/supplier-contract-status-disapprove-view'
    )
);
const CampaignStatusRequestView = dynamic(
  () => import('@/components/campaign/campaign-status-request-view')
);
const CampaignStatusReRegisterView = dynamic(
  () => import('@/components/campaign/campaign-status-re-register-view')
);
const CampaignStatusApproveView = dynamic(
  () => import('@/components/campaign/campaign-status-approve-view')
);
const CampaignStatusCancelApproveView = dynamic(
  () => import('@/components/campaign/campaign-status-cancel-approve-view')
);
const CampaignStatusCancelRequestView = dynamic(
  () => import('@/components/campaign/campaign-status-cancel-request-view')
);
const CampaignStatusRejectView = dynamic(
  () => import('@/components/campaign/campaign-status-reject-view')
);
const CampaignProductListPopup = dynamic(
  () => import('@/components/campaign/campaign-product-list-popup')
);
const CustomerStatusApproveView = dynamic(
  () => import('@/components/customer/customer-status-approve-view')
);
const CustomerStatusDisapproveView = dynamic(
  () => import('@/components/customer/customer-status-disapprove-view')
);
const CustomerContractStatusApproveView = dynamic(
  () =>
    import(
      '@/components/customer-contract/customer-contract-status-approve-view'
    )
);
const CustomerContractStatusDisapproveView = dynamic(
  () =>
    import(
      '@/components/customer-contract/customer-contract-status-disapprove-view'
    )
);
const DeliveryStatusApproveView = dynamic(
  () => import('@/components/delivery/delivery-status-approve-view')
);
const DeliveryStatusRejectView = dynamic(
  () => import('@/components/delivery/delivery-status-reject-view')
);
const DeliveryStatusRequestView = dynamic(
  () => import('@/components/delivery/delivery-status-request-view')
);
const DeliveryStatusCancelRequestView = dynamic(
  () => import('@/components/delivery/delivery-status-cancel-request-view')
);
const DeliveryStatusCancelApproveView = dynamic(
  () => import('@/components/delivery/delivery-status-cancel-approve-view')
);
const CategoriesPopup = dynamic(
  () => import('@/components/good/categories-popup')
);
const ExceptedStoresPopup = dynamic(
  () => import('@/components/good/excepted-stores-popup')
);
const BrandsBULKPopup = dynamic(
  () => import('@/components/good/brandsBULK-popup')
);
const GoodsBULKPopup = dynamic(
  () => import('@/components/good/products-bulk-popup')
);
const ProductsChoicesTypePopup = dynamic(
  () => import('@/components/good/products-choices-popup')
);
const AdminChangePasswordPopup = dynamic(
  () => import('@/components/admin/admin-change-password-popup')
);

const GoodUploadExternalPinPopup = dynamic(
  () => import('@/components/good/good-upload-external-pin-popup')
);

const CsStatusDisableView = dynamic(
  () => import('@/components/cs/cs-status-disable-view')
);
const ApproveExtendCsPopup = dynamic(
  () => import('@/components/cs/approve-extend-cs-view')
);
const RequestExtendCsPopup = dynamic(
  () => import('@/components/cs/request-extend-cs-view')
);
const CsStatusResendView = dynamic(
  () => import('@/components/cs/cs-status-resend-view')
);

function renderModal(view: MODAL_VIEWS | undefined, data: any) {
  switch (view) {
    case 'DELETE_MENU_GROUP':
      return <MenuGroupDeleteView />;
    case 'DELETE_MENU':
      return <MenuDeleteView />;
    case 'DELETE_ROLE':
      return <RoleDeleteView />;
    case 'DELETE_SUPPLIER':
      return <SupplierDeleteView />;
    case 'APPROVE_SUPPLIER':
      return <SupplierStatusApproveView />;
    case 'DISAPPROVE_SUPPLIER':
      return <SupplierStatusDisapproveView />;
    case 'DELETE_SUPPLIER_CONTRACT':
      return <SupplierContractDeleteView />;
    case 'APPROVE_SUPPLIER_CONTRACT':
      return <SupplierContractStatusApproveView />;
    case 'DISAPPROVE_SUPPLIER_CONTRACT':
      return <SupplierContractStatusDisapproveView />;
    case 'DELETE_BRAND':
      return <BrandDeleteView />;
    case 'DELETE_CATEGORY':
      return <CategoryDeleteView />;
    case 'WARNING_ACTIVE_CATEGORY':
      return <CategoryWarningActiveView />;
    case 'WARNING_BULK_DELETE_POPUP':
      return <BulkWarningDeletePopup />;
    case 'DELETE_GOOD':
      return <GoodDeleteView />;
    case 'DELETE_CAMPAIGN':
      return <CampaignDeleteView />;
    case 'REQ_CAMPAIGN':
      return <CampaignStatusRequestView />;
    case 'RE_REQ_CAMPAIGN':
      return <CampaignStatusReRegisterView />;
    case 'APPROVE_CAMPAIGN':
      return <CampaignStatusApproveView />;
    case 'CANCEL_APPROVE_CAMPAIGN':
      return <CampaignStatusCancelApproveView />;
    case 'CANCEL_REQUEST_CAMPAIGN':
      return <CampaignStatusCancelRequestView />;
    case 'REJECT_CAMPAIGN':
      return <CampaignStatusRejectView />;
    case 'PRODUCT_LIST_CAMPAIGN':
      return (
        <CampaignProductListPopup selectedProducts={data?.selectedProducts} />
      );
    case 'DELETE_STORE':
      return <StoreDeleteView />;
    case 'DELETE_CUSTOMER':
      return <CustomerDeleteView />;
    case 'APPROVE_CUSTOMER':
      return <CustomerStatusApproveView />;
    case 'DISAPPROVE_CUSTOMER':
      return <CustomerStatusDisapproveView />;
    case 'DELETE_CUSTOMER_CONTRACT':
      return <CustomerContractDeleteView />;
    case 'APPROVE_CUSTOMER_CONTRACT':
      return <CustomerContractStatusApproveView />;
    case 'DISAPPROVE_CUSTOMER_CONTRACT':
      return <CustomerContractStatusDisapproveView />;
    case 'APPROVE_DELIVERY':
      return <DeliveryStatusApproveView />;
    case 'REJECT_DELIVERY':
      return <DeliveryStatusRejectView />;
    case 'REQUEST_DELIVERY':
      return <DeliveryStatusRequestView />;
    case 'CANCEL_REQUEST_DELIVERY':
      return <DeliveryStatusCancelRequestView />;
    case 'CANCEL_APPROVE_DELIVERY':
      return <DeliveryStatusCancelApproveView />;
    case 'DELETE_DELIVERY':
      return <DeliveryDeleteView />;
    case 'GIFTPOP_GOOD_CODE':
      return (
        <GiftPopGoodPage
          checkAddGiftPop={true}
          selectedBrand={data?.selectedBrand}
        />
      );
    case 'URBOX_GOOD_CODE':
      return (
        <UrBoxGoodPage
          checkAddUrBox={true}
          selectedBrand={data?.selectedBrand}
        />
      );
    case 'WATANE_GOOD_CODE':
      return (
        <WataneGoodsPage
          checkAddWatane={true}
        />
      );
    case 'GIFTPOP_BRANCH_CODE':
      return <GiftPopBrandPage checkAddGiftPop={true} />;
    case 'URBOX_BRANCH_CODE':
      return <UrBoxBrandPage checkAddUrBox={true} />;
    // return <GiftPopBrandPage checkAddGiftPop={true}/>;
    case 'SELECT_CATEGORIES':
      return <CategoriesPopup />;
    case 'SELECT_CUSTOMER_AMIN':
      return <Admins popupType={true}/>;
    case 'SELECT_EXCEPTED_STORE':
      return <ExceptedStoresPopup selectedBrand={data?.selectedBrand} />;
    case 'SELECT_BRANDS_BULK':
      return (
        <BrandsBULKPopup
          categoryCodeBULKSelected={data?.categoryCodeBULKSelected}
        />
      );
    case 'SELECT_GOODS_BULK':
      return (
        <GoodsBULKPopup
          categoryCodeBULKSelected={data?.categoryCodeBULKSelected}
          brandIdBULKSelected={data?.brandIdBULKSelected}
        />
      );
    case 'PRODUCTS_CHOICES_TYPE':
      return <ProductsChoicesTypePopup />;
    case 'CHANGE_PASSWORD':
      return <AdminChangePasswordPopup data={data?.data} />;
    case 'UPLOAD_EXTERNAL_PIN':
      return <GoodUploadExternalPinPopup good={data?.good} displayType={data?.displayType}/>;
    case 'DISABLE_CS':
      return <CsStatusDisableView />;
    case 'APPROVE_EXTEND_CS':
      return <ApproveExtendCsPopup />;
    case 'REQUEST_EXTEND_CS':
      return <RequestExtendCsPopup />;
    case 'RESEND_CS':
      return <CsStatusResendView />;
    default:
      return null;
  }
}

const ManagedModal = () => {
  const { isOpen, view, data } = useModalState();

  const { closeModal } = useModalAction();

  return (
    <Modal open={isOpen} onClose={closeModal}>
      {renderModal(view, data)}
    </Modal>
  );
};

export default ManagedModal;

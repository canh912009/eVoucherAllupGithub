import ConfirmationSimplePopup from '@/components/common/confirmation-simple';
import { useModalAction, useModalState } from '@/components/ui/modal/modal.context';
import {POPUP_DELETE_TYPE} from "@/utils/constants";

const BulkWarningDeletePopup = () => {
  const { closeModal } = useModalAction();
  const { data } = useModalState();
  // console.log('data', data);

  function handleSubmit() {
    data?.popupConfirmDelete(true, data?.id);
    closeModal();
  }

  function handleCancel() {
    data?.popupConfirmDelete(false, data?.id);
    closeModal();
  }

  function getTitle() {
    if(data?.typeDelete === POPUP_DELETE_TYPE.CATEGORY) {
      return "Delete Category"
    }
    if(data?.typeDelete === POPUP_DELETE_TYPE.BRAND) {
      return "Delete Brand"
    }
    if(data?.typeDelete === POPUP_DELETE_TYPE.GOOD) {
      return "Delete Product"
    }

    return ""
  }

  function getDescriptions() {
    if(data?.typeDelete === POPUP_DELETE_TYPE.CATEGORY) {
      return [
        'Deleting this category will ALSO DELETE ALL BRANDS AND PRODUCTS THAT ARE RELATED to this category.',
        'Are you sure you want to delete?',
      ]
    }
    if(data?.typeDelete === POPUP_DELETE_TYPE.BRAND) {
      return [
        'Deleting this brand will ALSO DELETE ALL PRODUCTS THAT RELATED to this brand..',
        'Are you sure you want to delete?',
      ]
    }
    if(data?.typeDelete === POPUP_DELETE_TYPE.GOOD) {
      return [
        'Are you sure you want to delete?',
      ]
    }

    return []
  }


  return (
    <ConfirmationSimplePopup
      onCancel={handleCancel}
      onSubmit={handleSubmit}
      title={getTitle()}
      titleClassName="text-heading text-red-700 text-left !normal-case"
      descriptions={getDescriptions()}
      descriptionClassNameWrapper='text-left !pt-6'
    />
  );
};

export default BulkWarningDeletePopup;

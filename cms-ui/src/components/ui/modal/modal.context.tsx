import React from 'react';

export type MODAL_VIEWS =
  | 'DELETE_PRODUCT'
  | 'DELETE_STORE_NOTICE'
  | 'DELETE_MENU_GROUP'
  | 'DELETE_MENU'
  | 'DELETE_ROLE'
  | 'DELETE_SUPPLIER'
  | 'APPROVE_SUPPLIER'
  | 'DISAPPROVE_SUPPLIER'
  | 'DELETE_SUPPLIER_CONTRACT'
  | 'APPROVE_SUPPLIER_CONTRACT'
  | 'DISAPPROVE_SUPPLIER_CONTRACT'
  | 'CLOSE_SUPPLIER_CONTRACT'
  | 'DELETE_BRAND'
  | 'DELETE_CATEGORY'
  | 'WARNING_ACTIVE_CATEGORY'
  | 'WARNING_BULK_DELETE_POPUP'
  | 'DELETE_CAMPAIGN'
  | 'REQ_CAMPAIGN'
  | 'RE_REQ_CAMPAIGN'
  | 'APPROVE_CAMPAIGN'
  | 'CANCEL_APPROVE_CAMPAIGN'
  | 'CANCEL_REQUEST_CAMPAIGN'
  | 'REJECT_CAMPAIGN'
  | 'PRODUCT_LIST_CAMPAIGN'
  | 'DELETE_STORE'
  | 'DELETE_GOOD'
  | 'DELETE_CUSTOMER'
  | 'APPROVE_CUSTOMER'
  | 'DISAPPROVE_CUSTOMER'
  | 'DELETE_CUSTOMER_CONTRACT'
  | 'APPROVE_CUSTOMER_CONTRACT'
  | 'DISAPPROVE_CUSTOMER_CONTRACT'
  | 'APPROVE_DELIVERY'
  | 'REJECT_DELIVERY'
  | 'REQUEST_DELIVERY'
  | 'CANCEL_REQUEST_DELIVERY'
  | 'CANCEL_APPROVE_DELIVERY'
  | 'DELETE_DELIVERY'
  | 'GIFTPOP_BRANCH_CODE'
  | 'URBOX_BRANCH_CODE'
  | 'GIFTPOP_GOOD_CODE'
  | 'URBOX_GOOD_CODE'
  | 'WATANE_GOOD_CODE'
  | 'SELECT_CATEGORIES'
  | 'SELECT_CUSTOMER_AMIN'
  | 'SELECT_EXCEPTED_STORE'
  | 'SELECT_BRANDS_BULK'
  | 'SELECT_GOODS_BULK'
  | 'PRODUCTS_CHOICES_TYPE'
  | 'CHANGE_PASSWORD'
  | 'UPLOAD_EXTERNAL_PIN'
  | 'DISABLE_CS'
  | 'APPROVE_EXTEND_CS'
  | 'REQUEST_EXTEND_CS'
  | 'RESEND_CS'
  ;

interface State {
  view?: MODAL_VIEWS;
  data?: any;
  isOpen: boolean;
}
type Action =
  | { type: 'open'; view?: MODAL_VIEWS; payload?: any }
  | { type: 'close' };

const initialState: State = {
  view: undefined,
  isOpen: false,
  data: null,
};

function modalReducer(state: State, action: Action): State {
  switch (action.type) {
    case 'open':
      return {
        ...state,
        view: action.view,
        data: action.payload,
        isOpen: true,
      };
    case 'close':
      return {
        ...state,
        view: undefined,
        data: null,
        isOpen: false,
      };
    default:
      throw new Error('Unknown Modal Action!');
  }
}

const ModalStateContext = React.createContext<State>(initialState);
ModalStateContext.displayName = 'ModalStateContext';
const ModalActionContext = React.createContext<
  React.Dispatch<Action> | undefined
>(undefined);
ModalActionContext.displayName = 'ModalActionContext';

export const ModalProvider: React.FC<{ children?: React.ReactNode }> = ({
  children,
}) => {
  const [state, dispatch] = React.useReducer(modalReducer, initialState);
  return (
    <ModalStateContext.Provider value={state}>
      <ModalActionContext.Provider value={dispatch}>
        {children}
      </ModalActionContext.Provider>
    </ModalStateContext.Provider>
  );
};

export function useModalState() {
  const context = React.useContext(ModalStateContext);
  if (context === undefined) {
    throw new Error(`useModalState must be used within a ModalProvider`);
  }
  return context;
}

export function useModalAction() {
  const dispatch = React.useContext(ModalActionContext);
  if (dispatch === undefined) {
    throw new Error(`useModalAction must be used within a ModalProvider`);
  }
  return {
    openModal(view?: MODAL_VIEWS, payload?: unknown) {
      dispatch({ type: 'open', view, payload });
    },
    closeModal() {
      dispatch({ type: 'close' });
    },
  };
}

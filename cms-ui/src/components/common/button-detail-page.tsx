import React from 'react';
import LinkButton from "@/components/ui/link-button";
import Button from "@/components/ui/button";
import {getAuthCredentials} from "@/utils/auth-utils";
import {PERMISSIONS_EV} from "@/utils/constants";
import {Routes} from "@/config/routes";
import { useTranslation } from 'next-i18next';
import {useModalAction} from "@/components/ui/modal/modal.context";

type IProps = {
  backLink: string,
  editLink: string,
  initialValues: any;
  rejectModelView: string,
  approveModelView: string,
};

const ButtonsDetailPage = ({ backLink, editLink, initialValues, rejectModelView, approveModelView} : IProps) => {
  const { t } = useTranslation();
  const { token, permissions } = getAuthCredentials();
  const { openModal } = useModalAction();
  function handleStatus(modalView: any, id: string) {
    openModal(modalView, id);
  }
  return (
    <div className="mb-4 text-end">
      <LinkButton
        variant="outline"
        href={`${backLink}`}
        className="me-4 hover:bg-red-800 text-end"
      >
        {t('Close')}
      </LinkButton>
      {!(initialValues?.approveStatusCode === 'APPRV' || initialValues?.approveStatusCode === 'REJCT') &&
          <LinkButton
              href={`${editLink}`}
              className="bg-black me-4 hover:bg-red-800"
          >
              <span className="xl:block">Edit</span>
          </LinkButton>
      }
      {initialValues?.approveStatusCode === 'REQ' && permissions?.includes(PERMISSIONS_EV.ROLE_ADMIN) &&
          <>
              <Button
                  variant="outline"
                  className="me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus(rejectModelView, initialValues?.id as string )
                  }
              >
                {t('Reject')}
              </Button>
              <Button
                  className="bg-red-700 me-4 hover:bg-red-800"
                  onClick={() =>
                    handleStatus(approveModelView, initialValues?.id as string)
                  }
              >
                {t('Approve')}
              </Button>
          </>
      }
    </div>
  );
};

export default ButtonsDetailPage;

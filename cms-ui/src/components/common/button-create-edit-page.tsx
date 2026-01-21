import React from 'react';
import LinkButton from "@/components/ui/link-button";
import Button from "@/components/ui/button";
import {getAuthCredentials} from "@/utils/auth-utils";
import {PERMISSIONS_EV} from "@/utils/constants";

type IProps = {
  linkBackButton: string,
  initialValues: any;
  updating?: boolean;
  creating?: boolean;
  handleDraftClick: () => void;
  handleRegisterClick: () => void;
};

const ButtonsCreateEditPage = ({ linkBackButton, initialValues , updating, creating,
                                 handleDraftClick, handleRegisterClick } : IProps) => {
  const { token, permissions } = getAuthCredentials();
  return (
    <div className="mb-4 text-end">
      <LinkButton
        variant="outline"
        href={linkBackButton}
        className="me-4 hover:bg-red-800 text-end"
      > Back </LinkButton>

      {/* Create page || Edit page have approveStatusCode == null */}
      {(!initialValues || (initialValues && !initialValues.approveStatusCode)) && (
        <Button
          variant="outline"
          className="me-4 hover:bg-red-800"
          loading={initialValues ? updating : creating}
          onClick={handleDraftClick}
        >
          Save as a Draft
        </Button>
      )}

      {(permissions?.includes(PERMISSIONS_EV.ROLE_ADMIN) || permissions?.includes(PERMISSIONS_EV.ROLE_OPERATOR)) && (
        <Button
          className="bg-red-700 hover:bg-red-800"
          loading={initialValues ? updating : creating}
          onClick={handleRegisterClick}
        >
          Register
        </Button>
      )}
    </div>
  );
};

export default ButtonsCreateEditPage;

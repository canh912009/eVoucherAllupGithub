import { useTranslation } from 'next-i18next';
import { CommonApproveStatusAction, SupplierContract } from '@/types';
import { Routes } from '@/config/routes';
import Card from '@/components/common/card';
import { DownloadIcon } from '@/components/icons/download-icon1';
import ApproveStatusCodeBadge from '../common/approve-status-code-badge';
import Button from '@/components/ui/button';
import LinkButton from '@/components/ui/link-button';
import Radio from '@/components/ui/radio/radio';
import { useModalAction } from '../ui/modal/modal.context';
import { useRouter } from 'next/router';
import {uploadClient} from "@/data/client/upload";
import {toast} from "react-toastify";
import { isPermitted } from '@/utils/auth-utils';
import { PERMISSIONS_EV as p } from '@/utils/constants';

type IProps = {
  initialValues?: SupplierContract | null;
};

export default function SupplierContractDetail({ initialValues }: IProps) {
  const rootClassName =
    'cursor-not-allowed bg-gray-100 ps-10 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const router = useRouter();
  const { t } = useTranslation();
  const { openModal } = useModalAction();

  async function handleDownloadFile() {
    try {
      const response = await uploadClient.downloadFiles(initialValues?.contractFilePath || '');
      const blob = new Blob([response.data], { type: 'application/octet-stream' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = initialValues?.contractFilePath as string;
      link.click();
      window.URL.revokeObjectURL(url);
    } catch (error: any) {
      console.log(error)
      if (error.isAxiosError && !error.response) {
        toast.error('Network Error:' + error?.response?.data.message);
      } else {
        toast.error('Error:' + error?.response?.data.message);
      }
    }
  }

  function handleStatus(modalView: any, id: string) {
    openModal(modalView, id);
  }

  return (
    <div>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Supplier Contract Information')}
              </h1>
            </div>
          </div>

          {initialValues && (
            <div className="mb-5 flex flex-wrap">
              <div className="flex w-full flex-wrap px-4 md:w-1/2">
                <label className="w-full py-2 text-heading md:w-1/4">
                  {t('Status')} <span className="text-red-500">*</span>
                </label>
                {
                  <ApproveStatusCodeBadge
                    approveStatusCode={initialValues?.approveStatusCode}
                    className="py-3 font-medium"
                  />
                }
              </div>
            </div>
          )}
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                id="contractName"
                value={initialValues?.contractName}
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Start date')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                disabled={true}
                id="startDate"
                value={initialValues?.startDate}
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Supplier name')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.supplier?.supplierName}
                disabled={true}
                id="supplierName"
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('End date')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.endDate}
                disabled={true}
                id="endDate"
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Supplier ID')} <span className="text-red-500">*</span>
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.supplier?.id}
                id="supplierId"
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('VAT (included)')}
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  disabled={true}
                  className="flex w-full md:w-1/2"
                  label={t('Yes')}
                  name="supplyVatIncludeYn"
                  id="vat_y"
                  checked={initialValues?.supplyVatIncludeYn === 'Y'}
                  value="Y"
                />
                <Radio
                  disabled={true}
                  className="flex w-full md:w-1/2"
                  label={t('No')}
                  name="supplyVatIncludeYn"
                  id="vat_n"
                  checked={initialValues?.supplyVatIncludeYn === 'N'}
                  value="N"
                />
              </div>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Discount rate')}
              </label>
              <input
                className={rootClassName}
                type="string"
                disabled={true}
                value={initialValues?.supplyDiscountRate ?? ''}
                id="supplyDiscountRate"
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Settlement method')}
              </label>
              <input
                className={rootClassName}
                type="string"
                disabled={true}
                value={initialValues?.supplySettlementMethodCode}
                id="supplySettlementMethodCode"
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Discount amount')}
              </label>
              <input
                className={rootClassName}
                type="string"
                disabled={true}
                value={initialValues?.supplyDiscountAmount ?? ''}
                id="supplyDiscountAmount"
                autoComplete="off"
              />
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Commission rate')}
              </label>
              <input
                className={rootClassName}
                type="string"
                disabled={true}
                value={initialValues?.supplyCommissionRate ?? ''}
                id="supplyCommissionRate"
                autoComplete="off"
              />
            </div>
          </div>

          <div className="mb-5 mt-10 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              <h1 className="text-lg font-semibold uppercase">
                {t('Contract file upload')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract file upload')}
              </label>
              <div className="w-full pt-1 md:w-3/4">
                {initialValues?.contractFilePath && (
                  <button onClick={handleDownloadFile}>
                    <DownloadIcon className="w-5 shrink-0" />
                  </button>
                )}
              </div>
            </div>
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Contract file name')}
              </label>
              <input
                className={rootClassName}
                type="text"
                value={initialValues?.contractFileName ?? ''}
                disabled={true}
                autoComplete="off"
              />
            </div>
          </div>
        </Card>
      </div>
      <div className="mb-4 text-end">
        {/* <LinkButton
          variant="outline"
          href={`${Routes?.supplierContracts.list}`}
          className="me-4 hover:bg-red-800"
        >
          {t('Close')}
        </LinkButton> */}
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>
        {initialValues && (
          <>
            {isPermitted([p.ROLE_ADMIN, p.ROLE_OPERATOR]) &&
              !initialValues.approveStatusCode && (
                <>
                  <LinkButton
                    variant="outline"
                    href={`${Routes.supplierContracts.editWithoutLang(
                      initialValues?.id
                    )}`}
                    className="me-4 hover:bg-red-800"
                  >
                    <span className="xl:block">Edit</span>
                  </LinkButton>
                </>
              )}

            {isPermitted([p.ROLE_ADMIN, p.ROLE_OPERATOR]) &&
              initialValues?.approveStatusCode ===
                CommonApproveStatusAction.REQ && (
                <>
                  <LinkButton
                    variant="outline"
                    href={`${Routes.supplierContracts.editWithoutLang(
                      initialValues.id
                    )}`}
                    className="me-4 hover:bg-red-800"
                  >
                    <span className="xl:block">Edit</span>
                  </LinkButton>
                </>
              )}

            {isPermitted([p.ROLE_ADMIN]) &&
              initialValues?.approveStatusCode ===
                CommonApproveStatusAction.REQ && (
                <>
                  <Button
                    variant="outline"
                    className="me-4 hover:bg-red-800"
                    onClick={() =>
                      handleStatus(
                        'DISAPPROVE_SUPPLIER_CONTRACT',
                        initialValues.id
                      )
                    }
                  >
                    {t('Reject')}
                  </Button>
                  <Button
                    className="bg-red-700 me-4 hover:bg-red-800"
                    onClick={() =>
                      handleStatus(
                        'APPROVE_SUPPLIER_CONTRACT',
                        initialValues.id
                      )
                    }
                  >
                    {t('Approve')}
                  </Button>
                </>
              )}
          </>
        )}
      </div>
    </div>
  );
}

import { useModalAction } from '@/components/ui/modal/modal.context';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import React, {ReactNode, useState} from 'react';
import {Routes} from "@/config/routes";
import {Table} from "@/components/ui/table";
import Button from "@/components/ui/button";
import LinkButton from "@/components/ui/link-button";
import {CommonStatusCode} from "@/types";

const InfoSection = (props: { title: string, children: ReactNode }) => (
  <div className="shadow-lg shadow-gray-400 border-gray-950 rounded-lg overflow-hidden m-5">
    <div className=" p-4 text-base text-gray-700 uppercase  bg-gray-200 ">{props.title}</div>
    <div className=" p-4 ">
      {props.children}
    </div>
  </div>
);

const InfoItem = (props: { label: string, value: string}) => (
  <div className="my-3 ">
    <p className=" text-gray-600 text-sm font-bold">{props.label}</p>
    <p className="text-xl font-bold text-gray-900 uppercase mt-1">{props.value }</p>
  </div>
);

export default function CsExtendDetail({ initialValues }: Readonly<any>) {
  const rootClassName =
    'bg-gray-100 ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const router = useRouter();
  const { t } = useTranslation();
  const { openModal } = useModalAction();
  const [showPassword, setShowPassword] = useState(false);

  function handleStatus(modalView: any, evExtend: any) {
    openModal(modalView, evExtend);
  }
  const { request, voucher, publish, customer, approvalHistory } = initialValues;
  const columns = [
    {
      title: 'NO',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 40,
      render: (text: any, record: any, index: number) => {
        return index + 1;
      },
    },
    {
      title: t('REQUESTER NAME'),
      dataIndex: ['requestedAdmin', 'adminName'],
      key: 'adminName',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (adminName: string, record: any) => (
        <span className="truncate whitespace-nowrap">
          {record?.requestedAdmin?.adminName}
        </span>
      ),
    },
    {
      title: t('REQUESTER ID'),
      dataIndex:  'reqId' ,
      key: 'id',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: ( reqId: any) => (
        <span className="truncate whitespace-nowrap">
          {reqId}
        </span>
      ),
    },
    {
      title: 'REQUEST DATE',
      dataIndex: 'reqDt',
      key: 'reqDt',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (reqDt: string) => (
        <span className="truncate whitespace-nowrap">
          {reqDt}
        </span>
      ),
    },
    {
      title: 'REQUEST STATUS',
      dataIndex: 'reqStatus',
      key: 'reqStatus',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (reqStatus: string) => (
        <span className="truncate whitespace-nowrap">
          {reqStatus}
        </span>
      ),
    },
    {
      title: t('APPROVER NAME'),
      dataIndex: ['approvedAdmin', 'adminName'],
      key: 'adminName',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (adminName: string, record: any) => (
        <span className="truncate whitespace-nowrap">
          {record?.approvedAdmin?.adminName}
        </span>
      ),
    },
    {
      title: t('APPROVER ID'),
      dataIndex: ['approvedAdmin', 'id'],
      key: 'id',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: ( id: string, record: any) => (
        <span className="truncate whitespace-nowrap">
          {record?.approvedAdmin?.id}
        </span>
      ),
    },
    {
      title: 'APPROVED DATE',
      dataIndex: 'approveDate',
      key: 'approveDate',
      width: 100,
      align: 'center',
      ellipsis: true,
      render: (approveDate: string) => (
        <span className="truncate whitespace-nowrap">
          {approveDate}
        </span>
      ),
    },
  ]

  return (
    <div>
      <h1 className="text-lg font-bold uppercase my-5 ">
        {t('PIN Information')}
      </h1>
      <div className=" mx-auto ">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <InfoSection title="PIN INFORMATION">
            <InfoItem label="Voucher UUID" value={voucher.ev} />
            <InfoItem label="PIN" value={voucher.externalPinNo} />
          </InfoSection>

          <InfoSection title="TARGET INFORMATION">
            <InfoItem label="Target name" value={voucher.userName} />
            <InfoItem label="Target phone number" value={voucher.userMobileNumber} />
          </InfoSection>

          <InfoSection title="DELIVERY INFORMATION">
            <InfoItem label="Delivery name" value={publish.publishName} />
            <InfoItem label="Delivery ID" value={publish.id} />
          </InfoSection>

          <InfoSection title="CUSTOMER INFORMATION">
            <InfoItem label="Customer name" value={customer.customerName} />
            <InfoItem label="Customer ID" value={customer.id} />
          </InfoSection>
        </div>

        <InfoSection title="REQUEST INFORMATION">
          <div className="grid grid-cols-2 md:grid-cols-2  ">
            <InfoItem label="Requester name" value={request?.requestedAdmin?.adminName} />
            <InfoItem label="Request Type" value={request?.requestType ?? "Extend"} />
            <InfoItem label="Requester ID" value={request?.reqId} />
            <InfoItem label="Request Date" value={request?.reqDt} />
            <InfoItem label="Requester memo" value={request?.memo} />
          </div>
        </InfoSection>
      </div>

      <h1 className="text-lg font-bold uppercase my-10 ">
        {t('Approval History')}
      </h1>
      <div className=" m-5 shadow-lg shadow-gray-400 rounded-lg" >
        <Table
          //@ts-ignore
          columns={columns}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">
                {t('No Data')}
              </div>
            </div>
          )}
          data={approvalHistory}
          rowKey="approvalHistory"
          scroll={{ x: 1000 }}
        />
      </div>
      <div className="m-4 text-end ">
        <LinkButton
          variant="outline"
          className="me-4 hover:bg-red-800 rounded-2xl"
          href={`${Routes?.csApproval.list}`}
        >
          {t('form:button-label-back')}
        </LinkButton>

        <Button
          className="bg-red-600 me-4 hover:bg-red-700 rounded-2xl"
          onClick={() =>
            handleStatus('APPROVE_EXTEND_CS', {...initialValues, approveAction: CommonStatusCode.REJECTED} )
          }
        >
          {t('Reject')}
        </Button>

        <Button
          className="bg-green-700 me-4 hover:bg-green-800 rounded-2xl"
          onClick={() =>
            handleStatus('APPROVE_EXTEND_CS', {...initialValues, approveAction: CommonStatusCode.APPROVED }  )
          }
        >
          {t('Approve')}
        </Button>

      </div>
    </div>
  );
}

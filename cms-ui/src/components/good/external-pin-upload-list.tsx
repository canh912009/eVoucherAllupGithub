import { ExternalPinUpload, Good, MappedPaginatorInfoEV } from "@/types";
import { useRouter } from "next/router";
import { useTranslation } from "react-i18next";
import { Table } from '@/components/ui/table';
import LinkButton from '@/components/ui/link';
import { Routes } from '@/config/routes';
import Card from "../common/card";
import Button from '@/components/ui/button';
import Pagination from '@/components/ui/pagination';
import { PAGE_SIZE } from '@/utils/constants';


type IProps = {
  externalPinUploads?: ExternalPinUpload[];
  good?: Good;
  paginatorInfo?: MappedPaginatorInfoEV | null;
  onPagination: (current: number) => void;
};

export default function ExternalPinUploadList({ externalPinUploads, good, paginatorInfo, onPagination }: IProps) {

  // console.log(externalPinUploads)
  const { t } = useTranslation();
  const router = useRouter();

  const rootClassName =
    'bg-gray-200 ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  let classes = {
    // title: 'font-semibold',
    // content: 'font-normal text-[#212121]',
    wrapper:
      'flex flex-wrap pb-8 my-5 border-b border-dashed border-border-base sm:my-8',
    side_left: 'w-full px-0 pb-5 sm:w-1/4 sm:py-8 sm:pe-4 md:w-1/4 md:pe-5',
    side_right: 'w-full sm:w-3/4 md:w-3/4',
    row: 'mb-1 flex flex-wrap',
    title: 'w-full md:w-1/4 px-4 py-2 font-semibold text-heading',
    content: 'w-full md:w-3/4 px-4 py-2',
  };

  const columns = [
    {
      title: 'NO.',
      dataIndex: 'index',
      key: 'index',
      align: 'center',
      width: 40,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },
    {
      title: <span className="uppercase">Upload name</span>,
      dataIndex: 'uploadName',
      key: 'uploadName',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (uploadName: string, object: any) => (
        // <span className="truncate whitespace-nowrap">{uploadName}</span>
        <LinkButton
          href={Routes?.externalPinUpload?.details(object?.id)}
          className="e_not_link"
        >
          <span className="truncate whitespace-nowrap">{uploadName}</span>
        </LinkButton>
      ),
    },
    {
      title: <span className="uppercase">Upload file name</span>,
      dataIndex: 'uploadFileName',
      key: 'uploadFileName',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (uploadFileName: string) => (
        <span className="truncate whitespace-nowrap">{uploadFileName}</span>
      ),
    },
    {
      title: <span className="uppercase">Memo</span>,
      dataIndex: 'memo',
      key: 'memo',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (memo: string) => (
        <span className="truncate whitespace-nowrap">{memo}</span>
      ),
    },
    {
      title: <span className="uppercase">Registration Date</span>,
      dataIndex: 'regDt',
      key: 'regDt',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (regDt: string) => (
        <span className="truncate whitespace-nowrap">{regDt}</span>
      ),
    }, ,
    {
      title: <span className="uppercase">Modified Date</span>,
      dataIndex: 'updtDt',
      key: 'updtDt',
      width: 150,
      align: 'center',
      ellipsis: true,
      render: (updtDt: string) => (
        <span className="truncate whitespace-nowrap">{updtDt}</span>
      ),
    },
  ];

  return (
    <>
      <div className="mb-3 overflow-hidden rounded shadow">
        <Table
          //@ts-ignore
          columns={columns}
          emptyText={() => (
            <div className="flex flex-col items-center py-6">
              <div className="pt-6 text-sm font-semibold">
                {t('table:empty-table-data')}
              </div>
            </div>
          )}
          data={externalPinUploads}
          rowKey="id"
          scroll={{ x: 1000 }}
        />
      </div>

      <div className="mb-6">
        {!!paginatorInfo?.totalCount && (
          <div className="flex items-center justify-end">
            <Pagination
              total={paginatorInfo.totalCount}
              current={paginatorInfo.currentPage}
              pageSize={PAGE_SIZE}
              onChange={onPagination}
            />
          </div>
        )}
      </div>

      <div className="mb-1 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 bg-red-700 hover:bg-red-700"
          type="button"
        >
          {t('Back')}
        </Button>
      </div>
    </>
  );
}
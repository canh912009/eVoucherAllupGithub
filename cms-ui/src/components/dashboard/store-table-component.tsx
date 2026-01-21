import {
  Brands,
  Customer,
  DashboardTimeQueryOptions as SearchValue
} from '@/types';
import {PAGE_SIZE , PAGE_SIZE_MAX} from '@/utils/constants';
import {useState} from "react";
import SelectInput from "@/components/ui/select-input-autocomplete";
import {useTranslation} from "next-i18next";
import useInputTimeout from "@/utils/use-input-timeout";
import {useForm} from "react-hook-form";
import { useStoreTableQuery} from "@/data/dashboard-admin";
import {useBrandsQuery} from "@/data/brands";
import {Table} from "@/components/ui/table";
import Pagination from "@/components/ui/pagination";
import StoreChart from "@/components/dashboard/store-chart";

type IProps = {
  supplierIdObject: any;
  searchTimeOptions: any;
  brandDashboard?: boolean;
};

const StoreTableComponent = ({ supplierIdObject, searchTimeOptions, brandDashboard = false } : IProps) => {
  const { t } = useTranslation();
  const { inputText: brandName, onInputChange: handleInputChangeBrand } =
    useInputTimeout();
  const { brands, loading: loadingBrands } = useBrandsQuery(
    { page: 1, pageSize: PAGE_SIZE },
    // {
    //   brandName: brandName,
    //   supplierIdObject: supplierIdObject.supplierIdObject,
    //   validYn: CommonYesNoEnum.YES,
    // }
    supplierIdObject,
    !brandDashboard
  );

  const [searchWithBrandID, setSearchWithBrandID] = useState<Partial<SearchValue>>({});
  const [page, setPage] = useState(1);
  const { storeTableData, loading, paginatorInfo, error } = useStoreTableQuery(
    { page, pageSize: PAGE_SIZE },
    searchWithBrandID
  );
  // console.log("storeTableData", storeTableData)
  const [storeId, setStoreId] = useState("");
  const [storeName, setStoreName] = useState("");


  const handleChangeBrand = (brands: Brands) => {
    // @ts-ignore
    setValue('brand', brands);
    const newDataQuery = {/* ...searchOptions,*/ brandId: brands?.id };
    setSearchWithBrandID(newDataQuery);
  };
  function handlePagination(current: number) {
    setPage(current);
  }
  function handleStoreNameClick(storeName: string, storeId: string) {
    setStoreName(storeName)
    setStoreId(storeId)
  }
  const columnsStoreTable = [
    {
      title: 'No',
      dataIndex: 'index',
      key: 'index',
      align: 'left',
      width: 0.3,
      render: (text: any, record: any, index: number) => {
        const currentPage = paginatorInfo?.currentPage ?? 1;
        const perPage = paginatorInfo?.perPage ?? 0;
        return (currentPage - 1) * perPage + index + 1;
      },
    },{
      title: <span className="text-xs">Store id</span>,
      dataIndex: 'storeId',
      key: 'storeId',
      align: 'left',
      width: 0,
      render: (storeId: string ) => (
        <span className="truncate whitespace-nowrap text-purple-500" >
            {storeId}
        </span>
      ),
    },
    {
      title: <span className="text-xs">Store name</span>,
      dataIndex: 'storeName',
      key: 'storeName',
      align: 'left',
      width: 2,
      render: (storeName: string, record: any) => (
        <span
          className="truncate whitespace-nowrap text-purple-500"
          onClick={() => handleStoreNameClick(storeName, record.storeId)}
          style={{ cursor: 'pointer' }}
        >
        {storeName}
      </span>
      ),
    },
    {
      title: <span className="text-xs">Used voucher count</span>,
      dataIndex: 'usedCount',
      key: 'usedCount',
      width: 1,
      align: 'left',
      ellipsis: true,
      render: (usedCount: string) => (
        <span className="truncate whitespace-nowrap">{usedCount}</span>
      ),
    },
    {
      title: <span className="text-xs">Sales Amount</span>,
      dataIndex: 'usedAmount',
      key: 'usedAmount',
      width: 1,
      align: 'left',
      ellipsis: true,
      render: (usedAmount: string) => (
        <span className="truncate whitespace-nowrap ">{usedAmount}</span>
      ),
    }
  ];
  const {
    register,
    handleSubmit,
    control,
    setValue,
    setError,
    formState: { errors },
  } = useForm<{
    customer: Customer;
  }>({});
  const onSubmit = async (values: any) => {};

  return (
    <>
      <div className="w-full md:w-1/2 shadow-md bg-white p-4 h-full md:h-1/2">
        <form onSubmit={handleSubmit(onSubmit)}>
          {/*select*/}
          {brands.length > 0 && <div className="flex w-full">
              <div className="flex w-full flex-wrap ">
                  <label className="w-full py-2 text-heading md:w-2/6 font-bold">
                    {t('Select brand to show store data: ')}
                  </label>
                  <div className="w-full md:w-4/6 ">
                      <SelectInput
                          name="brand"
                          options={brands}
                          isLoading={loadingBrands}
                          getOptionLabel={(option: any) =>
                            option.brandName + ' - ' + option.id
                          }
                          getOptionValue={(option: any) => option.id}
                          onInputChange={handleInputChangeBrand}
                          onChange={handleChangeBrand}
                          placeholder={t('common:filter-by-group-placeholder')}
                          control={control}
                          isClearable={true}
                      />
                  </div>
              </div>
          </div>}

          {/*store table*/}
          <h1 className="w-full font-bold ">Stores</h1>
          <div className="mb-6 overflow-hidden rounded shadow mt-4">
            <Table
              //@ts-ignore
              columns={columnsStoreTable}
              emptyText={() => (
                <div className="flex flex-col items-center py-6">
                  <div className="pt-6 text-sm font-semibold">
                    {('table:empty-table-data')}
                  </div>
                </div>
              )}
              data={storeTableData}
              rowKey="index"
              // scroll={{ x: 1000 }}
            />
          </div>

          {!!paginatorInfo?.totalCount && (
            <div className="flex items-center justify-end">
              <Pagination
                total={paginatorInfo.totalCount}
                current={paginatorInfo.currentPage}
                pageSize={PAGE_SIZE}
                onChange={handlePagination}
              />
            </div>
          )}
        </form>
      </div>
      {storeTableData.length > 0 && <div className="w-full md:w-1/2 h-full   ">
        <div className=" mt-28 shadow-md bg-white ">
          <StoreChart
              storeId={storeId}
              storeName={storeName}
              searchTimeOptions={searchTimeOptions}
          />
        </div>
      </div> }
    </>
  );
};

export default StoreTableComponent;

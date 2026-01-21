import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { useTranslation } from 'next-i18next';
import Layout from '@/components/layouts/owner';
import Card from '@/components/common/card';
import StoreQRCode from '@/components/store/store-qrcode';
import Button from '@/components/ui/button';
import Link from '@/components/ui/link';
import {
  Brands,
  Store,
  MappedPaginatorInfoEV,
  PaginatorInfoEV,
  PathOptions,
} from '@/types';
import { Routes } from '@/config/routes';
import { getUrlPublicAsset, getUrlLocalAsset } from '@/data/download';
import { downloadImage, getCurrentDateString } from '@/utils/common-utils';
import { PAGE_SIZE } from '@/utils/constants';
import Pagination from '@/components/ui/pagination';
import camelCaseKeys from 'camelcase-keys';

type IProps = {
  data?: Brands | null;
};

// const PAGE_SIZE = 1;
export default function BrandStoreListQr({ data }: IProps) {
  const router = useRouter();
  const { t } = useTranslation();

  const [page, setPage] = useState(1);
  const [pageTotal, setPageTotal] = useState(1);
  const [paginatorInfo, setPaginatorInfo] = useState<MappedPaginatorInfoEV>();

  // 'stores' is a state variable that holds an array of Store objects of a page (pagination)
  const [stores, setStores] = useState<Store[]>([]);
  const [isLoadingStores, setLoadingStores] = useState<boolean>(true);

  /**
   * 'mapStoreQr' is a state variable that holds a Map where keys are data generated QR codes (string) and values are data URLs (string) of the corresponding QR code images.
   */
  const [mapStoreQr, setMapStoreQr] = useState(new Map<string, string>());

  // console.log('mapStoreQr keys', mapStoreQr.keys());
  // console.log('mapStoreQr size', mapStoreQr.size);

  /**
   * Updates the map-like data structure `mapStoreQr`
   * This function associates the provided QR code text with the corresponding data URL.
   * @param {string} qrText - The QR code text to be used as the key.
   * @param {any} dataUrl - The data URL to be associated with the QR code text.
   */
  const handleCallbackDataUrl = (qrText: string, dataUrl: any) => {
    // console.log('handleCallbackDataUrl', qrText, dataUrl);

    if (qrText && dataUrl) {
      // Check qrText is id of one object of array stores (in current page)
      if (stores.some((store) => store.id === qrText)) {
        setMapStoreQr((prevMap) => {
          const newMap = new Map(prevMap);
          newMap.set(qrText, dataUrl);
          return newMap;
        });
      }
    }
  };

  /**
   * Function download all QRCode images of all Stores
   */
  const handleDownloadAllQR = async () => {
    // console.log('handleDownloadAllQR', mapStoreQr.size);

    // Download one by one Store's QRCode image
    mapStoreQr.forEach((value: string, key: string) => {
      // console.log(key, value);
      let dataUrl = value;
      let fileName = `store_qrcode_${key}_${getCurrentDateString()}.png`;
      let store = data?.stores.find((store) => store.id === key);
      if (store) {
        fileName = `store_qrcode_${store.id}_${
          store.storeName
        }_${getCurrentDateString()}.png`;
      }
      downloadImage(dataUrl, fileName);
    });
  };

  /**
   * Handles pagination for the store list.
   *
   * @param current The current page number (F).
   */
  const handlePagination = (current: number) => {
    // console.log('handlePagination', current);

    // Update the current page number
    setPage(current);
    setLoadingStores(true);
  };

  /**
   * Maps and updates the data for pagination.
   *
   * @param pathOps Path options for pagination.
   * @param storeAll The array of all stores.
   */
  const mapPaginatorData = (
    pathOps: PathOptions,
    storeAll: Store[] | undefined
  ): void => {
    if (!storeAll) return;

    // console.log('mapPaginatorData pathOps', pathOps);
    // console.log('mapPaginatorData storeAll', storeAll);

    // Slice the storeAll array to get stores for the current page
    const sliceStores = storeAll.slice(
      (pathOps.page! - 1) * pathOps.pageSize!,
      pathOps.page! * pathOps.pageSize!
    );

    // console.log('mapPaginatorData sliceStores', sliceStores)

    // Update the stores with the sliced array
    setStores(sliceStores);

    // Prepare pagination information
    const pageRes: PaginatorInfoEV<Store> = {
      totalCount: storeAll.length,
    } as PaginatorInfoEV<Store>;

    // Set per_page and current_page
    pageRes.per_page = pathOps?.pageSize ?? PAGE_SIZE;
    pageRes.current_page = pathOps?.page ?? 1;

    // Calculate last_page
    if (pageRes.totalCount % pageRes.per_page) {
      pageRes.last_page = pageRes.totalCount / pageRes.per_page;
      setPageTotal(Math.ceil(pageRes.last_page));
    } else {
      pageRes.last_page = pageRes.totalCount / pageRes.per_page + 1;
      setPageTotal(pageRes.last_page - 1);
    }

    // Prepare pageInfo with camelCased keys and hasMorePages flag
    const pageInfo = {
      ...camelCaseKeys(pageRes),
      hasMorePages: pageRes.last_page !== pageRes.current_page,
    };

    // console.log('mapPaginatorData pageInfo', pageInfo);

    // Update paginator info
    setPaginatorInfo(pageInfo);
  };

  /**
   * This effect manages pagination after loading all QR codes for stores on a page.
   */
  useEffect(() => {
    if (stores.length > 0 && mapStoreQr.size < stores.length) {
      setLoadingStores(true);
    } else {
      setLoadingStores(false);
    }
  }, [mapStoreQr]);

  /**
   * This effect handles page changes.
   */
  useEffect(() => {
    // console.log('useEffect', page);

    // Clear the map containing data URLs of Store's QRCode images for the old page
    setMapStoreQr(new Map<string, string>());

    // Call the 'mapPaginatorData' function with the current page and the array of stores
    mapPaginatorData({ page: page, pageSize: PAGE_SIZE }, data?.stores);
  }, [page]);

  return (
    <div>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          {stores.length > 0 && (
            <div className="mb-5 flex flex-wrap">
              {!isLoadingStores && (
                <div className="flex w-full flex-wrap px-4">
                  <Button
                    variant="outline"
                    size="small"
                    className="px-6 hover:bg-gray-500"
                    type="button"
                    onClick={handleDownloadAllQR}
                  >
                    {t('Download all QR')}
                  </Button>
                </div>
              )}
              <div className="flex w-full flex-wrap px-4">
                <span className="pt-2">
                  {isLoadingStores ? 'Loading' : 'Loaded'} {mapStoreQr.size}/
                  {stores.length}
                  &nbsp;(page {page}/{pageTotal})
                </span>
              </div>
            </div>
          )}

          <div className="mb-2 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4">
              {!!paginatorInfo?.totalCount && (
                <div className="flex items-center">
                  <Pagination
                    total={paginatorInfo.totalCount}
                    current={paginatorInfo.currentPage}
                    pageSize={PAGE_SIZE}
                    onChange={handlePagination}
                    disabled={isLoadingStores}
                  />
                </div>
              )}
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            {stores.map((store) => (
              <div
                className="flex w-full flex-wrap px-4 py-4 md:w-1/2"
                key={store.id}
              >
                <StoreQRCode
                  qrText={store?.id}
                  storeName={store?.storeName}
                  storeImgUrl={getUrlPublicAsset(store?.storeImagePath)!}
                  storeImgUrlLocal={getUrlLocalAsset(store?.storeImagePath)!}
                  onCallbackDataUrl={handleCallbackDataUrl}
                />
                <div className="flex w-full flex-wrap">
                  <Link
                    href={Routes?.stores?.details(store?.id)}
                    className="e_not_link"
                  >
                    <span className="truncate whitespace-nowrap">
                      {store?.id} {store?.storeName}
                    </span>
                  </Link>
                </div>
              </div>
            ))}
          </div>

          <div className="flex-reverse mb-4 flex text-end">
            <Button
              variant="outline"
              onClick={router.back}
              className="me-4 hover:bg-red-800"
              type="button"
            >
              {t('Back')}
            </Button>
          </div>
        </Card>
      </div>
    </div>
  );
}

BrandStoreListQr.layout = Layout;

import { useEffect, useRef, useState } from 'react';
import html2canvas from 'html2canvas';
import StoreQRCodeBackground from './store-qrcode-background';
import { DownloadIcon } from '@/components/icons/download-icon2';
import { downloadImage, getCurrentDateString } from '@/utils/common-utils';

type StoreQRCodeProps = {
  qrText: string;
  storeName: string;
  storeImgUrl?: string; //For display
  storeImgUrlLocal?: string; //For fetch data base64
  imgBgSize?: number;
  qrCodeSize?: number;
  logoSize?: number;
  onCallbackDataUrl?: (qrText: string, dataUrl: any) => void;
};

const StoreQRCode = ({
  qrText,
  storeName,
  storeImgUrl,
  storeImgUrlLocal = storeImgUrl,
  imgBgSize = 500,
  qrCodeSize = 300,
  logoSize = 50,
  onCallbackDataUrl,
}: StoreQRCodeProps) => {
  const imageStoreRef = useRef<HTMLImageElement | null>(null);
  const imageQrRef = useRef<HTMLDivElement | null>(null);

  /**
   * This state variable, imgStoreHeight, is used to manage the height of Store's image
   * that will be displayed in an HTML element. The value of this state has three possible meanings:
   * -1: Initialization state, indicating that the height has not been determined yet.
   *  0: The image couldn't be loaded or is not available, resulting in a height of 0.
   * >0: The actual height of the loaded image in pixels.
   */
  const [imgStoreHeight, setImgStoreHeight] = useState<number>(-1);

  /**
   * Data URL of the final image containing the QRCode.
   * Used for caching the image when first loaded to improve download speed.
   */
  const [storeQRCodeDataUrl, setStoreQRCodeDataUrl] = useState<string | null>();

  const btnDownloadStyle: React.CSSProperties = {
    position: 'relative',
    marginBottom: '-30px',
    right: '-20px',
    zIndex: 1,
  };

  const imgStoreStyle: React.CSSProperties = {
    position: 'relative',
    width: imgBgSize,
    // maxHeight: imgBgSize,
    // zIndex: 1,
    // border: '1px solid purple'
  };

  // /**
  //  * Returns the current date and time as a formatted string.
  //  *
  //  * @returns {string} The current date and time in the format: "YYYY-MM-DD_HH-MM-SS".
  //  */
  // const getCurrentDateString = (): string => {
  //   // Get the current date and time
  //   const now = new Date();

  //   // Extract year, month, day, hours, minutes, and seconds
  //   const year = now.getFullYear();
  //   const month = String(now.getMonth() + 1).padStart(2, '0');
  //   const day = String(now.getDate()).padStart(2, '0');
  //   const hours = String(now.getHours()).padStart(2, '0');
  //   const minutes = String(now.getMinutes()).padStart(2, '0');
  //   const seconds = String(now.getSeconds()).padStart(2, '0');

  //   // Create and return the formatted date and time string
  //   return `${year}-${month}-${day}_${hours}-${minutes}-${seconds}`;
  // };

  /**
   * Fetches the data URL of an image from the image Store.
   * @param imageUrl The URL of the image to fetch.
   * @returns The data URL of the fetched image.
   */
  const fetchImageStore = async (imageUrl: string) => {
    // console.log('fetchImageStore', imageUrl);
    try {
      const response = await fetch(
        `/api/convert-image?url=${encodeURIComponent(imageUrl)}`
      );
      if (!response.ok) {
        console.warn(
          'fetchImageStore',
          imageUrl,
          'request failed with status',
          response.status
        );
        return null;
      }

      const data = await response.json();
      // console.log('fetchImageStore', typeof data, data);

      if (data?.dataUrl?.startsWith('data:image/')) {
        return data.dataUrl;
      } else {
        console.warn('fetchImageStore', imageUrl, 'is not an image');
        return null;
      }
    } catch (error) {
      console.error(error);
      return null;
    }
  };

  /**
   * Fetches the data URL of a QRCode image (convert from html Div) (excluding image store).
   * @param imageRef Reference to the image element containing the QRCode.
   * @returns The data URL of the generated QRCode image.
   */
  const fetchImageQRCode = async (imageRef: any) => {
    // console.log('fetchImageQRCode');
    try {
      // Capture the content after the image has loaded
      if (imageRef.current) {
        const capturedCanvas = await html2canvas(imageRef.current);
        const dataUrl = capturedCanvas.toDataURL('image/png');
        return dataUrl;
      } else {
        console.error('fetchImageQRCode error');
        // throw new Error('Image reference is not available.');
      }
    } catch (error) {
      // throw error;
      console.error('fetchImageQRCode error', error);
    }
  };

  /**
   * Fetches the data URL of the final image containing both the image Store and QRCode.
   * @returns The data URL of the merged image.
   */
  const fetchImageStoreQRCode = async () => {
    // Fetch the store image and handle QR code image load
    const imgStoreDataUrl = await fetchImageStore(storeImgUrlLocal!);
    const imgQrDataUrl = await fetchImageQRCode(imageQrRef);

    if (imgStoreDataUrl) {
      if (imgQrDataUrl) {
        const mergedDataUrl = await mergeDataUrlImages(
          imgStoreDataUrl,
          imgQrDataUrl,
          imgBgSize
        );
        return mergedDataUrl;
      }
    } else {
      // If can't get image Store, just return image QRCode
      return imgQrDataUrl;
    }
  };

  // /**
  //  * Downloads an image using a Data URL.
  //  *
  //  * @param {string} dataUrl - The Data URL of the image to be downloaded.
  //  * @param {string} fileName - The desired name for the downloaded file.
  //  */
  // const downloadImage = (dataUrl: any, fileName: string) => {
  //   if (dataUrl && fileName) {
  //     // Create an anchor element with the data URL as the href and download attributes
  //     const link = document.createElement('a');
  //     link.href = dataUrl;
  //     link.download = fileName;
  //     link.click();
  //   }
  // };

  /**
   * Downloads an image QRCode (excluding image store).
   */
  const downloadImageQRCode = async () => {
    // Fetch the QR code data URL for the image
    const dataUrl = await fetchImageQRCode(imageQrRef);

    // Construct the download filename using relevant information
    const downloadFilename = `store_qrcode-only_${qrText}_${storeName}_${getCurrentDateString()}.png`;

    // Initiate the download of the QR code image
    downloadImage(dataUrl, downloadFilename);
  };

  /**
   * Downloads the image of the store.
   */
  const downloadImageStore = async () => {
    // Fetch the data URL for the store image
    const dataUrl = await fetchImageStore(storeImgUrlLocal!);

    // Construct the download filename using relevant information
    const downloadFilename = `store_img_${qrText}_${storeName}_${getCurrentDateString()}.png`;

    // Initiate the download of the store image
    downloadImage(dataUrl, downloadFilename);
  };

  /**
   * Downloads the store's QR code image (include Store's image and QRCode's image are combined)
   */
  const downloadImageStoreQRCode = async () => {
    // Fetch the data URL for the store's QR code image (or get from variable cached)
    const dataUrl = storeQRCodeDataUrl ?? (await fetchImageStoreQRCode());
    // const dataUrl = await fetchImageStoreQRCode();

    // If the QR code data URL is available
    if (dataUrl) {
      // // Invoke agaign (for update newest) the callback function with the data URL
      // onCallbackDataUrl?.(qrText, dataUrl);

      // Construct the download filename using relevant information
      const downloadFilename = `store_qrcode_${qrText}_${storeName}_${getCurrentDateString()}.png`;

      // Initiate the download of the store's QR code image
      downloadImage(dataUrl, downloadFilename);
    }
  };

  /**
   * This function takes two image URLs and a desired image size as input, and returns a Promise that resolves to a merged image data URL.
   * @param imageUrl1 The URL of the first image.
   * @param imageUrl2 The URL of the second image.
   * @param imgSize The desired size for the images.
   * @returns A Promise that resolves to a merged image data URL.
   */
  const mergeDataUrlImages = async (
    imageUrl1: string,
    imageUrl2: string,
    imgSize: number
  ): Promise<string> => {
    // console.log('mergeDataUrlImages imgStoreHeight', imgStoreHeight);

    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');

    if (!ctx) {
      console.error('Canvas context not supported');
      // throw new Error('Canvas context not supported');
      return '';
    }

    const img1 = new Image();
    img1.src = imageUrl1;
    img1.width = imgSize;
    img1.height = imgStoreHeight > 0 ? imgStoreHeight : imgSize;

    const img2 = new Image();
    img2.src = imageUrl2;
    img2.width = imgSize;
    img2.height = imgSize;

    await new Promise<void>((resolve, reject) => {
      img1.onload = () => resolve();
      img1.onerror = (error) => reject(error);
    });

    const _width = Math.max(img1.width, img2.width);
    canvas.width = _width;
    canvas.height = img1.height + img2.height; // Total height of both images

    // console.log('img1', img1.width, img1.height);
    // ctx.drawImage(img1, 0, 0);
    ctx.drawImage(img1, 0, 0, img1.width, img1.height);

    await new Promise<void>((resolve, reject) => {
      img2.onload = () => resolve();
      img2.onerror = (error) => reject(error);
    });

    ctx.drawImage(img2, 0, img1.height, img2.width, img2.height);

    return canvas.toDataURL('image/png');
  };

  /**
   * This function is called when the Store image has finished loading.
   * It adjusts the display height of the image and updates the state with the image's height.
   */
  const handleImageStoreLoad = async () => {
    // console.log('handleImageStoreLoad()');
    let heightTemp = 0;

    if (imageStoreRef.current) {
      // Get the current height of the store image using the imageStoreRef
      let { height } = imageStoreRef.current;

      // console.log(
      //   'handleImageStoreLoad(), height of the image store in div',
      //   height
      // );

      heightTemp = height;

      // If the height exceeds the desired background size, adjust the display height
      if (heightTemp > imgBgSize) {
        imageStoreRef.current.style.height = `${imgBgSize}px`;
      }
    }

    // Calculate the actual height to be used (minimum of height and background size)
    heightTemp = Math.min(heightTemp, imgBgSize);

    // Update the state with the calculated image height
    setImgStoreHeight(heightTemp);
  };

  /**
   * This function is called when the Store image fails to load.
   * It can be used for error handling and displaying a message to the user.
   */
  const handleImageStoreError = () => {
    console.warn('Image failed to load.', storeImgUrl);

    // Set height of image store to 0 to trigger useEffect
    setImgStoreHeight(0);
  };

  useEffect(() => {
    // console.log('useEffect(), imgStoreHeight', imgStoreHeight);

    if (imgStoreHeight > -1) {
      const loadStoreQRCode = async () => {
        let dataUrl = null;

        // If link image Store is wrong, just fetch dataUrl of QRCode image
        if (imgStoreHeight === 0) {
          dataUrl = await fetchImageQRCode(imageQrRef);
        } else {
          dataUrl = await fetchImageStoreQRCode();
        }
        if (dataUrl) {
          // Cache dataUrl of final image
          setStoreQRCodeDataUrl(dataUrl);

          // Callback to another component
          onCallbackDataUrl?.(qrText, dataUrl);

          // dd.tung for test download automatically
          // const downloadFilename = `store_qrcode_${qrText}_${storeName}_${getCurrentDateString()}.png`;
          // downloadImage(dataUrl, downloadFilename);
        }
      };
      loadStoreQRCode();
    }
  }, [imgStoreHeight]); // This effect runs whenever imgStoreHeight changes

  return (
    <div>
      <div className="flex flex-row-reverse" style={{ width: imgBgSize }}>
        <button onClick={downloadImageStoreQRCode} style={btnDownloadStyle}>
          <DownloadIcon className="h-12 w-12 shrink-0" />
        </button>
      </div>
      {/*<div style={imgStoreStyle}>*/}
      {/*  {storeImgUrl ? (*/}
      {/*    <img*/}
      {/*      ref={imageStoreRef}*/}
      {/*      src={storeImgUrl}*/}
      {/*      alt={`Store ${qrText}`}*/}
      {/*      width={imgBgSize}*/}
      {/*      // height={imgBgSize}*/}
      {/*      // max-height={imgBgSize}*/}
      {/*      onLoad={handleImageStoreLoad}*/}
      {/*      onError={handleImageStoreError}*/}
      {/*    />*/}
      {/*  ) : undefined}*/}
      {/*</div>*/}

      {/* Content of this div to be captured as an image */}
      <div ref={imageQrRef}>
        <StoreQRCodeBackground
          qrText={qrText}
          storeName={storeName}
          imgBgSize={imgBgSize}
          qrCodeSize={qrCodeSize}
          logoSize={logoSize}
        />
      </div>

      {/* <div className="mt-2 flex w-full flex-row-reverse">
        <button onClick={downloadImageStoreQRCode}>
          <DownloadIcon className="h-12 w-12 shrink-0" />
        </button>
      </div> */}

      {/* <div>
        <button onClick={downloadImageStore}>Download Store Image</button>
        <br />
        <button onClick={downloadImageQRCode}>Download QRCode Image</button>
        <br />
        <button onClick={downloadImageStoreQRCode}>
          Download Store QRCode Image
        </button>
      </div> */}
    </div>
  );
};

export default StoreQRCode;

import { DeliveryVoucher } from '@/types';
import { saveAs } from 'file-saver';
import JsBarcode from 'jsbarcode';
import JSZip from 'jszip';
import {toast} from "react-toastify";

export const downloadBarcode = async (
  data: string,
  filename: string = 'barcode'
): Promise<void> => {
  try {
    const canvas = document.createElement('canvas');

    JsBarcode(canvas, data, {
      format: 'CODE128',
      width: 2,
      height: 100,
      displayValue: true,
      fontSize: 16,
      margin: 10,
    });

    const barcodeDataURL = canvas.toDataURL('image/png');
    const res = await fetch(barcodeDataURL);
    const blob = await res.blob();

    saveAs(blob, `${filename}.png`);
  } catch (error) {
    console.error(`Error downloading barcode for ${filename}:`, error);
  }
};

export const downloadVouchersBarcodeZip = async (
  vouchers: DeliveryVoucher[],
  filename: string = 'vouchers_barcode'
): Promise<void> => {
  try {
    const zip = new JSZip();

    // Generate all barcodes
    const barcodePromises = vouchers.map(async (voucher, index) => {
      if (!voucher.extPin && !voucher.serialNo) {
        return null;
      }
      const canvas = document.createElement('canvas');
      const dataDownload = voucher.extPin ? voucher.extPin : voucher.serialNo;

      JsBarcode(canvas, dataDownload, {
        format: 'CODE128',
        width: 6,
        height: 300,
        displayValue: true,
        fontSize: 48,
        margin: 30,
      });

      const barcodeDataURL = canvas.toDataURL('image/png');
      const res = await fetch(barcodeDataURL);
      const blob = await res.blob();

      return {
        name: `${index + 1}.png`,
        blob: blob,
      };
    });

    const barcodes = (await Promise.all(barcodePromises)).filter(
      (result): result is { name: string; blob: Blob } => result !== null
    );

    // Add files directly to zip root
    barcodes.forEach(({ name, blob }) => {
      zip.file(name, blob);
    });

    const zipBlob = await zip.generateAsync({ type: 'blob' });
    saveAs(zipBlob, `${filename}.zip`);
  } catch (error) {
    console.error('Error creating barcode zip:', error);
  }
};

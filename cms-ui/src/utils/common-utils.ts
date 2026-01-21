export function generateId(): string {
  return Date.now().toString();
}

export function generateRandomString(): string {
  const characters =
    'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  //   const length = Math.floor(Math.random() * 10); // Random length less than 10
  const length = 10;
  let result = '';

  for (let i = 0; i < length; i++) {
    const randomIndex = Math.floor(Math.random() * characters.length);
    result += characters.charAt(randomIndex);
  }

  return result;
}


// Định dạng số với dấu , phân cách hàng nghìn
export const formatNumberWithCommas = (value: any): string => {
  if (typeof value === 'string') {
    value = parseFloat(value); // Chuyển đổi chuỗi thành số
  }
  return new Intl.NumberFormat('en-US').format(value);
};

export function formatDate(dateString: string): string {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

/**
 * Returns the current date and time as a formatted string.
 *
 * @returns {string} The current date and time in the format: "YYYY-MM-DD_HH-MM-SS".
 */
export function getCurrentDateString(): string {
  // Get the current date and time
  const now = new Date();

  // Extract year, month, day, hours, minutes, and seconds
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');

  // Create and return the formatted date and time string
  return `${year}-${month}-${day}_${hours}-${minutes}-${seconds}`;
}

/**
 * Validate string is type format date 'yyyy-MM-dd'
 * @param dateString
 * @returns
 */
export function isValidDateFormat(dateString: string): boolean {
  // Regular expression for 'yyyy-MM-dd' format with optional single-digit month and day
  const dateFormatRegex = /^\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2]\d|3[01])$/;
  return dateFormatRegex.test(dateString);
}

// Function to compare a date string in "yyyy-MM-dd" format with the current date
export function compareDates(dateString: string): number {
  // Convert the input date string to a Date object
  const inputDate = new Date(dateString);

  // Get the current date
  const currentDate = new Date();

  // Compare the two dates
  if (inputDate < currentDate) {
    return -1; // The input date is earlier than the current date
  } else if (inputDate > currentDate) {
    return 1; // The input date is later than the current date
  } else {
    return 0; // The input date is equal to the current date
  }
}

/**
 * Replace all matched non-digit characters with an empty string
 * @param input
 * @returns
 */
export function extractNumbers(input: string): string {
  // Use a regular expression to match all non-digit characters and replace them with an empty string
  if (!input) return '';
  const numbersOnly = input.replace(/\D/g, '');
  return numbersOnly;
}

/**
 * Downloads an image using a Data URL.
 *
 * @param {string} dataUrl - The Data URL of the image to be downloaded.
 * @param {string} fileName - The desired name for the downloaded file.
 */
export function downloadImage(dataUrl: any, fileName: string) {
  // console.log('downloadImage', fileName, dataUrl);
  if (dataUrl && fileName) {
    // Create an anchor element with the data URL as the href and download attributes
    const link = document.createElement('a');
    link.href = dataUrl;
    link.download = fileName;
    link.click();
  }
}

export function reformatTextSms(inputText: string, maxLength?: number): string {
  if (!inputText) return '';

  // Replace dots with spaces
  const cleanedText = inputText.toString().replace(/\./g, ' ').trim();

  // Use a regular expression to match and replace special characters with spaces
  // const cleanedText = inputText.replace(/[^a-zA-Z0-9\s]/g, ' ');

  // Replace multiple spaces with a single space
  let finalText = cleanedText.replace(/\s+/g, ' ');

  // Limit the string to characters
  if (maxLength && maxLength > 0) {
    finalText = finalText.slice(0, maxLength);
  }

  return finalText;
}

export function formatNumber(
  number: number,
  showK?: boolean,
  showVND?: boolean
): string {
  if (isNaN(number)) {
    return '';
  }
  const formattedAmount = new Intl.NumberFormat('vi-VN').format(number);

  if (showK) return `${formattedAmount}K`;
  if (showVND) return `${formattedAmount} VND`;
  return formattedAmount;
}

export function formatCurrencyVND(amount: number): string {
  if (isNaN(amount)) {
    return '';
  }
  const currencyFormatter = new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    minimumFractionDigits: 0,
  });

  const formattedCurrency = currencyFormatter.format(amount);

  // Replace the currency symbol '₫' with 'VND'
  const formattedCurrencyWithVND = formattedCurrency.replace('₫', 'VND');

  return formattedCurrencyWithVND;
}

export function validIPsList(ipString: any) {
    // Nếu input rỗng
    if (!ipString || typeof ipString !== 'string') {
        return false;
    }

    // Split chuỗi input thành mảng các IP, loại bỏ khoảng trắng
    const ips = ipString.split(',').map(ip => ip.trim());

    // Kiểm tra từng IP
    return ips.every(ip => {
        // Nếu IP rỗng sau khi trim
        if (!ip) return false;

        // Split IP thành các phần
        const parts = ip.split('.');

        // Kiểm tra có đúng 4 phần
        if (parts.length !== 4) return false;

        // Kiểm tra từng phần của IP
        return parts.every(part => {
            if (!/^\d+$/.test(part)) return false;
            const num = parseInt(part, 10);
            return num >= 0 && num <= 255;
        });
    });
}

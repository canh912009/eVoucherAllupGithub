import React, {useEffect, useMemo, useRef, useState} from 'react';
import {SYSTEM_BRAND_TYPE} from "@/utils/constants";
import {VOUCHER_TYPE_CODES} from "@/components/common/status-code-badge";
import Button from '@/components/ui/button';

type DownloadCsvDropdownProps = {
	onDownload: (system?: string, voucherType?: string) => Promise<void>;
	isDownloading?: boolean
};

const DownloadCsvDropdown: React.FC<DownloadCsvDropdownProps> = ({onDownload, isDownloading = false}) => {
	const [downloadOpen, setDownloadOpen] = useState(false);
	const [internalOpen, setInternalOpen] = useState(false);
	const dropdownRef = useRef<HTMLDivElement>(null);

  const menuAll = useMemo(() => [
		{label: 'Internal', system: SYSTEM_BRAND_TYPE.INTERNAL, voucherType: [VOUCHER_TYPE_CODES.SI, VOUCHER_TYPE_CODES.PP, VOUCHER_TYPE_CODES.LC] },

		{label: 'Bulk', system: SYSTEM_BRAND_TYPE.BULK, voucherType: [VOUCHER_TYPE_CODES.BK]},

		{label: 'Choice', system: SYSTEM_BRAND_TYPE.CHOICE, voucherType: [VOUCHER_TYPE_CODES.CH]},

		{ label: 'External', system: SYSTEM_BRAND_TYPE.EXTERNAL, voucherType: [VOUCHER_TYPE_CODES.SI, VOUCHER_TYPE_CODES.PP] },

		{label: 'Giftpop', system: SYSTEM_BRAND_TYPE.GIFTPOP, voucherType: [VOUCHER_TYPE_CODES.SI, VOUCHER_TYPE_CODES.PP]},

		{label: 'Urbox', system: SYSTEM_BRAND_TYPE.UR_BOX, voucherType: [VOUCHER_TYPE_CODES.SI, VOUCHER_TYPE_CODES.PP]},

		{label: 'Watane', system: SYSTEM_BRAND_TYPE.WATANE, voucherType: [VOUCHER_TYPE_CODES.SI, VOUCHER_TYPE_CODES.PP]},

		{label: 'VNPT Epay', system: SYSTEM_BRAND_TYPE.VNPT_EPAY, voucherType: [VOUCHER_TYPE_CODES.SI, VOUCHER_TYPE_CODES.PP] },

		{label: 'Xpay', system: SYSTEM_BRAND_TYPE.XPAY, voucherType: [VOUCHER_TYPE_CODES.SI, VOUCHER_TYPE_CODES.PP] },

		{label: 'Download all', system: SYSTEM_BRAND_TYPE.ALL, voucherType: [] },
  ], []);

  const menuInternal = useMemo(() => [
		{label: 'Single item', voucherType: VOUCHER_TYPE_CODES.SI},
		{label: 'Pre-paid', voucherType: VOUCHER_TYPE_CODES.PP},
		{label: 'Limited count', voucherType: VOUCHER_TYPE_CODES.LC},
  ], []);

	useEffect(() => {
	  function handleClickOutside(event: MouseEvent) {
	    if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
	      setDownloadOpen(false);
	      setInternalOpen(false);
	    }
	  }

	  document.addEventListener('mousedown', handleClickOutside);
	  return () => {
	    document.removeEventListener('mousedown', handleClickOutside);
	  };
	}, []);

	function showButtonDropOrLoad() {
		if(isDownloading) return
		return <>
			{!downloadOpen
				? <svg
					className="-mr-2 ml-2 h-6 w-5"
					xmlns="http://www.w3.org/2000/svg"
					viewBox="0 0 20 20"
					fill="currentColor"
				>
					<path fillRule="evenodd"
					      d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
					      clipRule="evenodd"/>
				</svg>
				: <svg
					className="-mr-2 ml-2 h-6 w-5"
					xmlns="http://www.w3.org/2000/svg"
					viewBox="0 0 20 20"
					fill="currentColor"
				>
					<path
						fillRule="evenodd"
						d="M14.707 12.707a1 1 0 00-1.414 0L10 9.414l-3.293 3.293a1 1 0 11-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z"
						clipRule="evenodd"
					/>
				</svg>
			}
		</>;
	}

	return (
		<div className="relative inline-block text-left" ref={dropdownRef}>
			<Button
				type="button"
				className="ml-1 inline-flex justify-center w-full rounded-md border border-gray-300 shadow-sm py-3 bg-red-600  font-bold text-white hover:bg-red-700 focus:outline-none"
				onClick={() => {
					setDownloadOpen(!downloadOpen)
					if (!downloadOpen) {
						setInternalOpen(false)
					}
				}}
				loading={isDownloading}
				disabled={isDownloading}
        aria-expanded={downloadOpen}
        aria-haspopup="true"
        id="download-menu-button"
			>
				Download (CSV)
				{showButtonDropOrLoad()}
			</Button>

			{/* Dropdown */}
			{downloadOpen && <div className="origin-top-left absolute left-0 mt-2 w-25 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50">
				<div className="py-1">
					<button
						onMouseEnter={() => setInternalOpen(true)}
						className="w-full flex justify-center items-center px-4 py-3 text-sm text-gray-700 hover:bg-gray-100 relative  font-bold"
					>
						Internal
						<span className="absolute left-4">{'<'}</span>
					</button>
					{internalOpen && <div className="absolute top-0 right-full mt-2 w-full rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none">
						<div className="py-1">
							{menuInternal.map((item, index) => (
								<button
									key={index}
									onClick={() => {
											setDownloadOpen(false);
											onDownload(SYSTEM_BRAND_TYPE.INTERNAL, item.voucherType)
										}
									}
									className="w-full py-3 text-sm text-gray-700 hover:bg-gray-100 font-bold"
								>
									{item.label}
								</button>
							))}
						</div>
					</div>}
					{menuAll.filter(item => item.system !== SYSTEM_BRAND_TYPE.INTERNAL).map((item, index) => (
						<button
							key={index}
							onClick={() => {
									setDownloadOpen(false);
									onDownload(item.system)
								}
							}
							onMouseEnter={() => setInternalOpen(false)}
							className="w-full py-3 text-sm text-gray-700 hover:bg-gray-100 font-bold"
						>
							{item.label}
						</button>
					))}
				</div>
			</div>}
		</div>
	);
};

export default DownloadCsvDropdown;

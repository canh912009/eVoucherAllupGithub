import {
	Brands,
	Category,
	Code, CommonApproveStatusAction,
	GiftPopGood,
	Good,
	Store,
	SupplierContract,
	UrBoxBrand,
	UrBoxGood, VNPTEPayProvider,
	VnptGood, WataneGood
} from "@/types";
import {useTranslation} from "next-i18next";
import Card from "../common/card";
import {Controller, useForm} from "react-hook-form";
import {DatePicker} from '@/components/ui/date-picker';
import ValidationError from '@/components/ui/form-validation-error';
import SelectInput from '@/components/ui/select-input-autocomplete';
import {useCreateGoodMutation, useUpdateGoodMutation} from "@/data/good";
import {getErrorMessage} from "@/utils/form-error";
import {yupResolver} from "@hookform/resolvers/yup";
import {goodValidationSchema} from "./good-validation-schema";
import Button from '@/components/ui/button';
import {useRouter} from 'next/router';
import {CODE_GROUP, PAGE_SIZE, PAGE_SIZE_MAX, SYSTEM_BRAND_TYPE} from "@/utils/constants";
import {useSupplierQuery, useSuppliersQuery} from "@/data/supplier";
import useInputTimeout from "@/utils/use-input-timeout";
import {useBrandsQuery} from "@/data/brands";
import React, {useEffect, useState} from "react";
import {useSupplierContractQuery, useSupplierContractsQuery} from "@/data/supplier-contract";
import {useCodeGroupQuery} from "@/data/code-group";
import Radio from '@/components/ui/radio/radio';
import TextArea from "@/components/ui/text-area";
import {formatDate, formatNumber} from '@/utils/common-utils';
import {useUploadImageMutation} from '@/data/upload';
import {ChangeEvent} from 'react';
import {CloseIcon} from '@/components/icons/close-icon';
import {useModalAction} from "../ui/modal/modal.context";
import {getUrlPublicAsset} from "@/data/download";
import {emptyPlaceholder} from '@/utils/placeholders';
import {supplierClient, supplierContractClient} from "@/data/client/crud-client";
import {toast} from "react-toastify";
import Loader from "@/components/ui/loader/loader";
import {useDropzone} from 'react-dropzone';
import {useCodeQuery} from "@/data/code";
import {SortableListCategory} from "@/components/ui/sortable-list-category";
import {SortableListBrand} from "@/components/ui/sortable-list-brand";
import {sortBulkCategoriesByDisplayIndex} from "@/data/categories";
import {SortableListGood} from "@/components/ui/sortable-list-good";
import {useProvidersVNPTEpayListQuery} from "@/data/vnpt-epay";
import {VnptGoodsCard} from "@/components/ui/vnptCard";
import {VOUCHER_TYPE_CODES} from "@/components/common/status-code-badge";
import {useProvidersXpayListQuery} from "@/data/xpay";


type IProps = {
	initialValues?: Good | null;
};

type FormValues = Partial<Good> & {
	id: string;
	supplierContract: SupplierContract;
	supplierId: string;
	brandId: string;
	goodsType: string;
	displayType: string;
	settlementMethodCode: string;
	errorMessage: string;
	image: any;
};

export const handlePriceInputChange = (event: React.ChangeEvent<HTMLInputElement>, id: string, setValue: (id, value) => void) => {
	const formattedValue = event.target.value.replace(/\D/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, ',')
	setValue(id, formattedValue);
};

export default function CreateOrUpdateGoodForm({initialValues}: IProps) {
	// console.log('[CreateOrUpdateGoodForm] initialValues = ', initialValues)
	const {t} = useTranslation();
	const router = useRouter();
	const {openModal, closeModal} = useModalAction();
	const cloneGood = router?.query?.action === 'clone';

	const rootClassName =
		'ps-4 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

	let classes = {
		// title: 'font-semibold',
		// content: 'font-normal text-[#212121]',
		wrapper:
			'flex flex-wrap pb-2 my-5 border-b border-dashed border-border-base sm:my-2 ',
		side_left: 'w-full px-0 pb-5 sm:w-1/4 sm:py-8 sm:pe-4 md:w-1/4 md:pe-5',
		side_right: 'w-full sm:w-3/4 md:w-3/4',
		row: 'mb-1 flex flex-wrap',
		title: 'w-full md:w-1/4 px-4 py-2 text-heading',
		content: 'w-full md:w-3/4 px-4 py-2',
	};

	const {
		register,
		handleSubmit,
		control,
		setError,
		setValue,
		getValues,
		watch,
		formState: {errors},
	} = useForm<FormValues>({
		resolver: yupResolver(goodValidationSchema),
		...(initialValues && {
			defaultValues: {
				...initialValues,
				startDate: initialValues.startDate
					? new Date(initialValues.startDate!)
					: '',
				endDate: initialValues.endDate
					? new Date(initialValues.endDate!)
					: '',
				periodExpireDate: initialValues.periodExpireDate
					? new Date(initialValues.periodExpireDate!)
					: '',
			} as any,
		}),
	});

	// const [selectedSupplierId, setSelectedSupplierId] = useState("");

	// (Update Page) initialize supplier, brand, supplier contract
	let supplierContractInit: any = null;

	if (initialValues?.supplierContractId) {
		const querySupplierContractResult = useSupplierContractQuery(initialValues?.supplierContractId as string);
		supplierContractInit = querySupplierContractResult.supplierContract;
	}

	const {inputText: supplierName, onInputChange: handleInputChangeSupplier} = useInputTimeout();

	// Just search suppliers approved
	const supplierId = watch('supplierId');
	const supplier = watch('supplier');
	const brand = watch('brand');
	const supContractId = watch('supplierContractId');
	const listPriceWatch = watch('listPrice')?.codeId as string;
	const goodsTypeCodeWatch = watch('goodsTypeCode')?.codeId

	/**
	 * Supplier handling
	 */
	const {suppliers, loading} = useSuppliersQuery(
		{page: 1, pageSize: PAGE_SIZE},
		{supplierName: supplierName, approveStatusCode: 'APPRV'}
	);

	const giftPopTypeResult = useCodeQuery(
		CODE_GROUP.GIFTPOP,
		"SUPPLIER_ID"
	);
	const urboxTypeResult = useCodeQuery(
		CODE_GROUP.UR_BOX,
		"SUPPLIER_ID"
	);
	const wataneTypeResult = useCodeQuery(
		CODE_GROUP.WATANE,
		"SUPPLIER_ID"
	);
	const vnptTopupTypeResult = useCodeQuery(
		CODE_GROUP.VNPT_EPAY,
		"SUPPLIER_ID"
	);
	const xpayTypeResult = useCodeQuery(
		CODE_GROUP.XPAY,
		"SUPPLIER_ID"
	);
	const [giftPopSupplier, setGiftPopSupplier] = useState(false);
	const [urboxSupplier, setUrboxSupplier] = useState(false);
	const [wataneSupplier, setWataneSupplier] = useState(false);

	const handleChangeSupplier = (option: any) => {
		setValue('supplier', option);
		// setSelectedSupplierId(option?.id);
		setValue('supplierId', option?.id);
		setValue('brand', null as unknown as Brands)
		setSelectedBrand(null as unknown as Brands)
		setValue('supplierContract', null as unknown as SupplierContract)
		// @ts-ignore
		setValue('supplierContractId', '');
		setValue('goodsDescription', "")
		setValue('sellPrice', "")
		setValue('listPrice', "")
		setValue('supplierGoodsId', "")
		setValue('goodsImgPath', '');
		setValue('goodsImgName', '');
	};

	useEffect(() => {
		if (supplier?.id === giftPopTypeResult?.code?.codeName) {
			setGiftPopSupplier(true)
			setValue('system', CODE_GROUP.GIFTPOP);
		} else {
			setGiftPopSupplier(false);
		}

		if (supplier?.id === urboxTypeResult?.code?.codeName) {
			setUrboxSupplier(true)
			setValue('system', CODE_GROUP.UR_BOX);
		} else {
			setUrboxSupplier(false);
		}

		if (supplier?.id === wataneTypeResult?.code?.codeName) {
			setWataneSupplier(true)
			setValue('system', CODE_GROUP.WATANE);
		} else {
			setWataneSupplier(false);
		}

		if (supplier?.id === vnptTopupTypeResult?.code?.codeName) {
			setValue('system', CODE_GROUP.VNPT_EPAY);
			setValue('supplierGoodsId', " ") // loại này k có product code đâu ạ @DaoNT
		}

		if (supplier?.id === xpayTypeResult?.code?.codeName) {
			setValue('system', CODE_GROUP.XPAY);
			setValue('supplierGoodsId', " ") // loại này k có product code đâu ạ @DaoNT
		}
		if (supplier === undefined) {
			setGiftPopSupplier(false);
			setUrboxSupplier(false);
			setWataneSupplier(false);
			setValue('system', "");
			setValue('supplierGoodsId', "")
		}

	}, [supplier]);


	/**
	 * Brand handling
	 */
	const {inputText: brandName, onInputChange: handleInputChangeBrand} = useInputTimeout();

	const {brands, loading: brandsLoading} = useBrandsQuery(
		{page: 1, pageSize: PAGE_SIZE},
		{brandName: brandName, supplierId: supplierId, 'validYn': 'Y'}
	);
	const [selectedBrand, setSelectedBrand] = useState<Brands>();
	// select Brand
	const handleChangeBrand = (option: any) => {
		// console.log('handleChangeBrand option = ', option)
		setValue('brand', option);
		setValue('system', option.system);
		setSelectedBrand(option);
		if (option.system === SYSTEM_BRAND_TYPE.CHOICE) {
			// @ts-ignore
			setValue('goodsTypeCode', VOUCHER_TYPE_CODES.CH);
		}
		if (option.system === SYSTEM_BRAND_TYPE.BULK) {
			// @ts-ignore
			setValue('goodsTypeCode', "BULK");
		}
		if (option.system === SYSTEM_BRAND_TYPE.VNPT_EPAY) {
			// @ts-ignore
			setValue('goodsTypeCode', "VNPT_EPAY");
		}
		if (option.system === SYSTEM_BRAND_TYPE.XPAY) {
			// @ts-ignore
			setValue('goodsTypeCode', "XPAY");
		}
		if (option)
			setValue('supplierId', option?.supplierId);
	};

	const system = watch('system');
	const systemINTERNAL = ((system === SYSTEM_BRAND_TYPE.INTERNAL) ? true : false)
	const systemCHOICE = ((system === SYSTEM_BRAND_TYPE.CHOICE) ? true : false)
	const systemBULK = ((system === SYSTEM_BRAND_TYPE.BULK) ? true : false)
	const systemVnptEpay = ((system === SYSTEM_BRAND_TYPE.VNPT_EPAY) ? true : false)
	const systemXpay = ((system === SYSTEM_BRAND_TYPE.XPAY) ? true : false)
	/**
	 * Contract selection
	 */
	const {supplierContracts} = useSupplierContractsQuery(
		{page: 1, pageSize: PAGE_SIZE},
		{supplierId: supplierId}
	);

	const optionsLcCount = Array.from({length: 100}, (_, i) => ({
		value: i + 1
	}));

	const handleChangeSupplierContract = (option: any) => {
		// console.log('supplierContract', option);
		setValue('supplierContract', option);
		if (option) {
			setValue('supplierId', option?.supplierId);
			setValue('supplierContractId', option?.id)
		}
	}

	const {codeGroup: goodsTypeCodeQuery, loading: codeLoading} = useCodeGroupQuery(CODE_GROUP.GOODS_TYPE);

	const {codeGroup: settlementMethodCodeQuery} = useCodeGroupQuery(CODE_GROUP.SETTLEMENT_METHOD_CD)

	const {
		codeGroup: vnptFaceValuesCodeQuery,
		loading: codeLoadingvnptFaceValues
	} = useCodeGroupQuery(CODE_GROUP.VNPT_FACE_VALUES)

	const {
		codeGroup: xpayFaceValuesCodeQuery,
		loading: codeLoadingXpayFaceValues
	} = useCodeGroupQuery(CODE_GROUP.XPAY_FACE_VALUES)

	const handleChangeGoodType = (option: any) => {
		// console.log('handleGoodTypeChange', option);
		setValue('goodsTypeCode', option)
	}

	const handleChangeSettlementMethod = (option: any) => {
		setValue('settlementMethodTypeCode', option)
	}

	const handleChangeVnptEpayListPrice = async (option: any) => {
		setValue('listPrice', option);

		await new Promise<void>((resolve) => {
			setVnptSelectedGoods?.((prevSelectedItems) => {
				const filteredItems = prevSelectedItems?.filter((item: any) => {
					try {
						const allowedFaces = JSON.parse(item?.vnptProvider?.allowedCardFaces ?? item?.allowedCardFaces);
						return Array.isArray(allowedFaces) && allowedFaces.includes(Number(option.codeId));
					} catch (error) {
						console.error("Error parsing allowedCardFaces:", error);
						return false;
					}
				});

				// Resolve Promise sau khi filter hoàn tất
				resolve();
				return filteredItems;
			});
		});
	};

	function handleSelectShowPopup(modalView: any) {
		if (modalView && modalView === "SELECT_CATEGORIES") {
			openModal(modalView, {handleSaveCategories});
		} else if (modalView && modalView === "SELECT_EXCEPTED_STORE") {
			openModal(modalView, {handleSaveExceptedStores, selectedBrand});
		} else if (modalView && modalView === "SELECT_BRANDS_BULK") {
			openModal(modalView, {handleSaveBrandsBULK, categoryCodeBULKSelected});
		} else if (modalView && modalView === "SELECT_GOODS_BULK") {
			openModal(modalView, {handleSaveGoodsBULK, categoryCodeBULKSelected, brandIdBULKSelected});
		} else if (modalView && modalView === "PRODUCTS_CHOICES_TYPE") {
			openModal(modalView, {handleSaveGoodsChoicesType});
		} else if (modalView && modalView === "GIFTPOP_GOOD_CODE") {
			openModal(modalView, {handleSaveGiftpop, selectedBrand});
		} else if (modalView && modalView === "URBOX_GOOD_CODE") {
			openModal(modalView, {handleSaveUrBox, selectedBrand});
		} else if (modalView && modalView === "WATANE_GOOD_CODE") {
			openModal(modalView, {handleSaveWatane});
		}
	}

	const [selectedCategories, setSelectedCategories] = useState<Category[]>(
		systemBULK ? sortBulkCategoriesByDisplayIndex(initialValues?.bulkCategories ?? []) : initialValues?.categories ?? []
	);

	function handleSaveCategories(items: any[]) {
		// console.log('items', items);
		items.forEach((item: any) => addCategory(item));
		closeModal();
	}

	const addCategory = (option: Category) => {
		setErrorMessageCategories('')
		if (option?.categoryCode) {
			let checkExist = selectedCategories.some((category) => category.categoryCode === option.categoryCode)
			if (!checkExist) {
				setSelectedCategories((prevCategories) => [...prevCategories, option])
			}
		}
	}
	const removeCategory = (option: Category) => {
		if (option?.categoryCode) {
			setSelectedCategories((prevCategories) =>
				prevCategories.filter((category) => category.categoryCode !== option.categoryCode)
			);
		}
	}

	const [selectedExceptStores, setSelectedExceptStores] = useState<Store[]>(
		initialValues?.exceptStores ?? []
	)

	function handleSaveExceptedStores(items: any) {
		items.forEach((item: any) => addExceptStore(item));
		closeModal();
	}

	const addExceptStore = (option: Store) => {
		if (option?.id) {
			let checkExist = selectedExceptStores.some((store) => store.id === option.id)
			if (!checkExist) {
				setSelectedExceptStores((prevExceptStores) => [...prevExceptStores, option])
			}
		}
	}
	const removeExceptStore = (option: Store) => {
		if (option?.id) {
			setSelectedExceptStores((prevExceptStore) =>
				prevExceptStore.filter((exceptStore) => exceptStore.id !== option.id)
			);
		}
	}

	const [categoryCodeBULKSelected, setCategoryCodeBULKSelected] = useState("");
	const onCategoryBULKSelected = (categoryCode: string) => {
		// console.log("categoryCodeBULKSelected", categoryCode)
		if (categoryCodeBULKSelected === categoryCode) {
			return
		}

		setCategoryCodeBULKSelected(categoryCode)
		setBrandIdBULKSelected("")
		selectedCategories?.filter(category => {
			if (category?.categoryCode === categoryCode) {
				setListBrandsBULK(category?.bulkBrands ?? [])
			}
		})
		setListGoodsBULK([])
	};
	const onCategoryBULKDelete = (categoryCode: string) => {
		if (categoryCodeBULKSelected === categoryCode) {
			setCategoryCodeBULKSelected("")
			setBrandIdBULKSelected("")
			setListBrandsBULK([])
			setListGoodsBULK([])
		}
		setSelectedCategories(prevCategories =>
			prevCategories.filter(category => category.categoryCode !== categoryCode)
		);
	};
	useEffect(() => {
		console.log('selectedCategories updated:', selectedCategories);
	}, [selectedCategories]);

	const bulkBrandsWithCategory = (categoryCode: string) => {
		const category = selectedCategories.find(cat => cat.categoryCode === categoryCode)
		return category?.bulkBrands ?? []
	};

	const bulkGoodsWithCategoryAndBrand = (categoryCode: string, brandId: string) => {
		const brand = bulkBrandsWithCategory(categoryCode).find(brand => brand.brandId === brandId)
		return brand?.bulkGoods ?? []
	};

	// brandsBULK
	const [brandIdBULKSelected, setBrandIdBULKSelected] = useState("");
	const onBrandBULKSelected = (brandID: string) => {
		// console.log("onBrandBULKSelected", brandID)
		if (brandIdBULKSelected === brandID) {
			return
		}
		setBrandIdBULKSelected(brandID)
		selectedCategories?.filter(category => {
			if (category?.categoryCode === categoryCodeBULKSelected) {
				category.bulkBrands?.filter(brand => {
					if (brand?.brandId === brandID) {
						setListGoodsBULK(brand?.bulkGoods ?? []);
					}
				})
			}
		})
	};
	const onBrandBULKDelete = (id: string) => {
		if (brandIdBULKSelected === id) {
			setBrandIdBULKSelected("");
			setListGoodsBULK([]);
		}

		setSelectedCategories(prevCategories =>
			prevCategories.map(category => {
				if (category?.categoryCode === categoryCodeBULKSelected) {
					return {
						...category,
						bulkBrands: category.bulkBrands?.filter(brand => brand?.brandId !== id) || []
					};
				}
				return category;
			})
		);
	};

	const [listBrandsBULK, setListBrandsBULK] = useState<Brands[]>(
		initialValues?.brandsBULK ?? []
	);

	function handleSaveBrandsBULK(items: any) {
		// console.log("handleSaveBrandsBULK", items)
		items.forEach((item: any) => addBrandsBULK(item));
		closeModal();
	}

	const addBrandsBULK = (option: Brands) => {
		setErrorMessageBrandsBULK('')

		// Then update selectedCategories using the latest listBrandsBULK
		// @ts-ignore
		setSelectedCategories(prevCategories => prevCategories.map(category => {
			if (category?.categoryCode === categoryCodeBULKSelected) {
				const existingBrands = category.bulkBrands || [];
				const modifiedOption = {...option, brandId: option.id, id: null};
				const isDuplicate = existingBrands.some(brand => brand.brandId === modifiedOption.brandId);

				if (!isDuplicate) {
					return {...category, bulkBrands: [...existingBrands, modifiedOption]};
				}
			}
			return category;
		}));
	}

	useEffect(() => {
		console.log('listBrandsBULK updated:', listBrandsBULK);
		setSelectedCategories(prevCategories =>
			prevCategories.map(category => {
				if (category?.categoryCode === categoryCodeBULKSelected) {
					return {
						...category,
						bulkBrands: listBrandsBULK
					};
				}
				return category;
			})
		);
	}, [listBrandsBULK]);

	// goodsBULK //productBULK
	const [listGoodBULK, setListGoodsBULK] = useState<Good[]>(
		initialValues?.goodsBULK ?? []
	);

	function handleSaveGoodsBULK(items: any) {
		items.forEach((item: any) => addGoodsBULK(item));
		closeModal();
		console.log("handleSaveGoodsBULK", items)
	}

	const addGoodsBULK = (option: Good) => {
		setErrorMessageGoodsBULK('');
		// @ts-ignore
		setSelectedCategories(prevCategories =>
			prevCategories.map(category => {
				if (category?.categoryCode === categoryCodeBULKSelected) {
					return {
						...category,
						bulkBrands: category.bulkBrands?.map(brand => {
							if (brand.brandId === brandIdBULKSelected) {
								const modifiedOption = {...option, goodsId: option.id, id: null};
								const goodExists = brand.bulkGoods?.some(good => String(good.goodsId) === String(modifiedOption.goodsId));
								if (!goodExists) {
									return {
										...brand,
										bulkGoods: [...(brand.bulkGoods || []), modifiedOption]
									};
								}
							}
							return brand;
						}) || []
					};
				}
				return category;
			})
		);
	};
	const moveTopGoodBULK = (option: Good) => {
		setSelectedCategories(prevCategories => prevCategories.map(category => {
			if (category?.categoryCode === categoryCodeBULKSelected) {
				return {
					...category,
					bulkBrands: category.bulkBrands?.map(brand => {
						if (brand.brandId === brandIdBULKSelected) {
							const updatedBulkGoods = [
								option,
								...(brand?.bulkGoods ?? []).filter(good => good.goodsId !== option.goodsId)
							];

							return {
								...brand,
								bulkGoods: updatedBulkGoods
							};
						}
						return brand;
					}) || []
				};
			}
			return category;
		}));
	};
	const moveUpGoodBULK = (option: Good) => {
		setSelectedCategories(prevCategories => prevCategories.map(category => {
			if (category?.categoryCode === categoryCodeBULKSelected) {
				return {
					...category,
					bulkBrands: category.bulkBrands?.map(brand => {
						if (brand.brandId === brandIdBULKSelected) {
							const index = brand?.bulkGoods?.findIndex(good => good.goodsId === option.goodsId);
							if (index !== undefined && index > 0) {
								// @ts-ignore
								const newBulkGoods = [...brand?.bulkGoods];
								[newBulkGoods[index - 1], newBulkGoods[index]] = [newBulkGoods[index], newBulkGoods[index - 1]];
								return {
									...brand,
									bulkGoods: newBulkGoods
								};
							}
						}
						return brand;
					}) || []
				};
			}
			return category;
		}));
	};
	const moveDownGoodBULK = (option: Good) => {
		setSelectedCategories(prevCategories => prevCategories.map(category => {
			if (category?.categoryCode === categoryCodeBULKSelected) {
				return {
					...category,
					bulkBrands: category.bulkBrands?.map(brand => {
						if (brand.brandId === brandIdBULKSelected) {
							const index = brand?.bulkGoods?.findIndex(good => good.goodsId === option.goodsId);
							if (index !== undefined && index < (brand.bulkGoods ?? []).length - 1) {
								const newBulkGoods = [...(brand.bulkGoods ?? [])];
								[newBulkGoods[index + 1], newBulkGoods[index]] = [newBulkGoods[index], newBulkGoods[index + 1]];
								return {
									...brand,
									bulkGoods: newBulkGoods
								};
							}
						}
						return brand;
					}) || []
				};
			}
			return category;
		}));
	};
	const removeGoodBULK = (goodsId: string) => {
		// @ts-ignore
		setSelectedCategories(prevCategories => prevCategories.map(category => {
			if (category?.categoryCode === categoryCodeBULKSelected) {
				return {
					...category,
					bulkBrands: category.bulkBrands?.map(brand => {
						if (brand.brandId === brandIdBULKSelected) {
							return {
								...brand,
								bulkGoods: brand?.bulkGoods?.filter((product) => product.goodsId !== goodsId) || []
							};
						}
						return brand;
					}) || []
				};
			}
			return category;
		}));
	};

	useEffect(() => {
		console.log('listGoodBULK updated:', listGoodBULK);
		setSelectedCategories(prevCategories =>
			prevCategories.map(category => {
				if (category?.categoryCode === categoryCodeBULKSelected) {
					return {
						...category,
						bulkBrands: category.bulkBrands?.map(brand => {
							if (brand.brandId === brandIdBULKSelected) {
								return {
									...brand,
									bulkGoods: listGoodBULK
								};
							}
							return brand;
						}) || []
					};
				}
				return category;
			})
		);
	}, [listGoodBULK]);


	// ProductsChoicesType
	const [selectedProductsChoicesType, setSelectedProductsChoicesType] = useState<Good[]>(
		initialValues?.listGoodsChoice ?? []
	)

	function handleSaveGoodsChoicesType(items: any) {
		items.forEach((item: any) => addProductsChoicesType(item));
		closeModal();
	}

	const addProductsChoicesType = (option: Good) => {
		setErrorMessageProductsChoices('')
		if (option?.id) {
			let checkExist = selectedProductsChoicesType.some((good) => good.id === option.id)
			if (!checkExist) {
				setSelectedProductsChoicesType((prevGoods) => [...prevGoods, option])
			}
		}
	}
	const removeProductsChoicesType = (id: string) => {
		if (id) {
			setSelectedProductsChoicesType((prevGoods) =>
				prevGoods.filter((product) => product.id !== Number(id))
			);
		}
	}

	const [selectedGiftPop, setSelectedGiftPop] = useState<GiftPopGood>();
	const [selectedUrbox, setSelectedUrbox] = useState<UrBoxGood>();
	const [selectedWatane, setSelectedWatane] = useState<WataneGood>();

	function handleSaveGiftpop(option: GiftPopGood) {
		setErrorMessageExternalPinService('')
		setSelectedGiftPop(option)
		// @ts-ignore
		setValue('supplierGoodsId', option[0].goodsId)
		// @ts-ignore
		setValue('goodsImgPath', option[0].originImg);
		// @ts-ignore
		setValue('goodsImgName', option[0].originImg);
		// @ts-ignore
		setValue('sellPrice', option[0].salePrice)
		// @ts-ignore
		setValue('listPrice', option[0].listPrice)
		// @ts-ignore
		setValue('goodsDescription', option[0].commtGuide)
		closeModal();
	}

	function handleSaveUrBox(option: UrBoxGood) {
		setErrorMessageExternalPinService('')
		setSelectedUrbox(option)
		// @ts-ignore
		setValue('goodsName', option[0].title)
		// @ts-ignore
		setValue('supplierGoodsId', option[0].id)
		// @ts-ignore
		setValue('goodsImgPath', option[0].images['160'] ?? option[0].image);
		// @ts-ignore
		setValue('goodsImgName', option[0].title);
		// @ts-ignore
		setValue('sellPrice', option[0].price)
		// @ts-ignore
		setValue('listPrice', option[0].price)
		// @ts-ignore
		let goodsDescription = option[0].note ?? ""

		setValue('goodsDescription', goodsDescription.includes("1900 299 232") || goodsDescription.includes("1900299232") // Hotline UrBox: 1900 299 232
			? goodsDescription
			: goodsDescription.length > 0
				? goodsDescription + "\nQuý khách vui lòng liên hệ Hotline UrBox: 1900 299 232 (từ 8h-22h hàng ngày, bao gồm Lễ Tết) để được hỗ trợ."
				: "Quý khách vui lòng liên hệ Hotline UrBox: 1900 299 232 (từ 8h-22h hàng ngày, bao gồm Lễ Tết) để được hỗ trợ.")
		closeModal();
	}

	function handleSaveWatane(option: WataneGood) {
		setErrorMessageExternalPinService('')
		setSelectedWatane(option)
		// @ts-ignore
		setValue('goodsName', option[0].name)
		// @ts-ignore
		setValue('supplierGoodsId', option[0].code)
		// @ts-ignore
		setValue('sellPrice', option[0].price)
		// @ts-ignore
		setValue('listPrice', option[0].price)
		// @ts-ignore
		setValue('goodsDescription', option[0].description ?? "")
		closeModal();
	}

	const [errorMessageExternalPinService, setErrorMessageExternalPinService] = useState<string>();

	const [showTerm, setShowTerm] = useState<boolean>(false)
	const [showDate, setShowDate] = useState<boolean>(false)
	const handleChangePeriodType = (event: any) => {
		setValue('periodType', event.target.value)
		if (event.target.value == 'FIXED_TERM') {
			setShowTerm(true)
			setShowDate(false)
		} else if (event.target.value == 'FIXED_DT') {
			setShowDate(true)
			setShowTerm(false)
		}
	}

	const handleSelectPeriodType = () => {
		setErrorMessagePeriodTerm('')
		setErrorMessagePeriodExpireDate('')
	}

	const handleChangeVatIncludeYn = (event: any) => {
		setValue('vatIncludeYn', event.target.value)
	}

	/**
	 * Product Image Type
	 */
	const goodsImgName = watch('goodsImgName');
	const [imagePaths, setImagePaths] = useState<Array<string>>([])
	const [imageNames, setImageNames] = useState<Array<string>>([])

	const {mutate: uploadImage, isLoading: uploadingImage} = useUploadImageMutation();

	const {getRootProps, getInputProps} = useDropzone(
		{
			onDrop(acceptedFiles, fileRejections, event) {
				const file = acceptedFiles[0];
				// console.log('file = ', file)
				if (file) {
					uploadImage(file, {
						onSuccess: (data: any) => {
							// console.log('data', data);
							setValue('goodsImgPath', data?.path);
							setValue('goodsImgName', file.name);
						},
						onError: (error: any) => {
							toast.error('Error:' + error?.response?.data.message);
						},
					});
				}
			}
		}
	);

	/**
	 * This function to update single image file to image server
	 * @param e
	 */
	const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
		setValue('goodsImgPath', '');
		setValue('goodsImgName', '');

		// console.log('files = ', e.target.files);
		if (e.target.files) {
			const file = e.target.files[0];

			if (file?.size > 100 * 1024) { // 100KB
				// Hiển thị thông báo hoặc xử lý khi tệp tin vượt quá dung lượng cho phép
				alert('File size exceeds 100KB limit!');
				// Xoá tập tin đã chọn (nếu muốn)
				// @ts-ignore
				e.target.value = null;
				return;
			}

			if (file) {
				uploadImage(file, {
					onSuccess: (data: any) => {
						// console.log('data', data);
						setValue('goodsImgPath', data?.path);
						setValue('goodsImgName', file.name);
					},
					onError: (error: any) => {
						toast.error('Error:' + error?.response?.data.message);
					},
				});
			}
		}
	};

	const goodsImgPath = watch('goodsImgPath');
	/**
	 * This function to upload multiple image files
	 * @param e
	 */
	const handleMultipleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
		setValue('goodsImgPath', '');
		setValue('goodsImgName', '');

		// console.log('files length = ', e.target.files?.length);
		let pathList: Array<string> = []
		let nameList: Array<string> = []
		let files = e.target.files

		if (files) {
			for (let index = 0; index < files.length; index++) {
				let file = files[index];
				uploadImage(file, {
					onSuccess: (data: any) => {
						pathList.push(data?.path);
						nameList.push(data?.name)
					}
				})
			}
			setImagePaths(pathList);
			setImageNames(nameList);
		}
	}

	// set supplier, supplier contract, brand information relating to products information
	useEffect(() => {
		if (initialValues?.supplier) {
			setValue('supplier', initialValues?.supplier);
			// setSelectedSupplierId(initialValues?.supplier?.id)
			setValue('supplierId', initialValues?.supplier?.id)
		}
		if (initialValues?.brand) {
			setValue('brand', initialValues?.brand);
			setSelectedBrand(initialValues?.brand)
		}

		if (initialValues?.goodsType &&
			goodsTypeCodeQuery &&
			goodsTypeCodeQuery.codes) {
			let goodTypeInit = goodsTypeCodeQuery.codes.find((element: {
				codeId: string
			}) => element.codeId === initialValues?.goodsType)
			if (goodTypeInit) {
				setValue('goodsTypeCode', goodTypeInit)
			}
		}

		if (initialValues?.goodsType === VOUCHER_TYPE_CODES.LC) {
			setValue('usageCount', initialValues?.usageCount)
		}

		if (initialValues?.settlementMethodCode &&
			settlementMethodCodeQuery &&
			settlementMethodCodeQuery?.codes) {
			let settlementMethodInit = settlementMethodCodeQuery.codes.find((element: {
				codeId: string
			}) => element.codeId === initialValues?.settlementMethodCode)
			if (settlementMethodInit) {
				setValue('settlementMethodTypeCode', settlementMethodInit)
			}
		}

		if ((systemVnptEpay || systemXpay) && initialValues?.listPrice &&
			vnptFaceValuesCodeQuery &&
			vnptFaceValuesCodeQuery?.codes) {
			let selectListPriceInit = vnptFaceValuesCodeQuery.codes.find((element: {
				codeId: string
			}) => element.codeId == initialValues?.listPrice)
			if (selectListPriceInit) {
				setValue('listPrice', selectListPriceInit)
			}
		}

		if (initialValues?.periodType === 'FIXED_TERM' && initialValues?.periodTerm !== undefined) {
			if (showDate && getValues('periodExpireDate')) {
				setShowDate(true)
				setShowTerm(false)
			} else {
				setShowTerm(true)
				setShowDate(false)
				if (!getValues('periodTerm')) setValue('periodTerm', initialValues?.periodTerm);
			}
		}

		if (initialValues?.periodType === 'FIXED_DT' && initialValues?.periodExpireDate !== undefined) {
			if (showTerm && getValues('periodTerm')) {
				setShowTerm(true)
				setShowDate(false)
			} else {
				setShowTerm(false)
				setShowDate(true)

				let periodExpireDateInit: any;
				periodExpireDateInit = initialValues.periodExpireDate ? new Date(initialValues.periodExpireDate!) : ''
				if (!getValues('periodExpireDate')) setValue('periodExpireDate', periodExpireDateInit);
			}
		}
	}, [initialValues, setValue, goodsTypeCodeQuery, settlementMethodCodeQuery]);

	// check with BE
	useEffect(() => {
		if (supplierContractInit) {
			setValue('supplierContract', supplierContractInit);
		}
	}, [supplierContractInit, setValue]);

	/**
	 * Set supplier if brand selected
	 */
	useEffect(() => {
		const fetchData = async () => {
			try {
				if (!supplierId) {
					return;
				}
				const response = await supplierClient.get(supplierId);
				// console.log('supplier', response.data);
				setValue('supplier', response.data);
			} catch (error) {
				console.error('Error fetching data:', error);
			}
		};
		fetchData();
	}, [supplierId]);

	useEffect(() => {
		const fetchData = async () => {
			try {
				if (!supContractId) {
					setValue('supplyDiscountAmount', '');
					setValue('supplyCommissionRate', '');
					setValue('settlementMethodTypeCode', null as unknown as Code);
					return;
				}
				if (!initialValues) { // create page
					const response = await supplierContractClient.get(supContractId);
					// console.log('useEffect() supplierContract = ', response.data);

					if (response.data?.supplySettlementMethodCode &&
						settlementMethodCodeQuery &&
						settlementMethodCodeQuery?.codes) {
						let stmMethodCode = settlementMethodCodeQuery.codes.find((element: {
							codeId: string
						}) => element.codeId === response.data?.supplySettlementMethodCode)
						setValue('settlementMethodTypeCode', stmMethodCode)
					}

					setValue('supplyDiscountAmount', response.data?.supplyDiscountAmount as unknown as string);
					setValue('supplyCommissionRate', response.data?.supplyCommissionRate as unknown as string);
					setValue('vatIncludeYn', response.data?.supplyVatIncludeYn);
					setValue('startDate', new Date(response.data?.startDate) as unknown as string);
					setValue('endDate', new Date(response.data?.endDate) as unknown as string);
				}

			} catch (error) {
				console.error('Error fetching data:', error);
			}
		};
		fetchData();
	}, [supContractId]);


	const [errorMessageCategories, setErrorMessageCategories] = useState<string>();
	const [errorMessageBrandsBULK, setErrorMessageBrandsBULK] = useState<string>();
	const [errorMessageGoodsBULK, setErrorMessageGoodsBULK] = useState<string>();
	const [errorMessageProductsChoices, setErrorMessageProductsChoices] = useState<string>();
	const [errorMessagePeriodTerm, setErrorMessagePeriodTerm] = useState<string>();
	const [errorMessagePeriodExpireDate, setErrorMessagePeriodExpireDate] = useState<string>();

	const {mutate: createGood, isLoading: creating} =
		useCreateGoodMutation();
	const {mutate: updateGood, isLoading: updating} =
		useUpdateGoodMutation();

	/**
	 * VnptEpay / XPAY topup
	 */
	const {providersList: allVnptGoods} = systemVnptEpay
    ? useProvidersVNPTEpayListQuery({page: 1, pageSize: PAGE_SIZE_MAX}, {})
    : useProvidersXpayListQuery({page: 1, pageSize: PAGE_SIZE_MAX}, {}) ;
	const vnptResGoodsAllValid = (systemVnptEpay ? initialValues?.vnptGoods : initialValues?.xpayGoods)?.map(
    item => ({
      ...item,
      providerCd: item?.providerCode
    })) ?? []
	const [vnptSelectedGoods, setVnptSelectedGoods] = useState<VnptGood[]>((systemVnptEpay ? initialValues?.vnptGoods : initialValues?.xpayGoods)?.filter(
    item => item.validYn === "Y").map((item) => ({
      ...item,
      providerCd: item?.providerCode
    })) ?? []
	);

	// systemBULK
	async function formatBulkCategoryParam(selectedCategories: Category[]) {
		// @ts-ignore
		return selectedCategories.map(categoryOld => {
			const {category, regId, regDt, updtId, updtDt, ...formattedCategory} = categoryOld;

			// @ts-ignore
			formattedCategory.bulkBrands = categoryOld?.bulkBrands?.map(brandOld => {
				const {brand, regId, regDt, updtId, updtDt, ...formattedBrand} = brandOld;

				// @ts-ignore
				formattedBrand.bulkGoods = brandOld?.bulkGoods?.map(goodOld => {
					const {goods, regId, regDt, updtId, updtDt, ...formattedGood} = goodOld;
					return formattedGood;
				});

				return formattedBrand;
			});

			return formattedCategory;
		});
	}

	useEffect(() => {
		if (cloneGood && (giftPopSupplier || urboxSupplier || wataneSupplier)) {
			setValue("supplierGoodsId", "");
		}
	}, [giftPopSupplier, urboxSupplier, wataneSupplier]);


	const onSubmit = async (values: FormValues & { image: any }) => {
		// console.log('onSubmit ... initialValues', initialValues);
		// console.log('[CreateOrUpdateGoodForm] onSubmit ... values', values);

		let isNotNumber: boolean = (!values.periodTerm || isNaN(Number(values.periodTerm)));
		let expiredate: string = values.periodExpireDate ? formatDate(values.periodExpireDate) : ''
		let isDateEmpty: boolean = (!expiredate || expiredate === '');

		if (showTerm) {
			if (isNotNumber) {
				setErrorMessagePeriodTerm('You must provide Period Term as Number')
				// toast.error('You must provide Period Term as Number')
				return
			}
		}
		if (showDate) {
			if (isDateEmpty) {
				setErrorMessagePeriodExpireDate('You must provide Period Expire Date')
				// toast.error('You must provide Period Expire Date')
				return
			}
		}
		if (selectedCategories.length == 0) {
			setErrorMessageCategories('You must need to provide Product Categories')
			// toast.error('You must need to provide Product Categories')
			return
		}
		if (systemBULK) {
			const totals = selectedCategories?.reduce((acc, category) => {
				// Count bulkBrands
				acc.bulkBrands += category.bulkBrands?.length || 0;

				// Count bulkGoods
				category.bulkBrands?.forEach(brand => {
					acc.bulkGoods += brand.bulkGoods?.length || 0;
				});

				return acc;
			}, {bulkBrands: 0, bulkGoods: 0});

			// console.log("XXXXx updated:", selectedCategories)
			// toast.error(`You must provide bulkBrands, ${totals?.bulkBrands || 0} `)
			// toast.error(`You must provide totalBulkGoods, ${totals?.bulkGoods || 0} `)

			if (totals?.bulkBrands === 0) {
				setErrorMessageBrandsBULK('You must need to provide Brands')
				// toast.error('You must need to to provide Brands')
				return
			}
			if (totals?.bulkGoods === 0) {
				setErrorMessageGoodsBULK('You must need to provide Products')
				// toast.error('You must need to provide Products')
				return
			}
		}
		if (selectedProductsChoicesType.length == 0 && systemCHOICE) {
			setErrorMessageProductsChoices('You must need to provide Products {Choices Type}')
			// toast.error('You must need to provide Products {Choices Type}')
			return
		}

		if (giftPopSupplier && (selectedGiftPop == null || selectedGiftPop == undefined || values.supplierGoodsId?.length === 0)) {
			setErrorMessageExternalPinService('You must provide Product Code (giftpop)')
		}
		if (urboxSupplier && (selectedUrbox == null || selectedUrbox == undefined || values.supplierGoodsId?.length === 0)) {
			setErrorMessageExternalPinService('You must provide Product Code (urbox)')
		}
		if (wataneSupplier && (selectedWatane == null || selectedWatane == undefined || values.supplierGoodsId?.length === 0)) {
			setErrorMessageExternalPinService('You must provide Product Code (Watane)')
		}

		// console.log('[CreateOrUpdateGoodForm] onSubmit ... pass ');

		let inputValues = {
			// id: values.id,
			goodsName: values.goodsName,
			supplierGoodsId: values.supplierGoodsId,
			supplierId: values.supplier?.id,
			supplierContractId: values.supplierContract ? values.supplierContract.id : '',
			brandId: values.brand?.id,
			listPrice: (systemVnptEpay || systemXpay) ? values.listPrice?.codeId as string : parseInt(values.listPrice.replace(/,/g, '')),
			sellPrice: parseInt(values.sellPrice.replace(/,/g, '')),
			validYn: values.validYn,
			goodsDescription: values.goodsDescription,
			periodType: values.periodType,
			periodTerm: showTerm ? (values.periodTerm ? values.periodTerm : '') : "",
			periodExpireDate: showDate ? (expiredate ? expiredate : '') : "",
			startDate: values.startDate ? formatDate(values.startDate) : '',
			endDate: values.endDate ? formatDate(values.endDate) : '',
			settlementMethodCode: values.settlementMethodTypeCode?.codeId,
			supplyDiscountAmount: parseInt(values.supplyDiscountAmount.replace(/,/g, '')),
			supplyCommissionRate: values.supplyCommissionRate,
			vatIncludeYn: values.vatIncludeYn,
			goodsImgName: values.goodsImgName,
			goodsImgPath: values.goodsImgPath,
			// goodsImgName: imageNames,
			// goodsImgPath: imagePaths,
			goodsType: systemCHOICE
				? VOUCHER_TYPE_CODES.CH
				: systemBULK
					? VOUCHER_TYPE_CODES.BK // BULK
					: values.goodsTypeCode?.codeId,
			// displayType: values.displayTypeCode?.codeId,
			categories: systemBULK ? [] : selectedCategories,
			// bulkCategories: systemBULK ? selectedCategories : [],
			bulkCategories: systemBULK
				? await formatBulkCategoryParam(selectedCategories)
				: [],
			exceptStores: selectedExceptStores.map(item => ({
				id: item.id,
				storeName: item.storeName,
				validYn: item.validYn,
			})),
			listGoodsChoice: selectedProductsChoicesType.map(item => ({
				id: item.id,
				goodsName: item.goodsName,
				validYn: item.validYn,
			})),
			vnptGoods: systemVnptEpay
        ? vnptSelectedGoods.map((item) => ({
            ...item,
            faceValue: listPriceWatch,
            providerCode: item?.providerCd
          }))
        : null ,
			xpayGoods: systemXpay
        ? vnptSelectedGoods.map((item) => ({
            ...item,
            faceValue: listPriceWatch,
            providerCode: item?.providerCd
          }))
        : null ,
			system: values.system,
			usageCount: goodsTypeCodeWatch === VOUCHER_TYPE_CODES.LC ? values.usageCount : "",
		};

		// console.log('[CreateOrUpdateGoodForm] onSubmit ... inputValues', inputValues);
		// return;

		try {
			if (!initialValues) {
				// @ts-ignore
				createGood({
					...inputValues,
				});
			} else {
				if (cloneGood) {
					if (systemBULK) {
						inputValues?.bulkCategories.forEach(category => {
							delete category.id;
							if (category.bulkBrands) {
								category.bulkBrands.forEach(brand => {
									// @ts-ignore
									delete brand.id;
									if (brand.bulkGoods) {
										brand.bulkGoods.forEach(good => {
											// @ts-ignore
											delete good.id;
										});
									}
								});
							}
						});
					}
					if (systemVnptEpay) {
						inputValues?.vnptGoods?.forEach(item => {
							// @ts-ignore
							delete item.id;
						})
					}
					if (systemXpay) {
						inputValues?.xpayGoods?.forEach(item => {
							// @ts-ignore
							delete item.id;
						})
					}
				}
				// @ts-ignore
				cloneGood ? createGood({...inputValues}) : updateGood({...inputValues, id: values?.id,})
			}
		} catch (error) {
			const serverErrors = getErrorMessage(error);
			Object.keys(serverErrors?.validation).forEach((field: any) => {
				setError(field.split('.')[1], {
					type: 'manual',
					message: serverErrors?.validation[field][0],
				});
			});
		}
	};

	return (
		<form onSubmit={handleSubmit(onSubmit)}>
			{/* Product Information */}

			<div className="my-4 flex flex-wrap">
				<Card className="w-full sm:w-full md:w-full">
					<div className="flex border-b border-dashed border-border-base md:py-2 sm:py-2">
						<h1 className="text-lg font-semibold text-heading uppercase text-red-600">
							{initialValues
								? cloneGood ? t('Clone Product') : t('Edit Product')
								: t('Create Product')}
						</h1>
					</div>

					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 py-2">
							<h1 className="text-base uppercase font-semibold">
								{t('Product Information')}
							</h1>
						</div>
					</div>

					<div className="mb-5 flex flex-wrap">
						{/* Product Name */}
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product name ')}<span className="text-red-500">*</span>
							</label>
							<input
								className={rootClassName}
								type="text"
								// disabled={initialValues ? false : true}
								id="goodsName"
								{...register('goodsName')}
								placeholder={initialValues?.goodsName}
							/>
							<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.goodsName?.message!)}
              </span>
						</div>

						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Supplier Name ')}<span className="text-red-500">*</span>
							</label>
							<div className="w-full md:w-3/4">
								<SelectInput
									name="supplier"
									options={suppliers}
									isLoading={loading}
									disabled={initialValues?.supplier
										? cloneGood ? false : true
										: false}
									getOptionLabel={(option: any) =>
										option.supplierName + ' - ' + option.id
									}
									getOptionValue={(option: any) => option.id}
									onInputChange={handleInputChangeSupplier}
									onChange={handleChangeSupplier}
									placeholder={t('common:filter-by-group-placeholder')}
									control={control}
									isClearable={true}
								/>
								<span className="w-full text-xs text-red-500 text-start">
                  {t(errors.supplier?.message!)}
                </span>
							</div>
						</div>
					</div>

					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Brand Name ')}<span className="text-red-500">*</span>
							</label>
							<div className="w-full md:w-3/4">
								<SelectInput
									name="brand"
									options={brands}
									isLoading={brandsLoading}
									disabled={initialValues?.supplierContractId
										? cloneGood ? false : true
										: false}
									getOptionLabel={(optionBrand: any) =>
										optionBrand.brandName + ' - ' + optionBrand.id
									}
									getOptionValue={(optionBrand: any) => optionBrand.id}
									onInputChange={handleInputChangeBrand}
									onChange={handleChangeBrand}
									placeholder={t('common:filter-by-group-placeholder')}
									control={control}
									// isClearable={true}
								/>
								<span className="w-full text-xs text-red-500 text-start">
                  {t(errors.brand?.message!)}
                </span>
							</div>
						</div>

						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Supplier Contract')}<span className="text-red-500">*</span>
							</label>
							<div className="w-full md:w-3/4">
								<SelectInput
									name="supplierContract"
									options={supplierContracts}
									disabled={initialValues?.supplierContractId
										? cloneGood ? false : true
										: false}
									getOptionLabel={(option: any) =>
										option.contractName + ' - ' + option.id
									}
									getOptionValue={(option: any) => option.id}
									onChange={handleChangeSupplierContract}
									placeholder={t('common:filter-by-group-placeholder')}
									control={control}
									// isClearable={true}
								/>
								<span className="w-full text-xs text-red-500 text-start">
                  {t(errors.supplierContract?.message!)}
                </span>
							</div>
						</div>
					</div>

					{/* Product Type + Brands Name */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product Type ')}<span className="text-red-500">*</span>
							</label>
							{systemCHOICE || systemBULK
								? <input
									className={true ? ' bg-gray-200 ' + rootClassName : rootClassName}
									type="text"
									id="goodsTypeCode"
									{...register('goodsTypeCode')}
									disabled={true}
									autoComplete="off"
								/>
								: <div className="w-full md:w-3/4 ">
									<SelectInput
										name="goodsTypeCode"
										control={control}
										getOptionLabel={(option: any) => option?.codeName + ' - ' + option?.codeId}
										getOptionValue={(option: any) => option?.codeId}
										onChange={handleChangeGoodType}
										options={systemINTERNAL
											? (brand?.displayType === "BARCODE" ? goodsTypeCodeQuery?.codes?.filter(item => item.codeId !== VOUCHER_TYPE_CODES.LC) : goodsTypeCodeQuery?.codes )
											: goodsTypeCodeQuery?.codes?.filter(item => item.codeId !== VOUCHER_TYPE_CODES.LC) ?? []}
										isLoading={codeLoading}
									/>
									<span className="w-full text-xs text-red-500 text-start">
                    {t(errors.goodsTypeCode?.message!)}
                  </span>
								</div>
							}
						</div>
						{!systemVnptEpay && !systemXpay && <div className="flex w-full flex-wrap px-4 md:w-1/2">
							<div className="flex w-full flex-wrap md:w-1/4">
								<label className="w-full py-2 text-heading  ">
									{t('Product Code ')}<span className="text-red-500">*</span>
								</label>
							</div>

							{giftPopSupplier || urboxSupplier || wataneSupplier
								? <>
									<div className="flex w-full flex-wrap md:w-2/4">
										<input
											disabled
											className={`${rootClassName} bg-gray-300`}
											type="text"
											id="supplierGoodsId"
											{...register('supplierGoodsId')}
											placeholder={t(' . . .')}
											autoComplete="off"
										/>
										<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                          {t(errors.supplierGoodsId?.message!)}
                        </span>
									</div>
									<div className="flex w-full flex-wrap md:w-1/4 py-2 ">
										{(!initialValues || cloneGood) && <Button
											size="small"
											className="bg-blue-800 hover:bg-blue-900 rounded-2xl"
											disabled={uploadingImage}
											onClick={(event) => {
												event.preventDefault();
												if (!selectedBrand) {
												  return alert('Select Brand Name first!');
												}
												if (giftPopSupplier) {
												  return handleSelectShowPopup('GIFTPOP_GOOD_CODE');
												}
												if (urboxSupplier) {
												  return handleSelectShowPopup('URBOX_GOOD_CODE');
												}
												if (wataneSupplier) {
												  return handleSelectShowPopup('WATANE_GOOD_CODE');
												}
											  return alert('alert other case');
											}}
										>
											{t('Browse Product')}
										</Button>}
									</div>
									{errorMessageExternalPinService && (
										<>
                      <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                        {t(errorMessageExternalPinService!)}
                      </span>
										</>
									)}
								</>
								: <>
									<input
										className={initialValues
											? cloneGood ? rootClassName : ' bg-gray-200 ' + rootClassName
											: rootClassName}
										type="text"
										id="supplierGoodsId"
										{...register('supplierGoodsId')}
										disabled={initialValues?.supplierGoodsId
											? cloneGood ? false : true
											: false}
										autoComplete="off"
									/>
									<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                      {t(errors.supplierGoodsId?.message!)}
                  </span>
								</>
							}
						</div>
						}
					</div>

					{/*Count LC*/}
					{goodsTypeCodeWatch === VOUCHER_TYPE_CODES.LC && <div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Count ')}<span className="text-red-500">*</span>
							</label>
							<input
								className={rootClassName}
								type="text"
								id="usageCount"
								{...register('usageCount')}
								placeholder={"Must in range 1 to 100"}
							/>
							<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.usageCount?.message!)}
              </span>
						</div>
					</div>}

					{(systemINTERNAL || systemCHOICE || systemBULK || systemVnptEpay || systemXpay) && <div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Period Type')} {systemINTERNAL && <span className="text-red-500">*</span>}
							</label>
							<div className="flex w-full md:w-3/4">
								<Radio
									className="flex w-full md:w-1/2"
									label={t('Fixed Term')}
									{...register('periodType')}
									id="periodTypeTerm"
									value="FIXED_TERM"
									onClick={handleChangePeriodType}
								/>
								<Radio
									className="flex w-full md:w-1/2"
									label={t('Fixed Date')}
									{...register('periodType')}
									id="periodTypeDate"
									value="FIXED_DT"
									onClick={handleChangePeriodType}
								/>
								<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {t(errors.periodType?.message!)}
                </span>
							</div>
						</div>


						{showTerm && (<>
							<div className="flex w-full flex-wrap px-4 md:w-1/2">
								<label className="w-full py-2 text-heading md:w-1/4">
									{t('Term (days) ')}{systemINTERNAL && <span className="text-red-500">*</span>}
								</label>
								<input
									className={rootClassName}
									type="text"
									// disabled={initialValues ? false : false}
									id="periodTerm"
									{...register('periodTerm')}
									autoComplete="off"
									onSelect={handleSelectPeriodType}
								/>
								<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {t(errorMessagePeriodTerm!)}
                </span>
							</div>
						</>)}

						{showDate && (<>
							<div className="flex w-full flex-wrap px-4 md:w-1/2">
								<label className="w-full py-2 text-heading md:w-1/4">
									{t('Term (days) ')} {systemINTERNAL && <span className="text-red-500">*</span>}
								</label>
								<div className="w-full md:w-3/4">
									<Controller
										control={control}
										name="periodExpireDate"
										render={({field: {onChange, onBlur, value}}) => (
											<DatePicker
												dateFormat="yyyy-MM-dd"
												onChange={onChange}
												onBlur={onBlur}
												//@ts-ignore
												selected={value}
												selectsStart
												startDate={new Date()}
												className="border border-border-base"
												onFocus={handleSelectPeriodType}
											/>
										)}
									/>
									{/* <ValidationError message={t(errors.periodExpireDate?.message!)} /> */}
									<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                    {t(errorMessagePeriodExpireDate!)}
                  </span>
								</div>
							</div>
						</>)}
					</div>}

					{/* Product Information */}
					<div className="flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product Description ')}<span className="text-red-500">*</span>
							</label>
							<div className="w-full md:w-3/4">
								<TextArea
									// name='goodsDescription'
									// disabled={initialValues ? true : false}
									id="goodsDescription"
									{...register('goodsDescription')}
									placeholder={t('Product Description')}
									variant="outline"
									autoComplete="off"
								/>
								<span className="w-full text-xs text-red-500 text-start ">
                  {t(errors.goodsDescription?.message!)}
                </span>
							</div>
						</div>

						{/* System */}
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('System ')}<span className="text-red-500">*</span>
							</label>
							<input
								className={true ? ' bg-gray-200 ' + rootClassName : rootClassName}
								type="text"
								id="system"
								{...register('system')}
								disabled={true}
								autoComplete="off"
							/>

							{/* Product Image */}
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product Image ')}<span className="text-red-500">*</span>
							</label>
							<div className="w-full pt-3 md:w-3/4">
								<div className="flex flex-wrap mb-5 w-full">
									<input
										id="file_input"
										type="file"
										accept=".jpg, .jpeg, .png, .gif, .svg"
										className={'e_hide-text'}
										onChange={handleFileChange}
										// onChange={handleMultipleFileChange}
										multiple={true}
									/>
									{uploadingImage && <Loader
										uploadFile={true}
										text={t('common:text-loading')}/>}
									{goodsImgName && <p>Selected image: <strong><em> {goodsImgName}</em></strong></p>}
								</div>
							</div>
							<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.goodsImgPath?.message!)}
              </span>
						</div>
					</div>

					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Active ')}<span className="text-red-500">*</span>
							</label>
							<div className="flex w-full md:w-3/4">
								<Radio
									className="py-3 w-full md:w-1/2 item-top"
									label={t('Yes')}
									{...register('validYn')}
									id="valid_Yes"
									value="Y"
								/>
								<Radio
									className="py-3 w-full md:w-1/2 item-top"
									label={t('No')}
									{...register('validYn')}
									id="valid_No"
									value="N"
								/>
							</div>
							<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.validYn?.message!)}
              </span>
						</div>

						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
							</label>
							{/* {imagePaths && (<>
                {imagePaths.map((img, i) => {
                  // console.log(img)
                  return (
                    <img className="preview rounded border border-gray"
                      src={img} alt={'Product Image'} key={i} width={140} height={100} />
                  );
                })
                }
              </>
              )} */}
							{goodsImgPath && (<>
								<img
									className="pt-3"
									src={getUrlPublicAsset(goodsImgPath) ?? emptyPlaceholder}
									alt={'Product Image'}
									width={300}
									height={300}
								/>
							</>)}
						</div>
					</div>

					{/* <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Active ')}<span className="text-red-500">*</span>
              </label>
              <div className="flex w-full md:w-3/4">
                <Radio
                  className="flex w-full md:w-1/2"
                  label={t('Yes')}
                  {...register('validYn')}
                  id="valid_Yes"
                  value="Y"
                />
                <Radio
                  className="flex w-full md:w-1/2"
                  label={t('No')}
                  {...register('validYn')}
                  id="valid_No"
                  value="N"
                />
              </div>
            </div>
          </div> */}

					<div className="mb-5 flex flex-wrap">
					</div>
					<div className="mb-5 flex flex-wrap">
					</div>


					{/* Contract Information */}
					<div className={classes?.wrapper}>
						<div className=" flex flex-wrap">
							<div className="flex w-full flex-wrap px-4 py-2">
								<h1 className="text-lg text-cyan-600 text-heading font-semibold uppercase">
									{t('Contract Information')}
								</h1>
							</div>
						</div>
					</div>

					{/* Sales Price and List Price */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Sell Price ')}<span className="text-red-500">*</span>
							</label>
							<input
								className={rootClassName}
								type="text"
								// disabled={initialValues ? true : false}
								id="sellPrice"
								{...register('sellPrice')}
								onChange={(e) => {
									handlePriceInputChange(e, "sellPrice", setValue);
								}}
							/>
							<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.sellPrice?.message!)}
              </span>
						</div>
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('List Price ')}<span className="text-red-500">*</span>
							</label>
							{(systemVnptEpay || systemXpay)
								? <div className="w-full md:w-3/4 ">
									<SelectInput
										name="listPrice"
										control={control}
										getOptionLabel={(option: any) => option?.codeName}
										getOptionValue={(option: any) => option?.codeId}
										onChange={handleChangeVnptEpayListPrice}
										options={systemVnptEpay
                      ? ( vnptFaceValuesCodeQuery?.codes.sort((a, b) => parseInt(a.codeId) - parseInt(b.codeId)) ?? [])
                      : ( xpayFaceValuesCodeQuery?.codes.sort((a, b) => parseInt(a.codeId) - parseInt(b.codeId)) ?? [] )
                    }
										isLoading={systemVnptEpay ? codeLoadingvnptFaceValues : codeLoadingXpayFaceValues}
									/>
								</div>
								: <input
									className={rootClassName}
									type="text"
									// disabled={initialValues ? true : false}
									id="listPrice"
									{...register('listPrice')}
									onChange={(e) => {
										handlePriceInputChange(e, "listPrice", setValue);
									}}
								/>
							}
							<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.listPrice?.message!)}
              </span>
						</div>
					</div>

					{/* Settlement Method and Discount Amount */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Settlement Method ')}<span className="text-red-500">*</span>
							</label>
							<div className="w-full md:w-3/4 ">
								<SelectInput
									name="settlementMethodTypeCode"
									control={control}
									getOptionLabel={(option: any) => option?.codeName + ' - ' + option?.codeId}
									getOptionValue={(option: any) => option?.codeId}
									onChange={handleChangeSettlementMethod}
									options={settlementMethodCodeQuery?.codes ?? []}
									isLoading={codeLoading}
								/>
								<span className="w-full text-xs text-red-500 text-start">
                  {t(errors.settlementMethodTypeCode?.message!)}
                </span>
							</div>
						</div>
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Discount Amount ')}<span className="text-red-500">*</span>
							</label>
							<input
								className={rootClassName}
								type="text"
								// disabled={initialValues ? true : false}
								id="supplyDiscountAmount"
								{...register('supplyDiscountAmount')}
								onChange={(e) => {
									handlePriceInputChange(e, "supplyDiscountAmount", setValue);
								}}
							/>
							<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplyDiscountAmount?.message!)}
              </span>
						</div>
					</div>

					{/* Commission Rate and Include VAT */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Commission Rate ')}<span className="text-red-500">*</span>
							</label>
							<input
								className={rootClassName}
								type="text"
								// disabled={initialValues ? true : false}
								id="supplyCommissionRate"
								{...register('supplyCommissionRate')}
							/>
							<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                {t(errors.supplyCommissionRate?.message!)}
              </span>
						</div>
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Include VAT ')}<span className="text-red-500">*</span>
							</label>
							<div className="flex w-full md:w-3/4">
								<Radio
									className="flex w-full md:w-1/2"
									label={t('Yes')}
									{...register('vatIncludeYn')}
									id="vatIncludeYes"
									value="Y"
									onClick={handleChangeVatIncludeYn}
								/>
								<Radio
									className="flex w-full md:w-1/2"
									label={t('No')}
									{...register('vatIncludeYn')}
									id="vatIncludeNo"
									value="N"
									onClick={handleChangeVatIncludeYn}
								/>
								<span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                  {t(errors.vatIncludeYn?.message!)}
                </span>
							</div>
						</div>
					</div>

					{/* Start Date and End Date */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Start Date ')} {<span className="text-red-500">*</span>}
							</label>
							<div className="w-full md:w-3/4">
								<Controller
									control={control}
									name="startDate"
									render={({field: {onChange, onBlur, value}}) => (
										<DatePicker
											dateFormat="yyyy-MM-dd"
											onChange={onChange}
											onBlur={onBlur}
											//@ts-ignore
											selected={value}
											selectsStart
											startDate={new Date()}
											className="border border-border-base"
										/>
									)}
								/>
								<ValidationError message={t(errors.startDate?.message!)}/>
							</div>
						</div>

						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('End Date ')} {<span className="text-red-500">*</span>}
							</label>
							<div className="w-full md:w-3/4">
								<Controller
									control={control}
									name="endDate"
									render={({field: {onChange, onBlur, value}}) => (
										<DatePicker
											dateFormat="yyyy-MM-dd"
											onChange={onChange}
											onBlur={onBlur}
											//@ts-ignore
											selected={value}
											selectsStart
											startDate={new Date()}
											className="border border-border-base"
										/>
									)}
								/>
								<ValidationError message={t(errors.endDate?.message!)}/>
							</div>
						</div>
					</div>

					{/* Categories + Brands title button */}
					<div className="mb-1 flex flex-wrap">
						{/* Categories title button */}
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4 uppercase font-bold">
								{t('Categories ')}<span className="text-red-500 ">*</span>
							</label>

							<div className="w-full md:w-3/4 flex justify-end ">
								<Button
									size="small"
									className="bg-red-700 hover:bg-red-800"
									onClick={(event) => {
										event.preventDefault();
										return handleSelectShowPopup('SELECT_CATEGORIES');
									}}
								>
									{t('Add Categories')}
								</Button>
								{errorMessageCategories && (
									<>
                    <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                      {t(errorMessageCategories!)}
                    </span>
									</>
								)}
							</div>
						</div>
						{/* Brands BULK title button */}
						{systemBULK && (
							<div className="flex w-full flex-wrap px-4 md:w-1/2">
								<label className="w-full py-2 text-heading md:w-1/4 uppercase font-bold">
									{t('Brands ')}<span className="text-red-500">*</span>
								</label>

								<div className="w-full md:w-3/4 flex justify-end">
									<Button
										size="small"
										className="bg-red-700 hover:bg-red-800"
										onClick={(event) => {
											event.preventDefault();
											return categoryCodeBULKSelected
												? handleSelectShowPopup('SELECT_BRANDS_BULK')
												: alert('Please select one Category!');
										}}
									>
										{t('Add brands')}
									</Button>
									{errorMessageBrandsBULK && (
										<>
                        <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                          {t(errorMessageBrandsBULK!)}
                        </span>
										</>
									)}
								</div>
							</div>
						)}
					</div>
					{/* Categories + Brands content */}
					<div className="mb-2 flex flex-wrap">
						{/* Categories  content */}
						{!systemBULK && <div className="flex w-full flex-wrap px-4">
							<div className="md:w-[12%]">
								<label className="h-full text-heading ">
								</label>
							</div>

							<div className="flex flex-wrap h-full md:w-[88%]">
								{selectedCategories && (<>
										{selectedCategories.map((data, i) => {
											return (
												<div className="pr-2 mb-3" key={data.categoryCode}>
													<div className="flex w-full items-center rounded bg-gray-400 text-body-dark md:h-9">
														<button
															// key={data.categoryCode}
															onClick={(event) => {
																event.preventDefault();
																return removeCategory(data)
															}
															}
														>
                            <span
	                            className="flex grid  md:h-9 w-6 place-items-center rounded bg-gray-400 transition hover:text-red-500 focus:text-red-500 focus:outline-none ltr:rounded-tr ltr:rounded-br rtl:rounded-tl rtl:rounded-bl">
                              <CloseIcon className="h-4 w-4 stroke-2"/>
                            </span>
														</button>
														<span className="pl-2 pr-3 text-center w-full">
                            {data.categoryName}
                          </span>
													</div>
												</div>
											)
										})}
									</>
								)}
							</div>
						</div>}
						{systemBULK &&
							<div className="flex flex-wrap h-full md:w-[50%] h-full overflow-auto max-h-96">
								{selectedCategories && <SortableListCategory
									categories={selectedCategories || []}
									onCategoriesChange={setSelectedCategories}
									onCategorySelected={onCategoryBULKSelected}
									onCategoryDelete={onCategoryBULKDelete}
									caseUpdate={initialValues ? true : false}/>}
							</div>
						}
						{/* Brands content */}
						{systemBULK &&
							<div className="flex flex-wrap h-full md:w-[50%] h-full overflow-auto max-h-96">
								<SortableListBrand
									brands={bulkBrandsWithCategory(categoryCodeBULKSelected)}
									onBrandsChange={setListBrandsBULK}
									onBrandSelected={onBrandBULKSelected}
									onBrandDelete={onBrandBULKDelete}
									caseUpdate={initialValues ? true : false}/>
							</div>
						}
					</div>

					{/* Products BULK*/}
					{systemBULK && <div>
						{/* Products BULK   */}
						<div className="mb-1 flex flex-wrap">
							<div className="flex w-full flex-wrap px-4 ">
								<label className="w-full py-2 text-heading md:w-1/4 uppercase font-bold">
									{t('Products ')}<span className="text-red-500 ">*</span>
								</label>

								<div className="w-full md:w-3/4 flex justify-end ">
									<Button
										size="small"
										className="bg-red-700 hover:bg-red-800"
										onClick={(event) => {
											event.preventDefault();
											console.log("categoryCodeBULKSelected : ", categoryCodeBULKSelected, "brandIdBULKSelected : ", brandIdBULKSelected)
											return categoryCodeBULKSelected
												? brandIdBULKSelected
													? handleSelectShowPopup('SELECT_GOODS_BULK')
													: alert('Please select one Brand! ')
												: alert('Please select one Category!');
										}}
									>
										{t('Add Products')}
									</Button>
									{errorMessageGoodsBULK && (
										<>
                    <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                      {t(errorMessageGoodsBULK!)}
                    </span>
										</>
									)}
								</div>
							</div>
						</div>
						<div className="flex flex-wrap h-full md:w-full h-full overflow-auto max-h-screen">
							<SortableListGood
								goods={bulkGoodsWithCategoryAndBrand(categoryCodeBULKSelected, brandIdBULKSelected)}
								onGoodChange={setListGoodsBULK}
								onGoodDelete={removeGoodBULK}
								caseUpdate={initialValues ? true : false}/>
						</div>
					</div>}

					{/* Except Store List */}
					{!systemBULK && !systemCHOICE && !systemVnptEpay && !systemXpay && <div className=" ">
						<div className="-mb-1 flex flex-wrap">
							<div className="flex w-full flex-wrap px-4">
								<div className="md:w-[12%]">
									<label className="py-2 text-heading ">
										{t('Except Store List ')}
									</label>
								</div>

								<div className="md:w-[38%]">
									<div className="mb-3 me-4 w-full flex justify-end">
										<Button
											size="small"
											className=" bg-red-700 me-4 hover:bg-red-800"
											onClick={(event) => {
												event.preventDefault();
												return handleSelectShowPopup('SELECT_EXCEPTED_STORE');
											}}
										>
											{t('Add Excepted Stores')}
										</Button>
									</div>
								</div>
							</div>
						</div>
						<div className="mb-2 flex flex-wrap">
							<div className="flex w-full flex-wrap px-4">
								<label className="w-full text-heading md:w-[12%]">
								</label>
								<div className="flex flex-wrap h-full md:w-[88%]">
									{selectedExceptStores && (<>
											{selectedExceptStores.map((data, i) => {
												return (
													<div className="pr-2 mb-3" key={data?.id}>
														<div className="flex w-full items-center rounded bg-gray-400 text-body-dark md:h-9">
															<button
																onClick={(event) => {
																	event.preventDefault();
																	removeExceptStore(data)
																}}
															>
                            <span
	                            className="flex grid  md:h-9 w-6 place-items-center rounded bg-gray-400 transition hover:text-red-500 focus:text-red-500 focus:outline-none ltr:rounded-tr ltr:rounded-br rtl:rounded-tl rtl:rounded-bl">
                              <CloseIcon className="h-4 w-4 stroke-2"/></span>
															</button>
															<span className="pl-2 pr-3 text-center w-full">{data.storeName}</span>
														</div>
													</div>
												)
											})}
										</>
									)}
								</div>
							</div>
						</div>
					</div>}

					{/* Products {Choices Type} new */}
					{systemCHOICE && <div>
						<div className="mb-1 flex flex-wrap">
							<div className="flex w-full flex-wrap px-4 ">
								<label className="w-full py-2 text-heading md:w-1/4 uppercase font-bold">
									{t('Products {Choices Type} ')}<span className="text-red-500">*</span>
								</label>

								<div className="w-full md:w-3/4 flex justify-end ">
									<Button
										size="small"
										className="bg-red-700 hover:bg-red-800"
										onClick={(event) => {
											event.preventDefault();
											return handleSelectShowPopup('PRODUCTS_CHOICES_TYPE');
										}}
									>
										{t('Add Products')}
									</Button>
									{errorMessageProductsChoices && (
										<>
                            <span className="w-full text-xs text-red-500 text-start md:w-3/4 md:pl-[25%]">
                              {t(errorMessageProductsChoices!)}
                            </span>
										</>
									)}
								</div>
							</div>
						</div>
						<div className="flex flex-wrap h-full md:w-full h-full overflow-auto max-h-screen">
							<SortableListGood
								goods={selectedProductsChoicesType}
								onGoodChange={setSelectedProductsChoicesType}
								onGoodDelete={removeProductsChoicesType}
								caseUpdate={initialValues ? true : false}
								systemChoice={true}/>
						</div>
					</div>}

					{/* VnptEpay / XPAY topup */}
					{(systemVnptEpay || systemXpay) && <>
						<div className="mb-10 flex flex-wrap">
						</div>
						<div className={classes?.wrapper}>
							<div className=" flex flex-wrap">
								<div className="flex w-full flex-wrap px-4 py-2">
									<h1 className="text-lg text-cyan-600 text-heading font-semibold uppercase">
										{systemVnptEpay ? t('VNPT PRODUCTS ') :  t('XPAY PRODUCTS ')}
									</h1>
								</div>
							</div>
						</div>
						<div className=" ">
							<div className="mb-5 flex flex-wrap">
								<div className="flex w-full flex-wrap px-4 md:w-1/2">
									<h2 className="w-full py-2 text-heading md:w-1/4 font-bold ">
										{t('Face value ')}<span className="text-red-500">*</span>
									</h2>
									<input
										className={true ? ' bg-gray-200 ' + rootClassName : rootClassName}
										type="text"
										disabled={true}
										value={formatNumber(Number(listPriceWatch), false, true)}
										placeholder={"Exactly the same with list price"}
									/>
								</div>
							</div>
						</div>
						<div className="mb-10 flex flex-wrap">
						</div>
						<div>
							<div className=" flex flex-wrap">
								<div className="flex w-full flex-wrap px-4 py-2">
									<h1 className="text-lg text-cyan-600 text-heading font-semibold uppercase">
										{t('Card providers ')}
									</h1>
								</div>
							</div>
						</div>
						<VnptGoodsCard
							list={allVnptGoods.filter(provider => provider.providerType === "MOBILE_CARD")}
							selectedItems={vnptSelectedGoods}
							setSelectedItems={setVnptSelectedGoods}
							faceValue={listPriceWatch}
							vnptResGoodsAllValid={vnptResGoodsAllValid}
							title="Mobile Card"/>
            {systemVnptEpay && <VnptGoodsCard
							list={allVnptGoods.filter(provider => provider.providerType === "MOBILE_DATA")}
							selectedItems={vnptSelectedGoods}
							setSelectedItems={setVnptSelectedGoods}
							faceValue={listPriceWatch}
							vnptResGoodsAllValid={vnptResGoodsAllValid}
							title="Mobile Data"/> }
            {systemVnptEpay && <VnptGoodsCard
							list={allVnptGoods.filter(provider => provider.providerType === "GAME_CARD")}
							selectedItems={vnptSelectedGoods}
							setSelectedItems={setVnptSelectedGoods}
							title="Game Card and others"
							vnptResGoodsAllValid={vnptResGoodsAllValid}
							faceValue={listPriceWatch}/> }
					</>}
					{/* End VnptEpay topup */}

				</Card>
			</div>

			<div className="mb-4 text-end">
				<Button
					variant="outline"
					onClick={router.back}
					className="me-4 bg-red-700 hover:bg-red-800"
					type="button"
				>
					{t('form:button-label-back')}
				</Button>
				<Button loading={updating || creating} className="me-4 bg-red-700 hover:bg-red-800">
					{initialValues
						? cloneGood ? t('Registration') : t('Save')
						: t('Registration')}
				</Button>
			</div>
		</form>
	)
}


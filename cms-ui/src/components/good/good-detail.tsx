import {Routes} from "@/config/routes";
import {useForm} from 'react-hook-form';
import {Code, Good} from "@/types";
import {useTranslation} from "next-i18next";
import Card from "../common/card";
import Button from '@/components/ui/button';
import {useRouter} from 'next/router';
import {getUrlPublicAsset} from "@/data/download";
import {emptyPlaceholder} from '@/utils/placeholders';
import {useSupplierContractQuery} from "@/data/supplier-contract";
import {useCodeGroupQuery} from "@/data/code-group";
import {CODE_GROUP, PAGE_SIZE_MAX, PERMISSIONS_EV, SYSTEM_BRAND_TYPE} from "@/utils/constants";
import GoodSupplierContractDetail from "./good-supplier-contract-detail";
import TextArea from "../ui/text-area";
import Radio from '@/components/ui/radio/radio';
import LinkButton from '@/components/ui/link-button';
import {SortableListCategory} from "@/components/ui/sortable-list-category";
import {SortableListBrand} from "@/components/ui/sortable-list-brand";
import React, {useState} from "react";
import {sortBulkCategoriesByDisplayIndex} from "@/data/categories";
import {SortableListGood} from "@/components/ui/sortable-list-good";
import {formatNumber} from "@/utils/common-utils";
import {VnptGoodsCard} from "@/components/ui/vnptCard";
import {useProvidersVNPTEpayListQuery} from "@/data/vnpt-epay";
import {VOUCHER_TYPE_CODES} from "@/components/common/status-code-badge";
import SelectInput from "@/components/ui/select-input-autocomplete";
import {getUserInfo} from "@/utils/auth-utils";
import {useProvidersXpayListQuery} from "@/data/xpay";

type IProps = {
	data?: Good | null;
};

export default function GoodDetail({data}: IProps) {
	// console.log("data", data)
	const {providersList: allVnptGoods} = useProvidersVNPTEpayListQuery(
		{page: 1, pageSize: PAGE_SIZE_MAX}, {}
	);
	const {providersList: allXpayGoods} = useProvidersXpayListQuery(
		{page: 1, pageSize: PAGE_SIZE_MAX}, {}
	);
  const xpayType = data?.system === SYSTEM_BRAND_TYPE.XPAY

	let code: Code | undefined;
	const systemINTERNAL = ((data?.brand?.system === SYSTEM_BRAND_TYPE.INTERNAL) ? true : false)
	const systemCHOICE = ((data?.brand?.system === SYSTEM_BRAND_TYPE.CHOICE) ? true : false)
	const systemBULK = ((data?.brand?.system === SYSTEM_BRAND_TYPE.BULK) ? true : false)
	const systemVnptEpay = ((data?.brand?.system === SYSTEM_BRAND_TYPE.VNPT_EPAY) ? true : false)
	const systemXpay = ((data?.brand?.system === SYSTEM_BRAND_TYPE.XPAY) ? true : false)

	const {t} = useTranslation();
	const router = useRouter();

	// BULK
	const bulkCategories = sortBulkCategoriesByDisplayIndex(data?.bulkCategories ?? [])
	const [bulkBrands, setBulkBrands] = useState([]);
	const [bulkGoods, setBulkGoods] = useState([]);
	const [categoryCodeBULKSelected, setCategoryCodeBULKSelected] = useState("");
	const [brandIdBULKSelected, setBrandIdBULKSelected] = useState("");

	const onCategoryBULKSelected = (categoryCode: string) => {
		// console.log("categoryCodeBULKSelected", categoryCode)
		if (categoryCodeBULKSelected === categoryCode) {
			return
		}
		setCategoryCodeBULKSelected(categoryCode)
		setBrandIdBULKSelected("")
		bulkCategories?.forEach(
			(bulkCategory) => {
				if (bulkCategory.categoryCode === categoryCode) {
					// @ts-ignore
					setBulkBrands(bulkCategory?.bulkBrands ?? [])
				}
			}
		)
		// @ts-ignore
		setBulkGoods([]);
	};
	const onBrandBULKSelected = (brandID: string) => {
		// console.log("onBrandBULKSelected", brandID)
		if (brandIdBULKSelected === brandID) {
			return
		}
		setBrandIdBULKSelected(brandID)
		bulkCategories?.forEach(category => {
			if (category?.categoryCode === categoryCodeBULKSelected) {
				category.bulkBrands?.forEach(brand => {
					if (brand?.brandId === brandID) {
						// @ts-ignore
						setBulkGoods(brand?.bulkGoods ?? []);
					}
				})
			}
		})
	};

	let isPeriodTerm: boolean = data?.periodType == "FIXED_TERM"
	let isPeriodDate: boolean = data?.periodType == "FIXED_DT"

	const {supplierContract} = useSupplierContractQuery(data?.supplierContractId as string)

	// const { supplier } = useSupplierQuery(data?.supplierIdObject as string)

	function handleGoodType(): string {
		const {codeGroup} = useCodeGroupQuery(
			CODE_GROUP.GOODS_TYPE
		)
		code = codeGroup?.codes?.find((temp: Code) => temp.codeId === data?.goodsTypeCode?.codeId)
		return (code === undefined ? '' : code?.codeName)
	}

	const {
		register,
		setValue,
		setError,
		control,
		formState: {errors},
	} = useForm<Partial<Good>>({});

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

	return (
		<>
			<div className="my-4 flex flex-wrap ">
				<Card className="w-full sm:w-full md:w-full">
					<div className="flex border-b border-dashed border-border-base md:py-2 sm:py-2">
						<h1 className="text-lg font-semibold text-heading uppercase text-red-600">
							{t('Product Information')}
						</h1>
					</div>

					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 py-2">
							<h1 className="text-base uppercase font-semibold">
								{t('Product Information')}
							</h1>
						</div>
					</div>

					{/* Product Name + Supplier Contract */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product name *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={data?.goodsName}
							/>
						</div>

						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Supplier Contract *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={supplierContract?.id + ' - ' + supplierContract?.contractName}
							/>
						</div>
					</div>

					{/* Product Code + Product ID*/}
					{/* Product code field not clear, check with Back-end later */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product Code *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={data?.supplierGoodsId}
							/>
						</div>
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product Id *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={data?.id.toString()}
							/>
						</div>
					</div>

					{/* Supplier Name + Product Type */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Supplier name *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={data?.supplier?.id + ' - ' + data?.supplier?.supplierName}
							/>
						</div>

						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product Type *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={data?.goodsType}
							/>
						</div>
					</div>

					{/*Count LC*/}
					{ data?.goodsType === VOUCHER_TYPE_CODES.LC && <div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
						</div>
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Count ')}<span className="text-red-500">*</span>
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={data?.usageCount}
							/>
						</div>
					</div>}

					{/* Period Type + Brand Name */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Brand Name *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={data?.brand?.id + ' - ' + data?.brand?.brandName}
							/>
						</div>
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('System *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
                placeholder={data?.system }
							/>
						</div>
					</div>

					{/* Product Description + Term (day) */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product Description *')}
							</label>
							<div className="w-full md:w-3/4">
								<TextArea
									readOnly
									disabled
									name='goodsDescription'
									placeholder={data?.goodsDescription}
									variant="outline"
								/>
							</div>
						</div>

						{(systemINTERNAL || systemCHOICE || systemVnptEpay || systemXpay || systemBULK) && <div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Period Type *')}
							</label>
							<div className="flex w-full md:w-3/4">
								<Radio
									readOnly
									className="flex w-full md:w-1/2"
									label={t('Fixed Term')}
									{...register('periodType')}
									id="periodTypeTerm"
									value="FIXED_TERM"
									checked={isPeriodTerm}
								/>
								<Radio
									readOnly
									className="flex w-full md:w-1/2"
									label={t('Fixed Date')}
									{...register('periodType')}
									id="periodTypeDate"
									value="FIXED_DT"
									checked={isPeriodDate}
								/>
							</div>
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Term (Days) *')}
							</label>
							<input
								readOnly
								className={rootClassName}
								disabled={true}
								placeholder={isPeriodTerm ? data?.periodTerm : data?.periodExpireDate}
							/>
						</div>}
					</div>


					{/* Active + Product Image */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Active *')}
							</label>
							<Radio
								readOnly
								className="py-2 w-full md:w-1/4"
								label={t('Yes')}
								{...register('validYn')}
								id="valid_Yes"
								value="Y"
								checked={data?.validYn === "Y" ? true : false}
							/>
							<Radio
								className="py-2 w-full md:w-1/2"
								label={t('No')}
								{...register('validYn')}
								id="valid_No"
								value="N"
								checked={data?.validYn === "N" ? true : false}
							/>
						</div>

						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4">
								{t('Product Image *')}
							</label>
							{
								data?.goodsImgPath &&
								<img
									src={getUrlPublicAsset(data?.goodsImgPath || '') ?? emptyPlaceholder}
									alt={'Product Logo'}
									width={300}
									height={300}
								/>
							}
						</div>
					</div>
					{/* </Card>
      </div> */}

					{/* Contract Information */}
					{/* <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full"> */}
					<div className="mb-5 flex flex-wrap">
						<div className="flex w-full flex-wrap font-semibold px-4 py-2">
							<h1 className="text-lg uppercase">
								{t('Contract Information')}
							</h1>
						</div>
					</div>
					<GoodSupplierContractDetail goodDetail={data}/>

					{!systemBULK && <div>

						<div className="mb-2 flex flex-wrap">
							<div className="flex w-full flex-wrap px-4">
								<div className="md:w-[12%]">
									<label className="h-full py-2 text-heading">
										{t('Product Category *')}
									</label>
								</div>

								<div className="flex flex-wrap h-full md:w-[88%]">
									{data?.categories && (<>
										{data?.categories.map((cate, i) => {
											return (
												<div className="pr-2 mb-3" key={cate?.categoryCode}>
													<div className="flex w-full items-center rounded bg-gray-400 text-body-dark md:h-9">
														<button
															disabled={true}
														>
														</button>
														<span className="p-4 text-center w-full">
                            {cate.categoryName}
                          </span>
													</div>
												</div>
											)
										})}
									</>)}
								</div>
							</div>
						</div>
						{!systemVnptEpay && !systemXpay && !systemCHOICE && <div className="mb-5 flex flex-wrap">
							<div className="flex w-full flex-wrap px-4">
								<div className="md:w-[12%]">
									<label className="h-full py-2 text-heading">
										{t('Except Store')}
									</label>
								</div>

								<div className="flex flex-wrap h-full md:w-[88%]">
									{data?.exceptStores && (<>
										{data?.exceptStores.map((exceptedStore, i) => {
											return (
												<div className="pr-2 mb-3" key={exceptedStore?.id}>
													<div className="flex w-full items-center rounded bg-gray-400 text-body-dark md:h-9">
														<button
															disabled={true}
														>
														</button>
														<span className="p-4 text-center w-full">
                            {exceptedStore.storeName}
                          </span>
													</div>
												</div>
											)
										})}
									</>)}
								</div>
							</div>
						</div>}

						{systemCHOICE && <div>
							<div className="mb-1 flex flex-wrap">
								<div className="flex w-full flex-wrap px-4 ">
									<label className="h-full py-2 text-heading">
										{t('Products {Choices Type} *')}
									</label>
								</div>
							</div>
							<div className="flex flex-wrap h-full md:w-full h-full overflow-auto max-h-screen">
								<SortableListGood
									goods={data?.listGoodsChoice ?? []}
									systemChoice={true}
									caseView={true}/>
							</div>
						</div>}
					</div>}


					{/* Categories + Brands title button */}
					{systemBULK && <div className="mb-1 flex flex-wrap">
						{/* Categories title button */}
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4 uppercase font-bold">
								{t('Categories ')}<span className="  ">*</span>
							</label>
							<div className="flex flex-wrap h-full  md:w-full h-full overflow-auto max-h-96">
								<SortableListCategory
									categories={bulkCategories || []}
									onCategorySelected={onCategoryBULKSelected}
									caseView={true}/>
							</div>
						</div>
						<div className="flex w-full flex-wrap px-4 md:w-1/2">
							<label className="w-full py-2 text-heading md:w-1/4 uppercase font-bold">
								{t('Brands ')}<span className=" ">*</span>
							</label>
							<div className="flex flex-wrap h-full md:w-full h-full overflow-auto ">
								{!categoryCodeBULKSelected
									? <label className="w-full py-2 text-heading  italic">
										{t('Select category to show Brands ')}
									</label>
									: bulkBrands?.length === 0
										? <label className="w-full py-2 text-heading  italic">
											{t('Empty Brands ')}
										</label>
										:
										<div className="flex flex-wrap h-full  md:w-full h-full overflow-auto max-h-96">
											<SortableListBrand
												brands={bulkBrands || []}
												onBrandSelected={onBrandBULKSelected}
												caseView={true}/>
										</div>
								}
							</div>
						</div>
						<div className="mb-2 flex flex-wrap w-full px-4  ">
							<label className="w-full py-2 text-heading md:w-1/4 uppercase font-bold">
								{t('Products ')}<span className="  ">*</span>
							</label>
							<div className="flex flex-wrap h-full md:w-full h-full overflow-auto ">
								{!brandIdBULKSelected
									? <label className="w-full py-2 text-heading  italic">
										{t('Select category and brand to show Products ')}
									</label>
									:
									<div className="flex flex-wrap h-full md:w-full h-full overflow-auto max-h-screen">
										<SortableListGood
											goods={bulkGoods || []}
											caseView={true}/>
									</div>
								}
							</div>

						</div>
					</div>}

					{/* VnptEpay topup */}
					{( systemVnptEpay || systemXpay) && <>
						<div className="mb-10 flex flex-wrap">
						</div>
						<div className={classes?.wrapper}>
							<div className=" flex flex-wrap">
								<div className="flex w-full flex-wrap px-4 py-2">
									<h1 className="text-lg text-cyan-600 text-heading font-semibold uppercase">
										{systemVnptEpay ? t('VNPT PRODUCTS') : t('XPAY PRODUCTS')}
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
										value={formatNumber(data?.listPrice as number, false, true)}
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
							list={(xpayType ? allXpayGoods : allVnptGoods).filter(provider => provider.providerType === "MOBILE_CARD")}
							selectedItems={(xpayType ? data?.xpayGoods : data?.vnptGoods)?.filter((item) => item.validYn === "Y").map((item) => ({
								...item,
								providerCd: item?.providerCode
							}))}
							faceValue={data?.listPrice}
							title="Mobile Card"/>
            {systemVnptEpay && <VnptGoodsCard
							list={(xpayType ? allXpayGoods : allVnptGoods).filter(provider => provider.providerType === "MOBILE_DATA")}
							selectedItems={(xpayType ? data?.xpayGoods : data?.vnptGoods)?.filter((item) => item.validYn === "Y").map((item) => ({
								...item,
								providerCd: item?.providerCode
							}))}
							faceValue={data?.listPrice}
							title="Mobile Data"/> }
            {systemVnptEpay && <VnptGoodsCard
							list={(xpayType ? allXpayGoods : allVnptGoods).filter(provider => provider.providerType === "GAME_CARD")}
							selectedItems={(xpayType ? data?.xpayGoods : data?.vnptGoods)?.filter((item) => item.validYn === "Y").map((item) => ({
								...item,
								providerCd: item?.providerCode
							}))}
							faceValue={data?.listPrice}
							title="Game Card and others"/> }
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
					{t('Back')}
				</Button>
				{getUserInfo().roleCode !== PERMISSIONS_EV.ROLE_SUPPLIER && <LinkButton size="medium"
				            href={data?.id ? `${Routes?.goods.editWithoutLang(data?.id.toString())}` : `${Routes?.goods}`}
				            className="bg-red-700 hover:bg-red-800">
					{t('Update & Edit')}
				</LinkButton> }
				{getUserInfo().roleCode !== PERMISSIONS_EV.ROLE_SUPPLIER && <LinkButton size="medium"
				            href={data?.id ? `${Routes?.goods.cloneWithoutLang(data?.id.toString())}` : `${Routes?.goods}`}
				            className="ms-4 bg-red-700 hover:bg-red-800">
					{t('Clone')}
				</LinkButton> }
			</div>
		</>
	)
}


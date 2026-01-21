import Checkbox from "@/components/ui/checkbox/checkbox";
import React, {useEffect, useRef, useState} from "react";
import {formatNumber} from "@/utils/common-utils";
import {tr} from "date-fns/locale";
import {Good, VNPTEPayProvider} from "@/types";

type IProps = {
  list ?: any;
  selectedItems ?: any;
  setSelectedItems ?: any;
  title ?: any;
  faceValue ?: any;
  vnptResGoodsAllValid ?: any;
};

// @ts-ignore
export const VnptGoodsCard = ({list, selectedItems, setSelectedItems, title, vnptResGoodsAllValid , faceValue}: IProps ) => {
  // console.log(list, faceValue, selectedItems)

  // case Edit good
  // useEffect để đảm bảo initSelectedRef chỉ được set một lần khi component mount
  useEffect(() => {
    list.forEach((item: any) => {
      providerCd: item.providerCode
    });
  }, []);


  const handleCheckboxChange = (card: any) => {
    // console.log("handleCheckboxChange card", card)
    // @ts-ignore
    setSelectedItems?.((prevSelectedItems) => {
      // @ts-ignore
      if (prevSelectedItems?.some((item) => item?.providerCd === card.providerCd)) {
        // Item is already selected, so remove it from the selection
        // @ts-ignore
        return prevSelectedItems?.filter((item) => item.providerCd !== card.providerCd);
      } else {
        // Item is not selected, so add it to the selection
        const newCard = (vnptResGoodsAllValid.length > 0 && vnptResGoodsAllValid.find((item: any) => item?.providerCode === card.providerCd))
          ? vnptResGoodsAllValid.find((item: any) => item?.providerCode === card.providerCd)
          : { ...card, validYn: 'Y' };

        return [...prevSelectedItems, newCard];
      }
    });
  };

  const handleTextareaChange = (providerCd: string, value: string) => {
    const truncatedValue = value.slice(0, 500); // Giới hạn ở 500 ký tự
    setSelectedItems((prevSelectedItems: any) => {
      return prevSelectedItems.map((item: any) =>
        item.providerCd === providerCd ? { ...item, description: truncatedValue } : item
      );
    });
  };

  //
  // console.log("handleCheckboxChange vnptResGoodsAllValid", vnptResGoodsAllValid)
  // console.log("handleCheckboxChange selectedItems", selectedItems)

  return (
    <div className="p-4">
      <h2 className=" font-bold mb-4">{title}</h2>
      <div className="grid grid-cols-2 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-4 ">
        {list?.map((item: VNPTEPayProvider) => (
          // CardItem
          <div key={item.providerCd}  className="flex w-full flex-col items-center px-4 md:flex-row border rounded-lg  p-4 shadow-lg    ">
            <div className="   items-center md:w-1/12 ">
              { faceValue
                ? JSON.parse(item?.allowedCardFaces).includes(Number(faceValue)) && <Checkbox
                    name={`item_${item?.providerCd}`}
                    checked={selectedItems?.some((it: any) => it.providerCd === item.providerCd)}
                    className="flex items-center font-semibold  "
                    onChange={() => handleCheckboxChange(item)} />
                : <Checkbox
                    name={`item_${item?.providerCd}`}
                    checked={selectedItems?.some((it: any) => it.providerCd === item.providerCd)}
                    className="flex items-center font-semibold  "
                    onChange={() => handleCheckboxChange(item)} />
              }
            </div>
            <div className="  flex flex-col w-full md:w-3/12 ">
              <span className="font-semibold   ">
                {item?.providerNm}
                { item?.validYn === "N" && <span
                    className=" px-2 py-1 ms-3 bg-gray-500 text-white  ml-auto rounded-xl text-sm font-mono"
                >
                  Invalid
                </span> }
              </span>
              <span className="text-sm text-gray-600 lowercase ">{item?.allowedActions}</span>
            </div>
            <textarea
              className="w-full md:w-8/12 px-4 ml-2 resize-none rounded-md border-gray-200 shadow-inner bg-gray-50"
              onChange={(e) => handleTextareaChange(item.providerCd, e.target.value)}
              value={selectedItems.find((it: any) => it.providerCd === item.providerCd)?.description || ''}
              rows={3}
              maxLength={500} // Giới hạn độ dài tối đa
              placeholder={item?.description || "Tick then enter description (max 500 characters)"}
              // disabled={!item?.allowedCardFaces.includes(faceValue)}
            />
          </div>
        ))}
      </div>
    </div>
  );
}

// @ts-ignore
export const VnptFacesCard = ({allCardFaces, allowedCardFaces } ) => {
  return (
    <div className="">
      <div className="mb-5 flex flex-wrap mt-10">
        <div className="flex w-full flex-wrap px-4 py-2">
          <h1 className="text-xl font-semibold uppercase">
            Card faces
          </h1>
        </div>
      </div>
      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4 w-9/12">
        {allCardFaces?.map( (face: any)  => (
          // CardItem
          <div className="flex w-full flex-col items-center px-6 py-5 md:flex-row border rounded-lg  p-4 shadow-lg bg-gray-100">
            <div className="   items-center md:w-1/5  ">
              <Checkbox
                name={`item_${face}`}
                checked={allowedCardFaces.includes(face)}
                className="flex items-center font-semibold  "
              />
            </div>
            <div className="  flex flex-col w-full md:w-4/5 ">
                  <span className="font-semibold   ">
                    { formatNumber(face, false, true)   }
                  </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

import React, { useEffect } from 'react';
import { Brands } from "@/types";
import {useModalAction} from "@/components/ui/modal/modal.context";
import {POPUP_DELETE_TYPE} from "@/utils/constants";

interface SortableListProps {
  brands: Brands[];
  onBrandsChange?: (brands: Brands[]) => void;
  onBrandSelected?: (id: string ) => void;
  onBrandDelete?: (brandID: string ) => void;
  caseUpdate?: boolean;
  caseView?: boolean;
}

export const SortableListBrand: React.FC<SortableListProps> = (
    { brands,
      onBrandsChange,
      onBrandSelected,
      onBrandDelete,
      caseView = false}) => {
  const [items, setItems] = React.useState<Brands[]>([]);
  const [selectedItem, setSelectedItem] = React.useState<string | null>(null);

  // Update items state when brands prop changes
  useEffect(() => {
    // @ts-ignore
    setItems(brands );
    if(items?.length === 0) setSelectedItem(null);
  }, [brands]);

  const [draggingIndex, setDraggingIndex] = React.useState(-1);
  const dragNode = React.useRef<HTMLDivElement | null>(null);

  const handleDragStart = (e: React.DragEvent<HTMLDivElement>, index: number) => {
    const { target } = e;
    setDraggingIndex(index);

    dragNode.current = target as HTMLDivElement;
    e.dataTransfer.effectAllowed = 'move';
    // @ts-ignore
    e.dataTransfer.setData('text/html', target);
  };

  const handleDragOver = (e: React.DragEvent<HTMLDivElement>, index: number) => {
    e.preventDefault();
    if (dragNode.current !== e.target) {
      let newItems = [...items];
      newItems.splice(index, 0, newItems.splice(draggingIndex, 1)[0]);
      setDraggingIndex(index);
      setItems(newItems);
      onBrandsChange?.(newItems);

      // console.log("newItemsnewItems", newItems)
    }
  };

  const { openModal } = useModalAction();
  function popupConfirmDelete(userConfirmed: boolean, id: string) {
    if (userConfirmed) {
      onBrandDelete?.(id);
      const newItems = items?.filter((item) => item.brandId !== id);
      setItems(newItems);
      onBrandsChange?.(newItems);
    }
  }
  const typeDelete = POPUP_DELETE_TYPE.BRAND
  const handleDelete = (id: string, e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault(); // Prevent form submission
    openModal("WARNING_BULK_DELETE_POPUP", { popupConfirmDelete, id, typeDelete });
  };

  const handleSelectItem = (id: string) => {
    setSelectedItem(id);
    onBrandSelected?.(id);
  };

  return (
      <div className="ml-auto md:w-[75%]">
        {items?.map((item, index) => (
            <div
                className="py-0.5 px-2 shadow-sm mb-2 flex justify-between items-center  md:w-full"
                key={item.brandId}
                draggable="true"
                onDragStart={(e) => caseView ? null : handleDragStart(e, index)}
                onDragOver={(e) => caseView ? null : handleDragOver(e, index)}
            >
              <span
                  className={`flex-grow md:w-9/12 md:truncate ${selectedItem === item.brandId ? 'text-red-600 font-bold' : ''}`}
                  onClick={() => handleSelectItem(item.brandId)}
              >
                 {caseView ? item?.brand?.brandName : item.brandName}
                { item?.brand?.validYn === "N" && <span
                    className=" px-2 ms-3 bg-gray-500 text-white  ml-auto rounded-xl text-sm"
                >
                  Invalid
                </span> }
              </span>
              { !caseView && <button
                  className="py-0.5 px-2 me-2 text-red-600 font-bold ml-auto"
                  onClick={(e) => handleDelete(item.brandId, e)}
              >
                x
              </button> }
            </div>
        ))}
      </div>
  );
};


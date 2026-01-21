import React, {useEffect, useState} from 'react';
import { Category } from "@/types";
import {useModalAction} from "@/components/ui/modal/modal.context";
import {POPUP_DELETE_TYPE} from "@/utils/constants";

interface SortableListProps {
  categories: Category[];
  onCategoriesChange?: (categories: Category[]) => void;
  onCategorySelected?: (categoryCode: string ) => void;
  onCategoryDelete?: (categoryCode: string ) => void;
  caseUpdate?: boolean;
  caseView?: boolean;
}

export const SortableListCategory: React.FC<SortableListProps> = ({
                                      categories,
                                      onCategoriesChange,
                                      onCategorySelected,
                                      onCategoryDelete,
                                      caseUpdate = false,
                                      caseView = false}) => {
  const [items, setItems] = React.useState<Category[]>([]);
  const [selectedItem, setSelectedItem] = React.useState<string | null>(null);

  // Update items state when categories prop changes
  useEffect(() => {
    // @ts-ignore
    setItems(categories );
    if(items?.length === 0) setSelectedItem(null);
  }, [categories]);

  const [draggingIndex, setDraggingIndex] = React.useState(-1);
  const dragNode = React.useRef<HTMLDivElement | null>(null);

  const handleDragStart = (e: React.DragEvent<HTMLDivElement>, index: number) => {
    const { target } = e;
    setDraggingIndex(index);

    dragNode.current = target as HTMLDivElement;
    e.dataTransfer.effectAllowed = 'move';
    // @ts-ignore
    e.dataTransfer.setData('text/html', target.innerHTML);
  };

  const handleDragOver = (e: React.DragEvent<HTMLDivElement>, index: number) => {
    e.preventDefault();
    if (dragNode.current !== e.target) {
      let newItems = [...items];
      newItems.splice(index, 0, newItems.splice(draggingIndex, 1)[0]);
      setDraggingIndex(index);
      setItems(newItems);
      onCategoriesChange?.(newItems);

      // console.log(newItems)
    }
  };

  const { openModal } = useModalAction();
  function popupConfirmDelete(userConfirmed: boolean, id: string) {
    if (userConfirmed) {
      onCategoryDelete?.(id);
      const newItems = items.filter((item) => item?.categoryCode !== id);
      setItems(newItems);
      onCategoriesChange?.(newItems);
    }
  }
  const typeDelete = POPUP_DELETE_TYPE.CATEGORY
  const handleDelete = (id: string, e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault(); // Prevent form submission
    openModal("WARNING_BULK_DELETE_POPUP", { popupConfirmDelete, id, typeDelete });
  };

  const handleSelectItem = (categoryCode: string) => {
    setSelectedItem(categoryCode);
    onCategorySelected?.(categoryCode);
  };

  return (
      <div className="ml-auto md:w-[75%]">
        {items?.map((item, index) => (
            <div
                className="py-0.5 px-2 shadow-sm mb-2 flex justify-between items-center  md:w-full"
                key={`${item?.categoryCode}-${index}`}
                draggable="true"
                onDragStart={(e) => caseView ? null : handleDragStart(e, index)}
                onDragOver={(e) => caseView ? null : handleDragOver(e, index)}
            >
              <span
                  className={`flex-grow md:w-9/12 md:truncate  ${selectedItem === item?.categoryCode ? 'text-red-600 font-bold' : ''}`}
                  onClick={() => handleSelectItem(item?.categoryCode)}
              >
                {caseView ? item?.category?.categoryName : item?.categoryName}
                { item?.category?.validYn === "N" && <span
                    className=" px-2 ms-3 bg-gray-500 text-white  ml-auto rounded-xl text-sm"
                >
                  Invalid
                </span> }
              </span>
              { !caseView && <button
                    className="py-0.5 px-2 me-2 text-red-600 font-bold ml-auto"
                    onClick={(e) => handleDelete(item?.categoryCode, e)}
                >
                x
              </button> }
            </div>
        ))}
      </div>
  );
};


import React, {useEffect, useLayoutEffect} from 'react';
import { Good } from "@/types";
import {useModalAction} from "@/components/ui/modal/modal.context";
import {POPUP_DELETE_TYPE} from "@/utils/constants";
import {MoveTopIcon} from "@/components/icons/move-top";
import {MoveUpIcon} from "@/components/icons/move-up";
import {MoveDownIcon} from "@/components/icons/move-down";
import {DeleteIcon} from "@/components/icons/delete-icon";

interface SortableListProps {
  goods: Good[];
  onGoodChange?: (Good: Good[]) => void;
  onGoodSelected?: (id: string ) => void;
  onGoodDelete?: (goodId: string ) => void;
  caseUpdate?: boolean;
  caseView?: boolean;
  systemChoice?: boolean;
}

export const SortableListGood: React.FC<SortableListProps> = (
    { goods,
      onGoodChange,
      onGoodSelected,
      onGoodDelete,
      caseView = false,
      systemChoice = false }) => {
  const [items, setItems] = React.useState<Good[]>([]);
  const [selectedItem, setSelectedItem] = React.useState<string | null>(null);

  // Update items state when Good prop changes
  useEffect(() => {
    // @ts-ignore
    setItems(goods );
    if(items?.length === 0) setSelectedItem(null);

    // console.log("goods", goods)
  }, [goods]);

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
      onGoodChange?.(newItems);

      // console.log("newItemsnewItems", newItems)
    }
  };

  const { openModal } = useModalAction();
  function popupConfirmDelete(userConfirmed: boolean, id: string) {
    if (userConfirmed) {
      onGoodDelete?.(id);
      const newItems = items?.filter((item) => (systemChoice ? String(item.id) : item?.goodsId) !== id);
      setItems(newItems);
      onGoodChange?.(newItems);
    }
  }
  const typeDelete = POPUP_DELETE_TYPE.GOOD
  const handleDelete = (id: string, e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault(); // Prevent form submission
    openModal("WARNING_BULK_DELETE_POPUP", { popupConfirmDelete, id, typeDelete });
  };

  const handleSelectItem = (id: string) => {
    setSelectedItem(id);
    onGoodSelected?.(id);
  };

  return (
      <div className="ml-auto md:w-[88%]">
        <div className="w-full sm:w-full md:w-full  bg-gray-100 py-1 font-bold flex " >
          <div className={`flex-grow md:w-1/12 ps-2` } >NO</div>
          <div className={`flex-grow md:w-1/12   text-center` } >ID</div>
          <div className={`flex-grow md:w-4/12   text-center` } >PRODUCT NAME</div>
          <div className={`flex-grow md:w-2/12  text-center ` } >VALID</div>
          <div className={`flex-grow md:w-3/12   text-center` } >SYSTEM</div>
          { !caseView && <div className={`flex-grow md:w-1/12   text-center` } >REMOVE</div> }
        </div>

        {items?.map((item, index) => (
            <div className="w-full sm:w-full md:w-full  py-0.5 px-2 shadow-sm mb-2 flex justify-between items-center  "
                key={(systemChoice ? String(item.id) : item?.goodsId)}
                draggable="true"
                onDragStart={(e) => caseView ? null : handleDragStart(e, index)}
                onDragOver={(e) => caseView ? null : handleDragOver(e, index)}
            >
              {/*NO*/}
              <span
                  className={` md:w-1/12 flex-grow md:truncate ps-2  ${selectedItem === (systemChoice ? String(item.id) : item?.goodsId) ? 'text-red-600 font-bold' : ''}`}
                  onClick={() => handleSelectItem(systemChoice ? String(item.id)  : item?.goodsId)}
              > {index + 1} </span>

              {/*ID*/}
              <span
                  className={` md:w-1/12 flex-grow md:truncate  text-center ${selectedItem === (systemChoice ? String(item.id) : item?.goodsId) ? 'text-red-600 font-bold' : ''}`}
                  onClick={() => handleSelectItem((systemChoice ? String(item.id)  : item?.goodsId))}
              > { (systemChoice ? String(item.id) : item?.goodsId) ?? item?.goods?.id  }  </span>

              {/*PRODUCT NAME*/}
              <span
                  className={` md:w-4/12 flex-grow  text-center ${selectedItem === (systemChoice ? String(item.id) : item?.goodsId) ? 'text-red-600 font-bold' : ''}`}
                  onClick={() => handleSelectItem((systemChoice ? String(item.id)  : item?.goodsId))}
              > { item?.goodsName ?? item?.goods?.goodsName  }  </span>

              {/*VALID*/}
              <span
                  className={` md:w-2/12 flex-grow text-center ${selectedItem === (systemChoice ? String(item.id) : item?.goodsId) ? 'text-red-600 font-bold' : ''}`}
                  onClick={() => handleSelectItem((systemChoice ? String(item.id)  : item?.goodsId))}
              > { item?.goods?.validYn ??  item?.validYn }  </span>

              {/*SYSTEM*/}
              <span
                  className={` md:w-3/12 flex-grow  text-center ${selectedItem === (systemChoice ? String(item.id) : item?.goodsId) ? 'text-red-600 font-bold' : ''}`}
                  onClick={() => handleSelectItem((systemChoice ? String(item.id)  : item?.goodsId))}
              > { item?.system ?? item?.goods?.system  }  </span>

              {/*ACTION*/}
              { !caseView && <button
                  className="md:w-1/12  py-1 text-red-600 font-bold  text-center"
                  onClick={(e) => handleDelete((systemChoice ? String(item.id)  : item?.goodsId), e)}
              >
                x
              </button> }
            </div>
        ))}
      </div>
  );
};


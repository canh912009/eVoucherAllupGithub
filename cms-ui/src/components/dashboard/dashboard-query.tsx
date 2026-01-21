import {Controller, useForm, useWatch} from 'react-hook-form';
import {useTranslation} from 'next-i18next';
import {Customer, DashboardTimeQueryOptions as SearchValue, Supplier} from '@/types';
import {DatePicker} from "@/components/ui/date-picker";
import {LangSwitcherIcon} from "@/components/icons/lang-switcher-icon";
import SelectInput from "@/components/ui/select-input-autocomplete";

type SearchProps = {
  title: string;
  showSelectBox?: boolean;
  optionsSelectInput?: Supplier[] | Customer[],
  loadingSelectInput?: boolean,
  handleInputSelect?: (newInputText: string) => void,
  handleChangeSelect?: (option: any) => void,
  className?: string;
  shadow?: boolean;
  variant?: 'normal' | 'solid' | 'outline';
  inputClassName?: string;
  onChangeStartTime: (...event: any[]) => void
  onChangeEndTime: (...event: any[]) => void
  // onCallback: (id: anyt) => void,
};

export function getFirstDateCurrentYear(): string {
  const year = new Date().getFullYear();
  return `${year}-01-01`;
}

export function getCurrentDate(): string {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

const DashboardQuery: React.FC<SearchProps> = ({
                                             // className,
                                             title,
                                             showSelectBox = false,
                                             optionsSelectInput,
                                             loadingSelectInput,
                                             handleInputSelect,
                                             handleChangeSelect,
                                             onChangeStartTime,
                                             onChangeEndTime,
                                             variant = 'outline',
                                             shadow = false,
                                             inputClassName,
                                             ...rest
                                           }) => {
  const {
    register,
    handleSubmit,
    control,
    setValue,
    formState: {errors},
  } = useForm<SearchValue>({
    defaultValues: {
      startDate: getFirstDateCurrentYear(),
      endDate: getCurrentDate(),
    },
  });
  const {t} = useTranslation();


  return (
    <>
      <form
        noValidate
        role="search"
      >
        <div className="flex justify-between">
          <div className="flex flex justify-center h-full font-bold text-lg">
            { title}
            {/*{showSelectBox && title}*/}
          </div>
          {showSelectBox && <div className="w-full md:w-2/5 ">
              <SelectInput
                // @ts-ignore
                  name="nameSelectInput"
                  options={optionsSelectInput}
                  isLoading={loadingSelectInput}
                  getOptionLabel={(option: any) =>
                    (option?.supplierName ?? option?.customerName) + " - ID : " + option?.id
                  }
                  getOptionValue={(option: any) => option.id}
                  onInputChange={handleInputSelect}
                  onChange={(selectedOption: any) => {
                    // @ts-ignore
                    setValue('nameSelectInput', selectedOption); // Gọi setValue
                    if (handleChangeSelect) {
                      handleChangeSelect(selectedOption);
                    } // Gọi handleChangeSelect
                  }}
                  placeholder={t('common:filter-by-group-placeholder')}
                  control={control}
                  isClearable={true}
              />
          </div>}
          <div className="flex items-start">
            <div className="mr-4 ">
              <Controller
                control={control}
                name="startDate"
                render={({field: {onChange, onBlur, value}}) => (
                  <div className="relative rounded-3xl">
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      onChange={(date) => {
                        const selectedDate = date ? date.toISOString().split('T')[0] : null;
                        onChange(selectedDate);
                        onChangeStartTime(selectedDate);
                      }}
                      onBlur={onBlur}
                      selected={value ? new Date(value) : null}
                      selectsStart
                      startDate={new Date()}
                      // className="border border-border-base h-1/2"
                      placeholderText={t('Start date')}
                    />
                    <div className="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none">
                      <LangSwitcherIcon className="text-gray-400" aria-hidden="true"/>
                    </div>
                  </div>
                )}
              />
            </div>
            <div>
              <Controller
                control={control}
                name="endDate"
                render={({field: {onChange, onBlur, value}}) => (
                  <div className="relative bg-amber-300 rounded-3xl">
                    <DatePicker
                      dateFormat="yyyy-MM-dd"
                      onChange={(date) => {
                        const selectedDate = date ? date.toISOString().split('T')[0] : null;
                        onChange(selectedDate);
                        onChangeEndTime(selectedDate);
                      }}
                      onBlur={onBlur}
                      selected={value ? new Date(value) : null}
                      selectsStart
                      startDate={new Date()}
                      className="border border-border-base"
                      placeholderText={t('End date')}
                    />
                    <div className="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none">
                      <LangSwitcherIcon className="text-gray-400" aria-hidden="true"/>
                    </div>
                  </div>

                )}
              />
            </div>
          </div>
        </div>
      </form>
    </>
  );
};

export default DashboardQuery;

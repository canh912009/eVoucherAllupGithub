import { Switch } from '@headlessui/react';

interface Props {
  label?: string;
  checked?: boolean;
  disabled?: boolean;
  [key: string]: unknown;
}

const SwitchDisplay = ({
  label,
  checked,
  disabled
}: Props) => {
  return (
    <Switch
        checked={checked}
        disabled={disabled}
        className={`${
          checked ? 'bg-red-600' : 'bg-gray-300'
        } relative inline-flex h-6 w-11 items-center rounded-full focus:outline-none ${
          disabled ? 'cursor-not-allowed bg-[#EEF1F4]' : ''
        }`}
        dir="ltr"
      >
        <span className="sr-only">Enable {label}</span>
        <span
          className={`${
            checked ? 'translate-x-6' : 'translate-x-1'
          } inline-block h-4 w-4 transform rounded-full bg-light transition-transform`}
        />
      </Switch>
  );
};

export default SwitchDisplay;

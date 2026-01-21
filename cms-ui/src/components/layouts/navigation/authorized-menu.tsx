import cn from 'classnames';
import { Fragment, useEffect } from 'react';
import { Menu, Transition } from '@headlessui/react';
import Avatar from '@/components/common/avatar';
import Link from '@/components/ui/link';
import { siteSettings } from '@/settings/site.settings';
import { useTranslation } from 'next-i18next';
import { useMeQuery } from '@/data/user';
import { useModalAction } from '@/components/ui/modal/modal.context';
import { setUserInfo } from '@/utils/auth-utils';

export default function AuthorizedMenu() {
  const { data } = useMeQuery();
  const { t } = useTranslation('common');
  const userInfo = data?.data
  const { openModal } = useModalAction();

  const handleChangePassword = () => {
    // console.log('data = ', data)
    openModal('CHANGE_PASSWORD', { data });
  }

  // Again, we're using framer-motion for the transition effect
  return (
    <Menu as="div" className="relative inline-block text-left">
      <Menu.Button className="flex items-center focus:outline-none">
        <Avatar
          src={
            // userInfo?.profile?.avatar?.thumbnail ??
            siteSettings?.avatar?.placeholder
          }
          alt="avatar"
        />
        <span className="text-xs ms-2">{userInfo?.roleCode?.slice(5) || userInfo?.email}</span>
      </Menu.Button>

      <Transition
        as={Fragment}
        enter="transition ease-out duration-100"
        enterFrom="transform opacity-0 scale-95"
        enterTo="transform opacity-100 scale-100"
        leave="transition ease-in duration-75"
        leaveFrom="transform opacity-100 scale-100"
        leaveTo="transform opacity-0 scale-95"
      >
        <Menu.Items
          as="ul"
          className="end-0 origin-top-end absolute mt-1 w-48 rounded bg-white shadow-md focus:outline-none"
        >
          <Menu.Item key={userInfo?.email}>
            <li
              className="flex w-full flex-col space-y-1 rounded-t px-4 py-3 text-sm text-white bg-red-700"
            >
              <span className="font-semibold capitalize">{userInfo?.name}</span>
              <span className="text-xs">{userInfo?.email || userInfo?.roleCode}</span>
            </li>
          </Menu.Item>

          <Menu.Item >
            <button
              onClick={handleChangePassword}
            >
              <span className={cn(
                'block px-4 py-3 text-sm font-semibold capitalize transition duration-200 text-red-700 hover:text-red-800',
                'text-heading'
              )}>
                {t('Change Password')}
              </span>
            </button>
          </Menu.Item>

          {siteSettings.authorizedLinks.map(({ href, labelTransKey }) => (
            <Menu.Item key={`${href}${labelTransKey}`}>
              {({ active }) => (
                <li className="cursor-pointer border-b border-gray-100 last:border-0">
                  <Link
                    href={href}
                    className={cn(
                      'block px-4 py-3 text-sm font-semibold capitalize transition duration-200 text-red-700 hover:text-red-800',
                      active ? 'text-accent' : 'text-heading'
                    )}
                  >
                    {t(labelTransKey)}
                  </Link>
                </li>
              )}
            </Menu.Item>
          ))}

        </Menu.Items>
      </Transition>
    </Menu>
  );
}

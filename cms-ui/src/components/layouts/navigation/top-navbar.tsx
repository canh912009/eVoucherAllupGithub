import Logo from '@/components/ui/logo';
import { useUI } from '@/contexts/ui.context';
import AuthorizedMenu from './authorized-menu';
import LinkButton from '@/components/ui/link-button';
import { NavbarIcon } from '@/components/icons/navbar-icon';
import { motion } from 'framer-motion';
import { useTranslation } from 'next-i18next';
import { Routes } from '@/config/routes';
import {
  adminAndOwnerOnly,
  getAuthCredentials,
  hasAccess,
} from '@/utils/auth-utils';
import LanguageSwitcher from './language-switer';
import { Config } from '@/config';

type Props = {
  menuName?: string;
};

const Navbar = ({menuName} : Props) => {
  const { t } = useTranslation();
  const { toggleSidebar } = useUI();

  const { permissions } = getAuthCredentials();

  const { enableMultiLang } = Config;

  return (
    <header className="fixed z-40 w-full e_size-bar shadow ">
      <nav className="flex items-center lg:e_header bg-[#F7F9FC] justify-between px-5 py-4 md:px-8 ">
        {/* <!-- Mobile menu button --> */}
        <motion.button
          whileTap={{ scale: 0.88 }}
          onClick={toggleSidebar}
          className="flex h-full items-center justify-center p-2 focus:text-accent focus:outline-none lg:hidden"
        >
          <NavbarIcon />
        </motion.button>

        <div className="ms-5 me-auto hidden md:flex">
          <Logo className='e_logo-wrapper'/>
          <div className="font-bold items-center text-2xl flex ml-24 ">{menuName}</div>
        </div>

        <div className="space-s-8 flex items-center">
          {enableMultiLang ? <LanguageSwitcher /> : null}
          <AuthorizedMenu />
        </div>
      </nav>
    </header>
  );
};

export default Navbar;

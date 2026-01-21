import Navbar from '@/components/layouts/navigation/top-navbar';
import MobileNavigation from '@/components/layouts/navigation/mobile-navigation';
import { useRouter } from 'next/router';
import { getAuthCredentials, hasAccess,   } from '@/utils/auth-utils';
import TreeMenu from '@/components/ui/tree-menu';
import {menuTree} from "@/utils/menuTree";

const OwnerLayout: React.FC<{ children?: React.ReactNode }> = ({
  children,
}) => {
  const { locale } = useRouter();
  const router = useRouter();
  const dir = locale === 'ar' || locale === 'he' ? 'rtl' : 'ltr';
  const { token, permissions } = getAuthCredentials();
  // const {role, loading, error} = useRoleQuery(permissions ? permissions[0] : "");
  // const menuTree = role?.menuGroups.sort((a, b) => a.sortOrder - b.sortOrder) || []
  const menuName:string = getMenuName(router.asPath)

  function getMenuName(pagePath: string):string {
    let menuName = ""
    menuTree?.map(menu0 => {
      menu0.menus.sort((a, b) => a.sortOrder - b.sortOrder).map(menu1 => {
        if(menu1.menuUrl === pagePath) {
          menuName = menu0.menuGroupName+ " > " + menu1.menuName
          return menuName
        }
      })
    })
    return menuName
  }

  // just for hard code json file ( no API menuTree )
  const menuTreeByRole = menuTree.map(menuGroup => {
    const filteredMenusInGroup = menuGroup.menus.filter(menu =>
      menu.permissions.includes((permissions != null) ? permissions[0] : "ROLL_STORE") );
    return {...menuGroup, menus: filteredMenusInGroup};
  }).filter(menuGroup => menuGroup.menus.length > 0);

  return (
    <div
      className="flex min-h-screen flex-col bg-gray-100 transition-colors duration-150"
      dir={dir}
    >
      <Navbar menuName={menuName}/>

      <MobileNavigation>
        <TreeMenu items={menuTreeByRole} className="xl:py-2" />
      </MobileNavigation>

      <div className="flex flex-1 pt-20">
        <aside className="e_size-bar xl:w-76 fixed bottom-0 hidden h-full w-72 overflow-y-auto
         pt-22 shadow ltr:left-0 ltr:right-auto rtl:right-0 rtl:left-auto lg:block">
          <TreeMenu items={menuTreeByRole} className="xl:py-2" />
        </aside>
        <main className="ltr:xl:pl-76 rtl:xl:pr-76 w-full ltr:lg:pl-72 rtl:lg:pr-72 rtl:lg:pl-0">
          <div className="h-full p-5 md:p-8">{children}</div>
        </main>
      </div>
    </div>
  );
};
export default OwnerLayout;

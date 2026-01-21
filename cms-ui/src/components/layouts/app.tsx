import { adminOnly, hasAccess } from '@/utils/auth-utils';
import dynamic from 'next/dynamic';

const AdminLayout = dynamic(() => import('@/components/layouts/admin'));
const OwnerLayout = dynamic(() => import('@/components/layouts/owner'));

export default function AppLayout({
  userPermissions,
  ...props
}: {
  userPermissions: string[];
}) {
  // if (hasAccess(adminOnly, userPermissions)) {
  //   return <AdminLayout {...props} />;
  // }
  return <OwnerLayout {...props} />;
}

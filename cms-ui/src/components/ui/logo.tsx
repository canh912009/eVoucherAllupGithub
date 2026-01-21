import Image from 'next/image';
import Link from '@/components/ui/link';
import cn from 'classnames';
import { siteSettings } from '@/settings/site.settings';
import { getAuthCredentials } from '@/utils/auth-utils';

const Logo: React.FC<React.AnchorHTMLAttributes<{}>> = ({
  className,
  ...props
}) => {
  // const { logo, siteTitle } = useSettings();
  const { token } = getAuthCredentials();
  return (
    <Link
      href={siteSettings.logo.href}
      className={cn('inline-flex', className)}
      {...props}
    >
      <span
        className="relative overflow-hidden"
        style={{
          width: siteSettings.logo.width,
          height: siteSettings.logo.height,
        }}
      >
        <Image
          src={token ? siteSettings.logo.url : siteSettings.logoAuth.url}
          priority
          alt={/*siteTitle ??*/token ? siteSettings.logo.alt : siteSettings.logoAuth.alt}
          fill
          sizes="(max-width: 768px) 100vw"
          className="object-contain"
          loading="eager"
        />
      </span>
    </Link>
  );
};

export default Logo;

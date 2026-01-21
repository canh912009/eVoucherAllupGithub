import Image from 'next/image';
import Link from '@/components/ui/link';
import cn from 'classnames';

const LogoBack: React.FC<React.AnchorHTMLAttributes<{}>> = ({
  className,
  ...props
}) => {
  // const { logo, siteTitle } = useSettings();
  // const { token } = getAuthCredentials();
  return (
    <Link
      href={''}
      className={cn('inline-flex', className)}
      {...props}
    >
      <span
        className="relative overflow-hidden"
        style={{
          width: 168,
          height: 60,
        }}
      >
        <Image
          src={'/logo-black.svg'}
          priority
          alt={''}
          fill
          sizes="(max-width: 768px) 100vw"
          className="object-contain"
          loading="eager"
        />
      </span>
    </Link>
  );
};

export default LogoBack;

import Card from '@/components/common/card';
import Button from '@/components/ui/button';
import { VNPTEPayProvider } from '@/types';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import VNPTEPayPremiumGiftList from './vnpt-epay-gift-list';

type IProps = {
  initialValues?: VNPTEPayProvider | null;
};

export default function VNPTEPayPremiumDetail({ initialValues }: IProps) {
  const router = useRouter();
  const { t } = useTranslation();

  return (
    <div>
      <div className="my-5 flex flex-wrap sm:my-8">
        <Card className="w-full sm:w-full md:w-full">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-4 py-2">
              {/* <VNPTEPayPremiumGiftList data={initialValues?.giftList} /> */}
            </div>
          </div>
        </Card>
      </div>
      <div className="mb-4 text-end">
        <Button
          variant="outline"
          onClick={router.back}
          className="me-4 hover:bg-red-800"
          type="button"
        >
          {t('form:button-label-back')}
        </Button>
      </div>
    </div>
  );
}

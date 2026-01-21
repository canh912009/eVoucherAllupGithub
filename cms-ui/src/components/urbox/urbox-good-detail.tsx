import Card from '@/components/common/card';
import { getUrlPublicAsset } from '@/data/download';
import { UrBoxGood, UrBoxGoodType } from '@/types';
import { formatNumber } from '@/utils/common-utils';
import { useTranslation } from 'next-i18next';
import { useEffect, useState } from 'react';
import TextArea from '../ui/text-area';

type IProps = {
  initialValues?: UrBoxGood | null;
};

export default function UrBoxGoodDetail({ initialValues }: Readonly<IProps>) {
  // console.log('initialValues', initialValues);

  const { t } = useTranslation();

  const rootClassName =
    'bg-gray-100 ps-6 pe-4 h-12 flex items-center w-full md:w-3/4 rounded appearance-none transition duration-300 ease-in-out text-heading text-sm focus:outline-none focus:ring-0 border border-border-base focus:border-accent';

  const [officeData, setOfficeData] = useState('');
  const aa =
    '<ul><li>Khách hàng được hưởng dịch vụ đặc quyền chăm sóc sức khoẻ tổng quát cao cấp tại các bệnh viện & phòng khám quốc tế lớn tại HN & HCM & các tỉnh thành lớn.</li><li>Áp dụng cho người lớn hoặc trẻ em, tùy thuộc vào gói dịch vụ lựa chọn (1 gói dịch vụ /1 khách hàng)</li><li>Quý khách hàng sở hữu quà tặng VIP eGift có thể sử dụng tại các cơ sở khám bệnh thuộc hệ thống y tế của Vinmec, Bệnh viện Thu Cúc và Phòng khám Quốc tế Careplus. Vui lòng xem thông tin địa chỉ <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://drive.google.com/file/d/1QPCbcoLzI9h-bc4ZwOdIaQ1ywjXhpYC6/view?usp=sharing"><span style="font-weight: bolder;">TẠI ĐÂY</span></a></span></u></li><li>eGift áp dụng cho 01 trong những gói khám sau:</li><li>- Gói khám tiêu chuẩn tại Vinmec (16 tuổi trở lên): <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://drive.google.com/file/d/1cR_PkIpBj1eyFGaOYAEmwTiISyzT7FgB/view?usp=sharing"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>- Gói khám trẻ em tại Vinmec (6-15 tuổi): <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://benhvienthucuc.vn/goi-kham-suc-khoe-co-ban/"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>- Gói khám sức khỏe tổng quát định kỳ - cơ bản (nam) tại BV Thu Cúc: <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://benhvienthucuc.vn/goi-kham-suc-khoe-co-ban/"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>- Gói khám sức khỏe tổng quát định kỳ - cơ bản (nữ) tại BV Thu Cúc: <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://benhvienthucuc.vn/goi-kham-suc-khoe-co-ban/"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>- Gói khám sức khỏe tổng quát định kỳ (Trẻ em từ 6 đến dưới 16 tuổi) tại BV Thu Cúc: <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://benhvienthucuc.vn/goi-kham-tre-em-tu-6-den-duoi-16-tuoi-kham-suc-khoe-tong-quat-dinh-ky/"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>- Gói khám sức khỏe tổng quát định kỳ (Thiếu niên từ 16 đến 18 tuổi) tại BV Thu Cúc: <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://benhvienthucuc.vn/goi-kham-thieu-nien-tu-16-den-18-tuoi-kham-suc-khoe-tong-quat-dinh-ky/"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>- Gói khám tổng quát tiêu chuẩn (Nam) tại Phòng khám Quốc tế Careplus: <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://careplusvn.com/vi/kham-tong-quat-goi-tieu-chuan-cho-nam"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>- Gói khám tổng quát tiêu chuẩn (Nữ) tại Phòng khám Quốc tế Careplus: <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://careplusvn.com/vi/kham-tong-quat-goi-tieu-chuan-cho-nu"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>- Gói khám tổng quát chi bé từ 1-6 tuổi tại Phòng khám Quốc tế Careplus: <u font-size:="" 13px;"="" "="" 13px;="" color:="" rgb(0,="" 0,="" 0);"="" style="font-size: 13.6px;"><span times="" new="" roman";="" mso-hansi-font-family:calibri;mso-bidi-font-family:calibri;color:#0563c1"=""><a href="https://careplusvn.com/vi/kham-tong-quat-cho-be-tu-1-6-tuoi"><span style="font-weight: bolder;">LINK THAM KHẢO</span></a></span></u></li><li>Quý khách sẽ nhận được cuộc gọi tư vấn dịch vụ từ tổng đài UrBox sau 4 giờ làm việc.</li><li>eGift sẽ không được hoàn lại tiền thừa và không có giá trị quy đổi thành tiền mặt. Khách hàng có thể được yêu cầu trả thêm tiền nếu sử dụng quá giá trị của eGift.</li><li>eGift không áp dụng cùng chương trình khuyến mãi khác.</li><li>Giá của sản phẩm đã bao gồm dịch vụ CSKH VIP của UrBox</li><li>Vui lòng đặt lịch khám trước tối thiểu 05 ngày sử dụng dịch vụ.</li><li>Để thay đổi hoặc hủy lịch khám, Quý khách vui lòng thông báo UrBox trước tối thiểu 3 ngày sử dụng dịch vụ. Ngoài thời gian trên trạng thái dịch vụ được xác nhận thành công và không thể hoàn, hủy dịch vụ trong mọi trường hợp.</li><li>Lưu ý: </li><li>Quý khách lưu ý ngày đặt/ thay đổi/ hủy lịch là ngày hành chính từ thứ 2- thứ 6 trong tuần, không tính Thứ 7, chủ nhật và Lễ Tết.   </li><li>Lịch khám được thay đổi duy nhất 01 lần trong thời hạn sử dụng eGift.</li><li><b>Do tình hình dịch bệnh diễn biến phức tạp, việc test Covid sẽ thực hiện theo quy định của Vinmec tại một số cơ sở với chi phí khách hàng tự chi trả như sau (giá có thể thay đổi tùy theo quy định của Vinmec):</b></li><li><b>Test nhanh Covid: 109.000đ/ lần (Chi phí test Covid không bao gồm trong gói dịch vụ)</b></li><li><b>Test PCR: 518.000đ/ lần (Chi phí test Covid không bao gồm trong gói dịch vụ)</b></li><li>eGift chỉ có giá trị sử dụng một lần. Không chấp nhận eGift quá hạn sử dụng, trạng thái “Đã sử dụng”.</li><li>eGift sẽ không được cấp lại sau khi đã cung cấp cho khách hàng trong những trường hợp bị mất hoặc ở trạng thái "đã sử dụng" trong mọi hoàn cảnh.</li><li>UrBox đồng hành và hỗ trợ khách hàng trong quá trình sử dụng dịch vụ nếu quý khách có các phản hồi về chất lượng dịch vụ chăm sóc sức khỏe.</li><li>eGift không áp dụng xuất hóa đơn giá trị gia tăng.</li><li>UrBox có thể sửa chữa hoặc thay đổi điều khoản và điều kiện theo quy định của nhà cung cấp mà không thông báo trước.</li><li>Hotline UrBox phục vụ dành riêng cho khách hàng VIP trong tất cả các ngày bao gồm Lễ Tết từ 8h - 22h hàng ngày, quý khách vui lòng liên hệ: 1800 28 28 36 để được hỗ trợ.</li>																			</ul>';

  useEffect(() => {
    if (initialValues?.office) {
      const formattedData = initialValues?.office
        .map((office) => {
          return `[City]: ${office.title_city} - [Code]: ${office.code} - [Address]: ${office.address} - [Phone]: ${office.phone}`;
        })
        .join('\n');
      setOfficeData(formattedData);
    }
  }, []);

  return (
    <div>
      <div className="flex border-b border-dashed border-border-base py-4">
        <h2 className="text-2xl font-semibold uppercase text-red-500">
          {initialValues?.title + ' (' + initialValues?.id + ')'}
        </h2>
      </div>
      <div className="my-5 flex flex-wrap sm:my-6">
        <Card className="w-full sm:w-full md:w-full md:p-4">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold">
                {t('Good Details (Product)')}
              </h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Good Id')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.id}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Good Name')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.title}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand Id')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.brand_id}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand Name')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.brand_name}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand Online')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={formatNumber(initialValues?.brand_online!)}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand Logo Loyalty')}
              </label>
              {initialValues?.brandLogoLoyalty && (
                <div className="w-full pt-1 md:w-3/4">
                  <img
                    src={
                      getUrlPublicAsset(initialValues?.brandLogoLoyalty) ?? ''
                    }
                    alt="Brand Logo Loyalty"
                    width={160}
                    height={160}
                  />
                </div>
              )}
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Brand Image')}
              </label>
              {initialValues?.brandImage && (
                <div className="w-full pt-1 md:w-3/4">
                  <img
                    src={getUrlPublicAsset(initialValues?.brandImage) ?? ''}
                    alt="Brand"
                    width={160}
                    height={160}
                  />
                </div>
              )}
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Category Id')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.cat_id}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Category Title')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.cat_title}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Good Type')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={
                  UrBoxGoodType[
                    initialValues?.type as keyof typeof UrBoxGoodType
                  ]
                }
              />
              <label className="mt-4 w-full py-2 text-heading md:w-1/4">
                {t('Price')}
              </label>
              <input
                className={`${rootClassName} mt-4`}
                disabled={true}
                type="text"
                value={formatNumber(initialValues?.price!)}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Product Image (Review)')}
              </label>
              {initialValues?.image && (
                <div className="w-full pt-1 md:w-3/4">
                  <img
                    src={getUrlPublicAsset(initialValues?.image) ?? ''}
                    alt="Product"
                    width={160}
                    height={160}
                  />
                </div>
              )}
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Point')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={formatNumber(initialValues?.point!)}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('View')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={formatNumber(initialValues?.view!)}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Stock')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={formatNumber(initialValues?.stock!)}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Quantity')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={formatNumber(initialValues?.quantity!)}
              />
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Expire Duration')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.expire_duration}
              />
            </div>
            <div className="flex w-full flex-wrap px-2 md:w-1/2">
              <label className="w-full py-2 text-heading md:w-1/4">
                {t('Display Type')}
              </label>
              <input
                className={rootClassName}
                disabled={true}
                type="text"
                value={initialValues?.code_display}
              />
            </div>
          </div>
        </Card>

        <Card className="mt-5 w-full sm:w-full md:w-full md:p-4">
          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2 py-2">
              <h1 className="text-lg font-semibold">{t('Addition Details')}</h1>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2">
              <label className="w-full py-2 text-heading md:w-[12%]">
                {t('Content')}
              </label>
              <p
                className="w-full md:w-[88%]"
                dangerouslySetInnerHTML={{ __html: initialValues?.content! }}
              ></p>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2">
              <label className="w-full py-2 text-heading md:w-[12%]">
                {t('Note')}
              </label>
              <p
                className="w-full md:w-[88%]"
                dangerouslySetInnerHTML={{ __html: initialValues?.note! }}
              ></p>
            </div>
          </div>

          <div className="mb-5 flex flex-wrap">
            <div className="flex w-full flex-wrap px-2">
              <label className="w-full py-2 text-heading md:w-[12%]">
                {t('Office')}
              </label>
              <TextArea
                disabled={true}
                name="note"
                value={officeData}
                variant="outline"
                className="w-full md:w-[88%]"
                rows={8}
              />
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}

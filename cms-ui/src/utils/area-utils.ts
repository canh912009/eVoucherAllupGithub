import { Province, District, Ward } from '@/types';
import VN_provinces from '@/data_temp/VN_provinces.json';
import VN_districts from '@/data_temp/VN_districts.json';
import VN_wards from '@/data_temp/VN_wards.json';

export const VN_Provinces: Province[] = VN_provinces.map((item: any) => {
  return {
    id: item.id,
    name: item.name,
    code: item.code,
  };
});

export const VN_Districts: District[] = VN_districts.map((item: any) => {
  return {
    id: item.id,
    name: item.name,
    code: item.code,
    parent_id: item.parent_id,
  };
});

export const VN_Wards: Ward[] = VN_wards.map((item: any) => {
  return {
    id: item.id,
    name: item.name,
    code: item.code,
    parent_id: item.parent_id,
  };
});
